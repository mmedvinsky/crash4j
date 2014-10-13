/**
 * 
 */
package com.crash4j.engine.spi.instrument.delegates;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.Set;

import com.crash4j.engine.spi.ResourceManagerSpi;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.instrument.EventHandler;
import com.crash4j.engine.spi.log.Log;
import com.crash4j.engine.spi.log.LogFactory;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.traits.ResourceAware;

/**
 * @author <MM>
 *
 */
public class java_nio_channels_ServerSocketChannel extends ServerSocketChannel implements ResourceAware
{
    protected ServerSocketChannel sc = null;
    protected static Method implCloseSelectableChannel = null;
    protected static Method implConfigureBlocking = null;
    protected ResourceSpi __res_ = null;
    protected static Log log = LogFactory.getLog(java_nio_channels_ServerSocketChannel.class);
    
    protected static ResourceSpec java_net_ServerSocket_java_nio_channels_ServerSocketChannel_accept = ResourceManagerSpi
            .getByFullSignature("java.net.ServerSocket!java.nio.channels.ServerSocketChannel#accept()Ljava/nio/channels/SocketChannel;");
    protected static ResourceSpec java_net_ServerSocket_java_nio_channels_ServerSocketChannel_close = ResourceManagerSpi
            .getByFullSignature("java.net.Socket!java.nio.channels.SocketChannel#implCloseSelectableChannel()V");    
    
    static 
    {
        try
        {
            implCloseSelectableChannel = AbstractSelectableChannel.class.getDeclaredMethod("implCloseSelectableChannel", (Class[])null);
            implCloseSelectableChannel.setAccessible(true);
            
            implConfigureBlocking = AbstractSelectableChannel.class.getDeclaredMethod("implConfigureBlocking", boolean.class);
            implConfigureBlocking.setAccessible(true);
        } 
        catch (Exception e) 
        {
            log.logError("Failed to enable accesss to protected methods", e);
        }        
    }
    
    /**
     * @param arg0
     */
    public java_nio_channels_ServerSocketChannel(ResourceSpi res, Object rv)
    {
        super(null);
        this.sc = (ServerSocketChannel)rv;
        this.setResource(res);
    }

    protected Object __data__ = null;
	@Override
	public Object getData() 
	{
		return __data__;
	}

	@Override
	public void setData(Object data) 
	{
		__data__ = data;
	}
    /**
     * @see java.nio.channels.ServerSocketChannel#accept()
     */
    @SuppressWarnings("finally")
    @Override
    public SocketChannel accept() throws IOException
    {
        Object ref = null;
        Exception ee = null;
        SocketChannel rv = null;
        ResourceSpec sp = java_net_ServerSocket_java_nio_channels_ServerSocketChannel_accept;         
        try
        {
            Object arr = Array.newInstance(Object.class, 1);
            ref = EventHandler.begin(sp.getId(), null, sc.socket());
            rv = sc.accept();
        }
        catch (Exception e)
        {
            ee = e;
            throw new IOException(e);
        }
        catch (Throwable e)
        {
        	IOException newe = new IOException(e);
            ee = newe;
            throw newe;
        }
        finally
        {
            try
            {
                rv = (SocketChannel)EventHandler.end(ref, ee, rv);
            }
            catch (Throwable e)
            {
                if (ee == null)
                {
                    if (e instanceof IOException)
                    {
                        throw (IOException)e;
                    }
                    else
                    {
                       log.logError("Unexpected exception", e);
                    }
                }
            }
        }
        return rv;
    }

    /**
     * @see java.nio.channels.ServerSocketChannel#socket()
     */
    @Override
    public ServerSocket socket()
    {
        try
        {
            return new java_net_ServerSocket(sc.socket(), this);
        } catch (IOException e)
        {
            log.logError("Error delegating the socket", e);
        }
        return null;
    }

    /**
     * @see java.nio.channels.spi.AbstractSelectableChannel#implCloseSelectableChannel()
     */
    @Override
    protected void implCloseSelectableChannel() throws IOException
    {
        Object ref = null;
        Exception ee = null;
        long rv = 0;
        
        ResourceSpec sp = java_net_ServerSocket_java_nio_channels_ServerSocketChannel_close;
         
        try
        {
            Object arr = Array.newInstance(Object.class, 1);
            ref = EventHandler.begin(sp.getId(), null, sc.socket());
            if (implCloseSelectableChannel != null)
            {
                implCloseSelectableChannel.invoke(sc, (Object[])null);
            }
        }
        catch (Exception e)
        {
            ee = e;
            throw new IOException(e);
        }
        catch (Throwable e)
        {
        	IOException newe = new IOException(e);
            ee = newe;
            throw newe;
        }
        finally
        {
            try
            {
                EventHandler.end(ref, ee);
            }
            catch (Throwable e)
            {
                if (ee == null)
                {
                    if (e instanceof IOException)
                    {
                        throw (IOException)e;
                    }
                    else
                    {
                       log.logError("Unexpected exception", e);
                    }
                }
            }
        }
    }

    /**
     * @see java.nio.channels.spi.AbstractSelectableChannel#implConfigureBlocking(boolean)
     */
    @Override
    protected void implConfigureBlocking(boolean block) throws IOException
    {
        try
        {
            if (implConfigureBlocking != null)
            {
                implConfigureBlocking.invoke(sc, (Object[])null);
            }
        } 
        catch (Exception e)
        {
            log.logError("Failed to call implCloseSelectableChannel", e);            
        }
    }

    @Override
    public ResourceSpi getResource()
    {
        return __res_;
    }

    @Override
    public void setResource(ResourceSpi res)
    {
        __res_ = res;
    }

	@Override
	public SocketAddress getLocalAddress() throws IOException 
	{
		return sc.getLocalAddress();
	}

	@Override
	public <T> T getOption(SocketOption<T> name) throws IOException 
	{
		return sc.getOption(name);
	}

	@Override
	public Set<SocketOption<?>> supportedOptions() 
	{
		return sc.supportedOptions();
	}

	@Override
	public ServerSocketChannel bind(SocketAddress local, int backlog)
			throws IOException 
	{
		return sc.bind(local);
	}

	@Override
	public <T> ServerSocketChannel setOption(SocketOption<T> name, T value)
			throws IOException 
	{
		return sc.setOption(name, value);
	}

}
