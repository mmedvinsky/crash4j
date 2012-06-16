/**
 * 
 */
package com.crash4j.engine.spi.inf;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * @author <MM>
 *
 */
public interface SystemAdapter
{
    /**
     * @return all {@link Filesystem} on this computer
     */
    public Filesystem[] getFilesystems() throws IOException;
    
    /**
     * @return a list of {@link Endpoint} for a specific address.
     * @throws IOException
     */
    public InetAddress[] traceroute(NetworkInterface inf, InetAddress to) throws IOException;

    /**
     * @return a list of {@link Endpoint} for a specific address.
     * @throws IOException
     */
    public double ping(InetAddress e) throws IOException;
}
