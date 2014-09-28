/**
 * @copyright
 */
package com.crash4j.engine.spi.remote;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.crash4j.engine.spi.context.ThreadContext;
import com.crash4j.engine.spi.json.JSONArray;
import com.crash4j.engine.spi.json.JSONBufferedStatsCollector;
import com.crash4j.engine.spi.log.Log;
import com.crash4j.engine.spi.log.LogFactory;

/**
 * {@link CrashServiceManager} will start a small web server within this runtime
 * bound to a specific port.  It will be primed with an API key of a client that is allowed to connect
 * to it.    
 * @author team
 *
 */
public class CrashServiceManager extends JSONBufferedStatsCollector implements Runnable
{
    protected Log log = LogFactory.getLog(CrashServiceManager.class);
    protected int port;
    protected String apikey;
    protected Thread serverThread = null;
    protected ServerSocket server = null;
    protected ExecutorService threadPool = Executors.newCachedThreadPool();
    protected boolean stopped = false;
    protected _client_handler client = null;
    /**
     * Handles client api communications
     */
    class _client_handler implements Callable<Boolean>
    {
    	protected SocketChannel client = null;
    	protected _client_handler(SocketChannel client)
    	{
    		this.client = client;
    	}
		@Override
		public Boolean call() throws Exception 
		{
			return null;
		}
    }
    
    /**
     * Initialization constructor
     * @param host
     * @param port
     * @param apikey
     */
    public CrashServiceManager(String host, int port, String apikey)
    {
        this.port = port;
        this.apikey = apikey;
    }
    
    /**
     * Initialization constructor
     * @param host
     * @param port
     * @param apikey
     */
    public CrashServiceManager()
    {
    }
    

    @Override
	public void start() 
    {
		super.start();
		/*start server listening thread */
		this.serverThread = new Thread(this);
		this.serverThread.setName("crush4jl_t");
		this.serverThread.start();
	}

	@Override
	public void stop() 
	{
		super.stop();
		stopped = true;
		this.serverThread.interrupt();
	}

	/**
     * @return the port
     */
    public int getPort()
    {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port)
    {
        this.port = port;
    }

    /**
     * @return the apikey
     */
    public String getApikey()
    {
        return apikey;
    }

    /**
     * @param apikey the apikey to set
     */
    public void setApikey(String apikey)
    {
        this.apikey = apikey;
    }

    /**
     * Send the current payload to the server
     * @param arr
     * @throws IOException 
     */
    protected void sendMessages(JSONArray arr) throws IOException
    {
        ThreadContext.beginIgnore();
        try
        {
            String payload = arr.toString();
            if (arr.length() == 0)
            {
            	return;
            }
        }
        finally
        {
            ThreadContext.endIgnore();
        }	
    }
    
    /**
     * @param os
     * @return
     */
    protected String readNextRecord(SocketChannel sc) throws IOException
    {
    	ByteBuffer buf = ByteBuffer.allocate(2048);
    	while (sc.read(buf) > 0)
    	{
    		
    	}
    	return null;
    }
    
	@Override
	public void run() 
	{
        ThreadContext.beginIgnore();
		try 
		{
			ServerSocketChannel sch = ServerSocketChannel.open();
			//Set the channel to be non-blocking
			SelectableChannel selector = sch.configureBlocking(true);
			sch.socket().bind(new InetSocketAddress(this.port));
			while (!stopped)
			{
				Socket clis = null;
				try
				{
					clis = sch.socket().accept();
				}
				catch (IOException e)
				{
					log.logError(e.getMessage(), e);
					continue;
				}
				
				if (clis != null)
				{
					SocketChannel channel = clis.getChannel();
					//1. Do the initial handshake.
					if (client != null)
					{
						;//Stream out connection error
						continue;
					}
					
				}
			}
		} 
		catch (IOException e) 
		{
			log.logError(e.getMessage(), e);
		}
		finally
		{
            ThreadContext.endIgnore();
		}
	}
 }
