/**
 * @copyright
 */
package com.crash4j.engine.types;

import java.util.HashMap;

import com.crash4j.engine.sim.Simulation;

/**
 * {@link BehaviorTypes} {@link Enum} is used to describe different {@link Simulation} behaviors that are possible 
 * with the current {@link Simulation} engine.
 * 
 * <pre>
 *      The following behavior  types are currently supported:
 *        
 *      (sa) static absolute behavior treats submitted values as static literal.  
 *      (sr) static relative behavior will execute value = current >= absolute ? (no modification) : abs(current - absolute)
 *      (wa) weighted absolute behavior will use provided weight and apply it to current value = (value * weight) 
 *      (wr) weighted relative behavior will use provided weight and apply it to current value = current >= (value * weight) ? (no modifications) : (value * weight)
 *      (ra) stochastic absolute behavior will use stochastic method to produce stochastic result controlled by available weight  value = (RANDOM() % weight);
 *      (rr) stochastic relative behavior will use stochastic method to produce stochastic result controlled 
 *           by available weight and current.  value  = current >= (RANDOM() % weight) ? (no modifications) : (RANDOM() % weight); 
 * </pre>
 * 
 * @author <crash4j team>
 * 
 */
public enum BehaviorTypes
{
    STATIC_ABSOLUTE("sa", 0, 2),  
    STATIC_RELATIVE("sr", 1, 4),  
    WEIGHTED_ABSOLUTE("wa", 2, 8),  
    WEIGHTED_RELATIVE("wr", 3, 16),  
    STOCHASTIC_ABSOLUTE("ra", 4, 32),  
    STOCHASTIC_RELATIVE("rr", 5, 64); 
    
    
    static final HashMap<String, BehaviorTypes> map = new HashMap<String, BehaviorTypes>();
    static 
    {
        map.put("sa", STATIC_ABSOLUTE);
        map.put("sr", STATIC_RELATIVE);
        map.put("wa", WEIGHTED_ABSOLUTE);
        map.put("wr", WEIGHTED_RELATIVE);
        map.put("ra", STOCHASTIC_ABSOLUTE);
        map.put("rr", STOCHASTIC_RELATIVE);
    }
    
    private String behavior = null;
    private int id = 0;
    private int mask = 0;
    
    /**
     * Initialization constructor.
     * @param action name
     * @param int id
     */
    private BehaviorTypes(String action, int id, int mask)
    {
        this.behavior = action;
        this.id = id;
        this.mask = mask;
    }
    
    /**
     * get an instance of {@link BehaviorTypes} translated from {@link String}
     * @param s 
     * @return
     */
    public static BehaviorTypes fromString(String s)
    {
        return map.get(s);
    }
    
    public int toInt()
    {
        return id;
    }
    
    /**
     * @return the mask
     */
    public int getMask()
    {
        return mask;
    }

    /**
     * @param mask the mask to set
     */
    public void setMask(int mask)
    {
        this.mask = mask;
    }

    /**
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString()
    {
        return this.behavior;
    }
}
