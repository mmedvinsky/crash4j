package com.crash4j.engine.spi.instrument.handlers;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import com.crash4j.engine.UnknownResourceException;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.resources.impl.FilesystemResourceSpiImpl;
import com.crash4j.engine.spi.resources.impl.HttpResourceSpiImpl;
import com.crash4j.engine.spi.traits.ResourceBuilder;
import com.crash4j.engine.spi.traits.ResourceClosure;

/**
 * 
 */

/**
 * Resource factory implementation for java.net.URLConnection#<init>(Ljava/net/URL;)V
 */
public class java_net_URLConnection__init__Ljava_net_URL__V extends DefaultHandler implements ResourceBuilder
{
    @Override
    public ResourceSpi createResource(ResourceSpec spec, Object args, Object instance, Object rv) 
    	throws UnknownResourceException, URISyntaxException
    {
        URL url = (URL)Array.get(args, 0);
        if (url.getProtocol().equalsIgnoreCase("file"))
        {
            return new FilesystemResourceSpiImpl(spec, url.getFile());
        }
        else if (url.getProtocol().equalsIgnoreCase("http"))
        {
        	try 
        	{
				return new HttpResourceSpiImpl(spec, url);
			} 
        	catch (MalformedURLException e) 
			{
				URISyntaxException ee = new URISyntaxException(e.getMessage(), "");
				ee.initCause(e);
				throw ee;
			}
        }
        else if (url.getProtocol().equalsIgnoreCase("jar"))
        {
        	return new FilesystemResourceSpiImpl(spec, url.getFile());
        }
        	
        return null;
    }
}
