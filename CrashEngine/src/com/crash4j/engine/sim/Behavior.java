package com.crash4j.engine.sim;

import java.util.Set;

import com.crash4j.engine.Action;
import com.crash4j.engine.spi.sim.impl.InstructionImpl;
import com.crash4j.engine.types.BehaviorTypes;
import com.crash4j.engine.types.InstructionTypes;

public interface Behavior
{
    /**
     * @return the stop
     */
    public boolean isStop();

    /**
     * @return the name
     */
    public String getName();
    
    /**
     * @return the id
     */
    public String getId();
    
    /**
     * @return <code>true</code> is the behavior is suspended. 
     */
    public boolean isSuspended();

    /**
     * Lookahead towards the next instruction and see if the starting tick is not hit yet 
     * 
     * @return next {@link InstructionImpl} for the next tick;
     */
    public Instruction next();
    
    /**
     * Suspend the behavior ops
     */
    public void suspend();
    
    /**
     * Resume the behavior
     */
    public void resume();
    
    /**
     * return {@link Set} of {@link Action}(s)
     */
    public Set<Action> getActions();

    /**
     * @return <code>true</code> if this behavior should effect this user action
     * @see Action
     */
    public boolean shouldEffect(Action action);

    /**
     * @return {@link InstructionImpl} for this position
     */
    public Instruction getInstruction();
    
    /**
     * @return instruction list for this position.
     */
    public Instruction[] getInstructions();

    /**
     * @return {@link BehaviorTypes} that was selected for this behavior
     */
    public BehaviorTypes getBehaviorType();
    
    /**
     * @return the retain
     */
    public boolean isRetain();

    /**
     * @return the rotate
     */
    public boolean isRotate();

    /**
     * @return the type
     */
    public InstructionTypes getType();
}