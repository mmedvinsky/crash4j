/**
 * 
 */
package com.crash4j.engine.spi.protocol.http;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.crash4j.engine.spi.ResourceManagerSpi;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.actions.ActionImpl;
import com.crash4j.engine.spi.instrument.handlers.RecognizedProtocolHandler;
import com.crash4j.engine.spi.protocol.ProtocolEvent;
import com.crash4j.engine.spi.protocol.ProtocolRecognizer;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.resources.impl.HttpResourceSpiImpl;
import com.crash4j.engine.spi.resources.impl.NetworkResourceSpiImpl;
import com.crash4j.engine.spi.traits.LifecycleHandler;
import com.crash4j.engine.types.ActionClasses;
import com.crash4j.engine.types.ResourceTypes;
import com.crash4j.engine.types.classtypes.HTTPTypes;

/**
 */
public class HttpProtocolRecognizer implements ProtocolRecognizer 
{
	/**
	 * States
	 * 0: initial state
	 * 1: running protocol recognition
	 * 2: protocol matched
	 * 3: Raise event
	 */
	protected static final int s0 = 0;
	protected static final int s1 = 1;
	protected static final int s2 = 2;
	protected static final int s3 = 3;
	
	/**
	 * events
	 * w: write occurred
	 * r: read occurred
	 * e: error occurred
	 * c: close stream occurred
	 */
	protected static final int w = 0;
	protected static final int r = 1;
	protected static final int e = 2;
	protected static final int c = 3;
	
	
	/** Transition table */
	int [][] t_table = {
			/*              w,   r,   e,   c  */ 
			/* s0 */	{  s1,  s0,  s0,  s0  },
			/* s1 */	{  s1,  s0,  s0,  s0  },
			/* s2 */	{  s2,  s3,  s3,  s3  },
			/* s3 */	{  s1,  s0,  s0,  s0  },
	};
	
	
	protected int state = s0;
	protected StringBuilder cdata = new StringBuilder();
	protected Pattern httpStatusLine = Pattern.compile("([A-Z]+)\\s+([^ ]+)\\s+(HTTP/[0-9][.][0-9]).*[\r\n].*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
	protected Matcher matcher = null;
	protected ProtocolEvent event = null;
	protected long writeStartTime = 0;

	/**
	 * @see com.crash4j.engine.spi.protocol.ProtocolRecognizer#beforeWrite(byte[], int, int)
	 */
	@Override
	public ProtocolEvent beforeWrite(ResourceSpi spi, byte[] data, int offset, int len) 
	{
		writeStartTime = System.nanoTime();
		return null;
	}

	/**
	 * Execute state machine
	 * 
	 * @param e
	 * @param data
	 * @param offset
	 * @param len
	 * @return
	 * @throws MalformedURLException 
	 */
	protected ProtocolEvent runStates(int e, NetworkResourceSpiImpl spi, byte[] data, int offset, int len) 
			throws MalformedURLException
	{
		if (!spi.isClient())
		{
			return null;
		}
		
		this.state = this.t_table[this.state][e];
		switch (this.state)
		{
		case s0:
		{
			break;
		}
		case s1:
		{
			if (writeStartTime <= 0)
			{
				writeStartTime = System.nanoTime();
			}
			
			String c = new String(data, offset, len);
			cdata.append(c);
			if (cdata.indexOf("HTTP/") != -1)
			{
				String [] details = cdata.toString().split("[ \r\n]");
				String method = details[0];
				String url = details[1];
				String version = details[2];
				
				URL u = new URL("http", spi.getHost().getHostName(), spi.getPort(), url);
				ResourceSpec spec = ResourceManagerSpi.createSpec("HTTP", method, "", "", false, (LifecycleHandler)new RecognizedProtocolHandler(), 
						new ActionImpl(method, ActionClasses.HTTP, HTTPTypes.valueOf(method)), 
						ResourceTypes.SERVICE, null, new HashSet<Class<?>>(), false, 0xFF);
				HttpResourceSpiImpl httRes = new HttpResourceSpiImpl(spec, u);
				event = new ProtocolEvent(httRes, spec.getDefaultAction());
				this.state = s2;
			}
			break;
		}
		case s2:
		{
			break;
		}
		case s3:			
		{
			event.setEventStartTime(writeStartTime);
			event.setEventStopTime(System.nanoTime());
			return event;
		}
		default:
			return null;
		}
		return null;
	}
	
	/**
	 * @see com.crash4j.engine.spi.protocol.ProtocolRecognizer#afterWrite(byte[], int, int, int, boolean)
	 */
	@Override
	public ProtocolEvent afterWrite(ResourceSpi spi, byte[] data, int offset, int len,
			int written) 
	{
		try 
		{
			return runStates(HttpProtocolRecognizer.w, (NetworkResourceSpiImpl)spi, data, offset, written);
		} 
		catch (MalformedURLException e) 
		{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.crash4j.engine.spi.protocol.ProtocolRecognizer#beforeRead(byte[], int, int)
	 */
	@Override
	public ProtocolEvent beforeRead(ResourceSpi spi, byte[] data, int offset, int len) 
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see com.crash4j.engine.spi.protocol.ProtocolRecognizer#afterRead(byte[], int, int, int, boolean)
	 */
	@Override
	public ProtocolEvent afterRead(ResourceSpi spi, byte[] data, int offset, int len, int read) 
	{
		try {
			return runStates(HttpProtocolRecognizer.r, (NetworkResourceSpiImpl)spi, data, offset, len);
		} catch (MalformedURLException e) {
			return null;
		}
	}

	@Override
	public ProtocolEvent onClose(ResourceSpi spi) 
	{
		try {
			return runStates(HttpProtocolRecognizer.c, (NetworkResourceSpiImpl)spi, null, 0, 0);
		} catch (MalformedURLException e) {
			return null;
		}
	}

	@Override
	public ProtocolEvent onError(ResourceSpi spi, Exception e) 
	{
		try {
			return runStates(HttpProtocolRecognizer.e, (NetworkResourceSpiImpl)spi, null, 0, 0);
		} catch (MalformedURLException e1) {
			return null;
		}
	}

	
	public static void main(String argv[])
	{
		HttpProtocolRecognizer rr = new HttpProtocolRecognizer();
		String s = "HEAD / HTTP/1.1\r\nUser-Agent: Java/1.6.0_29\r\nHost: www.google.com\r\nAccept: text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2\r\nConnection: keep-alive";
		
		Matcher mm = rr.httpStatusLine.matcher(s);
		if (mm.matches())
		{
			System.out.println(mm.group(1));
		}
	}
}
