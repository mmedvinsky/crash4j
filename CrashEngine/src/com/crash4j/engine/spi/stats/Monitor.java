/**
 * 
 */
package com.crash4j.engine.spi.stats;

import java.util.Date;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.crash4j.engine.Action;
import com.crash4j.engine.StatsCollector;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.types.StatTypes;
import com.crash4j.engine.types.UnitTypes;

/**
 * {@link Monitor} handle statistical data generated from the execution and simulations.
 */
public class Monitor
{
    protected static int rollingWindow = 10;
    
    protected double max = Long.MIN_VALUE;
    protected double min = Long.MAX_VALUE;
    protected long count = 0;
    protected long rcount = 0;
    protected long lut = 0;
    protected double luv = 0;
    protected double sample = 0;
    protected double actualsample = 0;
    protected long items = 0;
    protected int rollcount = 0;
    protected long totalTime = 0L;
    protected Action action = null;    
    protected StatTypes type = StatTypes.TIME;    
    protected UnitTypes utype = UnitTypes.MICROSECONDS;
    
    protected ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
      
    /**
     * Clear stats information
     */
    public void clear()
    {
        lock.writeLock().lock();
        try
        {
            this.count = 0;
            this.rcount = 0;
            this.sample = 0;
            this.max = 0;
            this.min = 0;
            this.luv = 0;
            this.lut = 0;
            this.actualsample = 0;
            this.totalTime = 0;
            this.rollcount = rollingWindow;
        }
        finally
        {
            lock.writeLock().unlock();
        }
        
    }
    
    /**
     * @param sample
     * @param items
     */
    public void addSample(long sample, long items)
    {
        lock.writeLock().lock();
        try
        {
            if (StatTypes.OCCUPANCY == type)
            {
                this.count++;
                this.sample = this.sample + sample;
                if (this.sample < 0)
                {
                    this.sample = 0;
                }
                this.max = (this.max >= sample ? this.max : sample);
                this.min = (this.min <= sample ? this.min : sample);
                this.luv = sample;
            }
            else if (StatTypes.COUNT == type)
            {
                this.count++;
                this.sample = this.sample + sample;
                if (this.sample < 0)
                {
                    this.sample = 0;
                }
                this.luv = sample;
            }
            else if (StatTypes.ERROR == type)
            {
                if (rollcount <= 0)
                {
                    rollcount = rollingWindow;
                    this.sample = 0;
                    this.rcount = 0;
                }
                
                this.count++;
                this.rcount++;
                this.sample = this.sample + sample;
                if (this.sample < 0)
                {
                    this.sample = 0;
                }
                this.max = (this.max >= sample ? this.max : sample);
                this.min = (this.min <= sample ? this.min : sample);
                this.luv = sample;
                this.rollcount--;
            }
            else if (StatTypes.TIME == type || 
                    StatTypes.ACTUALTIME ==type || 
                    StatTypes.THROUGHPUT == type)
            {
                double sl = UnitTypes.convert(UnitTypes.NANOSECONDS, utype, sample);
                double rate = 0;
                if (items != 0)
                {
                    rate = items/sl;
                }
                
                if (StatTypes.THROUGHPUT != type)
                {
                    this.totalTime += sl;
                }
                
                if (rollcount <= 0)
                {
                    rollcount = rollingWindow;
                    this.sample = 0;
                    this.rcount = 0;
                }
                this.count++;
                this.rcount++;
                if (type == StatTypes.THROUGHPUT)
                {
                    sl = rate;
                }
                this.sample = this.sample + (type == StatTypes.THROUGHPUT ? rate : sl);
                this.max = (this.max >= sl ? this.max : sl);
                this.min = (this.min <= sl ? this.min : sl);
                this.luv = sl;
                this.rollcount--;
            }
            this.lut = System.currentTimeMillis();
        }
        finally
        {
            lock.writeLock().unlock();
        }
        
    }
    /**
     * Collect and submit stats into {@link StatsCollector} 
     * @param c
     */
    public void collect(ResourceSpi res, StatsCollector c)
    {
        double max;
        double min;
        long count;
        long lut;
        double luv;
        double ttime;
        double sample;            
        StatTypes type;    
        UnitTypes utype;
        
        lock.readLock().lock();
        try
        {
            max = this.max;
            min = this.min;
            count = this.count;
            lut = this.lut;
            luv = this.luv;
            sample = this.sample;            
            type = this.type;    
            utype = this.utype;
            ttime = this.totalTime;
        }
        finally
        {
            lock.readLock().unlock();
        }
                
        if (count > 0)
        {
            if (StatTypes.COUNT == type || StatTypes.OCCUPANCY == type || StatTypes.ERROR == type)
            {
            }
            else
            {
                sample = sample/rcount;
            }
            
            c.submitStats(res, action, type, 
                utype, totalTime, count, max, min, lut, luv, sample);
        }
    }
    
