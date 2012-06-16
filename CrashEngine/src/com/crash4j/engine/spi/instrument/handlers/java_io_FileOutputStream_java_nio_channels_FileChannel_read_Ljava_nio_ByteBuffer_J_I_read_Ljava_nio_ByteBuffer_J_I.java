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
 * Resource factory implementation for java.io.FileOutputStream!java.nio.channels.FileChannel#read(Ljava/nio/ByteBuffer;J)I#read(Ljava/nio/ByteBuffer;J)I
 */
public class java_io_FileOutputStream_java_nio_channels_FileChannel_read_Ljava_nio_ByteBuffer_J_I_read_Ljava_nio_ByteBuffer_J_I 
       extends any_Throughput_Integer implements ResourceBuilder
{
    @Override
    public ResourceSpi createResource(ResourceSpec spec, Object args, Object instance, Object rv) 
    	throws UnknownResourceException, URISyntaxException
    {
        return null;
    }
}
