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
 * Resource factory implementation for java.io.File#<init>(Ljava/io/File;Ljava/lang/String;)V
 */
public class java_io_File__init__Ljava_io_File_Ljava_lang_String__V extends DefaultHandler implements ResourceBuilder
{
    @Override
    public ResourceSpi createResource(ResourceSpec spec, Object args, Object instance, Object rv) throws UnknownResourceException, URISyntaxException
    {
        File parent = (File)Array.get(args, 0);
        String child = (String)Array.get(args, 1);
        return new FilesystemResourceSpiImpl(spec, parent, child);
    }
}
