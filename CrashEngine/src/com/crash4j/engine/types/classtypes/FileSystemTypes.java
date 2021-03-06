/**
 * @copyright
 */
package com.crash4j.engine.types.classtypes;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import com.crash4j.engine.types.ActionClasses;

/**
 * 
 * @author <crash4j team>
 * 
 */
public enum FileSystemTypes
{
    CREATE,  
    RENAME,  
    DELETE, 
    CHANGESTATE, 
    CHECKPERMISSION,
    GETINFO,
    EXIST;
    
    /** mul^2 mask supports combinations*/
    private int mask = 0;
    
    /**
     * @param action
     */
    private FileSystemTypes()
    {
        this.mask = (int)Math.pow(2, (this.ordinal()+1));
    }
    public static DBTypes fromString(String s)
    {
    	return DBTypes.valueOf(s.toUpperCase());
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
