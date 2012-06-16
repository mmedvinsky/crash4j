/**
 * 
 */
package com.crash4j.engine.spi.instrument.delegates;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.SocketChannel;

import com.crash4j.engine.spi.ResourceManagerSpi;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.instrument.EventHandler;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.traits.ResourceAware;
import com.crash4j.engine.spi.util.ArrayUtil;

/**
 * @author <MM>
 * 
 */
public class java_net_Socket extends Socket implements ResourceAware
{
    protected ResourceSpi __res = null;
    protected Socket sock = null;
    protected SocketChannel channel = null;
    
    protected static ResourceSpec java_net_Socket_connect = ResourceManagerSpi.getByFullSignature("java.net.Socket#connect(Ljava/net/SocketAddress;I)V");
    protected static ResourceSpec java_net_Socket_connect2 = ResourceManagerSpi.getByFullSignature("java.net.Socket#connect(Ljava/net/SocketAddress;)V");
    protected static ResourceSpec java_net_Socket_close = ResourceManagerSpi.getByFullSignature("java.net.Socket#close()V");
    protected static ResourceSpec java_net_Socket_getOutptuStream = ResourceManagerSpi
            .getByFullSignature("java.net.Socket#getOutputStream()Ljava/io/OutputStream;");
    protected static ResourceSpec java_net_Socket_getInputStream = ResourceManagerSpi.getByFullSignature("java.net.Socket#getInputStream()Ljava/io/InputStream;");
    protected static ResourceSpec java_net_Socket_getChannel = ResourceManagerSpi
            .getByFullSignature("java.net.Socket#getChannel()Ljava/nio/channels/SocketChannel;");

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
         * 
         */
    public java_net_Socket(Socket s, SocketChannel channel)
    {
        this.sock = s;
        this.channel = channel;
    }

