/**
 * 
 */
package com.crash4j.engine.spi.instrument.delegates;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.ServerSocketChannel;

import com.crash4j.engine.spi.ResourceManagerSpi;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.instrument.EventHandler;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.traits.ResourceAware;
import com.crash4j.engine.spi.util.ArrayUtil;

/**
 * 
 *
 */
public class java_net_ServerSocket extends ServerSocket implements ResourceAware
{
    protected ResourceSpi __res = null;
    protected ServerSocket ssock = null;
    protected ServerSocketChannel channel = null;
        
    protected static ResourceSpec java_net_ServerSocket_accept = 
        ResourceManagerSpi.getByFullSignature("java.net.ServerSocket#accept()Ljava/net/Socket;");
    protected static ResourceSpec java_net_ServerSocket_bind1 = 
            ResourceManagerSpi.getByFullSignature("java.net.ServerSocket#bind(Ljava/net/SocketAddress;)V");
    protected static ResourceSpec java_net_ServerSocket_bind2 = 
            ResourceManagerSpi.getByFullSignature("java.net.ServerSocket#bind(Ljava/net/SocketAddress;I)V");
        
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
     * @throws IOException
     */
    public java_net_ServerSocket(ServerSocket sock, ServerSocketChannel channel) throws IOException
    {
        this.ssock = (ServerSocket)sock;
        this.channel = channel;
    }

    /**
     * @return
     * @throws IOException
     * @see java.net.ServerSocket#accept()
     */
    public Socket accept() throws IOException
    {
        Object ref = null;
        Exception ee = null;
        Socket rv = null;
                        
        ResourceSpec sp = java_net_ServerSocket_accept;
         
        try
        {
            ref = EventHandler.begin(sp.getId(), null, ssock);
            rv = ssock.accept();
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
                rv = (Socket)EventHandler.end(ref, ee, rv);
            }
            catch (Throwable e)
            {
                if (ee == null)
                {
                    if (e instanceof IOException)
                    {
                        throw (IOException)e;
                    }
                }
            }
        }
        return rv;
    }


    /**
     * @param endpoint
     * @param backlog
     * @throws IOException
     * @see java.net.ServerSocket#bind(java.net.SocketAddress, int)
     */
    public void bind(SocketAddress endpoint, int backlog) throws IOException
    {
        Object ref = null;
        Exception ee = null;
                        
        ResourceSpec sp = java_net_ServerSocket_bind2;
         
        try
        {
            Object arr = Array.newInstance(Object.class, 2);
            ArrayUtil.set(arr,  0, endpoint);            
            ArrayUtil.set(arr,  1, backlog);       
            ref = EventHandler.begin(sp.getId(), arr, ssock);
            SocketAddress sd = (SocketAddress)EventHandler.handleParameter(ref, 0, endpoint);
            int bl = EventHandler.handleParameter(ref, 1, backlog);
            ssock.bind(sd, bl);
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
                }
            }
        }
    }


    /**
     * @param endpoint
     * @throws IOException
     * @see java.net.ServerSocket#bind(java.net.SocketAddress)
     */
    public void bind(SocketAddress endpoint) throws IOException
    {
        Object ref = null;
        Exception ee = null;
                        
        ResourceSpec sp = java_net_ServerSocket_bind1;
         
        try
        {
            Object arr = Array.newInstance(Object.class, 1);
            ArrayUtil.set(arr,  0, endpoint);            
            ref = EventHandler.begin(sp.getId(), arr, ssock);
            SocketAddress sd = (SocketAddress)EventHandler.handleParameter(ref, 0, endpoint);
            ssock.bind(sd);
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
                }
            }
        }
    }


    /**
     * @throws IOException
     * @see java.net.ServerSocket#close()
     */
    public void close() throws IOException
    {
        ssock.close();
    }


    /**
     * @param arg0
     * @return
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object arg0)
    {
        return ssock.equals(arg0);
    }


    /**
     * @return
     * @see java.net.ServerSocket#getChannel()
     */
    public ServerSocketChannel getChannel()
    {
        return this.channel;
    }


    /**
     * @return
     * @see java.net.ServerSocket#getInetAddress()
     */
    public InetAddress getInetAddress()
    {
        return ssock.getInetAddress();
    }


    /**
     * @return
     * @see java.net.ServerSocket#getLocalPort()
     */
    public int getLocalPort()
    {
        return ssock.getLocalPort();
    }


    /**
     * @return
     * @see java.net.ServerSocket#getLocalSocketAddress()
     */
    public SocketAddress getLocalSocketAddress()
    {
        return ssock.getLocalSocketAddress();
    }


    /**
     * @return
     * @throws SocketException
     * @see java.net.ServerSocket#getReceiveBufferSize()
     */
    public int getReceiveBufferSize() throws SocketException
    {
        return ssock.getReceiveBufferSize();
    }


    /**
     * @return
     * @throws SocketException
     * @see java.net.ServerSocket#getReuseAddress()
     */
    public boolean getReuseAddress() throws SocketException
    {
        return ssock.getReuseAddress();
    }


    /**
     * @return
     * @throws IOException
     * @see java.net.ServerSocket#getSoTimeout()
     */
    public int getSoTimeout() throws IOException
    {
        return ssock.getSoTimeout();
    }


    /**
     * @return
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return ssock.hashCode();
    }


    /**
     * @return
     * @see java.net.ServerSocket#isBound()
     */
    public boolean isBound()
    {
        return ssock.isBound();
    }


    /**
     * @return
     * @see java.net.ServerSocket#isClosed()
     */
    public boolean isClosed()
    {
        return ssock.isClosed();
    }


    /**
     * @param connectionTime
     * @param latency
     * @param bandwidth
     * @see java.net.ServerSocket#setPerformancePreferences(int, int, int)
     */
    public void setPerformancePreferences(int connectionTime, int latency, int bandwidth)
    {
        ssock.setPerformancePreferences(connectionTime, latency, bandwidth);
    }


    /**
     * @param size
     * @throws SocketException
     * @see java.net.ServerSocket#setReceiveBufferSize(int)
     */
    public void setReceiveBufferSize(int size) throws SocketException
    {
        ssock.setReceiveBufferSize(size);
    }


    /**
     * @param on
     * @throws SocketException
     * @see java.net.ServerSocket#setReuseAddress(boolean)
     */
    public void setReuseAddress(boolean on) throws SocketException
    {
        ssock.setReuseAddress(on);
    }


    /**
     * @param timeout
     * @throws SocketException
     * @see java.net.ServerSocket#setSoTimeout(int)
     */
    public void setSoTimeout(int timeout) throws SocketException
    {
        ssock.setSoTimeout(timeout);
    }


    /**
     * @return
     * @see java.net.ServerSocket#toString()
     */
    public String toString()
    {
        return ssock.toString();
    }



    /**
     * @see com.crash4j.engine.spi.spi.traits.ResourceAware#getResource()
     */
    @Override
    public ResourceSpi getResource()
    {
        // TODO Auto-generated method stub
        return __res;
    }

    /**
     * @see com.crash4j.engine.spi.spi.traits.ResourceAware#setResource(com.crash4j.engine.spi.ResourceSpi)
     */
    @Override
    public void setResource(ResourceSpi res)
    {
        __res = res;
    }

}
