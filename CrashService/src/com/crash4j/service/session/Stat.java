/**
 * @copyright
 */
package com.crash4j.service.session;

import java.util.concurrent.TimeUnit;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A single {@link Stat}, acquired from the engine for a specific resource
 */
@XmlRootElement(name="stats")
public class Stat
{
    protected double max = Long.MIN_VALUE;
    protected double min = Long.MAX_VALUE;
    protected long count = 0;
    protected long lastUpdateTime = 0;
    protected double lastUpdateValue = 0;
    protected double average = 0;
    protected String action = null;
    protected String type = null;    
    protected TimeUnit timeUnit = null;
    
    
    public Stat()
    {        
    }
    /**
     * @param max
     * @param min
     * @param count
     * @param lastUpdateTime
     * @param lastUpdateValue
     * @param average
     * @param action
     * @param type
     * @param timeUnit
     */
    public Stat(double max, double min, long count, long lastUpdateTime, double 
            lastUpdateValue, double average, String action, String type, TimeUnit timeUnit)
    {
        this.max = max;
        this.min = min;
        this.count = count;
        this.lastUpdateTime = lastUpdateTime;
        this.lastUpdateValue = lastUpdateValue;
        this.average = average;
        this.action = action;
        this.type = type;
        this.timeUnit = timeUnit;
    }




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
    @XmlAttribute(name="timestamp")
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
    @XmlAttribute(name="last")
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
    @XmlAttribute(name="average")
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
    @XmlAttribute(name="unit")
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
