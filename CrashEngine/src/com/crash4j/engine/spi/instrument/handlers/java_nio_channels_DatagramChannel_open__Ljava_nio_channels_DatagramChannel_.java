/**
 */
 
package com.crash4j.engine.spi.instrument.handlers;
import java.net.URISyntaxException;
import java.nio.channels.DatagramChannel;

import com.crash4j.engine.UnknownResourceException;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.instrument.delegates.java_nio_channels_DatagramChannel;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.traits.FacadeBuilder;
import com.crash4j.engine.spi.traits.ResourceBuilder;
import com.crash4j.engine.spi.traits.ResourceClosure;


/**
 * This plug-in is generated to handle events produced by $key functional end-point
 * @see DefaultMessageHandler to understand the default behavior of thi class.
 */

/**
 * Resource factory implementation for $class#java.nio.channels.DatagramChannel#open()Ljava/nio/channels/DatagramChannel;
 */
public class java_nio_channels_DatagramChannel_open__Ljava_nio_channels_DatagramChannel_ extends DefaultHandler implements ResourceBuilder, FacadeBuilder
{
    @Override
    public ResourceSpi createResource(ResourceSpec spec, Object args, 
    				Object instance, 
    				Object rv) throws UnknownResourceException, URISyntaxException
    {
        return null;        
    }
    
    @Override
    public Object createFacade(ResourceSpec spec, ResourceSpi res, Object args, Object instance, Object rv, ResourceClosure c)
    {
        if (rv == null)
        {
            return null;
        }
        return new java_nio_channels_DatagramChannel((DatagramChannel)rv);
    }
        
}
