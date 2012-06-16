/**
 * @copyright
 */
package com.crash4j.engine.types;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.HashMap;

/**
 * The {@link ResourceTypes} enumeration describes resource types that application could raise during runtime.
 * 
 * <pre>
 * (net)  is a level 0 network resource type, describing various networking operations that application will perform.
 * (fsys) is a level 0 file system resource type, describing various file access operations.
 * (http) is a level 1 protocol resource that is executing res over HTTP/HTTPS protocol.
 * (db)   is a level 1 protocol resource type that describes database type interactions 
 * (ms)   is a level 1 protocol resource type that describes messaging type interactions 
 * (mail) is a level 1 protocol resource type that describes mail type interactions 
 * (gen)  is a level 2 generic resource type that can describes any user specified operation.
 * 
 * </pre>
 * 
 * <table>
 * <tr>
 * </tr>
 * </table>
 * 
 * @author <crash4j team>
 * 
 */
public enum ResourceTypes
{
    NET ("net"),  
    FSYS("fsys"), 
    HTTP("http"), 
    DB  ("db"), 
    MSG ("msg"), 
    SMTP("smtp"), 
    SERVICE("service"), 
    //Space for extensions
    GEN ("gen");
    
    protected static final HashMap<String, ResourceTypes> map = new HashMap<String, ResourceTypes>();
    static {
    	map.put("net", NET);
    	map.put("fsys", FSYS);
    	map.put("http", HTTP);
    	map.put("db", DB);
    	map.put("msg", MSG);
    	map.put("mail", SMTP);
    	map.put("gen", GEN);
    	map.put("service", SERVICE);
    }
    
    
    /** String representation of this res */
    private String res = null;
    /** mul^2 mask supports combinations*/
    private int mask = 0;
    
    /**
     * @param res
     */
    private ResourceTypes(String action)
    {
        this.res = action;
        this.mask = (int)Math.pow(2, (this.ordinal()+1));
    }
    
    public int toInt()
    {
        return this.ordinal();
    }
    
    public int toMask()
    {
        return mask;
    }
    
    /**
     * Convert to {@link ResourceTypes} from string
     * @param a
     * @return
     */
    public static ResourceTypes fromString(String a)
    {
    	return map.get(a);
    }
    /**
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString()
    {
        return this.res;
    }
}
