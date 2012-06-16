/**
 * 
 */
package com.crash4j.engine.spi.resources.impl;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Proxy;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.regex.Pattern;

import javax.management.ObjectName;

import com.crash4j.engine.UnknownResourceException;
import com.crash4j.engine.spi.ResourceManagerSpi;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.context.ContextFrame;
import com.crash4j.engine.spi.context.ThreadContext;
import com.crash4j.engine.spi.log.Log;
import com.crash4j.engine.spi.log.LogFactory;
import com.crash4j.engine.spi.resources.ResourceSpi;

/**
 * @see ResourceSpi
 */
public class NetworkResourceSpiImpl2 extends ResourceSpiImpl
{
    protected static final byte []_default_address_mask = {0,0,0,0};
    protected InetAddress sourceAddress = getDefaultAddress();
    protected int sourcePort = 0;
    protected InetAddress destAddress = getDefaultAddress();
    protected int destPort = 0;
    protected NetworkInterface nInf = null;
    protected String protocol = null;
    protected Proxy proxy = Proxy.NO_PROXY;
    
    protected static final Log log = LogFactory.getLog(NetworkResourceSpiImpl2.class);

    
    /**
     * @param spec
     * @param uri
     */
    public NetworkResourceSpiImpl2(ResourceSpec spec, Socket sock)
    {
        super(spec);
        setDataFromSocket(sock);
        this.protocol = this.spec.getAttribute("protocol");
    }

    /**
     * @return default address initialization.
     */
    protected InetAddress getDefaultAddress()
    {
        try
        {
            return InetAddress.getByAddress(_default_address_mask);
        } 
        catch (UnknownHostException e)
        {
            log.logError("Failed to get default address");
        }
        return null;
    }
    
    
    
    /* (non-Javadoc)
	 * @see com.crash4j.engine.spi.resources.impl.ResourceSpiImpl#setLastAccess(long)
	 */
	@Override
	public void setLastAccess(long lastAccess) {
		// TODO Auto-generated method stub
		super.setLastAccess(lastAccess);
		//log.logError(this.getETag()+" "+lastAccess);
	}

