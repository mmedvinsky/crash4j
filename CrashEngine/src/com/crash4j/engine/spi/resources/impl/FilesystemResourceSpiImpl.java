/**
 * 
 */
package com.crash4j.engine.spi.resources.impl;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Hashtable;
import java.util.regex.Pattern;

import javax.management.ObjectName;

import com.crash4j.engine.spi.ResourceManagerSpi;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.inf.Filesystem;
import com.crash4j.engine.spi.log.Log;
import com.crash4j.engine.spi.log.LogFactory;
import com.crash4j.engine.types.ResourceTypes;

/**
 * @author <MM>
 *
 */
public class FilesystemResourceSpiImpl extends ResourceSpiImpl
{
    protected static final Log log = LogFactory.getLog(FilesystemResourceSpiImpl.class);
    
    protected File fspoint = null;
    protected Filesystem fsbase = null;
    protected FileDescriptor fd = null;
    protected int mask = ResourceManagerSpi.getResourceConfig(ResourceTypes.FSYS);
    
    protected static final int mode_mounts = 2;
    protected static final int mode_dirs = 4;
    protected static final int mode_files = 8;
    protected static final int mode_streams = 16;
    
    
    public FilesystemResourceSpiImpl(ResourceSpec spec)
    {
        super(spec);
        setComplete(false);
    }
    /**
     * Initialize from {@link FileInputStream}. Will result in anonymous {@link FileDescriptor}
     * @param spec
     * @param fis
     */
    public FilesystemResourceSpiImpl(ResourceSpec spec, FileInputStream fis)
    {
        super(spec);
        try
        {
            this.fd = fis.getFD();
        } 
        catch (IOException e)
        {
            log.logError("Filed to get FileDescriptor");
        }
    }
    
    /**
     * Initialize from {@link FileOutputStream}. Will result in anonymous {@link FileDescriptor}
     * @param spec
     * @param fis
     */
    public FilesystemResourceSpiImpl(ResourceSpec spec, FileOutputStream fos)
    {
        super(spec);
        try
        {
            this.fd = fos.getFD();
        } 
        catch (IOException e)
        {
            log.logError("Filed to get FileDescriptor");
        }
    }
    
    /**
     * @param spec
     * @param uri
     */
    public FilesystemResourceSpiImpl(ResourceSpec spec, File file)
    {
        super(spec);
        this.fspoint = new File(file.getAbsolutePath());
        this.fsbase = ResourceManagerSpi.getInfrastructure().selectFilesystem(this.fspoint.getAbsolutePath());
    }
    
    
    /**
     * @param spec
     * @param uri
     */
    public FilesystemResourceSpiImpl(ResourceSpec spec, String file)
    {
        super(spec);
        this.fspoint = new File(file);
        this.fsbase = ResourceManagerSpi.getInfrastructure().selectFilesystem(this.fspoint.getAbsolutePath());
    }

    /**
     * @param spec
     * @param uri
     */
    public FilesystemResourceSpiImpl(ResourceSpec spec, FileDescriptor fd)
    {
        super(spec);
        this.fd = fd;
    }
    
    /**
     * @param spec
     * @param uri
     */
    public FilesystemResourceSpiImpl(ResourceSpec spec, String parent, String child)
    {
        super(spec);
        this.fspoint = new File(parent, child);
        this.fsbase = ResourceManagerSpi.getInfrastructure().selectFilesystem(this.fspoint.getAbsolutePath());
    }

    /**
     * @param spec
     * @param uri
     */
    public FilesystemResourceSpiImpl(ResourceSpec spec, File parent, String child)
    {
        super(spec);
        this.fspoint = new File(parent, child);
        this.fsbase = ResourceManagerSpi.getInfrastructure().selectFilesystem(this.fspoint.getAbsolutePath());
    }
    
    protected String getDirectory(File f)
    {
    	if (f.isDirectory())
    	{
    		return f.getAbsolutePath();
    	}
    	return f.getAbsoluteFile().getParent();
    }
    
    /**
     * 
     * @param spec
     * @param args
     * @param rv
     */
    public void completeResource(ResourceSpec spec, Object args, File rv)
    {
        this.fspoint = new File(rv.getAbsolutePath());
        this.fsbase = ResourceManagerSpi.getInfrastructure().selectFilesystem(this.fspoint.getAbsolutePath());
        this.setComplete(true);
    }
    private String getResourceName()
    {
        String resource = getMountPoint();
        if (this.fspoint == null)
        {
            if (this.fd != null)
            {
                if (fd.equals(FileDescriptor.in))
                {
                    resource="stdin";
                }
                else if (fd.equals(FileDescriptor.out))
                {
                    resource="stdout";
                }
                else if (fd.equals(FileDescriptor.err))
                {
                    resource="stderr";
                }
                else
                {
                    resource=fd.toString();
                }
            }
        }
        else
        {
            if ((this.mask & mode_files) == mode_files)
            {
            	resource = this.fspoint.getAbsolutePath();
            }
            else if ((this.mask & mode_dirs) == mode_dirs)
            {
            	resource = getDirectory(this.fspoint);
            }
        }
                
        return resource; 
    }
    
