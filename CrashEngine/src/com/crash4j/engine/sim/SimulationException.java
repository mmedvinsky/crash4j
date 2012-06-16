/**
 * @copyright
 */
package com.crash4j.engine.sim;

import com.crash4j.engine.spi.instrument.EventData;
import com.crash4j.engine.spi.resources.ResourceSpi;

/**
 * Used to initlize the {@link Throwable#initCause(Throwable)} for all simulated exceptions
 * @author team
 *
 */
public class SimulationException extends Exception
{
    protected String message = null;
    protected EventData eventData = null;
    /**
     * Create resource message. 
     */
    public SimulationException(EventData o, ResourceSpi res)
    {
        StringBuilder s = new StringBuilder("crash4j simulated exception for: ");
        s.append(res.getResourceType()).append(" resource ").append(" initiated from ").append(o.getSpec().getKey());
        message = s.toString();
        this.eventData = o;
    }

    
    
    public EventData getEventData() 
    {
		return eventData;
	}

	public void setEventData(EventData eventData) 
	{
		this.eventData = eventData;
	}



	/**
     * @see java.lang.Throwable#getLocalizedMessage()
     */
    @Override
    public String getLocalizedMessage()
    {
        return message;
    }

    /**
     * @see java.lang.Throwable#getMessage()
     */
    @Override
    public String getMessage()
    {
        return message;
    }
    
    
}
