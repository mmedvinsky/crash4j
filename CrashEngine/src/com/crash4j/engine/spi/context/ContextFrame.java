/**
 * 
 */
package com.crash4j.engine.spi.context;

import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.resources.ResourceSpi;

/**
 * Represents a single resource frame.
 * @author <MM>
 *
 */
public class ContextFrame
{
    protected ResourceSpec spec;
    protected ResourceSpi resource;
    protected boolean ignore = false;

    /**
     * @param resource
     */
    ContextFrame(ResourceSpec spec, ResourceSpi res)
    {
        this.spec = spec;
        this.resource = res;
    }

    /**
     * @param resource
     */
    ContextFrame(boolean ignore)
    {
        this.ignore = ignore;
    }

    /**
     * @return the ignore
     */
    public boolean isIgnore()
    {
        return ignore;
    }


    /**
     * @return the resource
     */
    public ResourceSpec getResourceSpec()
    {
        return spec;
    }

    /**
     * @param resource the resource to set
     */
    public void setResourceSpec(ResourceSpec spec)
    {
        this.spec = spec;
    }

    /**
     * @return the resource
     */
    public ResourceSpi getResource()
    {
        return resource;
    }

    /**
     * @param resource the resource to set
     */
    public void setResource(ResourceSpi resource)
    {
        this.resource = resource;
    }
    
}
