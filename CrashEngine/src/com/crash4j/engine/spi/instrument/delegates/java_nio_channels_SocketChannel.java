/**
 * 
 */
package com.crash4j.engine.spi.instrument.delegates;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
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
import com.crash4j.engine.spi.util.ArrayUtil;

/**
 * @author <MM>
 *
 */
public class java_nio_channels_SocketChannel extends SocketChannel  implements ResourceAware
{
    protected SocketChannel sc = null;
    protected static Method implCloseSelectableChannel = null;
    protected static Method implConfigureBlocking = null;
    
    protected static Log log = LogFactory.getLog(java_nio_channels_SocketChannel.class);
    
    protected ResourceSpi __res_ = null;

    protected static ResourceSpec java_net_Socket_java_nio_channels_SocketChannel_connect1 = ResourceManagerSpi
            .getByFullSignature("java.net.Socket!java.nio.channels.SocketChannel#connect(Ljava/net/SocketAddress;)Z");
    protected static ResourceSpec java_net_Socket_java_nio_channels_SocketChannel_read1 = ResourceManagerSpi
            .getByFullSignature("java.net.Socket!java.nio.channels.SocketChannel#read(Ljava/nio/ByteBuffer;)I");
    protected static ResourceSpec java_net_Socket_java_nio_channels_SocketChannel_read2 = ResourceManagerSpi
            .getByFullSignature("java.net.Socket!java.nio.channels.SocketChannel#read([Ljava/nio/ByteBuffer;II)J");
    protected static ResourceSpec java_net_Socket_java_nio_channels_SocketChannel_write1 = ResourceManagerSpi
            .getByFullSignature("java.net.Socket!java.nio.channels.SocketChannel#write(Ljava/nio/ByteBuffer;)I");
    protected static ResourceSpec java_net_Socket_java_nio_channels_SocketChannel_write2 = ResourceManagerSpi
            .getByFullSignature("java.net.Socket!java.nio.channels.SocketChannel#write([Ljava/nio/ByteBuffer;II)J");    
    protected static ResourceSpec java_net_Socket_java_nio_channels_SocketChannel_close = ResourceManagerSpi
            .getByFullSignature("java.net.Socket!java.nio.channels.SocketChannel#implCloseSelectableChannel()V");    
    
/**
 * java.nio.channels.SocketChannel#<init>(Ljava/nio/channels/spi/SelectorProvider;)V
java.nio.channels.SocketChannel#open()Ljava/nio/channels/SocketChannel;
java.nio.channels.SocketChannel#open(Ljava/net/SocketAddress;)Ljava/nio/channels/SocketChannel;
java.nio.channels.SocketChannel#validOps()I
java.nio.channels.SocketChannel#socket()Ljava/net/Socket;
java.nio.channels.SocketChannel#isConnected()Z
java.nio.channels.SocketChannel#isConnectionPending()Z
java.nio.channels.SocketChannel#connect(Ljava/net/SocketAddress;)Z
java.nio.channels.SocketChannel#finishConnect()Z
java.nio.channels.SocketChannel#read(Ljava/nio/ByteBuffer;)I
java.nio.channels.SocketChannel#read([Ljava/nio/ByteBuffer;II)J
java.nio.channels.SocketChannel#read([Ljava/nio/ByteBuffer;)J
java.nio.channels.SocketChannel#write(Ljava/nio/ByteBuffer;)I
java.nio.channels.SocketChannel#write([Ljava/nio/ByteBuffer;II)J
java.nio.channels.SocketChannel#write([Ljava/nio/ByteBuffer;)J
    
 */
    
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
     * @param arg0
     */
    public java_nio_channels_SocketChannel(ResourceSpec spec, ResourceSpi res, Object arg, Object owner, Object rv)
    {
        super(null);
        this.sc = (SocketChannel)rv;
        this.setResource(res);
        Socket s = this.sc.socket();
        if (s instanceof ResourceAware)
        {
            ResourceAware ra = (ResourceAware)s;
            ra.setResource(res);
        }
    }

