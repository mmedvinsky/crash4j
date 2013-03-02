/**
 * @copyright
 */
package com.crash4j.engine.types;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import com.crash4j.engine.Action;
import com.crash4j.engine.types.classtypes.DBTypes;
import com.crash4j.engine.types.classtypes.FileSystemTypes;
import com.crash4j.engine.types.classtypes.HTTPTypes;
import com.crash4j.engine.types.classtypes.IOTypes;
import com.crash4j.engine.types.classtypes.NetworkTypes;
import com.crash4j.engine.types.classtypes.ProcedureTypes;


/**
 * {@link ActionClasses} classification allows us to classify {@link Action}(s) by 
 * type of operations they perform. <p/>
 * <ul>
 * <li>{@link ActionClasses#IO}: references network and file IO streams that are derived from {@link Socket} and/or {@link File} apis</li>
 * </ul>
 * @author <crash4j team>
 * 
 */
public enum ActionClasses
{
    IO,  
    PROCEDURE, 
    NETWORK, 
    FILESYSTEM,
    HTTP,
    SMTP,
    SQL,
    FTP,
    PROTOCOL, 
    NOACTION;
    
    /** mul^2 mask supports combinations*/
    private int mask = 0;
    
    /**
     */
    private ActionClasses()
    {
        this.mask = (int)Math.pow(2, (this.ordinal()+1));
    }
    
    /**
     * Return class action type
     * @param s
     * @return
     */
    public int getClassActionTypeMaskFromString(String s)
    {
    	switch (this)
    	{
	    	case IO:
	    	{
	    		IOTypes en = IOTypes.valueOf(s);
	    		return (en == null ? -1 : en.toMask());
	    	}
	    	case PROCEDURE: 
	    	{
	    		ProcedureTypes en =  ProcedureTypes.valueOf(s);
	    		return (en == null ? -1 : en.toMask());
	    	}
	    	case NETWORK:
	    	{
	    		NetworkTypes en =  NetworkTypes.valueOf(s);
	    		return (en == null ? -1 : en.toMask());
	    	}
	    	case FILESYSTEM:
	    	{
	    		FileSystemTypes en =  FileSystemTypes.valueOf(s);
	    		return (en == null ? -1 : en.toMask());
	    	}
	    	case HTTP:
	    	{
	    		HTTPTypes en =  HTTPTypes.valueOf(s);
	    		return (en == null ? -1 : en.toMask());
	    	}
	    	case SQL:
	    	{
	    		DBTypes en =  DBTypes.valueOf(s);
	    		return (en == null ? -1 : en.toMask());
	    	}
	    	default:
	    	{
	    		return -1;
	    	}
    	}
    }
    
    /**
     * Return class action type
     * @param s
     * @return
     */
    public Enum<?> getClassActionTypeFromString(String s)
    {
    	switch (this)
    	{
	    	case IO:
	    	{
	    		return IOTypes.valueOf(s);
	    	}
	    	case PROCEDURE: 
	    	{
	    		return ProcedureTypes.valueOf(s);
	    	}
	    	case NETWORK:
	    	{
	    		return NetworkTypes.valueOf(s);
	    	}
	    	case FILESYSTEM:
	    	{
	    		return FileSystemTypes.valueOf(s);
	    	}
	    	case HTTP:
	    	{
	    		return HTTPTypes.valueOf(s);
	    	}
	    	case SQL:
	    	{
	    		return DBTypes.valueOf(s);
	    	}
	    	default:
	    	{
	    		return null;
	    	}
    	}
    }
    
    public int getClassActionTypeMask(Enum<?> en)
    {
    	switch (this)
    	{
	    	case IO:
	    	{
	    		IOTypes t = (IOTypes)en;
	    		return t.toMask();
	    	}
	    	case PROCEDURE: 
	    	{
	    		ProcedureTypes t = (ProcedureTypes)en;
	    		return t.toMask();
	    	}
	    	case NETWORK:
	    	{
	    		NetworkTypes t = (NetworkTypes)en;
	    		return t.toMask();
	    	}
	    	case FILESYSTEM:
	    	{
	    		FileSystemTypes t = (FileSystemTypes)en;
	    		return t.toMask();
	    	}
	    	case HTTP:
	    	{
	    		HTTPTypes t = (HTTPTypes)en;
	    		return t.toMask();
	    	}
	    	case SQL:
	    	{
	    		DBTypes t = (DBTypes)en;
	    		return t.toMask();
	    	}
	    	default:
	    	{
	    		return -1;
	    	}
    	}
    }
    
    /**
     * ActionClass tranlsation from string.
     * @param s
     * @return
     */
    public static ActionClasses fromString(String s)
    {
        return ActionClasses.valueOf(s);
    }
    
    public int toInt()
    {
        return this.ordinal();
    }
    
    public int toMask()
    {
        return mask;
    }
}
