package com.crash4j.engine.spi.instrument.handlers;
import java.io.FileDescriptor;
import java.lang.reflect.Array;
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
 * Resource factory implementation for java.io.FileOutputStream#<init>(Ljava/io/FileDescriptor;)V
 */
public class java_io_FileOutputStream__init__Ljava_io_FileDescriptor__V extends DefaultHandler implements ResourceBuilder
{
    @Override
    public ResourceSpi createResource(ResourceSpec spec, Object args, Object instance, Object rv) 
    	throws UnknownResourceException, URISyntaxException
    {
        Object v = Array.get(args, 0);
        if (v instanceof FileDescriptor)
        {
            FileDescriptor fd = (FileDescriptor)v;
            return new FilesystemResourceSpiImpl(spec, fd);
        } 
        return null;
    }
}
