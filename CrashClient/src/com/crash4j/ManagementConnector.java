/**
 * @copyright
 */
package com.crash4j;

/**
 * {@link ManagementConnector} is used to abstract 
 * remote communication to the management server.
 * @author team
 *
 */
public interface ManagementConnector
{
    /**
     * Register a listener that will get all the remote events.
     * @param res
     * @throws ManagementException
     */
    public void registerListener(ManagementListener l) throws ManagementException;
    
    /**
     * Unregister a listener
     * @param res
     * @throws ManagementException
     */
    public void unRegisterListener(ManagementListener l) throws ManagementException;
    
}
