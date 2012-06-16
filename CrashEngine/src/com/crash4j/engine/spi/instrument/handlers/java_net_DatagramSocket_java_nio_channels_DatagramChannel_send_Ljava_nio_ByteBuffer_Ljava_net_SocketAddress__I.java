/**
 */
 
package com.crash4j.engine.spi.instrument.handlers;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;

import com.crash4j.engine.UnknownResourceException;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.resources.impl.NetworkResourceSpiImpl;
import com.crash4j.engine.spi.traits.ResourceBuilder;
import com.crash4j.engine.spi.traits.ResourceClosure;
import com.crash4j.engine.spi.util.ArrayUtil;


/**
 * This plug-in is generated to handle events produced by $key functional end-point
 * @see DefaultHandler to understand the default behavior of thi class.
 */

/**
 * Resource factory implementation for $class#java.nio.channels.DatagramChannel#send(Ljava/nio/ByteBuffer;Ljava/net/SocketAddress;)I
 */
public class java_net_DatagramSocket_java_nio_channels_DatagramChannel_send_Ljava_nio_ByteBuffer_Ljava_net_SocketAddress__I extends any_Throughput_Integer implements ResourceBuilder
{
    @Override
    public ResourceSpi createResource(ResourceSpec spec, Object args, 
    				Object instance, 
    				Object rv) throws UnknownResourceException, URISyntaxException
    {
        DatagramSocket ch = (DatagramSocket)instance;
        InetSocketAddress sa = (InetSocketAddress)ArrayUtil.get(args, 1);
        return new NetworkResourceSpiImpl(spec, ch, null, sa, true);
    }
    /**
     * @see com.crash4j.engine.spi.instrument.handlers.DefaultHandler.spi.impl.DefaultMessageHandler#canCache()
     */
    @Override
    public boolean canCache()
    {
        return false;
    }

}
