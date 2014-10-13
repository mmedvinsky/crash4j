/**
 * 
 */
package com.crash4j.engine.spi.instrument.delegates;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.MembershipKey;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.nio.channels.spi.SelectorProvider;
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
public class java_nio_channels_DatagramChannel extends DatagramChannel implements ResourceAware
{
    protected ResourceSpi __res_ = null;
    protected DatagramChannel channel = null;
    protected static Method implCloseSelectableChannel = null;
    protected static Method implConfigureBlocking = null;

    protected static ResourceSpec java_net_DatagramSocket_java_nio_channels_DatagramChannel_send = ResourceManagerSpi
            .getByFullSignature("java.net.DatagramSocket!java.nio.channels.DatagramChannel#send(Ljava/nio/ByteBuffer;Ljava/net/SocketAddress;)I");
    protected static ResourceSpec java_net_DatagramSocket_java_nio_channels_DatagramChannel_read1 = ResourceManagerSpi
            .getByFullSignature("java.net.DatagramSocket!java.nio.channels.DatagramChannel#read([Ljava/nio/ByteBuffer;II)J");
    protected static ResourceSpec java_net_DatagramSocket_java_nio_channels_DatagramChannel_write1 = ResourceManagerSpi
            .getByFullSignature("java.net.DatagramSocket!java.nio.channels.DatagramChannel#write([Ljava/nio/ByteBuffer;II)J");
    protected static ResourceSpec java_net_DatagramSocket_java_nio_channels_DatagramChannel_socket = ResourceManagerSpi
            .getByFullSignature("java.net.DatagramSocket!java.nio.channels.DatagramChannel#socket()Ljava/net/DatagramSocket;");
    protected static ResourceSpec java_net_DatagramSocket_java_nio_channels_DatagramChannel_read2 = ResourceManagerSpi
            .getByFullSignature("java.net.DatagramSocket!java.nio.channels.DatagramChannel#read([Ljava/nio/ByteBuffer;)J");
    protected static ResourceSpec java_net_DatagramSocket_java_nio_channels_DatagramChannel_write2 = ResourceManagerSpi
            .getByFullSignature("java.net.DatagramSocket!java.nio.channels.DatagramChannel#write(Ljava/nio/ByteBuffer;)I");
    protected static ResourceSpec java_net_DatagramSocket_java_nio_channels_DatagramChannel_read = ResourceManagerSpi
            .getByFullSignature("java.net.DatagramSocket!java.nio.channels.DatagramChannel#read(Ljava/nio/ByteBuffer;)I");
    protected static ResourceSpec java_net_DatagramSocket_java_nio_channels_DatagramChannel_receive = ResourceManagerSpi
            .getByFullSignature("java.net.DatagramSocket!java.nio.channels.DatagramChannel#receive(Ljava/nio/ByteBuffer;)Ljava/net/SocketAddress;");

    protected static Log log = LogFactory.getLog(java_nio_channels_DatagramChannel.class);
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
     * @param remote
     * @return
     * @throws IOException
     * @see java.nio.channels.DatagramChannel#connect(java.net.SocketAddress)
     */
    public DatagramChannel connect(SocketAddress remote) throws IOException
    {
        return channel.connect(remote);
    }

    /**
     * @return
     * @throws IOException
     * @see java.nio.channels.DatagramChannel#disconnect()
     */
    public DatagramChannel disconnect() throws IOException
    {
        return channel.disconnect();
    }

    /**
     * @param arg0
     * @return
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object arg0)
    {
        return channel.equals(arg0);
    }

    /**
     * @return
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return channel.hashCode();
    }

    /**
     * @return
     * @see java.nio.channels.DatagramChannel#isConnected()
     */
    public boolean isConnected()
    {
        return channel.isConnected();
    }

