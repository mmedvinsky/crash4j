/**
 * @copyright
 */

package com.crash4j.engine.spi.instrument.handlers;
import com.crash4j.engine.Action;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.traits.ResourceBuilder;
import com.crash4j.engine.spi.traits.ResourceClosure;
import com.crash4j.engine.types.ActionClasses;

/**
 * Resource factory implementation for java.sql.Connection#close()V
 */
public class java_sql_BaseWithAction extends java_sql_Base 
implements ResourceBuilder
{
    /**
	 * @see com.crash4j.engine.spi.instrument.handlers.DefaultHandler#getAction(com.crash4j.engine.spi.resources.ResourceSpi, com.crash4j.engine.spi.ResourceSpec, java.lang.Object, java.lang.Object, com.crash4j.engine.spi.traits.ResourceClosure)
	 */
	@Override
	public Action getAction(ResourceSpi spi, ResourceSpec spec, Object args,
			Object instance, ResourceClosure c) 
	{
		Action act = (Action)c.getData(ActionClasses.SQL.ordinal());
		if (act == null)
		{	
			act = super.getAction(spi, spec, args, instance, c);
		}
		return act;
	}
}
