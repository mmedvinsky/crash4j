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
package com.crash4j.engine.spi.instrument.bcel.verifier.statics;



import com.crash4j.engine.spi.instrument.bcel.Constants;
import com.crash4j.engine.spi.instrument.bcel.generic.Type;

/**
 * This class represents the upper half of a LONG variable.
 * @version $Id: LONG_Upper.java 992350 2010-09-03 16:18:20Z markt $
 * @author Enver Haase
 */
public final class LONG_Upper extends Type{

	private static final long serialVersionUID = 3650377550491597107L;
    /** The one and only instance of this class. */
	private static final LONG_Upper singleInstance = new LONG_Upper();

	/** The constructor; this class must not be instantiated from the outside. */
	private LONG_Upper(){
		super(Constants.T_UNKNOWN, "Long_Upper");
	}

	/** Use this method to get the single instance of this class. */
	public static LONG_Upper theInstance(){
		return singleInstance;
	}
}
