/**
 * @copyright
 */
package com.crash4j;

import java.util.concurrent.TimeUnit;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 * A single {@link Stat}, acquired from the engine for a specific resource
 */
public class Stat
{
    protected double max = Long.MIN_VALUE;
    protected double min = Long.MAX_VALUE;
    protected long count = 0;
    protected long lastUpdateTime = 0;
    protected double lastUpdateValue = 0;
    protected double average = 0;
    protected double totalTime = 0;
    protected String action = null;
    protected String type = null;    
    protected TimeUnit timeUnit = null;
    /**
     * @return the max
     */
    @XmlAttribute(name="max")
    public double getMax()
    {
        return max;
    }
    /**
     * @return the action
     */
    @XmlAttribute(name="action")
    public String getAction()
    {
        return action;
    }
    /**
     * @param action the action to set
     */
    public void setAction(String action)
    {
        this.action = action;
    }
    /**
     * @param average the average to set
     */
    public void setAverage(double average)
    {
        this.average = average;
    }
    
    /**
     * @return
     */
    @XmlAttribute(name="totalTime")
    public double getTotalTime() 
    {
		return totalTime;
	}
    
	/**
	 * @param totalTime
	 */
    public void setTotalTime(double totalTime) 
	{
		this.totalTime = totalTime;
	}
	
	/**
     * @param max the max to set
     */
    public void setMax(double max)
    {
        this.max = max;
    }
    /**
     * @return the min
     */
    @XmlAttribute(name="min")
    public double getMin()
    {
        return min;
    }
    /**
     * @param min the min to set
     */
    public void setMin(double min)
    {
        this.min = min;
    }
    /**
     * @return the count
     */
    @XmlAttribute(name="count")
    public long getCount()
    {
        return count;
    }
    /**
     * @param count the count to set
     */
    public void setCount(long count)
    {
        this.count = count;
    }
    /**
     * @return the lastUpdateTime
     */
    @XmlAttribute(name="lut")
    public long getLastUpdateTime()
    {
        return lastUpdateTime;
    }
    /**
     * @param lastUpdateTime the lastUpdateTime to set
     */
    public void setLastUpdateTime(long lastUpdateTime)
    {
        this.lastUpdateTime = lastUpdateTime;
    }
    /**
     * @return the lastUpdateValue
     */
    @XmlAttribute(name="luv")
    public double getLastUpdateValue()
    {
        return lastUpdateValue;
    }
    /**
     * @param lastUpdateValue the lastUpdateValue to set
     */
    public void setLastUpdateValue(double lastUpdateValue)
    {
        this.lastUpdateValue = lastUpdateValue;
    }
    /**
     * @return the averaghe
     */
    @XmlAttribute(name="avg")
    public double getAverage()
    {
        return average;
    }
    /**
     * @param averaghe the average to set
     */
    public void setAveraghe(double average)
    {
        this.average = average;
    }
    /**
     * @return the type
     */
    @XmlAttribute(name="type")
    public String getType()
    {
        return type;
    }
    /**
     * @param type the type to set
     */
    public void setType(String type)
    {
        this.type = type;
    }
    /**
     * @return the timeUnit
     */
    @XmlAttribute(name="tunit")
    public TimeUnit getTimeUnit()
    {
        return timeUnit;
    }
    /**
     * @param timeUnit the timeUnit to set
     */
    public void setTimeUnit(TimeUnit timeUnit)
    {
        this.timeUnit = timeUnit;
    }

}
