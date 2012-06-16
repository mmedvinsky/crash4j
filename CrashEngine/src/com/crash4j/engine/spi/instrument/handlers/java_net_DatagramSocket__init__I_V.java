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
 * Resource factory implementation for java.net.DatagramSocket#<init>(I)V
 */
public class java_net_DatagramSocket__init__I_V extends DefaultHandler implements ResourceBuilder
{
    @Override
    public ResourceSpi createResource(ResourceSpec spec, Object args, Object instance, Object rv) 
    	throws UnknownResourceException, URISyntaxException
    {
        //Integer port = (Integer)Array.get(args, 0);
        //return new NetworkResourceSpiImpl(spec, (DatagramSocket)instance, null, 0, "localhost", port);
        return null;
    }
}
