/**
 * @copyright
 */

package com.crash4j.engine.spi.instrument.handlers;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URLConnection;

import com.crash4j.engine.UnknownResourceException;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.resources.impl.HttpResourceSpiImpl;
import com.crash4j.engine.spi.traits.ResourceBuilder;
import com.crash4j.engine.spi.traits.ResourceClosure;

/**
 * Resource factory implementation for java.net.HttpURLConnection#setRequestMethod(Ljava/lang/String;)V
 */
public class java_net_HttpURLConnection_setRequestMethod_Ljava_lang_String__V extends DefaultHandler 
implements ResourceBuilder
{
    @Override
    public ResourceSpi createResource(ResourceSpec spec, Object args, Object instance, Object rv) 
    	throws UnknownResourceException, URISyntaxException
    {
        String m = (String)Array.get(args, 0);
        URLConnection c = (URLConnection)instance;
        try 
        {
        	HttpResourceSpiImpl h = new HttpResourceSpiImpl(spec, c.getURL());
        	return h;
		} 
        catch (MalformedURLException e) 
		{
			URISyntaxException uiex =  new URISyntaxException("", "");
			uiex.initCause(e);
			throw uiex;
		}
    }

	/**
	 * @see com.crash4j.engine.spi.instrument.handlers.DefaultHandler#completeResource(com.crash4j.engine.spi.resources.ResourceSpi, com.crash4j.engine.spi.ResourceSpec, java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void completeResource(ResourceSpi current, ResourceSpec spec,
			Object args, Object instance, Object rv)
			throws UnknownResourceException 
	{
	}
}
