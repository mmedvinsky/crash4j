package com.crash4j.engine.spi.instrument.handlers;
import java.net.Socket;
import java.net.URISyntaxException;

import com.crash4j.engine.UnknownResourceException;
import com.crash4j.engine.spi.ResourceManagerSpi;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.protocol.ProtocolRecognizer;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.resources.impl.NetworkResourceSpiImpl;
import com.crash4j.engine.spi.traits.ResourceBuilder;
import com.crash4j.engine.spi.traits.ResourceClosure;
import com.crash4j.engine.spi.util.ArrayUtil;
import com.crash4j.engine.types.ActionClasses;


/**
 * 
 */

/**
 * Resource factory implementation for java.net.Socket!java.io.InputStream#read([BII)I;
 */
public class java_net_Socket_java_io_InputStream_read__BII_I_ extends any_Throughput_Integer implements ResourceBuilder
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
        int off = (Integer)ArrayUtil.get(args,  1);
        int len = (Integer)ArrayUtil.get(args,  2);
        ResourceManagerSpi.raiseProtocolEvent(pr.beforeRead(spi, b, off, len));
	}

	/**
	 * @see com.crash4j.engine.spi.instrument.handlers.any_Throughput_Integer#exit(com.crash4j.engine.spi.resources.ResourceSpi, com.crash4j.engine.spi.ResourceSpec, java.lang.Object, java.lang.Object, java.lang.Object, com.crash4j.engine.spi.traits.ResourceClosure, java.lang.Object, long, long, long)
	 */
	@Override
	public void exit(ResourceSpi spi, ResourceSpec spec, Object args,
			Object instance, Object rv, ResourceClosure c, Object ex,
			long testStartTime, long actualDt, long dt) 
	{
		super.exit(spi, spec, args, instance, rv, c, ex, testStartTime, actualDt, dt);
		
		ProtocolRecognizer pr = (ProtocolRecognizer)c.getData(ActionClasses.PROTOCOL.ordinal());
		if (ex != null)
		{
			ResourceManagerSpi.raiseProtocolEvent(pr.onError(spi, (Exception)ex));
		}
		else
		{
			//Get the instance of protocol recognizer
	        byte[] b = (byte[])ArrayUtil.get(args,  0);
	        int off = (Integer)ArrayUtil.get(args,  1);
	        int len = (Integer)ArrayUtil.get(args,  2);
	        ResourceManagerSpi.raiseProtocolEvent(pr.afterRead(spi, b, off, len, (Integer)rv));
		}
	}
}
