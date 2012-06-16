/**
 * @copyright
 */
package com.crash4j.engine.types.classtypes;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * 
 * @author <crash4j team>
 * 
 */
public enum DBTypes
{
    SELECT,  
    INSERT, 
    UPDATE, 
    DELETE, 
    USE, 
    GRANT, 
    CREATE,
    ALTER,
    DROP,
    ANALYZE,
    ASSOCIATE,
    AUDIT,
    COMMENT,
    DISASSOCIATE,
    FLASHBACK,
    NOAUDIT,
    PURGE,
    RENAME,
    REVOKE,
    TRUNCATE,
    UNDROP,
    CALL,
    EXPLAIN,
    LOCK,
    MERGE,
    COMMIT,
    ROLLBACK,
    SAVEPOINT,
    CLOSE,
    EXECUTE,
    SET,
    SQLCOMMAND;
    
    /** mul^2 mask supports combinations*/
    private int mask = 0;
    
    /**
     * @param action
     */
    private DBTypes()
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
