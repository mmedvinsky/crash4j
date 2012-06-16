/**
 * 
 */
package com.crash4j.engine.spi.instrument.delegates;

import java.io.FileDescriptor;
import java.io.FileInputStream;

import com.crash4j.engine.spi.ResourceManagerSpi;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.resources.impl.FilesystemResourceSpiImpl;
import com.crash4j.engine.spi.traits.ResourceAware;

/**
 * @author <MM>
 *
 */
public class StdStreamInputStream extends FileInputStream implements ResourceAware
{
    protected ResourceSpi resource;
    
    protected static ResourceSpec spec = ResourceManagerSpi
            .getByFullSignature("java.io.FileInputStream#<init>(Ljava/io/FileDescriptor;)V");

    /**
     * @param arg0
     */
    public StdStreamInputStream(FileDescriptor fd)
    {
        super(fd);
        resource = new FilesystemResourceSpiImpl(spec, fd); 
    }

    protected Object __data__ = null;
	@Override
	public Object getData() 
	{
		return __data__;
	}

	@Override
	public void setData(Object data) 
	{
		__data__ = data;
	}
    /**
     * @see com.crash4j.engine.spi.spi.traits.ResourceAware#getResource()
     */
    @Override
    public ResourceSpi getResource()
    {
        return this.resource;
    }

    /**
     * @see com.crash4j.engine.spi.spi.traits.ResourceAware#setResource(com.crash4j.engine.spi.ResourceSpi)
     */
    @Override
    public void setResource(ResourceSpi res)
    {
        this.resource = res;
    }
}
