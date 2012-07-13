/**
 * @copyright
 */

package com.crash4j.engine.spi.instrument.handlers;
import java.sql.Connection;

import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.instrument.EventData;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.traits.ResourceAware;
import com.crash4j.engine.spi.traits.ResourceBuilder;
import com.crash4j.engine.spi.traits.ResourceClosure;

/**
 * Resource factory implementation for java.sql.Connection#createStatement()Ljava/sql/Statement;
 */
public class java_sql_Connection_createStatement__Ljava_sql_Statement_ extends java_sql_Base 
implements ResourceBuilder
{
	@Override
	public void close(EventData o, ResourceSpi res) 
	{
		Connection c = (Connection)o.getInstance();
		try
		{
			c.close();
		}
		catch (Exception e)
		{
		}
	}
	/**
	 * @see com.crash4j.engine.spi.instrument.handlers.DefaultHandler#exit(com.crash4j.engine.spi.resources.ResourceSpi, com.crash4j.engine.spi.ResourceSpec, java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object, long, long, long)
	 */
	@Override
	public void exit(ResourceSpi spi, ResourceSpec spec, Object args,
			Object instance, Object rv, ResourceClosure c, Object ex, long testStartTime,
			long actualDt, long dt) 
	{
		if (rv instanceof ResourceAware)
		{
			((ResourceAware)rv).setResource(spi);
			((ResourceAware) rv).setData(c);
		}		
		super.exit(spi, spec, args, instance, rv, c, ex, testStartTime, actualDt, dt);
	}
}
