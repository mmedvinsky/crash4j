/**
 * 
 */
package com.crash4j.engine.spi.resources.impl;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.regex.Pattern;

import javax.management.ObjectName;

import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.log.Log;
import com.crash4j.engine.spi.log.LogFactory;
import com.crash4j.engine.spi.resources.ResourceSpi;

/**
 * Describes JDBC type resource.
 * @see ResourceSpi
 */
public class JDBCResourceSpiImpl extends ResourceSpiImpl
{
    protected static final Log log = LogFactory.getLog(JDBCResourceSpiImpl.class);
    protected String url = null;
    protected String driverName = null;
    protected String driverVersion = null;
    protected String dbname = null;
    
    /**
     * @param spec
     * @param uri
     * @throws MalformedURLException 
     */
    public JDBCResourceSpiImpl(ResourceSpec spec, Object conn)
    {
        super(spec);
        completeResource(conn);
    }

     
	/**
     * @param spec
     * @param uri
     * @throws MalformedURLException 
     */
    public JDBCResourceSpiImpl(ResourceSpec spec, String url)
    {
        super(spec);
        this.url = url;
        this.setComplete(false);
    }

    /**
     * Complete the database resource when connection is returned.
     * @param instance
     */
    public void completeResource(Object instance)
    {
        try 
        {
        	if (instance == null)
        	{
        		return;
        	}
			DatabaseMetaData mdata = ((Connection)instance).getMetaData();
			this.driverName = mdata.getDriverName();
			this.driverVersion = mdata.getDriverVersion();
			this.url = mdata.getURL();
			this.dbname = mdata.getDatabaseProductName();
		} 
        catch (SQLException e) 
        {
        	log.logError("Unable to get database meta data", e);
		}
        this.setComplete(true);
    }
    
	/**
     * @see com.crash4j.engine.spi.resources.impl.ResourceSpiImpl#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        JDBCResourceSpiImpl dbi = null;
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
        
        if (obj instanceof JDBCResourceSpiImpl)
        {
        	dbi = (JDBCResourceSpiImpl)obj;
        }
        else
        {
            return false;
        }

        if (this.etag == dbi.etag)
        {
            return true; //same instance.....
        }
        
        
        if (!dbi.isComplete())
        {
        	return false;
        }
        
        boolean c = (dbi.isComplete() == this.isComplete())  &&
                (dbi.dbname.equals(this.dbname)) && 
                (dbi.driverName == this.driverName) && 
                (dbi.url.equals(this.url));
        return c; 
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

    protected ObjectName buildVector()
    {
        try
        {
            Hashtable<String, String> details = new Hashtable<String, String>();
            details.put("driver", this.driverName);
            details.put("vendor", this.dbname);
            details.put("url", this.url.replace(":", "@"));
            if (this.getParent() != null)
            {
            	details.put("parent", this.getParent().getETag());
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
                || name.equalsIgnoreCase("url")
                || name.equalsIgnoreCase("driver")
                || name.equalsIgnoreCase("vendor")
                );
    }

    @Override
    public boolean match(String name, String value)
    {
        try
        {
            if (name.equalsIgnoreCase("type"))
            {
                return value.equalsIgnoreCase(this.getResourceType().toString());
            }
            else if (name.equalsIgnoreCase("url"))
            {
                return value.equalsIgnoreCase(this.url.toString());
            }
            else if (name.equalsIgnoreCase("driver"))
            {
                return value.equalsIgnoreCase(this.driverName);
            }
            else if (name.equalsIgnoreCase("vendor"))
            {
                return value.equalsIgnoreCase(this.dbname);
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
                return value.matcher(this.getResourceType().toString()).matches();
            }
            else if (name.equalsIgnoreCase("url"))
            {
                return value.matcher(this.url.toString()).matches();
            }
            else if (name.equalsIgnoreCase("driver"))
            {
                return value.matcher(this.driverName.toString()).matches();
            }
            else if (name.equalsIgnoreCase("vendor"))
            {
                return value.matcher(this.dbname.toString()).matches();
            }
        }
        catch (Exception e)
        {
            log.logError("Failed to match Simulations", e);
        }
        return false;
    }
}
