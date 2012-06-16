/**
 * 
 */
package com.crash4j.engine.spi.protocol.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import com.crash4j.engine.spi.ResourceManagerSpi;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.actions.ActionImpl;
import com.crash4j.engine.spi.instrument.handlers.RecognizedProtocolHandler;
import com.crash4j.engine.spi.log.Log;
import com.crash4j.engine.spi.log.LogFactory;
import com.crash4j.engine.spi.protocol.ProtocolEvent;
import com.crash4j.engine.spi.protocol.ProtocolRecognizer;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.resources.impl.HttpResourceSpiImpl;
import com.crash4j.engine.spi.resources.impl.NetworkResourceSpiImpl;
import com.crash4j.engine.spi.traits.LifecycleHandler;
import com.crash4j.engine.spi.traits.ProtocolBasedResourceFacotry;
import com.crash4j.engine.types.ActionClasses;
import com.crash4j.engine.types.ResourceTypes;
import com.crash4j.engine.types.classtypes.HTTPTypes;

/**
 */
public class GenericProtocolRecognizer implements ProtocolRecognizer, Callable<ProtocolEvent>
{
	
	protected static final Log log = LogFactory.getLog(GenericProtocolRecognizer.class);
	/**
	 * States
	 * 0: initial state
	 * 1: First write
	 * 2: subsequent writes
	 * 3: read|close|error: protocol match done here (raise event if nessesary
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
			/* s1 */	{  s2,  s3,  s3,  s3  },
			/* s2 */	{  s2,  s3,  s3,  s3  },
			/* s3 */	{  s1,  s0,  s0,  s0  },
	};
	
	/**
	 * Thread management pool.
	 */
	protected static ExecutorService service = Executors.newCachedThreadPool();
	
	protected PipedOutputStream pos = null;
	protected PipedInputStream pis = null;
	protected Future<ProtocolEvent> protocolFuture = null;
	protected int state = s0;
	protected long writeStartTime = 0;
	
	protected ResourceSpi resource = null;
	protected AtomicBoolean done = new AtomicBoolean(false);
	protected GenericProtocolLexer lexer = null;

	/**
	 * Default constructor
	 */
	public GenericProtocolRecognizer()
	{
		//Init lexer with default data
		lexer = new GenericProtocolLexer(new StringReader(""));
	}
	
	@Override
	public ProtocolEvent call() throws Exception 
	{
		ProtocolEvent pe = null;
		try
		{
			//Reset the current lexer to start processing data
			lexer.resetLexer(new InputStreamReader(pis));
			lexer.scan();
		}
		catch (Throwable t)
		{
		}
		
		if (lexer.hasProtocol())
		{
			ProtocolBasedResourceFacotry pbr =  (ProtocolBasedResourceFacotry)lexer.getActionClassType();
			ResourceSpi spi = pbr.createResource(resource, lexer.getExtractions());
			spi.setParent(resource);
			pe = new ProtocolEvent(spi, pbr.getSpec().getDefaultAction());
		}
		done.set(true);
		return pe;
	}
	
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
	 * @throws IOException 
	 * @throws TimeoutException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	protected ProtocolEvent runStates(int e, NetworkResourceSpiImpl spi, byte[] data, int offset, int len) 
			throws IOException, InterruptedException, ExecutionException, TimeoutException
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
				//Remember the start time of the protocol cycle
				writeStartTime = System.nanoTime();
				//Allocate enough to hold the first write completely....
				pis = new PipedInputStream(len);
				pos = new PipedOutputStream(pis);
				pos.write(data, offset, len);
				//submit work
				this.resource = spi;
				protocolFuture = service.submit(this);
				break;
			}
			case s2:
			{
				if (!this.done.get())
				{
					pos.write(data, offset, len);
				}
				break;
			}
			case s3:			
			{
				//Get ready made event here or nothing.
				pis.close();
				pos.close();
				
				ProtocolEvent ev = protocolFuture.get(1, TimeUnit.MICROSECONDS);
				if (ev == null)
				{
					return null;
				}
				
				ev.setEventStartTime(writeStartTime);
				ev.setEventStopTime(System.nanoTime());
				return ev;
			}
			default:
			{
				return null;
			}
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
			return runStates(GenericProtocolRecognizer.w, (NetworkResourceSpiImpl)spi, data, offset, written);
		} 
		catch (MalformedURLException e) 
		{
			log.logError("Error running protocol recognition", e);
		} 
		catch (IOException e) 
		{
			log.logError("Error running protocol recognition", e);
		} 
		catch (InterruptedException e) 
		{
			log.logError("Error running protocol recognition", e);
		} 
		catch (ExecutionException e) 
		{
			log.logError("Error running protocol recognition", e);
		} 
		catch (TimeoutException e) 
		{
			log.logError("Error running protocol recognition", e);
		}
		return null;
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
			return runStates(GenericProtocolRecognizer.r, (NetworkResourceSpiImpl)spi, data, offset, len);
		} 
		catch (MalformedURLException e) 
		{
			log.logError("Error running protocol recognition", e);
		}
		catch (IOException e) 
		{
			log.logError("Error running protocol recognition", e);
		} 
		catch (InterruptedException e) 
		{
			log.logError("Error running protocol recognition", e);
		} 
		catch (ExecutionException e) 
		{
			log.logError("Error running protocol recognition", e);
		} 
		catch (TimeoutException e) 
		{
			log.logError("Error running protocol recognition", e);
		}
		return null;
	}

	@Override
	public ProtocolEvent onClose(ResourceSpi spi) 
	{
		try {
			return runStates(GenericProtocolRecognizer.c, (NetworkResourceSpiImpl)spi, null, 0, 0);
		} 
		catch (MalformedURLException e) 
		{
			log.logError("Error running protocol recognition", e);
		}
		catch (IOException e) 
		{
			log.logError("Error running protocol recognition", e);
		} 
		catch (InterruptedException e) 
		{
			log.logError("Error running protocol recognition", e);
		} 
		catch (ExecutionException e) 
		{
			log.logError("Error running protocol recognition", e);
		} 
		catch (TimeoutException e) 
		{
			log.logError("Error running protocol recognition", e);
		}
		return null;
	}

	@Override
	public ProtocolEvent onError(ResourceSpi spi, Exception e1) 
	{
		try 
		{
			return runStates(GenericProtocolRecognizer.e, (NetworkResourceSpiImpl)spi, null, 0, 0);
		} 
		catch (MalformedURLException e) 
		{
			log.logError("Error running protocol recognition", e);
		}
		catch (IOException e) 
		{
			log.logError("Error running protocol recognition", e);
		} 
		catch (InterruptedException e) 
		{
			log.logError("Error running protocol recognition", e);
		} 
		catch (ExecutionException e) 
		{
			log.logError("Error running protocol recognition", e);
		} 
		catch (TimeoutException e) 
		{
			log.logError("Error running protocol recognition", e);
		}
		return null;
	}
}
