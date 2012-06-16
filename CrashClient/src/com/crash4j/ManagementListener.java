/**
 * @copyright
 */
package com.crash4j;

/**
 * {@link ManagementListener} is used to abstract 
 * remote communication to the management server.
 * @author team
 *
 */
public interface ManagementListener
{
    /**
     * Get list of resources in the system
     * @param res
     * @throws ManagementException
     */
    public Resource[] getResources() throws EngineException;
    
    /**
     * Get details about individual resource
     * @param res
     * @throws ManagementException
     */
    public Resource getResourceDetails(String tag) throws EngineException;
    
    /**
     * Get simulations in the system
     * @param res
     * @throws ManagementException
     */
    public Simulation[] getSimulations() throws EngineException;
    
    /**
     * Start simulation
     * @param res
     * @throws ManagementException
     */
    public void startSimulation(String id) throws EngineException;
    
    /**
     * Start simulation
     * @param res
     * @throws ManagementException
     */
    public void stopSimulation(String id) throws EngineException;
    /**
     * Clear all stats
     * @param res
     * @throws ManagementException
     */
    public void clearStats() throws EngineException;
    
    /**
     * Reset engine
     * @param res
     * @throws ManagementException
     */
    public void resetEngine() throws EngineException;
    
}
