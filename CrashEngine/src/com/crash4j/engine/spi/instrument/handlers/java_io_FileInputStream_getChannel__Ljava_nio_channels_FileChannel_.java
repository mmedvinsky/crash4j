package com.crash4j.engine.spi.instrument.handlers;
import java.net.URISyntaxException;

import com.crash4j.engine.UnknownResourceException;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.instrument.delegates.java_nio_channels_FileChannel;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.traits.FacadeBuilder;
import com.crash4j.engine.spi.traits.ResourceBuilder;
import com.crash4j.engine.spi.traits.ResourceClosure;

/**
 * 
 */

/**
 * Resource factory implementation for java.io.FileInputStream#getChannel()Ljava/nio/channels/FileChannel;
 */
public class java_io_FileInputStream_getChannel__Ljava_nio_channels_FileChannel_ extends DefaultHandler implements ResourceBuilder, FacadeBuilder
{
    @Override
    public ResourceSpi createResource(ResourceSpec spec, Object args, Object instance, Object rv) 
    	throws UnknownResourceException, URISyntaxException
    {
        return null;
    }

    @Override
    public Object createFacade(ResourceSpec spec, ResourceSpi res, Object args, Object instance, Object rv, ResourceClosure c)
    {
        return new java_nio_channels_FileChannel(spec, res, args, instance, rv);
    }
}
