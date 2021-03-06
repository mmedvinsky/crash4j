package com.crash4j.engine.spi.instrument.handlers;
import java.lang.reflect.Array;
import java.net.URI;
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
 * Resource factory implementation for java.io.File#<init>(Ljava/lang/String;)V
 */
public class java_io_File__init__Ljava_lang_String__V extends DefaultHandler implements ResourceBuilder
{
    @Override
    public ResourceSpi createResource(ResourceSpec spec, Object args, Object instance, Object rv) throws UnknownResourceException, URISyntaxException
    {
        String uri = (String)Array.get(args, 0);
        URI u = new URI(uri);
        return new FilesystemResourceSpiImpl(spec, u.getRawSchemeSpecificPart());
    }
}
