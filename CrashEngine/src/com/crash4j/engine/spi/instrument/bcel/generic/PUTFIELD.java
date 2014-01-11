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

import com.crash4j.engine.spi.instrument.bcel.Constants;
import com.crash4j.engine.spi.instrument.bcel.ExceptionConstants;

/** 
 * PUTFIELD - Put field in object
 * <PRE>Stack: ..., objectref, value -&gt; ...</PRE>
 * OR
 * <PRE>Stack: ..., objectref, value.word1, value.word2 -&gt; ...</PRE>
 *
 * @version $Id: PUTFIELD.java 1554577 2013-12-31 22:06:09Z ggregory $
 * @author  <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 */
public class PUTFIELD extends FieldInstruction implements PopInstruction, ExceptionThrower {

    private static final long serialVersionUID = -3931392044558815011L;


    /**
     * Empty constructor needed for the Class.newInstance() statement in
     * Instruction.readInstruction(). Not to be used otherwise.
     */
    PUTFIELD() {
    }


    public PUTFIELD(int index) {
        super(Constants.PUTFIELD, index);
    }


    @Override
    public int consumeStack( ConstantPoolGen cpg ) {
        return getFieldSize(cpg) + 1;
    }


    public Class<?>[] getExceptions() {
        Class<?>[] cs = new Class[2 + ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length];
        System.arraycopy(ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION, 0, cs, 0,
                ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length);
        cs[ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length + 1] = ExceptionConstants.INCOMPATIBLE_CLASS_CHANGE_ERROR;
        cs[ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length] = ExceptionConstants.NULL_POINTER_EXCEPTION;
        return cs;
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
        v.visitStackConsumer(this);
        v.visitPopInstruction(this);
        v.visitTypedInstruction(this);
        v.visitLoadClass(this);
        v.visitCPInstruction(this);
        if (v instanceof VisitorSupportsInvokeDynamic) {
            ((VisitorSupportsInvokeDynamic)v).visitNameSignatureInstruction(this);
        }
        v.visitFieldOrMethod(this);
        v.visitFieldInstruction(this);
        v.visitPUTFIELD(this);
    }
}
