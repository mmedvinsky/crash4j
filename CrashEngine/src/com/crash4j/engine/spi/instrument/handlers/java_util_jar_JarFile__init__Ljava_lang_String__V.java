/**
 * @copyright
 */

package com.crash4j.engine.spi.instrument.handlers;
import java.net.URISyntaxException;

import com.crash4j.engine.UnknownResourceException;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.resources.impl.FilesystemResourceSpiImpl;
import com.crash4j.engine.spi.traits.ResourceBuilder;
import com.crash4j.engine.spi.traits.ResourceClosure;
import com.crash4j.engine.spi.util.ArrayUtil;

/**
 * Resource factory implementation for java.util.jar.JarFile#<init>(Ljava/lang/String;)V
 */
public class java_util_jar_JarFile__init__Ljava_lang_String__V extends DefaultHandler 
implements ResourceBuilder
{
    @Override
    public ResourceSpi createResource(ResourceSpec spec, Object args, Object instance, Object rv) 
    	throws UnknownResourceException, URISyntaxException
    {
    	String file = (String)ArrayUtil.get(args, 0);
        return new FilesystemResourceSpiImpl(spec, file);
    }
}