    /**
     * @param dst
     * @return
     * @throws IOException
     * @see java.nio.channels.DatagramChannel#read(java.nio.ByteBuffer)
     */
    public int read(ByteBuffer dst) throws IOException
    {
        Object ref = null;
        Exception ee = null;
        int rv = 0;
        
        ResourceSpec sp = java_net_DatagramSocket_java_nio_channels_DatagramChannel_read;
         
        try
        {
            Object arr = Array.newInstance(Object.class, 1);
            ArrayUtil.set(arr,  0, dst);            
            ref = EventHandler.begin(sp.getId(), arr, channel.socket());
            dst = (ByteBuffer)EventHandler.handleParameter(ref, 0, dst);
            rv = channel.read(dst);
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
     * @param dsts
     * @param offset
     * @param length
     * @return
     * @throws IOException
     * @see java.nio.channels.DatagramChannel#read(java.nio.ByteBuffer[], int, int)
     */
    public long read(ByteBuffer[] dsts, int offset, int length) throws IOException
    {
        Object ref = null;
        Exception ee = null;
        long rv = 0;
        
        ResourceSpec sp = java_net_DatagramSocket_java_nio_channels_DatagramChannel_read1;
         
        try
        {
            Object arr = Array.newInstance(Object.class, 3);
            ArrayUtil.set(arr,  0, dsts);            
            ArrayUtil.set(arr,  1, offset);            
            ArrayUtil.set(arr,  2, length);            
            ref = EventHandler.begin(sp.getId(), arr, channel.socket());
            dsts = (ByteBuffer[])EventHandler.handleParameter(ref, 0, dsts);
            offset = EventHandler.handleParameter(ref, 0, offset);
            length = EventHandler.handleParameter(ref, 0, length);
            rv = channel.read(dsts, offset, length);
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
     * @param dst
     * @return
     * @throws IOException
     * @see java.nio.channels.DatagramChannel#receive(java.nio.ByteBuffer)
     */
    public SocketAddress receive(ByteBuffer dst) throws IOException
    {
        Object ref = null;
        Exception ee = null;
        SocketAddress rv = null;
        
        ResourceSpec sp = java_net_DatagramSocket_java_nio_channels_DatagramChannel_receive;
         
        try
        {
            Object arr = Array.newInstance(Object.class, 1);
            ArrayUtil.set(arr,  0, dst);            
            ref = EventHandler.begin(sp.getId(), arr, channel.socket());
            dst = (ByteBuffer)EventHandler.handleParameter(ref, 0, dst);
            rv = channel.receive(dst);
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
                rv = (SocketAddress)EventHandler.end(ref, ee, rv);
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
     * @param src
     * @param target
     * @return
     * @throws IOException
     * @see java.nio.channels.DatagramChannel#send(java.nio.ByteBuffer, java.net.SocketAddress)
     */
    public int send(ByteBuffer src, SocketAddress target) throws IOException
    {
        Object ref = null;
        Exception ee = null;
        int rv = 0;
        
        ResourceSpec sp = java_net_DatagramSocket_java_nio_channels_DatagramChannel_send;
         
        try
        {
            Object arr = Array.newInstance(Object.class, 2);
            ArrayUtil.set(arr,  0, src);            
            ArrayUtil.set(arr,  1, target);            
            ref = EventHandler.begin(sp.getId(), arr, channel.socket());
            src = (ByteBuffer)EventHandler.handleParameter(ref, 0, src);
            target = (SocketAddress)EventHandler.handleParameter(ref, 0, target);
            rv = channel.send(src, target);
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
     * @return
     * @see java.nio.channels.DatagramChannel#socket()
     */
    public DatagramSocket socket()
    {
        try
        {
            return new java_net_DatagramSocket(channel.socket(), this);
        } 
        catch (SocketException e)
        {
            return null;
        }
    }

    /**
     * @return
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return channel.toString();
    }

    /**
     * @param src
     * @return
     * @throws IOException
     * @see java.nio.channels.DatagramChannel#write(java.nio.ByteBuffer)
     */
    public int write(ByteBuffer src) throws IOException
    {
        Object ref = null;
        Exception ee = null;
        int rv = 0;
        
        ResourceSpec sp = java_net_DatagramSocket_java_nio_channels_DatagramChannel_write2;
         
        try
        {
            Object arr = Array.newInstance(Object.class, 1);
            ArrayUtil.set(arr,  0, src);            
            ref = EventHandler.begin(sp.getId(), arr, channel.socket());
            src = (ByteBuffer)EventHandler.handleParameter(ref, 0, src);
            rv = channel.write(src);
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
     * @param srcs
     * @param offset
     * @param length
     * @return
     * @throws IOException
     * @see java.nio.channels.DatagramChannel#write(java.nio.ByteBuffer[], int, int)
     */
    public long write(ByteBuffer[] srcs, int offset, int length) throws IOException
    {
        Object ref = null;
        Exception ee = null;
        long rv = 0;
        
        ResourceSpec sp = java_net_DatagramSocket_java_nio_channels_DatagramChannel_write1;
         
        try
        {
            Object arr = Array.newInstance(Object.class, 3);
            ArrayUtil.set(arr,  0, srcs);            
            ArrayUtil.set(arr,  1, offset);            
            ArrayUtil.set(arr,  2, length);            
            ref = EventHandler.begin(sp.getId(), arr, channel.socket());
            srcs = (ByteBuffer[])EventHandler.handleParameter(ref, 0, srcs);
            offset = EventHandler.handleParameter(ref, 0, offset);
            length = EventHandler.handleParameter(ref, 0, length);
            rv = channel.write(srcs, offset, length);
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

    public java_nio_channels_DatagramChannel(DatagramChannel channel)
    {
        super(SelectorProvider.provider());
        this.channel = channel;
    }

    /**
     * @see com.crash4j.engine.spi.spi.traits.ResourceAware#getResource()
     */
    @Override
    public ResourceSpi getResource()
    {
        return __res_;
    }

    /**
     * @see com.crash4j.engine.spi.spi.traits.ResourceAware#setResource(com.crash4j.engine.spi.ResourceSpi)
     */
    @Override
    public void setResource(ResourceSpi res)
    {
        __res_ = res;

    }

    @Override
    protected void implCloseSelectableChannel() throws IOException
    {
        try
        {
            if (implCloseSelectableChannel != null)
            {
                implCloseSelectableChannel.invoke(channel, (Object[])null);
            }
        }
        catch (Exception e)
        {
            throw new IOException(e);
        }
    }

    @Override
    protected void implConfigureBlocking(boolean block) throws IOException
    {
        try
        {
            if (implConfigureBlocking != null)
            {
                implConfigureBlocking.invoke(channel, (Object[])null);
            }
        } 
        catch (Exception e)
        {
            log.logError("Failed to call implCloseSelectableChannel", e);            
        }
    }

	@Override
	public MembershipKey join(InetAddress group, NetworkInterface interf)
			throws IOException 
	{
		return channel.join(group, interf);
	}

	@Override
	public MembershipKey join(InetAddress group, NetworkInterface interf,
			InetAddress source) throws IOException 
	{
		return channel.join(group, interf, source);
	}

	@Override
	public SocketAddress getLocalAddress() throws IOException 
	{
		return channel.getLocalAddress();
	}

	@Override
	public <T> T getOption(SocketOption<T> name) throws IOException 
	{
		return channel.getOption(name);
	}

	@Override
	public Set<SocketOption<?>> supportedOptions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DatagramChannel bind(SocketAddress local) throws IOException 
	{
		return channel.bind(local);
	}

	@Override
	public <T> DatagramChannel setOption(SocketOption<T> name, T value)
			throws IOException 
	{
		return channel.setOption(name, value);
	}

	@Override
	public SocketAddress getRemoteAddress() throws IOException 
	{
		return channel.getRemoteAddress();
	}

}
