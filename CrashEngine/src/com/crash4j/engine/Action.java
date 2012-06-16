/**
 * @copyright
 */
package com.crash4j.engine;

import com.crash4j.engine.types.ActionClasses;

/**
 * {@link Action} interface describes a contract that all actions implementations 
 * should implement.  
 */
public interface Action 
{
	
	/**
	 * @see ActionClasses
	 * @return type generalization for this action
	 */
	ActionClasses getActionClass();
	
	/**
	 * @see IOTypes, NetworkTypes, ....
	 * @return type generalization for this action
	 */
	Enum<?> getActionClassTypes();
}
