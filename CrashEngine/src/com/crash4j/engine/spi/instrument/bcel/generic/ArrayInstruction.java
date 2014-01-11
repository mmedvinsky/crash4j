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

/**
 * Super class for instructions dealing with array access such as IALOAD.
 *
 * @version $Id: ArrayInstruction.java 1149459 2011-07-22 04:34:27Z dbrosius $
 * @author  <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 */
public abstract class ArrayInstruction extends Instruction implements ExceptionThrower,
        TypedInstruction {

    private static final long serialVersionUID = 1355074014869910296L;


    /**
     * Empty constructor needed for the Class.newInstance() statement in
     * Instruction.readInstruction(). Not to be used otherwise.
     */
    ArrayInstruction() {
    }


    /**
     * @param opcode of instruction
     */
    protected ArrayInstruction(short opcode) {
        super(opcode, (short) 1);
    }


    public Class<?>[] getExceptions() {
        return com.crash4j.engine.spi.instrument.bcel.ExceptionConstants.EXCS_ARRAY_EXCEPTION;
    }


    /** @return type associated with the instruction
     */
    public Type getType( ConstantPoolGen cp ) {
        switch (opcode) {
            case com.crash4j.engine.spi.instrument.bcel.Constants.IALOAD:
            case com.crash4j.engine.spi.instrument.bcel.Constants.IASTORE:
                return Type.INT;
            case com.crash4j.engine.spi.instrument.bcel.Constants.CALOAD:
            case com.crash4j.engine.spi.instrument.bcel.Constants.CASTORE:
                return Type.CHAR;
            case com.crash4j.engine.spi.instrument.bcel.Constants.BALOAD:
            case com.crash4j.engine.spi.instrument.bcel.Constants.BASTORE:
                return Type.BYTE;
            case com.crash4j.engine.spi.instrument.bcel.Constants.SALOAD:
            case com.crash4j.engine.spi.instrument.bcel.Constants.SASTORE:
                return Type.SHORT;
            case com.crash4j.engine.spi.instrument.bcel.Constants.LALOAD:
            case com.crash4j.engine.spi.instrument.bcel.Constants.LASTORE:
                return Type.LONG;
            case com.crash4j.engine.spi.instrument.bcel.Constants.DALOAD:
            case com.crash4j.engine.spi.instrument.bcel.Constants.DASTORE:
                return Type.DOUBLE;
            case com.crash4j.engine.spi.instrument.bcel.Constants.FALOAD:
            case com.crash4j.engine.spi.instrument.bcel.Constants.FASTORE:
                return Type.FLOAT;
            case com.crash4j.engine.spi.instrument.bcel.Constants.AALOAD:
            case com.crash4j.engine.spi.instrument.bcel.Constants.AASTORE:
                return Type.OBJECT;
            default:
                throw new ClassGenException("Oops: unknown case in switch" + opcode);
        }
    }
}
