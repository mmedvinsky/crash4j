/**
 * @copyright
 */
package com.crash4j.service.session;

import java.util.NavigableSet;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author team
 *
 */
@XmlRootElement(name="crash4j")
public class StatHolder
{
    protected CrashSession session = null;
    protected String res = null;
    
    @XmlAttribute(name="etag")
    public String getEtag()
    {
        return session.getEtag();
    }
    
    /**
     * @param session
     * @param resource
     */
    public StatHolder(CrashSession session, String resource)
    {
        this.session = session;
        this.res = resource;
    }
    /**
     * @param session
     * @param resource
     */
    public StatHolder()
    {
    }
    
    /**
     * @return {@link Resource}
     */
    @XmlAttribute(name="resourceId")
    public String getResource()
    {
        return this.res;
    }    
    
    /**
     * @return the {@link Stat}
     */
    @XmlElement(name="stats")
    public NavigableSet<Stat> getStats()
    {
        return this.session.getAllStats(this.res);
    }

}
