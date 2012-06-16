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
 * Resource factory implementation for java.net.Socket!java.nio.channels.SocketChannel#connect(Ljava/net/SocketAddress;)Z
 */
public class java_net_Socket_java_nio_channels_SocketChannel_connect_Ljava_net_SocketAddress__Z extends DefaultHandler implements ResourceBuilder
{
    @Override
    public ResourceSpi createResource(ResourceSpec spec, Object args, Object instance, Object rv) 
    	throws UnknownResourceException, URISyntaxException
    {
        Socket s = (Socket)instance;        
        InetSocketAddress sa = (InetSocketAddress)s.getLocalSocketAddress();
        NetworkResourceSpiImpl r = null;
        InetSocketAddress rsa = (InetSocketAddress)Array.get(args,  0); 
        if (rsa == null)
        {
            return null;
        }
        if (sa == null)
        {
            r = new NetworkResourceSpiImpl(spec, (Socket)instance, rsa.getHostName(), rsa.getPort(), null, 0);
            r.setComplete(false);
        }
        return r;
    }

    /**
     * @see com.crash4j.engine.spi.instrument.handlers.DefaultHandler.spi.impl.DefaultMessageHandler#completeResource(com.crash4j.engine.spi.resources.spi.ResourceSpi, com.crash4j.engine.spi.ResourceSpec, java.lang.Object, java.lang.Object, java.lang.Object)
     */
    @Override
    public void completeResource(ResourceSpi current, ResourceSpec spec, Object args, Object instance, Object rv) throws UnknownResourceException
    {
        if (!current.isComplete())
        {
            NetworkResourceSpiImpl r = (NetworkResourceSpiImpl)current;
            r.completeResource(spec, args, instance, rv);
            r.setComplete(true);
        }
    }
}
