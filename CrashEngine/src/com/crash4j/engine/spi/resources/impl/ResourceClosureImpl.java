/**
 * 
 */
package com.crash4j.engine.spi.resources.impl;

import com.crash4j.engine.spi.traits.ResourceClosure;
import com.crash4j.engine.types.ActionClasses;

/**
 * @see ResourceClosure
 */
public class ResourceClosureImpl implements ResourceClosure 
{
	private int len = ActionClasses.values().length;
	private Object[] space = new Object[len];
	

	@Override
	public void setData(int h, Object data) 
	{
		space[h] = data;
	}

	@Override
	public Object getData(int h) 
	{
		return space[h];
	}
}