	/**
     * Default {@link Socket} initialization constructor for resolved {@link Socket} instance
     * @param sock
     */
    private void setDataFromSocket(Socket sock)
    {
        try
        {            
            InetSocketAddress la = (InetSocketAddress)sock.getLocalSocketAddress();
            InetSocketAddress ra = (InetSocketAddress)sock.getRemoteSocketAddress();
            
            if (la != null)
            {
                this.nInf = NetworkInterface.getByInetAddress(la.getAddress());
                this.sourceAddress = la.getAddress();
                this.sourcePort = la.getPort();
            }
            if (ra != null)
            {
                this.destAddress = ra.getAddress();
                this.destPort = ra.getPort();
            }
            
            if (this.nInf == null)
            {
                this.nInf = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()); 
            }
        }
        catch (Exception e)
        {
            log.logError("Error recovering socket data!", e);
        }
    }
    
    /**
     * {@link DatagramSocket} processing
     * @param spec
     * @param uri
     */
    public NetworkResourceSpiImpl2(ResourceSpec spec, DatagramSocket sock, DatagramPacket packet, SocketAddress sa, boolean send)
    {
        super(spec);
        try
        {            
            InetSocketAddress la = (InetSocketAddress)sock.getLocalSocketAddress();
            InetSocketAddress ra = (InetSocketAddress)sock.getRemoteSocketAddress();
            
            if (ra == null && send == true)
            {
                if (packet != null)
                {
                    ra = (InetSocketAddress)packet.getSocketAddress();
                }
                if (sa != null)
                {
                    ra = (InetSocketAddress)sa;
                }
            }
                            
            if (la != null)
            {
                this.nInf = NetworkInterface.getByInetAddress(la.getAddress());
                this.sourceAddress = la.getAddress();
                this.sourcePort = la.getPort();
            }
            if (ra != null)
            {
                this.destAddress = ra.getAddress();
                this.destPort = ra.getPort();
            }
            if (this.nInf == null)
            {
                this.nInf = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()); 
            }            
            this.protocol = this.spec.getAttribute("protocol");
        }
        catch (Exception e)
        {
            log.logError("Error getting DatagramSocket socket information.", e);
        }
    }

    
    
    
    /**
     * @param spec
     * @param uri
     */
    public NetworkResourceSpiImpl2(ResourceSpec spec, ServerSocket sock)
    {
        super(spec);
        try
        {            
            InetSocketAddress la = (InetSocketAddress)sock.getLocalSocketAddress();
            
            if (la != null)
            {
                this.nInf = NetworkInterface.getByInetAddress(la.getAddress());
                this.sourceAddress = la.getAddress();
                this.sourcePort = la.getPort();
            }            
            
            if (this.nInf == null)
            {
                this.nInf = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()); 
            }
            
            this.protocol = this.spec.getAttribute("protocol");
        }
        catch (Exception e)
        {
            log.logError("Error getting ServerSocket socket information.", e);
        }
    }
    
    /**
     * @param spec
     * @param uri
     */
    public NetworkResourceSpiImpl2(ResourceSpec spec, Socket instance, Proxy proxy)
    {
        super(spec);
        try
        {
        	this.proxy = proxy;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    
    /**
     * @param spec
     * @param uri
     */
    public NetworkResourceSpiImpl2(ResourceSpec spec, Socket instance, String rHost, int rPort, String localAddr, int localPort)
    {
        super(spec);
        try
        {
            InetSocketAddress la = (InetSocketAddress)instance.getLocalSocketAddress();

            this.sourceAddress = InetAddress.getByName(localAddr);
            this.sourcePort = localPort;
            this.destAddress = InetAddress.getByName(rHost);
            this.destPort = rPort;
            
            
            if (la != null)
            {
                this.nInf = NetworkInterface.getByInetAddress(la.getAddress());
            }
            else if (sourceAddress != null)
            {
                this.nInf = NetworkInterface.getByInetAddress(sourceAddress);
            }
            else
            {
                this.nInf = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            }
            this.protocol = this.spec.getAttribute("protocol");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @param spec
     * @param uri
     */
    public NetworkResourceSpiImpl2(ResourceSpec spec, DatagramSocket instance, 
            String host, int port, String localAddr, int localPort)
    {
        super(spec);
        try
        {
            InetSocketAddress la = (InetSocketAddress)instance.getLocalSocketAddress();

            this.sourceAddress = InetAddress.getByName(localAddr);
            this.sourcePort = localPort;
            this.destAddress = InetAddress.getByName(host);
            this.destPort = port;
            
            
            if (la != null)
            {
                this.nInf = NetworkInterface.getByInetAddress(la.getAddress());
            }
            else if (sourceAddress != null)
            {
                this.nInf = NetworkInterface.getByInetAddress(this.sourceAddress);
            }
            else
            {
                this.nInf = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            }
            this.protocol = this.spec.getAttribute("protocol");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @param spec
     * @param uri
     */
    public NetworkResourceSpiImpl2(ResourceSpec spec, ServerSocket sock, 
            String host, int port, String localAddr, int localPort)
    {
        super(spec);
        try
        {
            InetSocketAddress la = (InetSocketAddress)sock.getLocalSocketAddress();
            this.sourcePort = localPort;
            this.sourceAddress = InetAddress.getLocalHost();
            if (la != null)
            {
                this.nInf = NetworkInterface.getByInetAddress(la.getAddress());
                this.sourceAddress = la.getAddress();
                this.sourcePort = la.getPort();
            }            
            if (this.nInf == null)
            {
                this.nInf = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()); 
            }
            this.protocol = this.spec.getAttribute("protocol");
        }
        catch (Exception e)
        {
            log.logError("Unable to get ServerSocket information", e);
        }
    }
    
    /**
     * @see ResourceManagerSpi#completeResource(com.crash4j.engine.spi.resources.ResourceSpi, ResourceSpec, Object, Object, Object)
     */
    public void completeResource(ResourceSpec spec, Object args, Object instance, Object rv)
    throws UnknownResourceException
    {
        if (this.isComplete())
        {
            return;
        }
        
        if (instance instanceof ServerSocket)
        {
            if (rv == null)
            {
                return;
            }
            //check the heap to make sure we are recovering at the accept point 
            //within the server socket.
            Iterator<ContextFrame> it = ThreadContext.frameIterator();
            while (it.hasNext())
            {
                ContextFrame frame = it.next();
                if (frame.isIgnore())
                {
                    continue;
                }
                ResourceSpec spc = frame.getResourceSpec();
                if (spc.getMethod().equalsIgnoreCase("accept"))
                {
                    setDataFromSocket((Socket)rv);
                }
            }
        }
        else if (instance instanceof Socket)
        {
            setDataFromSocket((Socket)instance);
        }
        this.setComplete(true);
    }

    
    /**
     * @see com.crash4j.engine.spi.resources.impl.ResourceSpiImpl#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        NetworkResourceSpiImpl2 ni = null;
        if (obj == null)
        {
            return false;
        }

        //If this is a etag lookup then check it out
        if (obj instanceof TagKey)
        {
            TagKey thisk = new TagKey(this.getResourceType().toString(), etag, hashCode());
            return thisk.equals(obj);
        }
        
        if (obj instanceof String)
        {
            TagKey thisk = new TagKey(this.getResourceType().toString(), etag, hashCode());
            return thisk.equals(obj);
        }
        
        if (obj instanceof NetworkResourceSpiImpl2)
        {
            ni = (NetworkResourceSpiImpl2)obj;
        }
        else
        {
            return false;
        }

        if (this.etag == ni.etag)
        {
            return true; //same instance.....
        }
        
        boolean c = (ni.isComplete() == this.isComplete()) && ni.proxy.equals(this.proxy) &&
                (ni.sourceAddress.equals(this.sourceAddress)) && 
                (ni.sourcePort == this.sourcePort) && 
                (ni.destPort == this.destPort) && 
                (ni.nInf.getName().equalsIgnoreCase(this.nInf.getName())) && 
                (ni.destAddress.equals(this.destAddress));
        return c; 
        
        /*
        boolean c = (ni.isComplete() == this.isComplete()); 
        boolean c1 = (ni.lHostname.equals(this.lHostname));
        boolean c2= (ni.lPort == this.lPort); 
        boolean c3 = (ni.rPort == this.rPort);
        boolean c4 = (ni.nInf.getName().equalsIgnoreCase(this.nInf.getName())); 
        boolean c5 = (ni.rHostname.equals(this.rHostname));
        if (c && c1 && c2 && c3 && c4 && c5)
        {
            return true;
        }
        return false;
        */
    }

    /**
     * @see com.crash4j.engine.spi.resources.impl.ResourceSpiImpl#hashCode()
     */
    @Override
    public int hashCode()
    {
        int rh = (this.getResourceType().hashCode() >> 16) | ((this.sourcePort + this.destPort) << 16);
        return rh;
    }

    /**
     * @see com.crash4j.engine.spi.resources.impl.ResourceSpiImpl#getVector()
     */
    @Override
    public ObjectName getVector()
    {
        return buildVector();
    }

    protected ObjectName buildVector()
    {
        try
        {
            Hashtable<String, String> details = new Hashtable<String, String>();
            
            //String cmode = this.spec.getAttribute("mode");            
            
            details.put("protocol", this.protocol);            
            //if (cmode != null)
            //{
            //    details.put("mode", cmode);
            //}            
            
            if (this.getParent() != null)
            {
            	details.put("parent", this.getParent().getETag());
            }
            
            if (this.nInf != null)
            {
                details.put("inf", this.nInf.getName());
            }
            
            if (this.sourceAddress != null)
            {
                details.put("la", this.sourceAddress.getCanonicalHostName());
                details.put("lp", new Integer(this.sourcePort).toString());
            }
            
            if (this.destAddress != null)
            {
                details.put("ra", this.destAddress.getCanonicalHostName());
                details.put("rp", new Integer(this.destPort).toString());
            }
            if (!this.proxy.equals(Proxy.NO_PROXY))
            {
                details.put("proxy", this.proxy.address().toString());
            }
            return ObjectName.getInstance(this.getResourceType().toString(), details);
        } 
        catch (Exception e)
        {
            log.logError("Failed to create resource moniker", e);
        }
        return null;
    }

    @Override
    public boolean hasProperty(String name)
    {
        return (name.equalsIgnoreCase("type")
                || name.equalsIgnoreCase("protocol") 
                || name.equalsIgnoreCase("la") 
                || name.equalsIgnoreCase("lp") 
                || name.equalsIgnoreCase("ra") 
                || name.equalsIgnoreCase("rp") 
                || name.equalsIgnoreCase("proxy") 
                || name.equalsIgnoreCase("inf"));
    }

    @Override
    public boolean match(String name, String value)
    {
        try
        {
            if (name.equalsIgnoreCase("type"))
            {
                return value.equalsIgnoreCase(getResourceType().toString());
            }
            else if (name.equalsIgnoreCase("protocol"))
            {
                return value.equalsIgnoreCase(this.protocol);
            }
            else if (name.equalsIgnoreCase("la"))
            {
                return sourceAddress.equals(InetAddress.getByName(value));
            }
            else if (name.equalsIgnoreCase("lp"))
            {
                return Integer.parseInt(value) == this.sourcePort;
            }
            else if (name.equalsIgnoreCase("ra"))
            {
                return destAddress.equals(InetAddress.getByName(value));
            }
            else if (name.equalsIgnoreCase("rp"))
            {
                return Integer.parseInt(value) == this.destPort;
            }
            else if (name.equalsIgnoreCase("inf"))
            {
                return value.equalsIgnoreCase(this.nInf.getName());
            }
            else if (name.equalsIgnoreCase("proxy"))
            {
                return value.equalsIgnoreCase(this.proxy.toString());
            }
        }
        catch (Exception e)
        {
            log.logError("Failed to match Simulations", e);
        }
        return false;
    }

    @Override
    public boolean match(String name, Pattern value)
    {
        try
        {
            if (name.equalsIgnoreCase("type"))
            {
                return value.matcher(getResourceType().toString()).matches();
            }
            else if (name.equalsIgnoreCase("protocol"))
            {
                return value.matcher(this.protocol).matches();
            }
            else if (name.equalsIgnoreCase("la"))
            {
                if (value.matcher(sourceAddress.getHostName()).matches())
                {
                    return true;
                }
                else if (value.matcher(sourceAddress.getHostAddress()).matches())
                {
                    return true;
                }
                return false;    
            }
            else if (name.equalsIgnoreCase("lp"))
            {
                return value.matcher(new Integer(this.sourcePort).toString()).matches();
            }
            else if (name.equalsIgnoreCase("ra"))
            {
                if (value.matcher(destAddress.getHostName()).matches())
                {
                    return true;
                }
                else if (value.matcher(destAddress.getHostAddress()).matches())
                {
                    return true;
                }
                return false;    
            }
            else if (name.equalsIgnoreCase("rp"))
            {
                return value.matcher(new Integer(this.destPort).toString()).matches();
            }
            else if (name.equalsIgnoreCase("inf"))
            {
                return value.matcher(this.nInf.getName()).matches();
            }
            else if (name.equalsIgnoreCase("proxy"))
            {
                return value.matcher(this.proxy.toString()).matches();
            }
        }
        catch (Exception e)
        {
            log.logError("Failed to match Simulations", e);
        }
        return false;
    }
}
