/**
 * 
 */
package com.crash4j.engine.spi.inf;

/**
 * Describes a single mounted file system.
 * @author <MM>
 *
 */
public class Filesystem
{
    protected String mountPoint = null;
    protected long used;
    protected long available;
    protected String root = null;
    
    /**
     * @param mountPoint
     * @param used
     * @param available
     * @param root
     */
    public Filesystem(String mountPoint, long used, long available, String root)
    {
        this.mountPoint = mountPoint;
        this.used = used;
        this.available = available;
        this.root = root;
    }
    /**
     * @return the mountPoint
     */
    public String getMountPoint()
    {
        return mountPoint;
    }
    /**
     * @return the used
     */
    public long getUsed()
    {
        return used;
    }
    /**
     * @return the available
     */
    public long getAvailable()
    {
        return available;
    }
    /**
     * @return the root
     */
    public String getRoot()
    {
        return root;
    }
    
    
    
}