    /**
     * @see com.crash4j.engine.spi.spi.traits.ResourceAware#getResource()
     */
    @Override
    public ResourceSpi getResource()
    {
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

    /**
     * @param bindpoint
     * @throws IOException
     * @see java.net.Socket#bind(java.net.SocketAddress)
     */
    public void bind(SocketAddress bindpoint) throws IOException
    {
        sock.bind(bindpoint);
    }

    /**
     * @throws IOException
     * @see java.net.Socket#close()
     */
    public void close() throws IOException
    {
        Object ref = null;
        Exception ee = null;
                        
        ResourceSpec sp = java_net_Socket_close;
         
        try
        {
            ref = EventHandler.begin(sp.getId(), null, sock);
            sock.close();
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
     * @param timeout
     * @throws IOException
     * @see java.net.Socket#connect(java.net.SocketAddress, int)
     */
    public void connect(SocketAddress endpoint, int timeout) throws IOException
    {
        Object ref = null;
        Exception ee = null;
                        
        ResourceSpec sp = java_net_Socket_connect;
         
        try
        {
            Object arr = Array.newInstance(Object.class, 2);
            ArrayUtil.set(arr,  0, endpoint);            
            ArrayUtil.set(arr,  1, timeout);       
            ref = EventHandler.begin(sp.getId(), arr, this.sock);
            SocketAddress sd = (SocketAddress)EventHandler.handleParameter(ref, 0, endpoint);
            int bl = EventHandler.handleParameter(ref, 1, timeout);
            sock.connect(endpoint, timeout);
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
     * @see java.net.Socket#connect(java.net.SocketAddress)
     */
    public void connect(SocketAddress endpoint) throws IOException
    {
        Object ref = null;
        Exception ee = null;
                        
        ResourceSpec sp = java_net_Socket_connect2;
         
        try
        {
            Object arr = Array.newInstance(Object.class, 1);
            ArrayUtil.set(arr,  0, endpoint);            
            ref = EventHandler.begin(sp.getId(), arr, this.sock);
            SocketAddress sd = (SocketAddress)EventHandler.handleParameter(ref, 0, endpoint);
            sock.connect(endpoint);
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
     * @param arg0
     * @return
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object arg0)
    {
        return sock.equals(arg0);
    }

    /**
     * @return
     * @see java.net.Socket#getChannel()
     */
    public SocketChannel getChannel()
    {
        return this.channel;
    }

    /**
     * @return
     * @see java.net.Socket#getInetAddress()
     */
    public InetAddress getInetAddress()
    {
        return sock.getInetAddress();
    }

    /**
     * @return
     * @throws IOException
     * @see java.net.Socket#getInputStream()
     */
    public InputStream getInputStream() throws IOException
    {
        java_io_InputStream is = new java_io_InputStream(sock.getInputStream(), null, this);
        is.setResource(this.getResource());
        return is;
    }

    /**
     * @return
     * @throws SocketException
     * @see java.net.Socket#getKeepAlive()
     */
    public boolean getKeepAlive() throws SocketException
    {
        return sock.getKeepAlive();
    }

    /**
     * @return
     * @see java.net.Socket#getLocalAddress()
     */
    public InetAddress getLocalAddress()
    {
        return sock.getLocalAddress();
    }

    /**
     * @return
     * @see java.net.Socket#getLocalPort()
     */
    public int getLocalPort()
    {
        return sock.getLocalPort();
    }

    /**
     * @return
     * @see java.net.Socket#getLocalSocketAddress()
     */
    public SocketAddress getLocalSocketAddress()
    {
        return sock.getLocalSocketAddress();
    }

    /**
     * @return
     * @throws SocketException
     * @see java.net.Socket#getOOBInline()
     */
    public boolean getOOBInline() throws SocketException
    {
        return sock.getOOBInline();
    }

    /**
     * @return
     * @throws IOException
     * @see java.net.Socket#getOutputStream()
     */
    public OutputStream getOutputStream() throws IOException
    {
        java_io_OutputStream os = new java_io_OutputStream(getOutputStream(), null, this);
        os.setResource(this.getResource());
        return os;
    }

    /**
     * @return
     * @see java.net.Socket#getPort()
     */
    public int getPort()
    {
        return sock.getPort();
    }

    /**
     * @return
     * @throws SocketException
     * @see java.net.Socket#getReceiveBufferSize()
     */
    public int getReceiveBufferSize() throws SocketException
    {
        return sock.getReceiveBufferSize();
    }

    /**
     * @return
     * @see java.net.Socket#getRemoteSocketAddress()
     */
    public SocketAddress getRemoteSocketAddress()
    {
        return sock.getRemoteSocketAddress();
    }

    /**
     * @return
     * @throws SocketException
     * @see java.net.Socket#getReuseAddress()
     */
    public boolean getReuseAddress() throws SocketException
    {
        return sock.getReuseAddress();
    }

    /**
     * @return
     * @throws SocketException
     * @see java.net.Socket#getSendBufferSize()
     */
    public int getSendBufferSize() throws SocketException
    {
        return sock.getSendBufferSize();
    }

    /**
     * @return
     * @throws SocketException
     * @see java.net.Socket#getSoLinger()
     */
    public int getSoLinger() throws SocketException
    {
        return sock.getSoLinger();
    }

    /**
     * @return
     * @throws SocketException
     * @see java.net.Socket#getSoTimeout()
     */
    public int getSoTimeout() throws SocketException
    {
        return sock.getSoTimeout();
    }

    /**
     * @return
     * @throws SocketException
     * @see java.net.Socket#getTcpNoDelay()
     */
    public boolean getTcpNoDelay() throws SocketException
    {
        return sock.getTcpNoDelay();
    }

    /**
     * @return
     * @throws SocketException
     * @see java.net.Socket#getTrafficClass()
     */
    public int getTrafficClass() throws SocketException
    {
        return sock.getTrafficClass();
    }

    /**
     * @return
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return sock.hashCode();
    }

    /**
     * @return
     * @see java.net.Socket#isBound()
     */
    public boolean isBound()
    {
        return sock.isBound();
    }

    /**
     * @return
     * @see java.net.Socket#isClosed()
     */
    public boolean isClosed()
    {
        return sock.isClosed();
    }

    /**
     * @return
     * @see java.net.Socket#isConnected()
     */
    public boolean isConnected()
    {
        return sock.isConnected();
    }

    /**
     * @return
     * @see java.net.Socket#isInputShutdown()
     */
    public boolean isInputShutdown()
    {
        return sock.isInputShutdown();
    }

    /**
     * @return
     * @see java.net.Socket#isOutputShutdown()
     */
    public boolean isOutputShutdown()
    {
        return sock.isOutputShutdown();
    }

    /**
     * @param data
     * @throws IOException
     * @see java.net.Socket#sendUrgentData(int)
     */
    public void sendUrgentData(int data) throws IOException
    {
        sock.sendUrgentData(data);
    }

    /**
     * @param on
     * @throws SocketException
     * @see java.net.Socket#setKeepAlive(boolean)
     */
    public void setKeepAlive(boolean on) throws SocketException
    {
        sock.setKeepAlive(on);
    }

    /**
     * @param on
     * @throws SocketException
     * @see java.net.Socket#setOOBInline(boolean)
     */
    public void setOOBInline(boolean on) throws SocketException
    {
        sock.setOOBInline(on);
    }

    /**
     * @param connectionTime
     * @param latency
     * @param bandwidth
     * @see java.net.Socket#setPerformancePreferences(int, int, int)
     */
    public void setPerformancePreferences(int connectionTime, int latency, int bandwidth)
    {
        sock.setPerformancePreferences(connectionTime, latency, bandwidth);
    }

    /**
     * @param size
     * @throws SocketException
     * @see java.net.Socket#setReceiveBufferSize(int)
     */
    public void setReceiveBufferSize(int size) throws SocketException
    {
        sock.setReceiveBufferSize(size);
    }

    /**
     * @param on
     * @throws SocketException
     * @see java.net.Socket#setReuseAddress(boolean)
     */
    public void setReuseAddress(boolean on) throws SocketException
    {
        sock.setReuseAddress(on);
    }

    /**
     * @param size
     * @throws SocketException
     * @see java.net.Socket#setSendBufferSize(int)
     */
    public void setSendBufferSize(int size) throws SocketException
    {
        sock.setSendBufferSize(size);
    }

    /**
     * @param on
     * @param linger
     * @throws SocketException
     * @see java.net.Socket#setSoLinger(boolean, int)
     */
    public void setSoLinger(boolean on, int linger) throws SocketException
    {
        sock.setSoLinger(on, linger);
    }

    /**
     * @param timeout
     * @throws SocketException
     * @see java.net.Socket#setSoTimeout(int)
     */
    public void setSoTimeout(int timeout) throws SocketException
    {
        sock.setSoTimeout(timeout);
    }

    /**
     * @param on
     * @throws SocketException
     * @see java.net.Socket#setTcpNoDelay(boolean)
     */
    public void setTcpNoDelay(boolean on) throws SocketException
    {
        sock.setTcpNoDelay(on);
    }

    /**
     * @param tc
     * @throws SocketException
     * @see java.net.Socket#setTrafficClass(int)
     */
    public void setTrafficClass(int tc) throws SocketException
    {
        sock.setTrafficClass(tc);
    }

    /**
     * @throws IOException
     * @see java.net.Socket#shutdownInput()
     */
    public void shutdownInput() throws IOException
    {
        sock.shutdownInput();
    }

    /**
     * @throws IOException
     * @see java.net.Socket#shutdownOutput()
     */
    public void shutdownOutput() throws IOException
    {
        sock.shutdownOutput();
    }

    /**
     * @return
     * @see java.net.Socket#toString()
     */
    public String toString()
    {
        return sock.toString();
    }

}
