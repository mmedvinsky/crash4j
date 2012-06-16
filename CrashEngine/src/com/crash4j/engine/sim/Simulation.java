package com.crash4j.engine.sim;

import com.crash4j.engine.spi.sim.impl.SimulationImpl;

/**
 * The {@link SimulationImpl} class describes a single {@link SimulationImpl} that is running 
 * within the simulation engine.  A simulation is mapped onto a set of resources it produces behavior modifications for.
 * 
 * The simulation will run in its own thread and will count down the simulation ticks.
 * For each tick a running behavior within the simulation will be positioned or the current value, at which time a caller is able
 * to get a set of current actions that are suppose to be taken during this tick.
 * 
 * simulation runs [0...n] ticks 
 * instruction [tn, A, W, w]
 *
 * Behaviors are:
 * tn: is a number of tick where this starts
 * A : is an action to perform. (0:time delay, 1:resource drop, 2: throughput, 3: raise error)
 * W : is a weight that governs if the said behavior will manifest.  0-1 and 1 means that it will appear all the time.
 * w : is a weight that is used to generate the actual adjustment value.
 *       A(0) dt = t*w
 *       A(1) w is ignored.
 *       A(2) tp (bytes/us) = requested_bytes - (requested_bytes*w)  
 *       A(3) w is ignored.
 * 
 * @author team
 *
 */
public interface Simulation
{
    /**
     * 
     * @return list of {@link Command}
     */
    public Command[] getCommands();

    /**
     * @return the name
     */
    public String getName();
    
    /**
     * Get behaviors
     */
    public Behavior[] getBehaviors();

    /**
     * @return the id
     */
    public String getId();
    
    /**
     * Add behaviors to the {@link SimulationImpl}
     * @param b
     */
    public void addBehavior(Behavior b);
    
    /**
     * @return the tick
     */
    public int getTick();

    /**
     * @return the tickFrequency
     */
    public int getTickFrequency();

    /**
     * Start the simulation
     */
    public void start();
    
    /**
     * @param name
     */
    public void suspendBehavior(String id);
    
    /**
     * @param name
     */
    public void resumeBehavior(String id);
    
    /**
     * Stop the simulation....
     */
    public void stop();
}