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
 * ASTORE - Store reference into local variable
 * <PRE>Stack ..., objectref -&gt; ... </PRE>
 *
 * @version $Id: ASTORE.java 386056 2006-03-15 11:31:56Z tcurdt $
 * @author  <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 */
public class ASTORE extends StoreInstruction {

    /**
     * Empty constructor needed for the Class.newInstance() statement in
     * InstructionImpl.readInstruction(). Not to be used otherwise.
     */
    ASTORE() {
        super(com.crash4j.engine.spi.instrument.bcel.Constants.ASTORE, com.crash4j.engine.spi.instrument.bcel.Constants.ASTORE_0);
    }


    /** Store reference into local variable
     * @param n index of local variable
     */
    public ASTORE(int n) {
        super(com.crash4j.engine.spi.instrument.bcel.Constants.ASTORE, com.crash4j.engine.spi.instrument.bcel.Constants.ASTORE_0, n);
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
        super.accept(v);
        v.visitASTORE(this);
    }
}
