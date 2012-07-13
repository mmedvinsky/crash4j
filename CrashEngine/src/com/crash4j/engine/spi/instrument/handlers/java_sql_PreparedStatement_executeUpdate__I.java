/**
 * @copyright
 */

package com.crash4j.engine.spi.instrument.handlers;
import java.net.URISyntaxException;
import java.sql.Statement;

import com.crash4j.engine.UnknownResourceException;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.instrument.EventData;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.traits.ResourceBuilder;
import com.crash4j.engine.spi.traits.ResourceClosure;

/**
 * Resource factory implementation for java.sql.PreparedStatement#executeUpdate()I
 */
public class java_sql_PreparedStatement_executeUpdate__I extends java_sql_BaseWithAction 
implements ResourceBuilder
{
    @Override
    public ResourceSpi createResource(ResourceSpec spec, Object args, Object instance, Object rv) 
    	throws UnknownResourceException, URISyntaxException
    {
        return null;
    }
	@Override
	public void close(EventData o, ResourceSpi res) 
	{
		Statement c = (Statement)o.getInstance();
		try
		{
			c.close();
		}
		catch (Exception e)
		{
		}
	}
}
