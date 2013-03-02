package com.crash4j.engine.spi.sim.impl;

import com.crash4j.engine.sim.Instruction;

/**
 * a record that holds a single tick information.
 * @author team
 */
public abstract class InstructionImpl implements Instruction
{
    protected long tn = 0;
    protected double P = 0.0;
    protected Object parameter = null;
    /**
     * @param tn
     * @param p
     * @param weight
     */
    public InstructionImpl(long tn, double p, Object pr)
    {
        this.tn = tn;
        this.P = p;
        this.parameter = pr;
    }
    	
    public void setParamater(Object paramater) 
    {
		this.parameter = paramater;
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
    
	public abstract Object getParameter();

	public void setParameter(Object parameter) {
		this.parameter = parameter;
	}

}