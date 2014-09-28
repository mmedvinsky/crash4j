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

import com.crash4j.engine.spi.instrument.bcel.classfile.ClassElementValue;
import com.crash4j.engine.spi.instrument.bcel.classfile.ConstantUtf8;
import com.crash4j.engine.spi.instrument.bcel.classfile.ElementValue;
import com.crash4j.engine.spi.instrument.bcel.generic.ConstantPoolGen;
import com.crash4j.engine.spi.instrument.bcel.generic.ElementValueGen;
import com.crash4j.engine.spi.instrument.bcel.generic.ObjectType;

public class ClassElementValueGen extends ElementValueGen
{
	// For primitive types and string type, this points to the value entry in
	// the cpool
	// For 'class' this points to the class entry in the cpool
	private int idx;

	protected ClassElementValueGen(int typeIdx, ConstantPoolGen cpool)
	{
		super(ElementValueGen.CLASS, cpool);
		this.idx = typeIdx;
	}

	public ClassElementValueGen(ObjectType t, ConstantPoolGen cpool)
	{
		super(ElementValueGen.CLASS, cpool);
		// this.idx = cpool.addClass(t);
		idx = cpool.addUtf8(t.getSignature());
	}

	/**
	 * Return immutable variant of this ClassElementValueGen
	 */
	@Override
    public ElementValue getElementValue()
	{
		return new ClassElementValue(type, idx, cpGen.getConstantPool());
	}

	public ClassElementValueGen(ClassElementValue value, ConstantPoolGen cpool,
			boolean copyPoolEntries)
	{
		super(CLASS, cpool);
		if (copyPoolEntries)
		{
			// idx = cpool.addClass(value.getClassString());
			idx = cpool.addUtf8(value.getClassString());
		}
		else
		{
			idx = value.getIndex();
		}
	}

	public int getIndex()
	{
		return idx;
	}

	public String getClassString()
	{
		ConstantUtf8 cu8 = (ConstantUtf8) getConstantPool().getConstant(idx);
		return cu8.getBytes();
		// ConstantClass c = (ConstantClass)getConstantPool().getConstant(idx);
		// ConstantUtf8 utf8 =
		// (ConstantUtf8)getConstantPool().getConstant(c.getNameIndex());
		// return utf8.getBytes();
	}

	@Override
    public String stringifyValue()
	{
		return getClassString();
	}

	@Override
    public void dump(DataOutputStream dos) throws IOException
	{
		dos.writeByte(type); // u1 kind of value
		dos.writeShort(idx);
	}
}
