package com.crash4j.engine.spi.sim.impl;

import com.crash4j.engine.sim.Instruction;

/**
 * a record that holds a single tick information.
 * @author team
 */
public class InstructionImpl implements Instruction
{
    protected long tn = 0;
    protected double P = 0.0;
    protected double weight = 0.0;

    /**
     * @param tn
     * @param p
     * @param weight
     */
    public InstructionImpl(long tn, double p, double weight)
    {
        this.tn = tn;
        this.P = p;
        this.weight = weight;
    }
    /**
     * @see com.crash4j.engine.sim.Instruction#getTick()
     */
    @Override
    public long getTick()
    {
        return tn;
    }
    /**
     * @param tn the tn to set
     */
    public void setTick(long tn)
    {
        this.tn = tn;
    }
    /**
     * @see com.crash4j.engine.sim.Instruction#getP()
     */
    @Override
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
     * @see com.crash4j.engine.sim.Instruction#getWeight()
     */
    @Override
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