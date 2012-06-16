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
 * Resource factory implementation for java.io.FileInputStream!java.nio.channels.FileChannel#map(Ljava/nio/channels/FileChannel$MapMode;JJ)Ljava/nio/MappedByteBuffer;#map(Ljava/nio/channels/FileChannel$MapMode;JJ)Ljava/nio/MappedByteBuffer;
 */
public class java_io_FileInputStream_java_nio_channels_FileChannel_map_Ljava_nio_channels_FileChannel$MapMode_JJ_Ljava_nio_MappedByteBuffer__map_Ljava_nio_channels_FileChannel$MapMode_JJ_Ljava_nio_MappedByteBuffer_ extends DefaultHandler implements ResourceBuilder
{
    @Override
    public ResourceSpi createResource(ResourceSpec spec, Object args, Object instance, Object rv) 
    	throws UnknownResourceException, URISyntaxException
    {
        return null;
    }
}
