/**
 * @copyright
 */
package com.crash4j.engine.spi.actions;

import com.crash4j.engine.Action;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.types.ActionClasses;

/**
 * {@link ResourceSpec} based {@link Action}, where, the resulting action 
 * consists of {@link ResourceSpec#getMethod()} + "#" + {@link ResourceSpec#getActionType()}
 */
public class ActionImpl implements Action 
{
	protected ActionClasses aClass = null;
	protected Enum<?> aClassType = null;
	protected String method = null;
	
	/**
	 * Construct the action...
	 * @param aClass
	 * @param cTypes
	 */
	public ActionImpl(String method, ActionClasses aClass, Enum<?> cTypes)
	{
		this.aClass = aClass;
		if (cTypes != null)
		{
			aClassType = cTypes;
		}
		this.method = method;
	}
	
	
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) 
	{
		if (obj instanceof Action)
		{
			Action a = (Action)obj;
			return this.aClassType == a.getActionClassTypes() && this.getActionClass() == this.aClass;
		}
		return false;
	}



	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() 
	{
		return super.hashCode();
	}



	/**
	 * @see com.crash4j.engine.Action#getActionClass()
	 */
	@Override
	public ActionClasses getActionClass() 
	{
		return this.aClass;
	}
	
	/**
	 * @see com.crash4j.engine.Action#getActionClassTypes()
	 */
	@Override
	public Enum getActionClassTypes() 
	{
		return aClassType;
	}
	
	/** (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() 
	{
		StringBuffer b = new StringBuffer();
		if (this.aClassType == null || this.aClass == null)
		{
			return "n:n";
		}
		b.append(this.aClass.name().toLowerCase()).append(":").append(this.aClassType.name().toLowerCase());
		return b.toString();
	}
	
	
}