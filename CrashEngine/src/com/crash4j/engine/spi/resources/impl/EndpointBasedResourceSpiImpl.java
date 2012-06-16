/**
 * 
 */
package com.crash4j.engine.spi.resources.impl;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.regex.Pattern;

import javax.management.ObjectName;

import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.log.Log;
import com.crash4j.engine.spi.log.LogFactory;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.types.ResourceTypes;

/**
 * Generic TCP based discovered resource.
 * @see ResourceSpi
 */
public class EndpointBasedResourceSpiImpl extends ResourceSpiImpl
{
    protected InetAddress host = null;
    protected int port = 0;
	protected String name = null;
	
    protected static final Log log = LogFactory.getLog(EndpointBasedResourceSpiImpl.class);
    
    /**
     * @param spec
     * @param uri
     * @throws MalformedURLException 
     */
    public EndpointBasedResourceSpiImpl(ResourceSpec spec, InetAddress host, int port) 
    		throws MalformedURLException
    {
        super(spec);
        this.setComplete(true);
        name = spec.getActionClass().name();
    }

     

	/**
     * @see com.crash4j.engine.spi.resources.impl.ResourceSpiImpl#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        EndpointBasedResourceSpiImpl ni = null;
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
        
        if (obj instanceof EndpointBasedResourceSpiImpl)
        {
            ni = (EndpointBasedResourceSpiImpl)obj;
        }
        else
        {
            return false;
        }

        if (this.etag == ni.etag)
        {
            return true; //same instance.....
        }
        
        return this.host.equals(ni.host) && this.port == ni.port; 
    }

    /**
     * @see com.crash4j.engine.spi.resources.impl.ResourceSpiImpl#hashCode()
     */
    @Override
    public int hashCode()
    {
        return this.host.hashCode();
    }

    /**
     * @see com.crash4j.engine.spi.resources.impl.ResourceSpiImpl#getVector()
     */
    @Override
    public ObjectName getVector()
    {
        return buildVector();
    }

    /**
	 * @see com.crash4j.engine.spi.resources.impl.ResourceSpiImpl#getResourceType()
	 */
	@Override
	public ResourceTypes getResourceType() 
	{
		return ResourceTypes.HTTP;
	}

	protected ObjectName buildVector()
    {
        try
        {
            Hashtable<String, String> details = new Hashtable<String, String>();
            details.put("host", this.host.getHostName());
            details.put("port", new Integer(this.port).toString());
            return ObjectName.getInstance(name, details);
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
                || name.equalsIgnoreCase("host")
                || name.equalsIgnoreCase("port"));
    }

    @Override
    public boolean match(String name, String value)
    {
        try
        {
            if (name.equalsIgnoreCase("type"))
            {
                return value.equalsIgnoreCase(this.name);
            }
            else if (name.equalsIgnoreCase("host"))
            {
            	return host.equals(InetAddress.getByName(value));
            }
            else if (name.equalsIgnoreCase("port"))
            {
            	return port == Integer.parseInt(value);
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
                return value.matcher(this.name).matches();
            }
            else if (name.equalsIgnoreCase("host"))
            {
                return (value.matcher(host.getHostName()).matches());
            }
            else if (name.equalsIgnoreCase("port"))
            {
                return value.matcher(String.valueOf(this.port)).matches();
            }
        }
        catch (Exception e)
        {
            log.logError("Failed to match Simulations", e);
        }
        return false;
    }
}
