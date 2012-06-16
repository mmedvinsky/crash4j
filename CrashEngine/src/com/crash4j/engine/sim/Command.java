/**
 * @copyright
 */
package com.crash4j.engine.sim;

/**
 * Simulater command is a single behavior instruction that a caller should apply onto the running behavior at this time.
 * @author team
 *
 */
public interface Command
{
    /**
     * @return behavior that initiated this instruction
     */
    public Behavior getBehavior();
    
    /**
     * @return actual instruction to perform
     */
    public Instruction getInstruction();
    
    /**
     * @return <code>true</code> is this command is enabled to run.
     */
    public boolean isEnabled();
}
