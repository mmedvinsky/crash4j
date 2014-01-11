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

/** 
 * GOTO - Branch always (to relative offset, not absolute address)
 *
 * @version $Id: GOTO.java 1152072 2011-07-29 01:54:05Z dbrosius $
 * @author  <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 */
public class GOTO extends GotoInstruction implements VariableLengthInstruction {

    private static final long serialVersionUID = 6106731367505145625L;


    /**
     * Empty constructor needed for the Class.newInstance() statement in
     * Instruction.readInstruction(). Not to be used otherwise.
     */
    GOTO() {
    }


    public GOTO(InstructionHandle target) {
        super(com.crash4j.engine.spi.instrument.bcel.Constants.GOTO, target);
    }


    /**
     * Dump instruction as byte code to stream out.
     * @param out Output stream
     */
    @Override
    public void dump( DataOutputStream out ) throws IOException {
        index = getTargetOffset();
        if (opcode == com.crash4j.engine.spi.instrument.bcel.Constants.GOTO) {
            super.dump(out);
        } else { // GOTO_W
            index = getTargetOffset();
            out.writeByte(opcode);
            out.writeInt(index);
        }
    }


    /** Called in pass 2 of InstructionList.setPositions() in order to update
     * the branch target, that may shift due to variable length instructions.
     */
    @Override
    protected int updatePosition( int offset, int max_offset ) {
        int i = getTargetOffset(); // Depending on old position value
        position += offset; // Position may be shifted by preceding expansions
        if (Math.abs(i) >= (32767 - max_offset)) { // to large for short (estimate)
            opcode = com.crash4j.engine.spi.instrument.bcel.Constants.GOTO_W;
            short old_length = length;
            length = 5;
            return length - old_length;
        }
        return 0;
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
        v.visitVariableLengthInstruction(this);
        v.visitUnconditionalBranch(this);
        v.visitBranchInstruction(this);
        v.visitGotoInstruction(this);
        v.visitGOTO(this);
    }
}
