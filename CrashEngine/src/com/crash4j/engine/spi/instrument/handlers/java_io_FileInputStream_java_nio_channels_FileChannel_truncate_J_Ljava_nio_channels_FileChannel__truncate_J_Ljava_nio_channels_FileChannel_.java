package com.crash4j.engine.spi.instrument.handlers;
import java.net.URISyntaxException;

import com.crash4j.engine.UnknownResourceException;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.traits.ResourceBuilder;
import com.crash4j.engine.spi.traits.ResourceClosure;

/**
 * 
 */

/**
 * Resource factory implementation for java.io.FileInputStream!java.nio.channels.FileChannel#truncate(J)Ljava/nio/channels/FileChannel;#truncate(J)Ljava/nio/channels/FileChannel;
 */
public class java_io_FileInputStream_java_nio_channels_FileChannel_truncate_J_Ljava_nio_channels_FileChannel__truncate_J_Ljava_nio_channels_FileChannel_ extends DefaultHandler implements ResourceBuilder
{
    @Override
    public ResourceSpi createResource(ResourceSpec spec, Object args, Object instance, Object rv) 
    	throws UnknownResourceException, URISyntaxException
    {
        return null;
    }
}
