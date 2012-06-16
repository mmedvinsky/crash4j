/**
 * 
 */
package com.crash4j.engine.spi;

import java.util.Set;

import com.crash4j.engine.Action;
import com.crash4j.engine.spi.traits.LifecycleHandler;
import com.crash4j.engine.spi.traits.ResourceBuilder;
import com.crash4j.engine.types.ActionClasses;
import com.crash4j.engine.types.ResourceTypes;
import com.crash4j.engine.types.classtypes.NetworkTypes;


/**
 * Describe specification for  single specification.
 *
 */
public interface ResourceSpec 
{
    /**
     * @return <code>id</code> of this spec
     */
    public int getId();
    
    /**
     * @return composite specification key
     */
    public String getKey();
    
    
    /**
     * @return <code>true</code> if instrumentation os exteranl and will take place in the caller's code.
     */
    public boolean isNative();
    
    /**
     * @return the instrumentSubclasses
     */
    public boolean instrumentSubclasses();
    
    /**
     * @return the exceptions
     */
    public Set<Class<?>> getExceptions();
    
	/**
	 * @return the {@link String} name of the class, described by this spec.
	 */
	public String getEntityName();
	
    /**
     * @return the {@link String} signature of the method
     */
    public String getSignature();
    
    /**
     * @return <code>true</code> if this is a constructor
     */
    public boolean isConstructor();
    
    /**
     * @return the {@link String} method name.
     */
    public String getMethod();
    
    /**
     * {@link ActionClasses} for this event
     */
    public ActionClasses getActionClass();

    /**
     * ActionsClasses types
     * @see NetworkTypes
     */
    public Enum<?> getActionClassTypes();

    /**
     * Actions 
     */
    public Action getDefaultAction();
    
    /**
     * @return instance of the {@link ResourceBuilder} that handles this resource creation.
     */
    public String getResourceFactoryImpl();
    
    /**
     * @return {@link ResourceBuilder} instance for this spec
     */
    public LifecycleHandler getHandler();
    
    /**
     * @return resource type.
     */
    public ResourceTypes getResourceType();
    
    /**
     * @return additional spec attribute specific to the resource
     */
    public String getAttribute(String name);
    
    /**
     * 
     * @return accept mask for this specification
     */
    public int getAccepts();
    
    /**
     * @return true if this is interface
     */
    //public boolean isInterface();
}
