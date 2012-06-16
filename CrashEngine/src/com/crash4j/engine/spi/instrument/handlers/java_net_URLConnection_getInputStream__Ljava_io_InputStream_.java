package com.crash4j.engine.spi.instrument.handlers;
import java.io.InputStream;
import java.net.URISyntaxException;

import com.crash4j.engine.UnknownResourceException;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.instrument.delegates.java_io_InputStream;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.traits.FacadeBuilder;
import com.crash4j.engine.spi.traits.ResourceBuilder;
import com.crash4j.engine.spi.traits.ResourceClosure;

/**
 * 
 */

/**
 * Resource factory implementation for java.net.URLConnection#getInputStream()Ljava/io/InputStream;
 */
public class java_net_URLConnection_getInputStream__Ljava_io_InputStream_ extends DefaultHandler implements ResourceBuilder, FacadeBuilder
{
    @Override
    public ResourceSpi createResource(ResourceSpec spec, Object args, Object instance, Object rv) 
    	throws UnknownResourceException, URISyntaxException
    {
        return null;
    }

    @Override
    public Object createFacade(ResourceSpec spec, ResourceSpi res, Object args, Object instance, Object rv, ResourceClosure c)
    {
        java_io_InputStream iss = new java_io_InputStream((InputStream)rv, spec, instance);
        iss.setResource(res);
        return iss;
    }
}
