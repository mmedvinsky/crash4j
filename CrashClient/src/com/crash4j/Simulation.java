package com.crash4j;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The {@link SimulationImpl} class describes a single {@link SimulationImpl} that is running 
 * within the simulation engine.  A simulation is mapped onto a set of resources it produces behavior modifications for.
 * 
 * The simulation will run in its own thread and will count down the simulation ticks.
 * For each tick a running behavior within the simulation will be positioned or the current value, at which time a caller is able
 * to get a set of current actions that are suppose to be taken during this tick.
 * 
 * simulation runs [0...n] ticks 
 * instruction [tn, A, W, w]
 *
 * Behaviors are:
 * <pre>
 * tn: is a number of tick where this starts
 * A : is an action to perform. (0:time delay, 1:resource drop, 2: throughput, 3: raise error)
 * W : is a weight that governs if the said behavior will manifest.  0-1 and 1 means that it will appear all the time.
 * w : is a weight that is used to generate the actual adjustment value.
 *       A(0) dt = t*w
 *       A(1) w is ignored.
 *       A(2) tp (bytes/us) = requested_bytes - (requested_bytes*w)  
 *       A(3) w is ignored.
 * </pre>
 * @author team
 *
 */
@XmlRootElement(name="simulation")
public class Simulation
{
    protected String name = null;
    protected String description = null;
    protected Set<Behavior> behaviors = new HashSet<Behavior>();
    protected String id = UUID.randomUUID().toString();
    protected long tick = 0;
    protected int frequency = 1;
    protected Set<String> mappings = new HashSet<String>();
    
    /**
     * @return the description
     */
    @XmlAttribute(name="description")
    public String getDescription()
    {
        return description;
    }
    /**
     * @param description the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
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
	 * @return the mappings
	 */
    @XmlElement(name="mappings", type=String.class)
	public Set<String> getMappings() {
		return mappings;
	}
	/**
	 * @param mappings the mappings to set
	 */
	public void setMappings(Set<String> mappings) {
		this.mappings = mappings;
	}
	/**
     * @return the behaviors
     */
    @XmlElement(name="behaviors", type=Behavior.class)
    public Set<Behavior> getBehaviors()
    {
        return behaviors;
    }
    /**
     * @param behaviors the behaviors to set
     */
    public void setBehaviors(Set<Behavior> behaviors)
    {
        this.behaviors = behaviors;
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
     * @return the tick
     */
    @XmlAttribute(name="tick")
    public long getTick()
    {
        return tick;
    }
    /**
     * @param tick the tick to set
     */
    public void setTick(long tick)
    {
        this.tick = tick;
    }
    /**
     * @return the frequency
     */
    @XmlAttribute(name="frequency")
    public int getFrequency()
    {
        return frequency;
    }
    /**
     * @param frequency the frequency to set
     */
    public void setFrequency(int frequency)
    {
        this.frequency = frequency;
    }

    
    
}