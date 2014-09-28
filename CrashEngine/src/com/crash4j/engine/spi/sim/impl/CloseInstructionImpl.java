/**
 * 
 */
package com.crash4j.engine.spi.sim.impl;

/**
 */
public class CloseInstructionImpl extends InstructionImpl 
{

	/**
	 * @param tn
	 * @param p
	 * @param pr
	 */
	public CloseInstructionImpl(long tn, double p, Object pr) 
	{
		super(tn, p, pr);
	}

	/**
	 * @see com.crash4j.engine.spi.sim.impl.InstructionImpl#getParameter()
	 */
	@Override
	public Object getParameter() 
	{
		return null;
	}
}
