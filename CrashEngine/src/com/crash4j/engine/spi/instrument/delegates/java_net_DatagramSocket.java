/**
 * @copyright
 */
package com.crash4j.engine.spi.instrument.delegates;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.DatagramChannel;

import com.crash4j.engine.spi.ResourceManagerSpi;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.instrument.EventHandler;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.traits.ResourceAware;
import com.crash4j.engine.spi.util.ArrayUtil;

public class java_net_DatagramSocket extends DatagramSocket implements ResourceAware
{
    protected static ResourceSpec java_net_DatagramSocket_send = 
            ResourceManagerSpi.getByFullSignature("java.net.DatagramSocket#send(Ljava/net/DatagramPacket;)V");
    protected static ResourceSpec java_net_DatagramSocket_receive = 
            ResourceManagerSpi.getByFullSignature("java.net.DatagramSocket#receive(Ljava/net/DatagramPacket;)V");
    
    
    protected DatagramSocket sock;
    protected DatagramChannel channel;
    protected ResourceSpi __res = null;
    
    
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

	public java_net_DatagramSocket(DatagramSocket sock, DatagramChannel channel) throws SocketException
    {
        super((SocketAddress)null);
        this.channel = channel;
        this.sock = sock;
    }

    @Override
    public ResourceSpi getResource()
    {
        return __res;
    }

    @Override
    public void setResource(ResourceSpi res)
    {
        __res = res;
    }

    /**
     * @param arg0
     * @throws SocketException
     * @see java.net.DatagramSocket#bind(java.net.SocketAddress)
     */
    public void bind(SocketAddress sa) throws SocketException
    {
        if (sa == null)
        {
            return;
        }
        sock.bind(sa);
    }

    /**
     * 
     * @see java.net.DatagramSocket#close()
     */
    public void close()
    {
        sock.close();
    }

    /**
     * @param arg0
     * @param arg1
     * @see java.net.DatagramSocket#connect(java.net.InetAddress, int)
     */
    public void connect(InetAddress arg0, int arg1)
    {
        sock.connect(arg0, arg1);
    }

    /**
     * @param arg0
     * @throws SocketException
     * @see java.net.DatagramSocket#connect(java.net.SocketAddress)
     */
    public void connect(SocketAddress arg0) throws SocketException
    {
        sock.connect(arg0);
    }

