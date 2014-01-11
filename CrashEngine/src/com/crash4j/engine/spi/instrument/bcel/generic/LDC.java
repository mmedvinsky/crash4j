/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. 
 *
 */
package com.crash4j.engine.spi.instrument.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;

import com.crash4j.engine.spi.instrument.bcel.util.ByteSequence;

/** 
 * LDC - Push item from constant pool.
 *
 * <PRE>Stack: ... -&gt; ..., item</PRE>
 *
 * @version $Id: LDC.java 1152072 2011-07-29 01:54:05Z dbrosius $
 * @author  <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 */
public class LDC extends CPInstruction implements PushInstruction, ExceptionThrower {

    private static final long serialVersionUID = -972820476154330719L;


    /**
     * Empty constructor needed for the Class.newInstance() statement in
     * Instruction.readInstruction(). Not to be used otherwise.
     */
    LDC() {
    }


    public LDC(int index) {
        super(com.crash4j.engine.spi.instrument.bcel.Constants.LDC_W, index);
        setSize();
    }


    // Adjust to proper size
    protected final void setSize() {
        if (index <= com.crash4j.engine.spi.instrument.bcel.Constants.MAX_BYTE) { // Fits in one byte?
            opcode = com.crash4j.engine.spi.instrument.bcel.Constants.LDC;
            length = 2;
        } else {
            opcode = com.crash4j.engine.spi.instrument.bcel.Constants.LDC_W;
            length = 3;
        }
    }


    /**
     * Dump instruction as byte code to stream out.
     * @param out Output stream
     */
    @Override
    public void dump( DataOutputStream out ) throws IOException {
        out.writeByte(opcode);
        if (length == 2) {
            out.writeByte(index);
        } else {
            out.writeShort(index);
        }
    }


    /**
     * Set the index to constant pool and adjust size.
     */
    @Override
    public final void setIndex( int index ) {
        super.setIndex(index);
        setSize();
    }


    /**
     * Read needed data (e.g. index) from file.
     */
    @Override
    protected void initFromFile( ByteSequence bytes, boolean wide ) throws IOException {
        length = 2;
        index = bytes.readUnsignedByte();
    }


    public Object getValue( ConstantPoolGen cpg ) {
        com.crash4j.engine.spi.instrument.bcel.classfile.Constant c = cpg.getConstantPool().getConstant(index);
        switch (c.getTag()) {
            case com.crash4j.engine.spi.instrument.bcel.Constants.CONSTANT_String:
                int i = ((com.crash4j.engine.spi.instrument.bcel.classfile.ConstantString) c).getStringIndex();
                c = cpg.getConstantPool().getConstant(i);
                return ((com.crash4j.engine.spi.instrument.bcel.classfile.ConstantUtf8) c).getBytes();
            case com.crash4j.engine.spi.instrument.bcel.Constants.CONSTANT_Float:
                return new Float(((com.crash4j.engine.spi.instrument.bcel.classfile.ConstantFloat) c).getBytes());
            case com.crash4j.engine.spi.instrument.bcel.Constants.CONSTANT_Integer:
                return Integer.valueOf(((com.crash4j.engine.spi.instrument.bcel.classfile.ConstantInteger) c).getBytes());
            case com.crash4j.engine.spi.instrument.bcel.Constants.CONSTANT_Class:
            	int nameIndex = ((com.crash4j.engine.spi.instrument.bcel.classfile.ConstantClass) c).getNameIndex();
            	c = cpg.getConstantPool().getConstant(nameIndex);
            	return new ObjectType(((com.crash4j.engine.spi.instrument.bcel.classfile.ConstantUtf8) c).getBytes());
            default: // Never reached
                throw new RuntimeException("Unknown or invalid constant type at " + index);
        }
    }


    @Override
    public Type getType( ConstantPoolGen cpg ) {
        switch (cpg.getConstantPool().getConstant(index).getTag()) {
            case com.crash4j.engine.spi.instrument.bcel.Constants.CONSTANT_String:
                return Type.STRING;
            case com.crash4j.engine.spi.instrument.bcel.Constants.CONSTANT_Float:
                return Type.FLOAT;
            case com.crash4j.engine.spi.instrument.bcel.Constants.CONSTANT_Integer:
                return Type.INT;
            case com.crash4j.engine.spi.instrument.bcel.Constants.CONSTANT_Class:
                return Type.CLASS;
            default: // Never reached
                throw new RuntimeException("Unknown or invalid constant type at " + index);
        }
    }


    public Class<?>[] getExceptions() {
        return com.crash4j.engine.spi.instrument.bcel.ExceptionConstants.EXCS_STRING_RESOLUTION;
    }


    /**
     * Call corresponding visitor method(s). The order is:
     * Call visitor methods of implemented interfaces first, then
     * call methods according to the class hierarchy in descending order,
     * i.e., the most specific visitXXX() call comes last.
     *
     * @param v Visitor object
     */
    @Override
    public void accept( Visitor v ) {
        v.visitStackProducer(this);
        v.visitPushInstruction(this);
        v.visitExceptionThrower(this);
        v.visitTypedInstruction(this);
        v.visitCPInstruction(this);
        v.visitLDC(this);
    }
}
