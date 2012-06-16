/**
 * @copyright
 */
package com.crash4j.engine.types.classtypes;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Stack;

import com.crash4j.engine.UnknownResourceException;
import com.crash4j.engine.spi.ResourceManagerSpi;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.actions.ActionImpl;
import com.crash4j.engine.spi.instrument.handlers.RecognizedProtocolHandler;
import com.crash4j.engine.spi.protocol.ProtocolEvent;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.resources.impl.HttpResourceSpiImpl;
import com.crash4j.engine.spi.resources.impl.NetworkResourceSpiImpl;
import com.crash4j.engine.spi.traits.LifecycleHandler;
import com.crash4j.engine.spi.traits.ProtocolBasedResourceFacotry;
import com.crash4j.engine.spi.traits.ResourceBuilder;
import com.crash4j.engine.spi.util.Utils;
import com.crash4j.engine.types.ActionClasses;
import com.crash4j.engine.types.ResourceTypes;


/**
 * 
 * @author <crash4j team>
 * 
 */
public enum HTTPTypes implements ProtocolBasedResourceFacotry
{
    GET,  
    POST, 
    PUT, 
    HEAD, 
    OPTIONS, 
    DELETE, 
    TRACE,
    CONNECT;
    
    /** mul^2 mask supports combinations*/
    private int mask = 0;
    private ResourceSpec spec = null;
    
    /**
     * @param action
     */
    private HTTPTypes()
    {
        this.mask = (int)Math.pow(2, (this.ordinal()+1));
        this.spec = ResourceManagerSpi.createSpec(ActionClasses.HTTP.name(), name(), "", "", false, (LifecycleHandler)new RecognizedProtocolHandler(), 
				new ActionImpl(name(), ActionClasses.HTTP, this), ResourceTypes.SERVICE, null, new HashSet<Class<?>>(), false, 0xFF);;
    }
    
    public static HTTPTypes fromString(String s)
    {
    	return HTTPTypes.valueOf(s.toUpperCase());
    }
    
    public int toInt()
    {
        return this.ordinal();
    }
    
    public int toMask()
    {
        return mask;
    }

	@Override
	public ResourceSpi createResource(ResourceSpi owner, Stack<String> params) throws IOException
	{
		if (!(owner instanceof NetworkResourceSpiImpl))
		{
			return null;
		}
		
		params.pop();
		String url = params.pop();
		
		// All of this should be hidden below specific filter.
		NetworkResourceSpiImpl netr = (NetworkResourceSpiImpl)owner;
		try
		{
			URL u = new URL("http", netr.getHost().getHostName(), netr.getPort(), url);
			return new HttpResourceSpiImpl(spec, u);
		}
		catch (Exception e)
		{
			throw new IOException(e);
		}
	}

	@Override
	public ResourceSpec getSpec() 
	{
		return this.spec;
	}
}
