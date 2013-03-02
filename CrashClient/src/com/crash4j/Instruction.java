package com.crash4j;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @see Simulation
 */
public class Instruction
{
    protected long tick = 0;
    protected double P = 0.0;
    protected Object parameter = null;

    
    /**
     * @param tick
     * @param p
     * @param parameter
     */
    public Instruction()
    {
    }
    
    
    /**
     * @param tick
     * @param p
     * @param parameter
     */
    public Instruction(long tick, double p, Object param)
    {
        this.tick = tick;
        P = p;
        this.parameter = param;
    }
    /**
     * @return the tick
     */
    @XmlAttribute(name="tick")
    public long getTick()
    {
        return tick;
    }
    /**
     * @param tick the tick to set
     */
    public void setTick(long tick)
    {
        this.tick = tick;
    }
    /**
     * @return the p
     */
    @XmlAttribute(name="p")
    public double getP()
    {
        return P;
    }
    /**
     * @param p the p to set
     */
    public void setP(double p)
    {
        P = p;
    }
    /**
     * @return the weight
     */
    @XmlAttribute(name="parameter")
    public Object getParameter()
    {
        return this.parameter;
    }
    /**
     * @param weight the weight to set
     */
    public void setParameter(Object p)
    {
        this.parameter = p;
    }
    
    
}