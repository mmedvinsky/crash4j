/**
 * 
 */
package com.crash4j.engine.types;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * {@link UnitTypes} represent different types of units and is closely related to {@link TimeUnit}
 */
public enum UnitTypes
{
    NANOSECONDS(0, TimeUnit.NANOSECONDS, "nan"),
    MICROSECONDS(1,TimeUnit.MICROSECONDS, "us"), 
    MILLISECONDS(2, TimeUnit.MILLISECONDS, "ms"), 
    SECONDS(3, TimeUnit.SECONDS, "sec"), 
    NONE(4, null, "none"), 
    PERCENT(5, null, "%"); 
    
    private int utype = 0;
    private String name;
    private TimeUnit tunit = null;
    
    protected static HashMap<String, UnitTypes> strmap = new HashMap<String, UnitTypes>();
    protected static HashMap<UnitTypes, String> typemap = new HashMap<UnitTypes, String>();
    
    static {
        
        strmap.put(SECONDS.toString(), UnitTypes.SECONDS);      
        strmap.put(MILLISECONDS.toString(), UnitTypes.MILLISECONDS);      
        strmap.put(MICROSECONDS.toString(), UnitTypes.MICROSECONDS);      
        strmap.put(NANOSECONDS.toString(), UnitTypes.NANOSECONDS);      
        strmap.put(NONE.toString(), UnitTypes.NONE);      
    }
    
    /**
     * Convert one unit to another
     * @param source
     * @param dest
     * @param val
     * @return
     */
    public static long convert(UnitTypes source, UnitTypes dest, long val)
    {
        if (source.tunit != null)
        {
    		return dest.tunit.convert(val, source.tunit);
        }
        throw new RuntimeException("Incompatable types");
    }
    
    /**
     * @param Monitor type
     */
    private UnitTypes(int mtype, TimeUnit tu, String name)
    {
        this.utype = mtype;
        this.name = name;
        this.tunit = tu;
    }
    
    public static UnitTypes fromString(String s)
    {
        return strmap.get(s);
    }
    
    /**
     * @return int value of this enum
     */
    public int intValue()
    {
        return utype;
    }
    
    /**
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString()
    {
        return this.name;
    }
    
    public TimeUnit toTimeUnit()
    {
        return tunit;
    }
}

