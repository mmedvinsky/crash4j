/**
 * @copyright
 */

package com.crash4j.engine.spi.instrument.handlers;
import java.net.URISyntaxException;

import com.crash4j.engine.UnknownResourceException;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.resources.impl.JDBCResourceSpiImpl;
import com.crash4j.engine.spi.traits.ResourceBuilder;
import com.crash4j.engine.spi.traits.ResourceClosure;
import com.crash4j.engine.spi.util.ArrayUtil;

/**
 * Resource factory implementation for java.sql.Driver#connect(Ljava/lang/String;Ljava/util/Properties;)Ljava/sql/Connection;
 */
public class java_sql_Driver_connect_Ljava_lang_String_Ljava_util_Properties__Ljava_sql_Connection_ extends DefaultHandler 
implements ResourceBuilder
{
    @Override
    public ResourceSpi createResource(ResourceSpec spec, Object args, Object instance, Object rv) 
    	throws UnknownResourceException, URISyntaxException
    {
        return new JDBCResourceSpiImpl(spec, ArrayUtil.get(args, 0).toString());
    }

	/**
	 * @see com.crash4j.engine.spi.instrument.handlers.DefaultHandler#completeResource(com.crash4j.engine.spi.resources.ResourceSpi, com.crash4j.engine.spi.ResourceSpec, java.lang.Object, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void completeResource(ResourceSpi current, ResourceSpec spec, Object args, Object instance, Object rv)
			throws UnknownResourceException 
	{
		if (current.isComplete())
		{
			return;
		}
		JDBCResourceSpiImpl dbres = (JDBCResourceSpiImpl)current;
		dbres.completeResource(rv);
	}
}
