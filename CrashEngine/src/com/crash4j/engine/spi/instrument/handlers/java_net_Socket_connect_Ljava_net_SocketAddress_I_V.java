package com.crash4j.engine.spi.instrument.handlers;
import java.lang.reflect.Array;
import java.net.InetSocketAddress;
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
 * Resource factory implementation for java.net.Socket#connect(Ljava/net/SocketAddress;I)V
 */
public class java_net_Socket_connect_Ljava_net_SocketAddress_I_V extends DefaultHandler implements ResourceBuilder
{
    @Override
    public ResourceSpi createResource(ResourceSpec spec, Object args, Object instance, Object rv) 
    	throws UnknownResourceException, URISyntaxException
    {
        InetSocketAddress host = (InetSocketAddress)Array.get(args, 0);
        return new NetworkResourceSpiImpl(spec, (Socket)instance, host.getHostName(), host.getPort(), null, 0);
    }
}
