package com.crash4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * The {@link Behaviors} class describes a single behavior that can be used to modify the base behavior of the {@link Resource} 
 * at runtime.</p>
 * For example:<br/>
 * <b>fsys:mt=/dev/dev1,resource=/user/a/b/c.txt</b> is being read, an event that will yield a <b>(r)</b> action to be raised.  The read time or throughput monitors will 
 * produce time(us) and throughput(bytes/us) data that can be modified by the behavior.
 * @author <crash4j team>
 */
public class Behavior
{
    protected boolean stop = false;
    protected boolean retain = false;
    protected boolean replay = true;
    protected boolean suspended = false;
    protected String id = UUID.randomUUID().toString();
    protected String name = null;
    protected Set<String> actions = new HashSet<String>();
    protected String iType = null;
    protected String bType = null;
    protected ArrayList<Instruction> instructions = new ArrayList<Instruction>();
    
    
    /**
     * @return the stop
     */
    @XmlAttribute(name="stop")
    public boolean isStop()
    {
        return stop;
    }
    /**
     * @param stop the stop to set
     */
    public void setStop(boolean stop)
    {
        this.stop = stop;
    }
    /**
     * @return the retain
     */
    @XmlAttribute(name="retain")
    public boolean isRetain()
    {
        return retain;
    }
    /**
     * @param retain the retain to set
     */
    public void setRetain(boolean retain)
    {
        this.retain = retain;
    }
    /**
     * @return the replay
     */
    @XmlAttribute(name="rotate")
    public boolean isRotate()
    {
        return replay;
    }
    /**
     * @param replay the replay to set
     */
    public void setRotate(boolean replay)
    {
        this.replay = replay;
    }
    /**
     * @return the suspended
     */
    public boolean isSuspended()
    {
        return suspended;
    }
    /**
     * @param suspended the suspended to set
     */
    public void setSuspended(boolean suspended)
    {
        this.suspended = suspended;
    }
    /**
     * @return the id
     */
    @XmlAttribute(name="id")
    public String getId()
    {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(String id)
    {
        this.id = id;
    }
    /**
     * @return the name
     */
    @XmlAttribute(name="name")
    public String getName()
    {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }
    /**
     * @return the actions
     */
    @XmlElement(name="actions", type=String.class)
    public Set<String> getActions()
    {
        return actions;
    }
    /**
     * @param actions the actions to set
     */
    public void setActions(Set<String> actions)
    {
        this.actions = actions;
    }
    /**
     * <pre>
     *      (d) effects time delay
     *      (t) effects throughput bytes/us
     *      (e) causes errors 
     *      (c) closes resources  
     * </pre>
     * 
     * @return the instruction type
     * 
     */
    @XmlAttribute(name="itype")
    public String getType()
    {
        return iType;
    }
    /**
     * @param type the type to set
     */
    public void setType(String type)
    {
        this.iType = type;
    }
    
    /**
     * 
     * <pre>
     *      (sa) static absolute behavior treats submitted values as static literal.  
     *      (sr) static relative behavior will execute value = current >= absolute ? (no modification) : abs(current - absolute)
     *      (wa) weighted absolute behavior will use provided weight and apply it to current value = (value * weight) 
     *      (wr) weighted relative behavior will use provided weight and apply it to current value = current >= (value * weight) ? (no modifications) : (value * weight)
     *      (ra) stochastic absolute behavior will use stochastic method to produce stochastic result controlled by available weight  value = (RANDOM() % weight);
     *      (rr) stochastic relative behavior will use stochastic method to produce stochastic result controlled 
     *           by available weight and current.  value  = current >= (RANDOM() % weight) ? (no modifications) : (RANDOM() % weight); 
     * </pre>
     * @return the behavior type
     */
    @XmlAttribute(name="btype")
    public String getBehaviorType()
    {
        return bType;
    }
    /**
     * @param type the type to set
     */
    public void setBehaviorType(String type)
    {
        this.bType = type;
    }
    /**
     * @return the instructions
     */
    @XmlElement(name="instructions", type=Instruction.class)
    public List<Instruction> getInstructions()
    {
        return instructions;
    }
}