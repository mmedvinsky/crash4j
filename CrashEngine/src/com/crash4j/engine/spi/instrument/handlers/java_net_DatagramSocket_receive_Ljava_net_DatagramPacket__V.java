package com.crash4j.engine.spi.instrument.handlers;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.URISyntaxException;

import com.crash4j.engine.Action;
import com.crash4j.engine.UnknownResourceException;
import com.crash4j.engine.spi.ResourceManagerSpi;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.resources.impl.NetworkResourceSpiImpl;
import com.crash4j.engine.spi.stats.StatsManager;
import com.crash4j.engine.spi.traits.ResourceBuilder;
import com.crash4j.engine.spi.traits.ResourceClosure;
import com.crash4j.engine.spi.util.ArrayUtil;
import com.crash4j.engine.types.StatTypes;
import com.crash4j.engine.types.UnitTypes;


/**
 * 
 */

/**
 * Resource factory implementation for java.net.DatagramSocket#receive(Ljava/net/DatagramPacket;)V
 */
public class java_net_DatagramSocket_receive_Ljava_net_DatagramPacket__V extends DefaultHandler implements ResourceBuilder
{
    @Override
    public ResourceSpi createResource(ResourceSpec spec, Object args, Object instance, Object rv) 
    	throws UnknownResourceException, URISyntaxException
    {
        if (args == null)
        {
            return null;
        }
        DatagramPacket packet = (DatagramPacket)Array.get(args, 0);
        return new NetworkResourceSpiImpl(spec, (DatagramSocket)instance, packet, null, false);
    }

    
    /**
     * @see com.crash4j.engine.spi.instrument.handlers.DefaultHandler.spi.impl.DefaultMessageHandler#canCache()
     */
    @Override
    public boolean canCache()
    {
        return false;
    }


    /**
     * @see com.crash4j.engine.spi.spi.traits.LifecycleHandler#exit(com.crash4j.engine.spi.resources.spi.ResourceSpi, java.lang.Object, java.lang.Object, java.lang.Object, long)
     */
    @Override
    public void exit(ResourceSpi spi, ResourceSpec spec, Object args, Object instance, Object rv, ResourceClosure c, Object ex, long testStartTime, long actualDt, long dt)
    {
        super.exit(spi, spec, args, instance, rv, c, ex, testStartTime, actualDt, dt);
        Action action = this.getAction(spi, spec, args, instance, c);
        if (action == null)
        {
            return;
        }
        StatsManager sMgr = ResourceManagerSpi.getStatsManager();
        DatagramPacket b = (DatagramPacket)ArrayUtil.get(args,  0);
        sMgr.submit(spi, StatTypes.THROUGHPUT, UnitTypes.MICROSECONDS, action, dt, b.getLength());
    }
}
