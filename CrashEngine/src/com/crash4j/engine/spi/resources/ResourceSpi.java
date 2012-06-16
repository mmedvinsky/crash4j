/**
 * 
 */
package com.crash4j.engine.spi.resources;

import java.net.URI;
import java.net.URISyntaxException;

import javax.management.ObjectName;

import com.crash4j.engine.Resource;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.traits.SimulationConsumer;
import com.crash4j.engine.types.ResourceTypes;

/**
 * {@link ResourceSpi} is a service provider interface for {@link Resource} abstraction.
 * @author <MM>
 *
 */
public interface ResourceSpi extends Resource, SimulationConsumer
{    
    /**
     * @param lastAccess the lastAccess to set
     */
    public void setLastAccess(long lastAccess);

    
    /**
     * @return long time of last access in millis
     */
    public long getLastAccess();
    
    /**
     * @return unique identifier for a resources. 
     */
    public URI getResourceURI() throws URISyntaxException;

    /**
     * @return unique identifier for a resources. 
     */
    public String getETag();
    
    
    /**
     * @return unique identifier for a resources. 
     */
    public ObjectName getVector();
    
    /** Return string representation of the resource type */
    public ResourceTypes getResourceType();
    
    /**
     * @return resource specification associated with this resource.
     */
    public ResourceSpec getResourceSpec();

     
    /**
     * @return a parent resource if there is one.  
     */
    public ResourceSpi getParent();
    /**
     * @param parent to set for this resource if any
     */
    public void setParent(ResourceSpi parent);
    /**
     * Get monitoring instances based on a specific type
     * @param type
     * @param action
     * @return
    public Monitor[][] getMonitors(IOTypes action);
    public Monitor[][][] getAllMonitors();
    public Monitor getMonitor(StatTypes type, UnitTypes utype, Action action);
     */
    
    /**
     * @return <code>true</code> if resource is completely resolved
     */
    public boolean isComplete();
    
    /**
     * @param c to set completion mode
     */
    public void setComplete(Boolean c);
}
