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
 * IDIV - Divide ints
 * <PRE>Stack: ..., value1, value2 -&gt; result</PRE>
 *
 * @version $Id: IDIV.java 1152072 2011-07-29 01:54:05Z dbrosius $
 * @author  <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 */
public class IDIV extends ArithmeticInstruction implements ExceptionThrower {

    private static final long serialVersionUID = -7104037931993634172L;


    /** Divide ints
     */
    public IDIV() {
        super(com.crash4j.engine.spi.instrument.bcel.Constants.IDIV);
    }


    /** @return exceptions this instruction may cause
     */
    public Class<?>[] getExceptions() {
        return new Class[] {
            com.crash4j.engine.spi.instrument.bcel.ExceptionConstants.ARITHMETIC_EXCEPTION
        };
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
        v.visitExceptionThrower(this);
        v.visitTypedInstruction(this);
        v.visitStackProducer(this);
        v.visitStackConsumer(this);
        v.visitArithmeticInstruction(this);
        v.visitIDIV(this);
    }
}
