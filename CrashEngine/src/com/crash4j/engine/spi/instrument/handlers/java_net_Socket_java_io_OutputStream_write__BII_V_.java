package com.crash4j.engine.spi.instrument.handlers;
import java.net.Socket;
import java.net.URISyntaxException;

import com.crash4j.engine.Action;
import com.crash4j.engine.UnknownResourceException;
import com.crash4j.engine.spi.ResourceManagerSpi;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.protocol.ProtocolRecognizer;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.resources.impl.NetworkResourceSpiImpl;
import com.crash4j.engine.spi.stats.StatsManager;
import com.crash4j.engine.spi.traits.ResourceBuilder;
import com.crash4j.engine.spi.traits.ResourceClosure;
import com.crash4j.engine.spi.util.ArrayUtil;
import com.crash4j.engine.types.ActionClasses;
import com.crash4j.engine.types.StatTypes;
import com.crash4j.engine.types.UnitTypes;


/**
 * 
 */

/**
 * Resource factory implementation for java.net.Socket!java.io.OutputStream#write([BII)V;
 */
public class java_net_Socket_java_io_OutputStream_write__BII_V_ extends DefaultHandler implements ResourceBuilder
{
    @Override
    public ResourceSpi createResource(ResourceSpec spec, Object args, Object instance, Object rv) 
    	throws UnknownResourceException, URISyntaxException
    {
        return new NetworkResourceSpiImpl(spec, (Socket)instance);
    }
    
	/**
	 * @see com.crash4j.engine.spi.instrument.handlers.DefaultHandler#eneter(com.crash4j.engine.spi.resources.ResourceSpi, com.crash4j.engine.spi.ResourceSpec, java.lang.Object, java.lang.Object, java.lang.Object, com.crash4j.engine.spi.traits.ResourceClosure)
	 */
	@Override
	public void eneter(ResourceSpi spi, ResourceSpec spec, Object args,
			Object instance, Object rv, ResourceClosure c) 
	{
		super.eneter(spi, spec, args, instance, rv, c);
		//Get the instance of protocol recognizer
		ProtocolRecognizer pr = (ProtocolRecognizer)c.getData(ActionClasses.PROTOCOL.ordinal());
		if (pr == null)
		{
			pr = ResourceManagerSpi.newProtocolRecognizer(spec.getResourceType());
			c.setData(ActionClasses.PROTOCOL.ordinal(), pr);
		}
        byte[] b = (byte[])ArrayUtil.get(args,  0);
        Integer off = (Integer)ArrayUtil.get(args,  1);
        Integer len = (Integer)ArrayUtil.get(args,  2);
        ResourceManagerSpi.raiseProtocolEvent(pr.beforeWrite(spi, b, off, len));
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
        byte[] b = (byte[])ArrayUtil.get(args,  0);
        Integer off = (Integer)ArrayUtil.get(args,  1);
        Integer len = (Integer)ArrayUtil.get(args,  2);
        
        sMgr.submit(spi, StatTypes.THROUGHPUT, UnitTypes.MICROSECONDS, action, dt, len);
        
		//Get the instance of protocol recognizer
		ProtocolRecognizer pr = (ProtocolRecognizer)c.getData(ActionClasses.PROTOCOL.ordinal());
		if (ex != null)
		{
			ResourceManagerSpi.raiseProtocolEvent(pr.onError(spi, (Exception)ex));
		}
		else
		{
			ResourceManagerSpi.raiseProtocolEvent(pr.afterWrite(spi, b, off, len, len));
		}
    }
}
