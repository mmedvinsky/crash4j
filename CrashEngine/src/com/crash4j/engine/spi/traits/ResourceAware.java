/**
 * @copyright
 */
package com.crash4j.engine.spi.traits;

import com.crash4j.engine.spi.resources.ResourceSpi;

/**
 * Used for class instrumentation in order to add a resource affinity to the object itself.
 * @author <crash4j team>
 */
public interface ResourceAware
{
    /**
     * @return {@link ResourceSpi} assigned to this object
     */
    public ResourceSpi getResource();
    
    /**
     * @return {@link ResourceSpi} assigned to this object
     */
    public void setResource(ResourceSpi res);
    /**
     * @return opaque {@link Object} associated with an instance
     */
    public Object getData();
    
    /**
     * @param sets the opaque {@link Object}
     */
    public void setData(Object res);
}
