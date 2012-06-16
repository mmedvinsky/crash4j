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
 * {@link CrashOutput} annotation describes location for collecting output
 * 
 * @author <crash4j team>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface CrashOutput
{    
    /**
     * @return default output directory for this test
     */
     String dir() default ".";

     
     /**
      * @return <code>true</code> if stats collection is enabled and <code>false</code> otherwise
      */
      boolean collectStats() default true;
     
     /**
      * @return default collection period in millis
      */
      long every() default 100;
      
      /**
       * @return true if we should retain errors/exceptions from the simulation
       */
       boolean retainErrors() default false;
}
