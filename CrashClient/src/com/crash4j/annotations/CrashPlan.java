/**
 * @copyright
 */
package com.crash4j.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * {@link CrashPlan} annotation describes simulation behavior that is used to alter 
 * base behavior of running resources.
 * 
 * @author <crash4j team>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface CrashPlan
{    
    /**
     * @return specify number of times the test will be run
     */
    int iterations() default 1;
    /**
     * @return specify how many parallel threads will handle the run.
     */
    int concurrency() default 1;
    
    /**
     * @return specify how many parallel threads will handle the run.
     */
    int timeout() default -1;
    
    /**
     * simulations to use for a test
     * @return list of files
     */
    Simulation[] simulations() default {};

    /**
     * @return toleranceLevel
     * <p/>
     * <ul>
     * <li>0: no fault tolerance of any kind is assumed</li>
     * <li>1: the soft is fault recoverable,  meaning that the software will be able to reestablish good state after errors stop.</li>
     * <li>2: the soft is fault tolerant  can continue to operate</li>
     * </ul>
     */
    int toleranceLevel() default 0;  
}
