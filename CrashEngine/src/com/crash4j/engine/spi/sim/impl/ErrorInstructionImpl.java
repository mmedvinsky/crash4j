/**
 * 
 */
package com.crash4j.engine.spi.sim.impl;

/**
 */
public class ErrorInstructionImpl extends InstructionImpl 
{
	/**
	 * @param tn
	 * @param p
	 * @param pr
	 * @throws ClassNotFoundException 
	 */
	public ErrorInstructionImpl(long tn, double p, Object pr) throws ClassNotFoundException 
	{
		super(tn, p, pr);
		Class<Throwable> exc = (Class<Throwable>)Class.forName(pr.toString());
		setParamater(exc);
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