    /**
     * @see java.nio.channels.SocketChannel#connect(java.net.SocketAddress)
     */
    @SuppressWarnings("finally")
    @Override
    public boolean connect(SocketAddress add) throws IOException
    {
        Object ref = null;
        Exception ee = null;
        boolean rv = false;
                        
        ResourceSpec sp = java_net_Socket_java_nio_channels_SocketChannel_connect1;
         
        try
        {
            Object arr = Array.newInstance(Object.class, 1);
            ArrayUtil.set(arr,  0, add);            
            ref = EventHandler.begin(sp.getId(), arr, sc.socket());
            add = (SocketAddress)EventHandler.handleParameter(ref, 0, add);
            rv = sc.connect(add);
        }
        catch (IOException e)
        {
            ee = e;
            throw e;
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
                rv = EventHandler.end(ref, ee, rv);
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
     * @see java.nio.channels.SocketChannel#finishConnect()
     */
    @Override
    public boolean finishConnect() throws IOException
    {
        return sc.finishConnect();
    }

    /**
     * @see java.nio.channels.SocketChannel#isConnected()
     */
    @Override
    public boolean isConnected()
    {
        return sc.isConnected();
    }

    /**
     * @see java.nio.channels.SocketChannel#isConnectionPending()
     */
    @Override
    public boolean isConnectionPending()
    {
        return sc.isConnectionPending();
    }

    /**
     * @see java.nio.channels.SocketChannel#read(java.nio.ByteBuffer)
     */
    @SuppressWarnings("finally")
    @Override
    public int read(ByteBuffer dst) throws IOException
    {
        Object ref = null;
        Exception ee = null;
        int rv = 0;
        
        ResourceSpec sp = java_net_Socket_java_nio_channels_SocketChannel_read1;
         
        try
        {
            Object arr = Array.newInstance(Object.class, 1);
            ArrayUtil.set(arr,  0, dst);            
            ref = EventHandler.begin(sp.getId(), arr, sc.socket());
            dst = (ByteBuffer)EventHandler.handleParameter(ref, 0, dst);
            rv = sc.read(dst);
        }
        catch (IOException e)
        {
            ee = e;
            throw e;
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
                rv = EventHandler.end(ref, ee, rv);
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
     * @see java.nio.channels.SocketChannel#read(java.nio.ByteBuffer[], int, int)
     */
    @SuppressWarnings("finally")
    @Override
    public long read(ByteBuffer[] dst, int offset, int length) throws IOException
    {
        Object ref = null;
        Exception ee = null;
        long rv = 0;
        
        ResourceSpec sp = java_net_Socket_java_nio_channels_SocketChannel_read2;
         
        try
        {
            Object arr = Array.newInstance(Object.class, 3);
            ArrayUtil.set(arr,  0, dst);            
            ArrayUtil.set(arr,  1, offset);            
            ArrayUtil.set(arr,  2, length);            
            ref = EventHandler.begin(sp.getId(), arr, sc.socket());
            dst = (ByteBuffer[])EventHandler.handleParameter(ref, 0, dst);
            offset = EventHandler.handleParameter(ref, 1, offset);
            length = EventHandler.handleParameter(ref, 2, length);
            rv = sc.read(dst, offset, length);
        }
        catch (IOException e)
        {
            ee = e;
            throw e;
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
                rv = EventHandler.end(ref, ee, rv);
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
     * @see java.nio.channels.SocketChannel#socket()
     */
    @Override
    public Socket socket()
    {
        return new java_net_Socket(sc.socket(), this);
    }

    /**
     * @see java.nio.channels.SocketChannel#write(java.nio.ByteBuffer)
     */
    @SuppressWarnings("finally")
    @Override
    public int write(ByteBuffer src) throws IOException
    {
        Object ref = null;
        Exception ee = null;
        int rv = 0;
        
        ResourceSpec sp = java_net_Socket_java_nio_channels_SocketChannel_write1;
         
        try
        {
            Object arr = Array.newInstance(Object.class, 1);
            ArrayUtil.set(arr,  0, src);                       
            ref = EventHandler.begin(sp.getId(), arr, sc.socket());
            src = (ByteBuffer)EventHandler.handleParameter(ref, 0, src);
            rv = sc.write(src);
       }
        catch (IOException e)
        {
            ee = e;
            throw e;
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
                rv = EventHandler.end(ref, ee, rv);
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
     * @see java.nio.channels.SocketChannel#write(java.nio.ByteBuffer[], int, int)
     */
    @SuppressWarnings("finally")
    @Override
    public long write(ByteBuffer[] src, int offset, int length) throws IOException
    {
        Object ref = null;
        Exception ee = null;
        long rv = 0;
        
        ResourceSpec sp = java_net_Socket_java_nio_channels_SocketChannel_write1;
         
        try
        {
            Object arr = Array.newInstance(Object.class, 1);
            ArrayUtil.set(arr,  0, src);                       
            ArrayUtil.set(arr,  1, offset);                       
            ArrayUtil.set(arr,  2, length);                       
            ref = EventHandler.begin(sp.getId(), arr, sc.socket());
            src = (ByteBuffer[])EventHandler.handleParameter(ref, 0, src);
            offset = EventHandler.handleParameter(ref, 1, offset);
            length = EventHandler.handleParameter(ref, 2, length);
            rv = sc.write(src, offset, length);
       }
        catch (IOException e)
        {
            ee = e;
            throw e;
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
                rv = EventHandler.end(ref, ee, rv);
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
     * @see java.nio.channels.spi.AbstractSelectableChannel#implCloseSelectableChannel()
     */
    @Override
    protected void implCloseSelectableChannel() throws IOException
    {
        Object ref = null;
        Exception ee = null;
        
        ResourceSpec sp = java_net_Socket_java_nio_channels_SocketChannel_close;
         
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
    protected void implConfigureBlocking(boolean arg0) throws IOException
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
	public SocketAddress getLocalAddress() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getOption(SocketOption<T> name) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<SocketOption<?>> supportedOptions() 
	{
		return sc.supportedOptions();
	}

	@Override
	public SocketChannel bind(SocketAddress local) throws IOException 
	{
		return sc.bind(local);
	}

	@Override
	public <T> SocketChannel setOption(SocketOption<T> name, T value)
			throws IOException 
	{
		return sc.setOption(name, value);
	}

	@Override
	public SocketChannel shutdownInput() throws IOException 
	{
		return sc.shutdownInput();
	}

	@Override
	public SocketChannel shutdownOutput() throws IOException 
	{
		return sc.shutdownOutput();
	}

	@Override
	public SocketAddress getRemoteAddress() throws IOException 
	{
		return sc.getRemoteAddress();
	}

}
