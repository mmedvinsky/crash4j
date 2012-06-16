/**
 * 
 */
package com.crash4j.engine.spi.traits;

import java.io.IOException;
import java.util.Stack;

import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.types.ActionClasses;
import com.crash4j.engine.types.classtypes.HTTPTypes;
import com.crash4j.engine.types.classtypes.SMTPTypes;

/**
 * {@link ProtocolBasedResourceFacotry} is used with the {@link ActionClasses} and action class types, such as
 * {@link HTTPTypes}, {@link SMTPTypes} etc. to create appropriate resources.
 */
public interface ProtocolBasedResourceFacotry 
{
	/**
	 * Creates {@link ResourceSpi} from parent resource information and recognized and extracted parameters.
	 * @param owner
	 * @param params
	 * @return
	 */
	public ResourceSpi createResource(ResourceSpi owner, Stack<String> params) throws IOException;
	
	/**
	 * @return {@link ResourceSpec} associated with this Protocol event.
	 */
	public ResourceSpec getSpec();
}
