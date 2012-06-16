/**
 * @copyright
 */

package com.crash4j.engine.spi.instrument.handlers;
import java.net.URISyntaxException;

import com.crash4j.engine.Action;
import com.crash4j.engine.UnknownResourceException;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.actions.sql.SQLActionImpl;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.resources.impl.JDBCResourceSpiImpl;
import com.crash4j.engine.spi.traits.ResourceBuilder;
import com.crash4j.engine.spi.traits.ResourceClosure;
import com.crash4j.engine.spi.util.ArrayUtil;
import com.crash4j.engine.types.ActionClasses;

/**
 * Resource factory implementation for java.sql.Connection#close()V
 */
public class java_sql_Base extends DefaultHandler 
implements ResourceBuilder
{
	@Override
    public ResourceSpi createResource(ResourceSpec spec, Object args, Object instance, Object rv) 
    	throws UnknownResourceException, URISyntaxException
    {
        return new JDBCResourceSpiImpl(spec, instance);
    }
}
