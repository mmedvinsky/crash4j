/*
 * Copyright  2000-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); 
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
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

/** 
 * LDC2_W - Push long or double from constant pool
 *
 * <PRE>Stack: ... -&gt; ..., item.word1, item.word2</PRE>
 *
 * @version $Id: LDC2_W.java 386056 2006-03-15 11:31:56Z tcurdt $
 * @author  <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 */
public class LDC2_W extends CPInstruction implements PushInstruction, TypedInstruction {

    /**
     * Empty constructor needed for the Class.newInstance() statement in
     * InstructionImpl.readInstruction(). Not to be used otherwise.
     */
    LDC2_W() {
    }


    public LDC2_W(int index) {
        super(com.crash4j.engine.spi.instrument.bcel.Constants.LDC2_W, index);
    }


    public Type getType( ConstantPoolGen cpg ) {
        switch (cpg.getConstantPool().getConstant(index).getTag()) {
            case com.crash4j.engine.spi.instrument.bcel.Constants.CONSTANT_Long:
                return Type.LONG;
            case com.crash4j.engine.spi.instrument.bcel.Constants.CONSTANT_Double:
                return Type.DOUBLE;
            default: // Never reached
                throw new RuntimeException("Unknown constant type " + opcode);
        }
    }


    public Number getValue( ConstantPoolGen cpg ) {
        com.crash4j.engine.spi.instrument.bcel.classfile.Constant c = cpg.getConstantPool().getConstant(index);
        switch (c.getTag()) {
            case com.crash4j.engine.spi.instrument.bcel.Constants.CONSTANT_Long:
                return new Long(((com.crash4j.engine.spi.instrument.bcel.classfile.ConstantLong) c).getBytes());
            case com.crash4j.engine.spi.instrument.bcel.Constants.CONSTANT_Double:
                return new Double(((com.crash4j.engine.spi.instrument.bcel.classfile.ConstantDouble) c).getBytes());
            default: // Never reached
                throw new RuntimeException("Unknown or invalid constant type at " + index);
        }
    }


    /**
     * Call corresponding visitor method(s). The order is:
     * Call visitor methods of implemented interfaces first, then
     * call methods according to the class hierarchy in descending order,
     * i.e., the most specific visitXXX() call comes last.
     *
     * @param v Visitor object
     */
    public void accept( Visitor v ) {
        v.visitStackProducer(this);
        v.visitPushInstruction(this);
        v.visitTypedInstruction(this);
        v.visitCPInstruction(this);
        v.visitLDC2_W(this);
    }
}
