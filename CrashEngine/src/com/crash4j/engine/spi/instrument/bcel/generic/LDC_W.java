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

import java.io.IOException;

import com.crash4j.engine.spi.instrument.bcel.util.ByteSequence;

/** 
 * LDC_W - Push item from constant pool (wide index)
 *
 * <PRE>Stack: ... -&gt; ..., item.word1, item.word2</PRE>
 *
 * @version $Id: LDC_W.java 1152072 2011-07-29 01:54:05Z dbrosius $
 * @author  <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 */
public class LDC_W extends LDC {

    private static final long serialVersionUID = -8040188785844554411L;


    /**
     * Empty constructor needed for the Class.newInstance() statement in
     * Instruction.readInstruction(). Not to be used otherwise.
     */
    LDC_W() {
    }


    public LDC_W(int index) {
        super(index);
    }


    /**
     * Read needed data (i.e., index) from file.
     */
    @Override
    protected void initFromFile( ByteSequence bytes, boolean wide ) throws IOException {
        setIndex(bytes.readUnsignedShort());
        // Override just in case it has been changed
        opcode = com.crash4j.engine.spi.instrument.bcel.Constants.LDC_W;
        length = 3;
    }
}
