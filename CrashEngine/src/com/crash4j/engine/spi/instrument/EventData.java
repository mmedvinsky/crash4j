/**
 * 
 */
package com.crash4j.engine.spi.instrument;

import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.traits.ResourceClosure;

/**
 * {@link EventData} object is used for passing information into and back from instrumented areas
 */
public class EventData
{
    private Object instance = null;
    private long startTime = 0;
    private long stopTime = 0;
    private ResourceSpec spec = null;
    private Object params = null;
    private int actions = 0;
    private Object paramsOverwrite = null;
    private boolean ignored = false;
    private ResourceSpi resource = null;
    private Throwable outgoingException = null;
    private boolean outgoingExceptionRaised = false;
    private ResourceClosure closure = null;
    
    /**
     * Raise this{@link #outgoingException}
     * @throws Throwable
     */
    public void raiseOutgoingException() throws Throwable
    {
        if (this.outgoingException != null)
        {
            this.outgoingExceptionRaised = true;
            throw this.outgoingException;
        }
    }
    
	/**
     * @return the outgoingExceptionRaised
     */
    public boolean isOutgoingExceptionRaised()
    {
        return outgoingExceptionRaised;
    }
    /**
     * @param outgoingExceptionRaised the outgoingExceptionRaised to set
     */
    public void setOutgoingExceptionRaised(boolean outgoingExceptionRaised)
    {
        this.outgoingExceptionRaised = outgoingExceptionRaised;
    }
    /**
     * @return the outgoingException
     */
    public Throwable getOutgoingException()
    {
        return outgoingException;
    }
    /**
     * @param outgoingException the outgoingException to set
     */
    public void setOutgoingException(Throwable outgoingException)
    {
        this.outgoingException = outgoingException;
    }
    /**
     * @return the resource
     */
    public ResourceSpi getResource()
    {
        return resource;
    }
    /**
     * @param resource the resource to set
     */
    public void setResource(ResourceSpi resource)
    {
        this.resource = resource;
    }
    /**
     * @return the ignored
     */
    public boolean isIgnored()
    {
        return ignored;
    }
    /**
     * @param ignored the ignored to set
     */
    public void setIgnored(boolean ignored)
    {
        this.ignored = ignored;
    }
    /**
     * @return the instance
     */
    public Object getInstance()
    {
        return instance;
    }
    /**
     * @param instance the instance to set
     */
    public void setInstance(Object instance)
    {
        this.instance = instance;
    }
    
    
    
    /**
     * @return the stopTime
     */
    public long getStopTime()
    {
        return stopTime;
    }
    /**
     * @param stopTime the stopTime to set
     */
    public void setStopTime(long stopTime)
    {
        this.stopTime = stopTime;
    }
    /**
     * @return the starttime
     */
    public long getStartTime()
    {
        return startTime;
    }
    /**
     * @param starttime the starttime to set
     */
    public void setStartTime(long time)
    {
        this.startTime = time;
    }
    /**
     * @return the spec
     */
    public ResourceSpec getSpec()
    {
        return spec;
    }
    /**
     * @param spec the spec to set
     */
    public void setSpec(ResourceSpec spec)
    {
        this.spec = spec;
    }
    /**
     * @return the params
     */
    public Object getParams()
    {
        return params;
    }
    /**
     * @param params the params to set
     */
    public void setParams(Object params)
    {
        this.params = params;
    }
    /**
     * @return the actions
     */
    public int getActions()
    {
        return 0;
    }
    /**
     * @param actions the actions to set
     */
    public void setActions(int actions)
    {
        this.actions = actions;
    }
    /**
     * @return the paramsOverwrite
     */
    public Object getParamsOverwrite()
    {
        return paramsOverwrite;
    }
    /**
     * @param paramsOverwrite the paramsOverwrite to set
     */
    public void setParamsOverwrite(Object paramsOverwrite)
    {
        this.paramsOverwrite = paramsOverwrite;
    }

	/**
	 * @return the closure
	 */
	public ResourceClosure getClosure() 
	{
		return closure;
	}

	/**
	 * @param closure the closure to set
	 */
	public void setClosure(ResourceClosure closure) 
	{
		this.closure = closure;
	}
    
}
