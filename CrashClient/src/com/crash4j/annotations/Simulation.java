/**
 * @copyright
 */
package com.crash4j.annotations;

/**
 * @see com.crash4j.Simulation for details.
 * @author team
 *
 */
public @interface Simulation
{
    /**
     * @return id of this simulation
     */
    String id() default "";
    /**
     * @return frequency of this simulation 
     */
    int frequency() default 1;
    /**
     * @return name of the simulation
     */
    
    String name() default "";
    /**
     * @return description of the simulation
     */
    String description() default "";
    /**
     * 
     * @return mappings for this resource
     */
    String[] mappings() default {};
    /**
     * 
     * @return behaviors selected for this simulation
     */
    Behavior[] behaviors() default {};
    
    /**
     * 
     * @return source file that could be used to configure specific simulation
     */
    String source() default "";
}
