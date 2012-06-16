/**
 * 
 */
package com.crash4j.engine.spi.protocol;

import com.crash4j.engine.Action;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.traits.ResourceAware;
import com.crash4j.engine.spi.traits.ResourceBuilder;
import com.crash4j.engine.types.ActionClasses;

/**
 * @see Action
 */
public class ProtocolEvent implements ResourceAware 
{
	protected ResourceSpi resource = null;
	protected ResourceSpi registeredResource = null;
	protected Action action = null;
	protected Object data = null;
	protected long eventStartTime = 0L;
	protected long eventStopTime = 0L;
	
	/**
	 * ProtocolEvent constructor.
	 * @param method
	 * @param aClass
	 * @param cTypes
	 */
	public ProtocolEvent(ResourceSpi spi, Action action) 
	{
		this.resource = spi;
		this.action = action;
	}
	
	/**
	 * @return the eventStartTime
	 */
	public long getEventStartTime() 
	{
		return eventStartTime;
	}

	/**
	 * @param eventStartTime the eventStartTime to set
	 */
	public void setEventStartTime(long eventStartTime) 
	{
		this.eventStartTime = eventStartTime;
	}

	/**
	 * @return the eventStopTime
	 */
	public long getEventStopTime() {
		return eventStopTime;
	}

	/**
	 * @param eventStopTime the eventStopTime to set
	 */
	public void setEventStopTime(long eventStopTime) {
		this.eventStopTime = eventStopTime;
	}

	/**
	 * @return this one is returned as a call to {@link ResourceBuilder} factory in the handler when 
	 * {@link #registeredResource} will be null and then it will be reset.  This will register the resource with the engine.
	 */
	public ResourceSpi getTargetResource()
	{
		return resource;
	}
	
	/**
	 * @return the action
	 */
	public Action getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(Action action) {
		this.action = action;
	}

	@Override
	public ResourceSpi getResource() 
	{
		return this.registeredResource;
	}
	
	@Override
	public void setResource(ResourceSpi res) 
	{
		this.registeredResource = res;
	}
	
	@Override
	public Object getData() 
	{
		return this.data;
	}
	
	@Override
	public void setData(Object d) 
	{
		this.data = d;
	}
}
