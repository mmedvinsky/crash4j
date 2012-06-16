/**
 * @copyright
 */
package com.crash4j.engine.spi.traits;

import com.crash4j.engine.sim.Simulation;

/**
 * Any class that will consume simulations.
 * @author <crash4j team>
 *
 */
public interface SimulationConsumer
{
    /**
     * Associate simulation with implementor
     * @param simulation
     */
    public void setSimulation(Simulation simulation);
    
    /**
     * @param simulation
     */
    public Simulation getSimulation();
}
