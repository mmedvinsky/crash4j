/**
 * @copyright
 */
package com.crash4j.annotations;

import com.crash4j.EngineAdapter;


/**
 * {@link Behavior} annotation describes simulation behavior that is used to alter 
 * base behavior of running resources.
 * 
 * @author <crash4j team>
 *
 */
public @interface Behavior
{
    /**
     * @return specified Id of the simulation.
     */
    String id();
        
    /**
     * @see EngineAdapter#getResourceDetails(String)
     * @return a list of actions that this behavior can modify after it is mapped onto the set of resources
     */
    String []actions() default {};
    
    /**
     * @return mode for this behavior. It can be (stop|retain|rotate)
     */
    String mode() default "rotate";
}
