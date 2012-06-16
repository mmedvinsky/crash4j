/**
 * 
 */
package com.crash4j.engine.spi.actions.http;

import com.crash4j.engine.spi.actions.ActionImpl;
import com.crash4j.engine.types.ActionClasses;

/**
 * {@link HTTPActionImpl} is responsible for handling and recognizing an HTTP traffic.
 */
public class HTTPActionImpl extends ActionImpl {

	/**
	 * @param method
	 * @param aClass
	 * @param cTypes
	 */
	public HTTPActionImpl(String method) 
	{
		super(method, ActionClasses.HTTP, null);
	}

}
