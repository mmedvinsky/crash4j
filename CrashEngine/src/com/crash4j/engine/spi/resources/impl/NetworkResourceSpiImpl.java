/**
 * 
 */
package com.crash4j.engine.spi.resources.impl;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.regex.Pattern;

import javax.management.ObjectName;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;

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
public class NetworkResourceSpiImpl extends ResourceSpiImpl
{
    protected static final byte []_default_address_mask = {0,0,0,0};
    //protected InetAddress sourceAddress = getDefaultAddress();
    //protected int sourcePort = 0;
    protected InetAddress host = getDefaultAddress();
    protected int port = 0;
    protected boolean client = true;
    protected String protocol = null;
    protected Proxy proxy = Proxy.NO_PROXY;
    protected boolean secure = false;
    protected static final Log log = LogFactory.getLog(NetworkResourceSpiImpl.class);

    
    /**
     * @param spec
     * @param uri
     */
    public NetworkResourceSpiImpl(ResourceSpec spec, Socket sock)
    {
        super(spec);
        setDataFromSocket(sock, true);
        secure = sock instanceof SSLSocket;
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
    
    
    
    /**
	 * @return the host
	 */
	public InetAddress getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(InetAddress host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the client
	 */
	public boolean isClient() {
		return client;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(boolean client) {
		this.client = client;
	}

	/**
	 * @return the protocol
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * @param protocol the protocol to set
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
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
     * @param boolean falg that select this socket as client or server.
     */
    private void setDataFromSocket(Socket sock, boolean cli)
    {
        try
        {            
            InetSocketAddress add = (InetSocketAddress)sock.getRemoteSocketAddress();            
        	if (cli)
        	{
	            add = (InetSocketAddress)sock.getRemoteSocketAddress();            
        	}
        	else
        	{
	            add = (InetSocketAddress)sock.getLocalSocketAddress();            
        	}
            if (add != null)
            {
                this.host = add.getAddress();
                this.port = add.getPort();
            }
            this.client = cli;
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
    public NetworkResourceSpiImpl(ResourceSpec spec, DatagramSocket sock, DatagramPacket packet, SocketAddress sa, boolean send)
    {
        super(spec);
        try
        {            
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
                            
            if (ra != null)
            {
                this.host = ra.getAddress();
                this.port = ra.getPort();
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
    public NetworkResourceSpiImpl(ResourceSpec spec, ServerSocket sock)
    {
        super(spec);
        try
        {            
            secure = sock instanceof SSLServerSocket;
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
    public NetworkResourceSpiImpl(ResourceSpec spec, Socket instance, Proxy proxy)
    {
        super(spec);
        try
        {
            secure = instance instanceof SSLSocket;
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
    public NetworkResourceSpiImpl(ResourceSpec spec, Socket instance, String rHost, int rPort, String localAddr, int localPort)
    {
        super(spec);
        try
        {
            this.host = InetAddress.getByName(rHost);
            this.port = rPort;
            this.protocol = this.spec.getAttribute("protocol");
            secure = instance instanceof SSLSocket;
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
    public NetworkResourceSpiImpl(ResourceSpec spec, DatagramSocket instance, 
            String host, int port, String localAddr, int localPort)
    {
        super(spec);
        try
        {
            this.host = InetAddress.getByName(host);
            this.port = port;
            this.protocol = this.spec.getAttribute("protocol");
            secure = false;
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
    public NetworkResourceSpiImpl(ResourceSpec spec, ServerSocket sock, 
            String host, int port, String localAddr, int localPort)
    {
        super(spec);
        try
        {
            secure = sock instanceof SSLServerSocket;
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
        
    	this.setLastAccess(System.currentTimeMillis());
        if (instance instanceof ServerSocket)
        {
            //check the heap to make sure we are recovering at the accept point 
            //within the server socket.
        	boolean isUnderAccept = false;
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
                    setDataFromSocket((Socket)rv, false);
                    isUnderAccept = true;
                }
            }
            if (!isUnderAccept)
            {
                try
                {           
                	ServerSocket sock = (ServerSocket)instance;
                    InetSocketAddress add = (InetSocketAddress)sock.getLocalSocketAddress();            
                    if (add != null)
                    {
                        this.host = add.getAddress();
                        this.port = add.getPort();
                    }
                    this.client = false;
                }
                catch (Exception e)
                {
                    log.logError("Error recovering socket data!", e);
                }
            }
        }
        else if (instance instanceof Socket)
        {
            setDataFromSocket((Socket)instance, true);
        }
        this.setComplete(true);
    }

    
    /**
     * @see com.crash4j.engine.spi.resources.impl.ResourceSpiImpl#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        NetworkResourceSpiImpl ni = null;
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
        
        if (obj instanceof NetworkResourceSpiImpl)
        {
            ni = (NetworkResourceSpiImpl)obj;
        }
        else
        {
            return false;
        }

        if (this.etag == ni.etag)
        {
            return true; //same instance.....
        }
        
        boolean c = ni.proxy.equals(this.proxy) &&
                (ni.port == this.port) && 
                (ni.client == this.client) && 
                (ni.secure == this.secure) && 
                (ni.host.equals(this.host));
        
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
        return this.port;
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
            
            if (this.host != null)
            {
                details.put("host", this.host.getCanonicalHostName());
                details.put("port", new Integer(this.port).toString());
                details.put("server", new Boolean(!this.client).toString());
                details.put("secure", new Boolean(this.secure).toString());
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
                || name.equalsIgnoreCase("host") 
                || name.equalsIgnoreCase("port") 
                || name.equalsIgnoreCase("server") 
                || name.equalsIgnoreCase("secure") 
                || name.equalsIgnoreCase("proxy"));
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
            else if (name.equalsIgnoreCase("host"))
            {
                return host.equals(InetAddress.getByName(value));
            }
            else if (name.equalsIgnoreCase("port"))
            {
                return Integer.parseInt(value) == this.port;
            }
            else if (name.equalsIgnoreCase("server"))
            {
                return Boolean.parseBoolean(value) == !this.client;
            }
            else if (name.equalsIgnoreCase("secure"))
            {
                return Boolean.parseBoolean(value) == this.secure;
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
            else if (name.equalsIgnoreCase("host"))
            {
                return (value.matcher(host.getHostName()).matches());
            }
            else if (name.equalsIgnoreCase("port"))
            {
                return value.matcher(String.valueOf(this.port)).matches();
            }
            else if (name.equalsIgnoreCase("server"))
            {
                return value.matcher(String.valueOf(!this.client)).matches();
            }
            else if (name.equalsIgnoreCase("secure"))
            {
                return value.matcher(String.valueOf(this.secure)).matches();
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
