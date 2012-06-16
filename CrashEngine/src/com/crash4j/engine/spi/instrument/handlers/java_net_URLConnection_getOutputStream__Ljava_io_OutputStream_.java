package com.crash4j.engine.spi.instrument.handlers;
import java.io.OutputStream;
import java.net.URISyntaxException;

import com.crash4j.engine.UnknownResourceException;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.instrument.delegates.java_io_OutputStream;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.traits.FacadeBuilder;
import com.crash4j.engine.spi.traits.ResourceBuilder;
import com.crash4j.engine.spi.traits.ResourceClosure;

/**
 * 
 */

/**
 * Resource factory implementation for java.net.URLConnection#getOutputStream()Ljava/io/OutputStream;
 */
public class java_net_URLConnection_getOutputStream__Ljava_io_OutputStream_ extends DefaultHandler implements ResourceBuilder, FacadeBuilder
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
        java_io_OutputStream iss = new java_io_OutputStream((OutputStream)rv, spec, instance);
        iss.setResource(res);
        return iss;
    }
}
