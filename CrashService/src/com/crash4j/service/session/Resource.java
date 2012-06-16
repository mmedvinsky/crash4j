/**
 * @copyright
 */
package com.crash4j.service.session;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.xml.bind.annotation.XmlAttribute;
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
    
    public Resource(){}
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
     * @return the name
     */
    @XmlAttribute(name="name")
    public String getName()
    {
        return name.toString();
    }
    /**
     * @param name the name to set
     * @throws NullPointerException 
     * @throws MalformedObjectNameException 
     */
    public void setName(String name) throws MalformedObjectNameException, NullPointerException
    {
        this.name = new ObjectName(name);
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
    public Resource(ObjectName name, String id)
    {
        this.name = name;
        this.id = id;
    }
    /**
     * @return the simulationId
     */
    @XmlAttribute(name="simulationId")
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
    
    
}
