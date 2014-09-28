/**
 * 
 */
package com.crash4j.engine.spi.sim.impl;

/**
 */
public class DelayInstructionImpl extends InstructionImpl 
{
	/**
	 * @param tn
	 * @param p
	 * @param pr
	 */
	public DelayInstructionImpl(long tn, double p, Object pr) 
	{
		super(tn, p, pr);
	}

	/**
	 * @see com.crash4j.engine.spi.sim.impl.InstructionImpl#getParameter()
	 */
	@Override
	public Object getParameter() 
	{
		return this.parameter;
	}
}
