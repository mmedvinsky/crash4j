/**
 * @copyright
 */

package com.crash4j.engine.spi.instrument.handlers;
import java.io.File;
import java.net.URISyntaxException;

import com.crash4j.engine.UnknownResourceException;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.resources.impl.FilesystemResourceSpiImpl;
import com.crash4j.engine.spi.traits.ResourceBuilder;
import com.crash4j.engine.spi.traits.ResourceClosure;

/**
 * Resource handler that derives {@link DefaultHandler} and produces a file resource 
 * from {@link File} instance
 */
public class java_io_File_HandleWithInstance extends DefaultHandler implements ResourceBuilder
{
    @Override
    public ResourceSpi createResource(ResourceSpec spec, Object args, Object instance, Object rv) 
    	throws UnknownResourceException, URISyntaxException
    {
        return new FilesystemResourceSpiImpl(spec, (File)instance);        
    }
}
