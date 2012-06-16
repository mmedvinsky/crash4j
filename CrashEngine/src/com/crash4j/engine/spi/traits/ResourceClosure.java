/**
 * Copyright 
 */
package com.crash4j.engine.spi.traits;

import com.crash4j.engine.spi.ResourceManagerSpi;

/**
 * Closure structure that allows state association and affinity between 
 * resources and objects that spawned them.  If the {@link Object} is {@link ResourceAware} the association 
 * is done via resource aware implementation extensions of the object itself and if not it is acquired from
 * the {@link ResourceManagerSpi#resolveResource(com.crash4j.engine.spi.ResourceSpec, Object, Object, Object)} execution
 */
public interface ResourceClosure 
{	
	/**
	 * Set opaque object to be associated with this closure
	 * @param handle
	 * @param data
	 */
	public void setData(int h, Object data);
	
	/**
	 * @param handle that identifies you private space
	 * @return the opaque object associated with this closure
	 */
	public Object getData(int h);
}
