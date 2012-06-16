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
 * {@link Behaviors} annotation describes simulation behavior that is used to alter 
 * base behavior of running resources.
 * 
 * @author <crash4j team>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface Behaviors
{    
    /**
     * files, containing behaviors to load
     * @return list of files
     */
    String[] sources() default {};
}
