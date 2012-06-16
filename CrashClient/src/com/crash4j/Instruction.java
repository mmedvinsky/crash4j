package com.crash4j;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @see Simulation
 */
public class Instruction
{
    protected long tick = 0;
    protected double P = 0.0;
    protected double weight = 0.0;

    
    /**
     * @param tick
     * @param p
     * @param weight
     */
    public Instruction()
    {
    }
    
    
    /**
     * @param tick
     * @param p
     * @param weight
     */
    public Instruction(long tick, double p, double weight)
    {
        this.tick = tick;
        P = p;
        this.weight = weight;
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
    @XmlAttribute(name="weight")
    public double getWeight()
    {
        return weight;
    }
    /**
     * @param weight the weight to set
     */
    public void setWeight(double weight)
    {
        this.weight = weight;
    }
    
    
}