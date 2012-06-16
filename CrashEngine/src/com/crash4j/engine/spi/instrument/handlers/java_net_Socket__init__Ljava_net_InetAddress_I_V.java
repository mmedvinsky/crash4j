package com.crash4j.engine.spi.instrument.handlers;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.Socket;
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
 * Resource factory implementation for java.net.Socket#<init>(Ljava/net/InetAddress;I)V
 */
public class java_net_Socket__init__Ljava_net_InetAddress_I_V extends DefaultHandler implements ResourceBuilder
{
    @Override
    public ResourceSpi createResource(ResourceSpec spec, Object args, Object instance, Object rv) 
    	throws UnknownResourceException, URISyntaxException
    {
        InetAddress host = (InetAddress)Array.get(args, 0);
        Integer port = (Integer)Array.get(args, 1);
        NetworkResourceSpiImpl r = new NetworkResourceSpiImpl(spec, (Socket)instance, host.getHostName(), port, null, 0);
      //MM. r.setComplete(false);
        return r;
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
        NetworkResourceSpiImpl ns = (NetworkResourceSpiImpl)current;
        ns.completeResource(spec, args, instance, rv);
    }
}
