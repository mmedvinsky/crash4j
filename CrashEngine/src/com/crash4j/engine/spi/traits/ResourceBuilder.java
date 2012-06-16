/**
 * @copyright
 */
package com.crash4j.engine.spi.traits;

import java.net.DatagramSocket;
import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.Resource;

import com.crash4j.engine.UnknownResourceException;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.resources.ResourceSpi;

/**
 * {@link ResourceBuilder} is used to translate method messages into the 
 * unique resource#message combinations.
 * 
 * 
 * @author <crash4j team>
 */
public interface ResourceBuilder
{
    /**
     * @return unique key for this resource.  This key will be used by the resource
     * manager to map it to the managing object instance.
     */
    public Object getUniqueKey(Object instance, Object args, Object rv);
    
    /**
     * @return <code>true</code> if {@link Resource} can be associated with object instance or cached with {@link ResourceAware}
     *         trait. If this returns false, then the resource {@link URI} is resolved every time and then matched against previously initiated resources
     *         for grouping.  This is necessary for  {@link DatagramSocket}s, where a single socket can {@link DatagramSocket#send(java.net.DatagramPacket)} 
     *         or {@link DatagramSocket#receive(java.net.DatagramPacket)} data to many end-points.
     */
    public boolean canCache();
    
    
    /**
     * Creates an instance of the {@link ResourceSpi} object that describes the program's current access requests
     * @param spec is an instance of {@link ResourceSpec}
     * @param args is an Array of parameters
     * @param instance of the object to be translated
     * @return an instance of the {@link Resource}
     * @throws UnknownResourceException
     */
    public ResourceSpi createResource(ResourceSpec spec, Object args, Object instance, Object rv) 
            throws UnknownResourceException, URISyntaxException;
    /**
     * Complete incomplete resource when necessary. Each {@link ResourceSpi} instance 
     * is individually responsible for setting completion status and handling completion updates.
     * @param current
     * @param spec
     * @param args
     * @param instance
     * @param rv
     */
    public void completeResource(ResourceSpi current, ResourceSpec spec, Object args, 
    		Object instance, Object rv) throws UnknownResourceException;
}
