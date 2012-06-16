/**
 * 
 */
package com.crash4j.engine.spi.resources.impl;

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
 * Describes http resource
 * @see ResourceSpi
 */
public class HttpResourceSpiImpl extends ResourceSpiImpl
{
    protected URL url = null;
    protected static final Log log = LogFactory.getLog(HttpResourceSpiImpl.class);
    
    /**
     * @param spec
     * @param uri
     * @throws MalformedURLException 
     */
    public HttpResourceSpiImpl(ResourceSpec spec, String url) throws MalformedURLException
    {
        super(spec);
        this.url = new URL(url);
        this.setComplete(true);
    }

    /**
     * @param spec
     * @param uri
     * @throws MalformedURLException 
     */
    public HttpResourceSpiImpl(ResourceSpec spec, URL url) throws MalformedURLException
    {
        super(spec);
        this.url = url;
        this.setComplete(true);
    }

     

	/**
     * @see com.crash4j.engine.spi.resources.impl.ResourceSpiImpl#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        HttpResourceSpiImpl ni = null;
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
        
        if (obj instanceof HttpResourceSpiImpl)
        {
            ni = (HttpResourceSpiImpl)obj;
        }
        else
        {
            return false;
        }

        if (this.etag == ni.etag)
        {
            return true; //same instance.....
        }
        
        return this.url.equals(ni.url); 
    }

    /**
     * @see com.crash4j.engine.spi.resources.impl.ResourceSpiImpl#hashCode()
     */
    @Override
    public int hashCode()
    {
        return this.url.hashCode();
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
            details.put("host", this.url.getHost());
            details.put("port", Integer.toString(this.url.getPort() > 0 ?  this.url.getPort() : 80));
            details.put("path", this.url.getPath());
            if (this.getParent() != null)
            {
            	details.put("parent", this.getParent().getETag());
            }
            if (this.url.getQuery() != null)
            {
            	details.put("query", this.url.getQuery());
            }
            return ObjectName.getInstance(ResourceTypes.HTTP.toString(), details);
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
                || name.equalsIgnoreCase("url"));
    }

    @Override
    public boolean match(String name, String value)
    {
        try
        {
            if (name.equalsIgnoreCase("type"))
            {
                return value.equalsIgnoreCase("http");
            }
            else if (name.equalsIgnoreCase("url"))
            {
                return value.equalsIgnoreCase(this.url.toString());
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
                return value.matcher("http").matches();
            }
            else if (name.equalsIgnoreCase("url"))
            {
                return value.matcher(this.url.toString()).matches();
            }
        }
        catch (Exception e)
        {
            log.logError("Failed to match Simulations", e);
        }
        return false;
    }
}
