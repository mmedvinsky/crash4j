package com.crash4j.engine.spi.instrument.handlers;
import java.net.URISyntaxException;

import com.crash4j.engine.UnknownResourceException;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.traits.ResourceBuilder;
import com.crash4j.engine.spi.traits.ResourceClosure;


/**
 * 
 */

/**
 * Resource factory implementation for java.net.InetAddress#getByAddress([B)Ljava/net/InetAddress;
 */
public class java_net_InetAddress_getByAddress__B_Ljava_net_InetAddress_ extends DefaultHandler implements ResourceBuilder
{
    @Override
    public ResourceSpi createResource(ResourceSpec spec, Object args, Object instance, Object rv) 
    	throws UnknownResourceException, URISyntaxException
    {
        return null;
    }
}
