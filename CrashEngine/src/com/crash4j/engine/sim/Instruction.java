package com.crash4j.engine.sim;


public interface Instruction
{
    /**
     * @return the starting tick
     */
    public long getTick();

    /**
     * @return the probability of this instruction to take place
     */
    public double getP();

    /**
     * @return the weight that is applied in different ways for different things.
     */
    public Object getParameter();
}