package com.crash4j.engine.spi.instrument.handlers;
import java.net.ServerSocket;
import java.net.URISyntaxException;

import com.crash4j.engine.UnknownResourceException;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.resources.impl.NetworkResourceSpiImpl;
import com.crash4j.engine.spi.traits.ResourceBuilder;
import com.crash4j.engine.spi.traits.ResourceClosure;


/**
 * 
 */

/**
 * Resource factory implementation for java.net.ServerSocket#accept()Ljava/net/Socket;
 */
public class java_net_ServerSocket_accept__Ljava_net_Socket_ extends DefaultHandler implements ResourceBuilder
{
    @Override
    public ResourceSpi createResource(ResourceSpec spec, Object args, Object instance, Object rv) 
    	throws UnknownResourceException, URISyntaxException
    {
        if (instance == null)
        {
            return null;
        }
        
        NetworkResourceSpiImpl netr = new NetworkResourceSpiImpl(spec, (ServerSocket)instance);
        netr.setComplete((((ServerSocket)instance).isBound()));
        return netr;
    }

    
    
    /**
     * @see com.crash4j.engine.spi.instrument.handlers.DefaultHandler.spi.impl.DefaultMessageHandler#completeResource(com.crash4j.engine.spi.resources.spi.ResourceSpi, com.crash4j.engine.spi.ResourceSpec, java.lang.Object, java.lang.Object, java.lang.Object)
     */
    @Override
    public void completeResource(ResourceSpi current, ResourceSpec spec, Object args, Object instance, Object rv) throws UnknownResourceException
    {
        if (rv == null)
        {
            return;
        }
        if (current.isComplete())
        {
            return;
        }
        NetworkResourceSpiImpl ns = (NetworkResourceSpiImpl)current;
        ns.completeResource(spec, args, instance, rv);
    }
}
