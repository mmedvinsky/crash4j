/**
 * @copyright
 */
package com.crash4j.engine.spi.util;

import java.util.regex.Pattern;

/**
 * An implementor of this class is able to collect matches from {@link PropertyTree}
 * @author team
 *
 */
public interface PropertyOwner
{
    /**
     * @return <code>true</code> if property exists
     */
    public boolean hasProperty(String name);
    
    /**
     * Match the property and value against the {@link PropertyOwner} information
     */
    public boolean match(String name, String value);
    
    /**
     * Match the property and value against the {@link PropertyOwner} information
     */
    public boolean match(String name, Pattern value);
}
