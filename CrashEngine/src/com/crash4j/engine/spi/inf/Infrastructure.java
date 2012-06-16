/**
 * 
 */
package com.crash4j.engine.spi.inf;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import com.crash4j.engine.spi.inf.os.mac.SystemAdapterMAC;

/**
 */
public class Infrastructure implements Runnable
{
    protected Filesystem[] fsys = null;
    protected SystemAdapter adapter = null;
    protected Thread iThread = null;
    protected LinkedBlockingQueue<_inet_pair> inQueue = new LinkedBlockingQueue<_inet_pair>();
    protected ConcurrentHashMap<NetworkInterface, ConcurrentHashMap<Endpoint, Endpoint>> endpoints = 
            new ConcurrentHashMap<NetworkInterface, ConcurrentHashMap<Endpoint, Endpoint>>();
    protected boolean shutdown = false;
    protected static Infrastructure inf = null;
    
    
    class _inet_pair
    {
        NetworkInterface ni = null;
        InetAddress to = null;
    }
    
    /** 
     * @return a default localhost net interface
     */
    protected NetworkInterface getDefaultNetworkIntefrface()
    {
        try
        {
            return NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
        } catch (SocketException e)
        {
            e.printStackTrace();
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * @param ni
     * @param address
     * @return the route that has being resolved already
     * @throws UnknownHostException 
     */
    public InetAddress[] lookupRoute(NetworkInterface ni, InetAddress address) throws UnknownHostException
    {
        if (ni == null)
        {
            ni = getDefaultNetworkIntefrface();
        }
        ConcurrentHashMap<Endpoint, Endpoint> ends = endpoints.get(ni);
        if (ends != null)
        {
            ArrayList<InetAddress> route = new ArrayList<InetAddress>();
            if (traverseRoute(address, ends, route))
            {
                return route.toArray(new InetAddress[0]);
            }
        }
        return null;
    }
    
    protected boolean traverseRoute(InetAddress address, ConcurrentHashMap<Endpoint, Endpoint> ends, ArrayList<InetAddress> route)
    {
        for (Endpoint ce : ends.keySet())
        {
            byte[] i1 = ce.getAddress().getAddress();
            byte[] i2 = address.getAddress();
            boolean equal = true;
            for (int i = 0; i < i1.length -1; i++)
            {
                equal &= (i1[i] == i2[i]);
            }
            
            //System.out.println(i1[0]+"."+i1[1]+"."+i1[2]+" "+i2[0]+"."+i2[1]+"."+i2[2]);
            
            if (equal)
            {
                route.add(0, ce.getAddress());
                return true;
            }
            
            if (traverseRoute(address, ce.getHops(), route))
            {
                route.add(0, ce.getAddress());
                return true;
            }
        }
        return false;
    }
    
    /**
     * register the address
     * @param ni
     * @param address
     * @throws UnknownHostException 
     */
    public void registerAddress(NetworkInterface ni, InetAddress address) throws UnknownHostException
    {
        if (ni == null)
        {
            ni = getDefaultNetworkIntefrface();
        }
        
        byte[] b = address.getAddress();
        
        InetAddress add = InetAddress.getByAddress(b);
                
        ConcurrentHashMap<Endpoint, Endpoint> ends = endpoints.get(ni);

        if (ends != null && ends.containsKey(add))
        {
            return;
        }
        
        _inet_pair ip = new _inet_pair();
        ip.to = add;
        ip.ni = ni;
        this.inQueue.offer(ip);
    }
    
    /**
     * @see java.lang.Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable
    {
        shutdown = true;
    }

    @Override
    public void run()
    {
        LinkedList<_inet_pair> rec = new LinkedList<_inet_pair>();
        while (!shutdown)
        {
            while (this.inQueue.drainTo(rec) > 0)
            {
                //Get information we need for ips
                for (_inet_pair inp : rec)
                {
                    try
                    {
                        InetAddress[] addresses = adapter.traceroute(inp.ni, inp.to);
                        if (addresses != null && addresses.length > 0)
                        {
                           populateEndpoints(inp.ni, addresses);
                        }
                    } 
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                rec.clear();
            }
            try
            {
                Thread.sleep(5000);
            } catch (InterruptedException e)
            {
            }
        }
    }
    
    /**
     * Populate endpoint graph
     * @param addresses
     */
    protected void populateEndpoints(NetworkInterface ni, InetAddress[] addresses)
    {
        if (ni == null)
        {
            ni = getDefaultNetworkIntefrface();
        }

        ConcurrentHashMap<Endpoint, Endpoint> ends = endpoints.get(ni);
        if (ends == null)
        {
            ends = new ConcurrentHashMap<Endpoint, Endpoint>();
            endpoints.put(ni, ends);
        }
        
        for (int i = 0; i < addresses.length; i++)
        {
            //check if we have it registered
            Endpoint ee = new Endpoint(addresses[i]);
            Endpoint ce = ends.putIfAbsent(ee, ee);
            if (ce == null)
            {
                ce = ee;
            }
            ends = ce.getHops();
        }
    }
    
    /**
     * Initialize the singleton
     */
    protected void init()
    {
        if (isMac())
        {
            adapter =  new SystemAdapterMAC();
        }
        
        try
        {
            this.fsys = adapter.getFilesystems();
            this.iThread = Executors.defaultThreadFactory().newThread(this);
            this.iThread.start();
        } 
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public boolean isWindows() 
    {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("win") >= 0);
    }
 
    public boolean isMac()
    {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("mac") >= 0);
 
    }
 
    public boolean isUnix() 
    {
 
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
    }
 
    public boolean isSolaris() 
    {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("sunos") >= 0);
    }
    
    public Filesystem selectFilesystem(String f)
    {
        Filesystem candidate = null;
        for (Filesystem fs : fsys)
        {
            if (f.contains(fs.getRoot()))
            {
                if (candidate != null && candidate.getRoot().length() > fs.getRoot().length())
                {
                }
                else
                {
                    candidate = fs;
                }
            }
        }
        return candidate;
    }
    
    /**
     * @return os based implementation of {@link SystemAdapter}
     */
    public Filesystem[] getFilesystems()
    {
        return fsys;
    }
    
    /**
     * @return an {@link Infrastructure} singleton instance.
     */
    public static synchronized Infrastructure getInstance()
    {
        if (inf == null)
        {
            inf = new Infrastructure();
            inf.init();
        }
        return inf;
    }
}
