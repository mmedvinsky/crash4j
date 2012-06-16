/**
 *  @copyright
 */
package com.crash4j.engine.spi.resources.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.management.ObjectName;

import com.crash4j.engine.Action;
import com.crash4j.engine.sim.Simulation;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.stats.Monitor;
import com.crash4j.engine.spi.traits.SimulationConsumer;
import com.crash4j.engine.spi.util.PropertyOwner;
import com.crash4j.engine.spi.util.Utils;
import com.crash4j.engine.types.ResourceTypes;
import com.crash4j.engine.types.StatTypes;
import com.crash4j.engine.types.UnitTypes;

/**
 * Abstract base class for all {@link ResourceSpi} objects in the system.
 * @author team
 * 
 */
public abstract class ResourceSpiImpl implements ResourceSpi, SimulationConsumer, PropertyOwner
{
    protected ResourceSpec spec = null;
    //initially all are null
    protected volatile boolean complete = true;
    protected volatile long lastAccess = System.currentTimeMillis();
    protected ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    //protected Monitor[][][] actionMonitors = new Monitor[spec.getActions().length][StatTypes.values().length][UnitTypes.values().length];
    protected volatile Simulation simulation = null;
    protected int etag = 0;
    protected ResourceSpi parent = null;
    /**
     * @param spec
     */
    public ResourceSpiImpl(ResourceSpec spec)
    {
        this.spec = spec;    
        this.etag = Utils.getBaseHashCode(this);
    }
    
    /**
	 * @see com.crash4j.engine.spi.resources.ResourceSpi#getParent()
	 */
	@Override
	public ResourceSpi getParent() 
	{
		return this.parent;
	}

	/**
	 * Set the parent
	 */
	public void setParent(ResourceSpi parent)
	{
		if (this.parent == null)
		{
			this.parent = parent;
		}
	}
	
	protected String createEtag(String type)
    {
        return (new TagKey(type, etag, hashCode())).toString();
    }
    
    /**
     * @see com.crash4j.engine.spi.resources.ResourceSpi#setSimulation(com.crash4j.engine.sim.Simulation)
     */
    @Override
    public void setSimulation(Simulation sim)
    {
        this.simulation = sim;
    }
    
    public URI getResourceURI() throws URISyntaxException
    {
        ObjectName name = getVector();
        
        return new URI(name.getDomain(), name.getKeyPropertyListString(), null);
    }


    /**
     * @see com.crash4j.engine.spi.resources.ResourceSpi#getETag()
     */
    @Override
    public String getETag()
    {
        return createEtag(this.getResourceType().toString());
    }

    /**
     * @see com.crash4j.engine.Resource#asURI()
     */
    @Override
    public URI asURI() throws URISyntaxException
    {
        return this.getResourceURI();
    }

    /**
     * @see com.crash4j.engine.Resource#asVector()
     */
    @Override
    public ObjectName asVector()
    {
        return this.getVector();
    }

    /**
     * @see com.crash4j.engine.spi.resources.ResourceSpi#getSimulation()
     */
    @Override
    public Simulation getSimulation()
    {
        return this.simulation;
    }

    /**
     * @see com.crash4j.engine.spi.resources.ResourceSpi#getResourceType()
     */
    @Override
    public ResourceTypes getResourceType()
    {
        return this.spec.getResourceType();
    }

    /**
     * @see com.crash4j.engine.spi.resources.spi.ResourceSpi#getMonitors(com.crash4j.engine.types.ActionThemes.core.ResourceActions)
    @Override
    public Monitor[][] getMonitors(Action action)
    {
        lock.readLock().lock();
        try
        {
            Monitor[][] am = new Monitor[StatTypes.values().length][UnitTypes.values().length];
            System.arraycopy(actionMonitors[action.toInt()], 0, am, 0, StatTypes.values().length);   
            return am;
        }
        finally
        {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public Monitor getMonitor(StatTypes type, UnitTypes utype, Action action)
    {
        boolean createNew = false;
        lock.readLock().lock();
        try
        {
            Monitor[][] mons = actionMonitors[action];
            if (mons[type.intValue()][utype.intValue()] != null)
            {
                return mons[type.intValue()][utype.intValue()];
            }
            createNew = true;
        }
        finally
        {
            lock.readLock().unlock();
        }
        
        lock.writeLock().lock();
        try
        {
            Monitor[][] mons = actionMonitors[action];
            if (mons[type.intValue()][utype.intValue()] != null)
            {
                return mons[type.intValue()][utype.intValue()];
            }
            mons[type.intValue()][utype.intValue()] = new Monitor(type, utype);
            return mons[type.intValue()][utype.intValue()];
        }
        finally
        {
            lock.writeLock().unlock();
        }
    }

     */
    
    
    @Override
    public abstract boolean equals(Object obj);
    
    /**
     * @see java.lang.Object#hashCode()
     * @Override
     */
    public abstract int hashCode();
    
    /**
     * @see java.lang.Object#equals(java.lang.Object)
    @Override
    public boolean equals(Object obj)
    {
        if (this.vector != null && obj instanceof ResourceSpi)
        {
            ResourceSpi spi = ((ResourceSpi)obj);
            return spi.getVector().compareTo(this.vector) == 0;
        }
        return super.equals(obj);
    }
     */

    /**
     * @see java.lang.Object#hashCode()
    @Override
    public int hashCode()
    {
        if (this.vector != null)            
        {
            return this.vector.hashCode();
        }
        return super.hashCode();
    }
     */

    /**
     * 
     */
    
    
    
    @Override
    public ResourceSpec getResourceSpec()
    {
        return this.spec;
    }

    /**
     * @param lastAccess the lastAccess to set
     */
    public void setLastAccess(long lastAccess)
    {
        this.lastAccess = lastAccess;
    }

    @Override
    public long getLastAccess()
    {
        return lastAccess;
    }

    /*
    @Override
    public Monitor[][][] getAllMonitors()
    {
        return actionMonitors;
    }
    */
    
    @Override
    public boolean isComplete()
    {
        return this.complete;
    }

    @Override
    public void setComplete(Boolean c)
    {
        this.complete = c;        
    }
}
