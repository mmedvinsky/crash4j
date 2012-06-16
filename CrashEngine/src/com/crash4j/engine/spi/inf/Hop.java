/**
 * 
 */
package com.crash4j.engine.spi.inf;

import java.net.UnknownHostException;

/**
 * 
 */
public class Hop
{
    protected Endpoint from = null;
    protected Endpoint to = null;
    protected int bandwidth = 0; // megs/second
    
    /**
     * @throws UnknownHostException 
     * 
     */
    public Hop(String from, String to) throws UnknownHostException
    {
        this.from = new Endpoint(from);
        this.to = new Endpoint(to);
    }

    /**
     * 
     */
    public Hop(Endpoint from, Endpoint to)
    {
        this.from = from;
        this.to = to;
    }

    /**
     * @return the from
     */
    public Endpoint getFrom()
    {
        return from;
    }

    /**
     * @param from the from to set
     */
    public void setFrom(Endpoint from)
    {
        this.from = from;
    }

    /**
     * @return the to
     */
    public Endpoint getTo()
    {
        return to;
    }

    /**
     * @param to the to to set
     */
    public void setTo(Endpoint to)
    {
        this.to = to;
    }

    /**
     * @return the bandwidth
     */
    public int getBandwidth()
    {
        return bandwidth;
    }

    /**
     * @param bandwidth the bandwidth to set
     */
    public void setBandwidth(int bandwidth)
    {
        this.bandwidth = bandwidth;
    }
    
    
    
}
