/**
 * @copyright
 */
package com.crash4j;

import java.util.HashSet;
import java.util.Set;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Describes a single {@link Resource} instance that is accessed by the running application and
 * is discovered and managed by the crash4j engine.
 */
@XmlRootElement(name="resource")
public class Resource
{
    protected ObjectName name = null;
    protected String id = null;
    protected String simulationId = null;
    protected int currentTick = 0;
    protected Set<Stat> stat = new HashSet<Stat>(); 
    
    /**
     * @return the currentTick
     */
    @XmlAttribute(name="ctick")
    public int getCurrentTick()
    {
        return currentTick;
    }
    /**
     * @param currentTick the currentTick to set
     */
    public void setCurrentTick(int currentTick)
    {
        this.currentTick = currentTick;
    }
    /**
     * @return the simulationId
     */
    @XmlAttribute(name="simid")
    public String getSimulationId()
    {
        return simulationId;
    }
    /**
     * @param simulationId the simulationId to set
     */
    public void setSimulationId(String simulationId)
    {
        this.simulationId = simulationId;
    }

    
    /**
     * @return the name
     */
    @XmlAttribute(name="name")
    public String getObjectName()
    {
        return name.toString();
    }
    /**
     * @param name the name to set
     */
    public void setObjectName(String name)
    {
        try {
			this.name = new ObjectName(name);
		} catch (MalformedObjectNameException e) {
		} catch (NullPointerException e) {
		}
    }
    
    /**
     * @return the name
     */
    public ObjectName getName()
    {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(ObjectName name)
    {
        this.name = name;
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
     * @param name
     * @param id
     */
    public Resource()
    {
    }
    
    /**
     * @param name
     * @param id
     */
    public Resource(ObjectName name, String id)
    {
        this.name = name;
        this.id = id;
    }
    /**
     * @return the stat
     */
    @XmlElement(name="stat")
    public Set<Stat> getStat()
    {
        return stat;
    }
    /**
     * @param stat the stat to set
     */
    public void setStat(Set<Stat> stat)
    {
        this.stat = stat;
    }

}
