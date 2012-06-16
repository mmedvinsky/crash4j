/**
 * @copyright
 */
package com.crash4j.engine;

import java.net.URI;
import java.net.URISyntaxException;

import javax.management.ObjectName;

/**
 * Describes a single {@link Resource} instance that is accessed by the running application and
 * is discovered and managed by the crash4j engine.
 * </p>
 * A {@link Resource}, as defined by this product, is anything that is used by the hosted application runtime to fulfill its functions.</p>
 * Example of resources includes network, file-systems, memory, CPU, external and internal devices, etc.  There are also higher level resources that build on top
 * the first basic level - jdbc, jms, http, rmi, ..., all of this resources also play a role but are effected but the first level.
 *  
 */
public interface Resource
{
    /**
     * @return {@link Resource} {@link URI} representation. 
     */
    public URI asURI() throws URISyntaxException;
    
    /**
     * @return resource as an {@link ObjectName} vector
     */
    public ObjectName asVector();
}
