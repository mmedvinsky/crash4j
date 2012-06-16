/**
 * @copyright
 */
package com.crash4j.engine.types;

import com.crash4j.engine.sim.Simulation;



/**
 * {@link InstructionTypes} {@link Enum} is used to describe different {@link Simulation} behavior instructions that are possible 
 * with the current {@link Simulation} engine.
 * 
 * <pre>
 *      The following behavior instructions types are currently supported:
 *        
 *      (d) effects time delay
 *      (t) effects throughput bytes/us
 *      (e) causes errors 
 *      (c) closes resources  
 * </pre>
 * 
 * @author <crash4j team>
 * 
 */
public enum InstructionTypes
{
    DELAY("d", 0),  
    THROUGHPUT("t", 1), 
    ERROR("e", 2), 
    CLOSE("c", 3);
    
    private String behavior = null;
    private int id = 0;
    
    /**
     * Initialization constructor.
     * @param action name
     * @param int id
     */
    private InstructionTypes(String action, int id)
    {
        this.behavior = action;
        this.id = id;
    }
    
    /**
     * get an instance of {@link InstructionTypes} translated from {@link String}
     * @param s 
     * @return
     */
    public static InstructionTypes fromString(String s)
    {
        return fromString(s.charAt(0));
    }
    
    public int toInt()
    {
        return id;
    }
    
    /**
     * get an instance of {@link InstructionTypes} from string.
     * @param a
     * @return
     */
    public static InstructionTypes fromString(char a)
    {
        switch (a)
        {
        case 'd' : return DELAY;
        case 't' : return THROUGHPUT;
        case 'e' : return ERROR;
        case 'c' : return CLOSE;
        default : throw new RuntimeException("Unknwon behavior "+a);
        }
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
