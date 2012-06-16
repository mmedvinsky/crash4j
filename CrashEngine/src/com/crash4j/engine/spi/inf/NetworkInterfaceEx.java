/**
 * 
 */
package com.crash4j.engine.spi.inf;

import java.net.NetworkInterface;
import java.util.HashSet;

/**
 *  This class describes a single network interface details 
 */
public abstract class NetworkInterfaceEx
{
    protected HashSet<Endpoint> endpoints = new HashSet<Endpoint>();
    protected NetworkInterface inf = null;
    
    /**
     * 
     */
    public NetworkInterfaceEx(NetworkInterface inf)
    {
        this.inf = inf;
    }
    
    public NetworkInterface getInterface()
    {
        return this.inf;
    }

    /**
     * @return the endpoints
     */
    public HashSet<Endpoint> getEndpoints()
    {
        return endpoints;
    }

    /**
     * @param endpoints the endpoints to set
     */
    public void setEndpoints(HashSet<Endpoint> endpoints)
    {
        this.endpoints = endpoints;
    }

    /**
     * @return the inf
     */
    public NetworkInterface getInf()
    {
        return inf;
    }

    /**
     * @param inf the inf to set
     */
    public void setInf(NetworkInterface inf)
    {
        this.inf = inf;
    }
    
    
}