    /**
     * @param type
     */
    public Monitor(Action action, StatTypes type, UnitTypes utype)
    {
        this.type = type;
        this.utype = utype;
        this.action = action;
    }



    /**
     * @return the utype
     */
    public UnitTypes getUnits()
    {
        return utype;
    }



    /**
     * @return the type
     */
    public StatTypes getType()
    {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(StatTypes type)
    {
        this.type = type;
    }

    /** (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) 
	{
		if (obj instanceof Monitor)
		{
			Monitor m = (Monitor)obj;
			return action.equals(m.action) && this.type.intValue() == m.type.intValue() && this.utype.intValue() == m.utype.intValue();
		}
		return false;
	}

	/** (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() 
	{
		return this.type.intValue()+this.utype.intValue();
	}

	/**
     * @return Sample
     */
    public double getSample()
    {
        lock.readLock().lock();
        try
        {
            if (StatTypes.COUNT == type || StatTypes.OCCUPANCY == type)
            {
                return this.sample;
            }
            else if (StatTypes.TIME == type || StatTypes.THROUGHPUT == type || StatTypes.ACTUALTIME == type)
            {
                return (this.sample/this.count);
            }
            else
            {
                return sample;
            }
        }
        finally
        {
            lock.readLock().unlock();
        }
    }
    
    public double getMax()
    {
        lock.readLock().lock();
        try
        {
            return max;
        }
        finally
        {
            lock.readLock().unlock();
        }
    }
    
    public double getMin()
    {
        lock.readLock().lock();
        try
        {
            return min;
        }
        finally
        {
            lock.readLock().unlock();
        }
    }
    
    public double getTotalTime()
    {
        lock.readLock().lock();
        try
        {
            return this.totalTime;
        }
        finally
        {
            lock.readLock().unlock();
        }
    }
    
    public double getLastUpdate()
    {
        lock.readLock().lock();
        try
        {
            return luv;
        }
        finally
        {
            lock.readLock().unlock();
        }
    }
    
    public Date getLastUpdateTime()
    {
        lock.readLock().lock();
        try
        {
            return new Date(lut);
        }
        finally
        {
            lock.readLock().unlock();
        }
    }
    
    public long getLastUpdateTimeMillis()
    {
        lock.readLock().lock();
        try
        {
            return lut;
        }
        finally
        {
            lock.readLock().unlock();
        }
    }
    
    public long getCount()
    {
        lock.readLock().lock();
        try
        {
            return count;
        }
        finally
        {
            lock.readLock().unlock();
        }
    }
    
    public void incr()
    {
        addSample(1, 0);
    }
    
    
    public void decr()
    {
        addSample(-1, 0);
    }
    
    public void enter()
    {
        this.incr();
    }
    
    public void exit()
    {
        this.decr();
    }
    
    /**
     * @return
     */
    public double getTenants()
    {
        return this.getSample();
    }   
}
