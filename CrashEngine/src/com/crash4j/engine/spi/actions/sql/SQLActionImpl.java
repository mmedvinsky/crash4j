/**
 * 
 */
package com.crash4j.engine.spi.actions.sql;

import com.crash4j.engine.spi.actions.ActionImpl;
import com.crash4j.engine.types.ActionClasses;
import com.crash4j.engine.types.classtypes.DBTypes;

/**
 * SQL Action handles state management and recognition of SQL actions during the JDBC communication
 */
public class SQLActionImpl extends ActionImpl 
{
	/**
	 * @param method
	 * @param aClass
	 * @param cTypes
	 */
	public SQLActionImpl(String method, String sql) 
	{
		super(method, ActionClasses.SQL, null);
		try
		{
			String command = sql.substring(0, sql.indexOf(" "));
			if (command != null)
			{
				super.aClassType = DBTypes.valueOf(command.toUpperCase());
			}
		}
		catch (Exception e)
		{
		}
		super.aClassType = super.aClassType == null ? DBTypes.SQLCOMMAND : super.aClassType;
	}
}
