/**
 * @copyright
 */

package com.crash4j.engine.spi.instrument.handlers;
import java.net.URISyntaxException;
import java.util.jar.JarFile;

import com.crash4j.engine.UnknownResourceException;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.instrument.delegates.java_io_InputStream;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.resources.impl.FilesystemResourceSpiImpl;
import com.crash4j.engine.spi.traits.FacadeBuilder;
import com.crash4j.engine.spi.traits.ResourceBuilder;
import com.crash4j.engine.spi.traits.ResourceClosure;

/**
 * Resource factory implementation for java.util.jar.JarFile#getInputStream(Ljava/util/zip/ZipEntry;)Ljava/io/InputStream;
 */
public class java_util_jar_JarFile_getInputStream_Ljava_util_zip_ZipEntry__Ljava_io_InputStream_ extends DefaultHandler 
implements ResourceBuilder, FacadeBuilder
{
    @Override
    public ResourceSpi createResource(ResourceSpec spec, Object args, Object instance, Object rv) 
    	throws UnknownResourceException, URISyntaxException
    {
    	JarFile jf = (JarFile)instance;
        return new FilesystemResourceSpiImpl(spec, jf.getName());
    }

	@Override
	public Object createFacade(ResourceSpec spec, ResourceSpi res, Object args,
			Object instance, Object rv, ResourceClosure c) 
	{
        java_io_InputStream oss = new java_io_InputStream(rv, spec, instance);
        oss.setResource(res);
        return oss;
	}
}
