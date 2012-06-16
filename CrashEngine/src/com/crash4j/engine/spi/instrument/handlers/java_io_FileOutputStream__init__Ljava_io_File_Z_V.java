package com.crash4j.engine.spi.instrument.handlers;
import java.io.File;
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
 * Resource factory implementation for java.io.FileOutputStream#<init>(Ljava/io/File;Z)V
 */
public class java_io_FileOutputStream__init__Ljava_io_File_Z_V extends DefaultHandler implements ResourceBuilder
{
    @Override
    public ResourceSpi createResource(ResourceSpec spec, Object args, Object instance, Object rv) 
    	throws UnknownResourceException, URISyntaxException
    {
        return new FilesystemResourceSpiImpl(spec, (File)Array.get(args, 0));        
    }
}
