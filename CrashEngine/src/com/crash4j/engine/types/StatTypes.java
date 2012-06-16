/**
 * @copyright
 */
package com.crash4j.engine.types;

import java.util.HashMap;


/**
 *   
 *   TIME, time spent on any action (time)
 *   THROUGHPUT, throughput will calculate (units/time)
 *   OCCUPANCY,  In flight count
 *   COUNT;      total count
 * 
 */
public enum StatTypes
{
    TIME(0, "time"), 
    THROUGHPUT(1, "throughput"), 
    OCCUPANCY(2, "occupancy"), 
    COUNT(3, "count"), 
    ERROR(4, "error"),
    ACTUALTIME(5, "actualtime");
    
    private int mtype = 0;
    private String name;
    protected static HashMap<String, StatTypes> strmap = new HashMap<String, StatTypes>();
    
    static {
        
        strmap.put("time", StatTypes.TIME);      
        strmap.put("throughput", StatTypes.THROUGHPUT);      
        strmap.put("occupancy", StatTypes.OCCUPANCY);      
        strmap.put("count", StatTypes.COUNT);      
        strmap.put("error", StatTypes.ERROR);      
        strmap.put("actualtime", StatTypes.ACTUALTIME);      
    }
    
    /**
     * @param Monitor type
     */
    private StatTypes(int mtype, String name)
    {
        this.mtype = mtype;
        this.name = name;
    }
    
    public static StatTypes fromString(String s)
    {
        return strmap.get(s);
    }
    
    /**
     * @return int value of this enum
     */
    public int intValue()
    {
        return mtype;
    }
    
    /**
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString()
    {
        return name;
    }
}

