package com.crash4j.engine.spi.instrument.handlers;
import java.io.FileInputStream;
import java.net.URISyntaxException;

import com.crash4j.engine.UnknownResourceException;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.resources.impl.FilesystemResourceSpiImpl;
import com.crash4j.engine.spi.traits.ResourceBuilder;
import com.crash4j.engine.spi.traits.ResourceClosure;


/**
 * 
 */

/**
 * Resource factory implementation for java.io.FileInputStream#read([BII)I
 */
public class java_io_FileInputStream_read__BII_I extends any_Throughput_Integer implements ResourceBuilder
{
    @Override
    public ResourceSpi createResource(ResourceSpec spec, Object args, Object instance, Object rv) 
    	throws UnknownResourceException, URISyntaxException
    {
        return new FilesystemResourceSpiImpl(spec, ((FileInputStream)instance));        
    }
}