    /**
     * @return mount point for this file system resource
     */
    private String getMountPoint()
    {
        String mtpoint = null;
        if (this.fspoint == null)
        {
            mtpoint = "stream";
        }
        else
        {
            mtpoint = this.fsbase.getMountPoint();
        }
        return mtpoint;
    }
    
    /**
     * @return resource vector
     */
    protected ObjectName buildFSResource()
    {
        try
        {
            Hashtable<String, String> hash = new Hashtable<String, String>();
            if ((this.mask & mode_mounts) == mode_mounts)
            {
                hash.put("mt", getMountPoint());
            }
            if ((this.mask & mode_files) == mode_files || 
            		(this.mask & mode_dirs) == mode_dirs)
            {
                String res = getResourceName();
                if (res != null)
                {
                	hash.put("resource", res.replace(':', '@'));
                }
            }
            
            if (this.getParent() != null)
            {
                hash.put("parent", this.getParent().getETag());
            }
            this.setComplete(true);
            return ObjectName.getInstance(this.getResourceType().toString(), hash);
        } 
        catch (Throwable e)
        {
            log.logError("Error creating resource moniker", e);
        }
        return null;
    }


    @Override
    public ObjectName getVector()
    {
        return buildFSResource();
    }


    @Override
    public boolean equals(Object obj)
    {
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
        
        FilesystemResourceSpiImpl in = null;
        if (obj instanceof FilesystemResourceSpiImpl)
        {
            in = (FilesystemResourceSpiImpl)obj;
        }
        else
        {
            return false;
        }
        
        //This is the same instance.....
        if (in.etag == this.etag)
        {
            //System.out.println("etag");
            return true;
        }
        
        if (this.fd != null)
        {
            if (in.fd != null)
            {
                //System.out.println("fd");
                return this.fd.equals(in.fd);
            }
        }
        
        boolean eq = false;
        if ((this.mask & mode_mounts) == mode_mounts)
        {
        	if (this.fsbase != null)
        	{
        		eq = this.getMountPoint().equals(in.getMountPoint());
        	}
        }
        
        if ((this.mask & mode_files) == mode_files)
        {
            if (this.fspoint != null)
            {
                if (in.fspoint != null)
                {
                    eq = eq & this.fspoint.equals(in.fspoint);
                }
            }
        }
        if ((this.mask & mode_dirs) == mode_dirs)
        {
            if (this.fspoint != null)
            {
                if (in.fspoint != null)
                {
                    eq = eq & getDirectory(this.fspoint).equals(getDirectory(in.fspoint));
                }
            }
        }
        //System.out.println("fs:"+eq);
        return eq;
    }


    @Override
    public int hashCode()
    {
    	int h = 0;
        if ((this.mask & mode_files) == mode_files)
        {
        	h = (this.fd != null ? this.fd.hashCode() : 0) + (this.fspoint != null ? this.fspoint.hashCode() : 0);
        }
        
        if ((this.mask & mode_dirs) == mode_dirs)
        {
        	h += (this.fd != null ? this.fd.hashCode() : 0) + (this.fspoint != null ? getDirectory(this.fspoint).hashCode() : 0);
        }
         
        if ((this.mask & mode_mounts) == mode_mounts)
        {
        	h += getMountPoint().hashCode();
        }
        return h;
    }
    
    @Override
    public boolean hasProperty(String name)
    {
        return (name.equalsIgnoreCase("type")
                || name.equalsIgnoreCase("mt") 
                || name.equalsIgnoreCase("resource"));
    }
    
    @Override
    public boolean match(String name, String value)
    {
        if (name.equalsIgnoreCase("type"))
        {
            return value.equalsIgnoreCase(getResourceType().toString());
        }
        else if (name.equalsIgnoreCase("mt"))
        {
        	if ((this.mask & mode_mounts) == mode_mounts)
        	{
        		String mtpoint = getMountPoint();
        		return value.equalsIgnoreCase(mtpoint);
        	}
        	return true;
        }
        else if (name.equalsIgnoreCase("resource"))
        {
            String resource = getResourceName();
            return value.equalsIgnoreCase(resource);
        }
        return false;
    }
    
    @Override
    public boolean match(String name, Pattern value)
    {
        if (name.equalsIgnoreCase("type"))
        {
            return value.matcher(getResourceType().toString()).matches();
        }
        else if (name.equalsIgnoreCase("mt"))
        {
           	if ((this.mask & mode_mounts) == mode_mounts)
        	{
           		String mtpoint = getMountPoint();            
           		return value.matcher(mtpoint).matches();
        	}
           	return true;
        }
        else if (name.equalsIgnoreCase("resource"))
        {
            String resource = getResourceName();
            return value.matcher(resource).matches();
        }
        return false;
    }
}
