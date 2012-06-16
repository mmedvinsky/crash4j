/**
 * @copyright
 */
package com.crash4j.engine.spi.instrument.handlers;

import java.net.URISyntaxException;

import com.crash4j.engine.Action;
import com.crash4j.engine.UnknownResourceException;
import com.crash4j.engine.spi.ResourceManagerSpi;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.protocol.ProtocolEvent;
import com.crash4j.engine.spi.protocol.ProtocolRecognizer;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.stats.StatsManager;
import com.crash4j.engine.spi.traits.ResourceBuilder;
import com.crash4j.engine.spi.traits.ResourceClosure;
import com.crash4j.engine.types.StatTypes;
import com.crash4j.engine.types.UnitTypes;

/**
 * Used to handle discovered protocol events.
 * @see ProtocolRecognizer
 */
public class RecognizedProtocolHandler extends DefaultHandler implements ResourceBuilder
{
	
	/**
	 * @see com.crash4j.engine.spi.instrument.handlers.DefaultHandler#getAction(com.crash4j.engine.spi.resources.ResourceSpi, com.crash4j.engine.spi.ResourceSpec, java.lang.Object, java.lang.Object, com.crash4j.engine.spi.traits.ResourceClosure)
	 */
	@Override
	public Action getAction(ResourceSpi spi, ResourceSpec spec, Object args,
			Object instance, ResourceClosure c) 
	{
		if (instance instanceof ProtocolEvent)
		{
			return ((ProtocolEvent)instance).getAction();
		}
		
		return super.getAction(spi, spec, args, instance, c);
	}

	@Override
	public ResourceSpi createResource(ResourceSpec spec, Object args,
			Object instance, Object rv) throws UnknownResourceException,
			URISyntaxException 
	{
		if (instance instanceof ProtocolEvent)
		{
			return ((ProtocolEvent)instance).getTargetResource();
		}		
		return null;
	}
	
}
