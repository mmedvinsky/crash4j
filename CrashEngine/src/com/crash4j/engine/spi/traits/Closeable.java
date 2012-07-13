/**
 * Copyright WalletKey Inc.
 */
package com.crash4j.engine.spi.traits;

import com.crash4j.engine.spi.instrument.EventData;
import com.crash4j.engine.spi.resources.ResourceSpi;

/**
 * Implement a trait for all object that can be closed
 * i.e. Streams, sockets, result sets, ...
 * 
 * @author mmedvinsky
 *
 */
public interface Closeable 
{
	/**
	 * Close the action that is on the way
	 * @param o
	 * @param res
	 */
	public void close(EventData o, ResourceSpi res);	
}