    /**
     * 
     * @see java.net.DatagramSocket#disconnect()
     */
    public void disconnect()
    {
        sock.disconnect();
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
     * @throws SocketException
     * @see java.net.DatagramSocket#getBroadcast()
     */
    public boolean getBroadcast() throws SocketException
    {
        return sock.getBroadcast();
    }

    /**
     * @return
     * @see java.net.DatagramSocket#getChannel()
     */
    public DatagramChannel getChannel()
    {
        return this.channel;
    }

    /**
     * @return
     * @see java.net.DatagramSocket#getInetAddress()
     */
    public InetAddress getInetAddress()
    {
        return sock.getInetAddress();
    }

    /**
     * @return
     * @see java.net.DatagramSocket#getLocalAddress()
     */
    public InetAddress getLocalAddress()
    {
        return sock.getLocalAddress();
    }

    /**
     * @return
     * @see java.net.DatagramSocket#getLocalPort()
     */
    public int getLocalPort()
    {
        return sock.getLocalPort();
    }

    /**
     * @return
     * @see java.net.DatagramSocket#getLocalSocketAddress()
     */
    public SocketAddress getLocalSocketAddress()
    {
        return sock.getLocalSocketAddress();
    }

    /**
     * @return
     * @see java.net.DatagramSocket#getPort()
     */
    public int getPort()
    {
        return sock.getPort();
    }

    /**
     * @return
     * @throws SocketException
     * @see java.net.DatagramSocket#getReceiveBufferSize()
     */
    public int getReceiveBufferSize() throws SocketException
    {
        return sock.getReceiveBufferSize();
    }

    /**
     * @return
     * @see java.net.DatagramSocket#getRemoteSocketAddress()
     */
    public SocketAddress getRemoteSocketAddress()
    {
        return sock.getRemoteSocketAddress();
    }

    /**
     * @return
     * @throws SocketException
     * @see java.net.DatagramSocket#getReuseAddress()
     */
    public boolean getReuseAddress() throws SocketException
    {
        return sock.getReuseAddress();
    }

    /**
     * @return
     * @throws SocketException
     * @see java.net.DatagramSocket#getSendBufferSize()
     */
    public int getSendBufferSize() throws SocketException
    {
        return sock.getSendBufferSize();
    }

    /**
     * @return
     * @throws SocketException
     * @see java.net.DatagramSocket#getSoTimeout()
     */
    public int getSoTimeout() throws SocketException
    {
        return sock.getSoTimeout();
    }

    /**
     * @return
     * @throws SocketException
     * @see java.net.DatagramSocket#getTrafficClass()
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
     * @see java.net.DatagramSocket#isBound()
     */
    public boolean isBound()
    {
        return sock.isBound();
    }

    /**
     * @return
     * @see java.net.DatagramSocket#isClosed()
     */
    public boolean isClosed()
    {
        return sock.isClosed();
    }

    /**
     * @return
     * @see java.net.DatagramSocket#isConnected()
     */
    public boolean isConnected()
    {
        return sock.isConnected();
    }

    /**
     * @param arg0
     * @throws IOException
     * @see java.net.DatagramSocket#receive(java.net.DatagramPacket)
     */
    public void receive(DatagramPacket p) throws IOException
    {
        Object ref = null;
        Exception ee = null;
                        
        ResourceSpec sp = java_net_DatagramSocket_receive;
         
        try
        {
            Object arr = Array.newInstance(Object.class, 1);
            ArrayUtil.set(arr,  0, p);            
            ref = EventHandler.begin(sp.getId(), arr, this.sock);
            p = (DatagramPacket)EventHandler.handleParameter(ref, 0, p);
            sock.receive(p);
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
     * @throws IOException
     * @see java.net.DatagramSocket#send(java.net.DatagramPacket)
     */
    public void send(DatagramPacket p) throws IOException
    {
        Object ref = null;
        Exception ee = null;
                        
        ResourceSpec sp = java_net_DatagramSocket_send;
         
        try
        {
            Object arr = Array.newInstance(Object.class, 1);
            ArrayUtil.set(arr,  0, p);            
            ref = EventHandler.begin(sp.getId(), arr, this.sock);
            SocketAddress sd = (SocketAddress)EventHandler.handleParameter(ref, 0, p);
            sock.receive(p);
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
     * @throws SocketException
     * @see java.net.DatagramSocket#setBroadcast(boolean)
     */
    public void setBroadcast(boolean arg0) throws SocketException
    {
        sock.setBroadcast(arg0);
    }

    /**
     * @param arg0
     * @throws SocketException
     * @see java.net.DatagramSocket#setReceiveBufferSize(int)
     */
    public void setReceiveBufferSize(int arg0) throws SocketException
    {
        sock.setReceiveBufferSize(arg0);
    }

    /**
     * @param arg0
     * @throws SocketException
     * @see java.net.DatagramSocket#setReuseAddress(boolean)
     */
    public void setReuseAddress(boolean arg0) throws SocketException
    {
        sock.setReuseAddress(arg0);
    }

    /**
     * @param arg0
     * @throws SocketException
     * @see java.net.DatagramSocket#setSendBufferSize(int)
     */
    public void setSendBufferSize(int arg0) throws SocketException
    {
        sock.setSendBufferSize(arg0);
    }

    /**
     * @param arg0
     * @throws SocketException
     * @see java.net.DatagramSocket#setSoTimeout(int)
     */
    public void setSoTimeout(int arg0) throws SocketException
    {
        sock.setSoTimeout(arg0);
    }

    /**
     * @param arg0
     * @throws SocketException
     * @see java.net.DatagramSocket#setTrafficClass(int)
     */
    public void setTrafficClass(int arg0) throws SocketException
    {
        sock.setTrafficClass(arg0);
    }

    /**
     * @return
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return sock.toString();
    }

}
