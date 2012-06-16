/**
 * 
 */
package com.crash4j.engine.spi.inf;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <MM>
 *
 */
public class Endpoint
{
    protected InetAddress address = null;
    protected ConcurrentHashMap<Endpoint, Endpoint> hops = new ConcurrentHashMap<Endpoint, Endpoint>();
    
    /**
     * Single network end-point
     */
    public Endpoint(InetAddress address)
    {
        this.address = address;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof InetAddress)
        {
            return this.address.equals(obj);
        }
        if (obj instanceof Endpoint)
        {
            return this.address.equals(((Endpoint)obj).getAddress());
        }
        return super.equals(obj);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return address.hashCode();
    }

    /**
     * Single network end-point
     * @throws UnknownHostException 
     */
    public Endpoint(String name) throws UnknownHostException
    {
        this.address = InetAddress.getByName(name);
    }

    /**
     * @return the address
     */
    public InetAddress getAddress()
    {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(InetAddress address)
    {
        this.address = address;
    }

    /**
     * @return the hops
     */
    public ConcurrentHashMap<Endpoint, Endpoint> getHops()
    {
        return hops;
    }
    
    
}
