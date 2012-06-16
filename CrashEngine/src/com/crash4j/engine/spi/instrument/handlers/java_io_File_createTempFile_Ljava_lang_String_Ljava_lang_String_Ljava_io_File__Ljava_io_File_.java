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
 * 
 */

/**
 * Resource factory implementation for java.io.File#createTempFile(Ljava/lang/String;Ljava/lang/String;Ljava/io/File;)Ljava/io/File;
 */
public class java_io_File_createTempFile_Ljava_lang_String_Ljava_lang_String_Ljava_io_File__Ljava_io_File_ extends DefaultHandler implements ResourceBuilder
{
    /**
     * @see com.crash4j.engine.spi.instrument.handlers.DefaultHandler.spi.impl.DefaultMessageHandler#getUniqueKey(java.lang.Object, java.lang.Object, java.lang.Object)
     */
    @Override
    public Object getUniqueKey(Object instance, Object args, Object rv)
    {
        if (rv != null)
        {
            return super.getUniqueKey(rv, args, rv);
        }
        return null;
    }

    @Override
    public ResourceSpi createResource(ResourceSpec spec, Object args, Object instance, Object rv) 
    	throws UnknownResourceException, URISyntaxException
    {
        if (rv != null)
        {
            return new FilesystemResourceSpiImpl(spec, (File)instance);  
        }
        return new FilesystemResourceSpiImpl(spec);  
    }
    /**
     * @see com.crash4j.engine.spi.instrument.handlers.DefaultHandler.spi.impl.DefaultMessageHandler#completeResource(com.crash4j.engine.spi.resources.spi.ResourceSpi, com.crash4j.engine.spi.ResourceSpec, java.lang.Object, java.lang.Object, java.lang.Object)
     */
    @Override
    public void completeResource(ResourceSpi current, ResourceSpec spec, Object args, Object instance, Object rv) throws UnknownResourceException
    {
        if (current.isComplete())
        {
            return;
        }
        FilesystemResourceSpiImpl fs = (FilesystemResourceSpiImpl)current;
        fs.completeResource(spec, args, (File)rv);
    }
}
