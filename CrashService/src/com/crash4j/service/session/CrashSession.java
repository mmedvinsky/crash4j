/**
 * @copyright
 */
package com.crash4j.service.session;

import java.util.Collection;
import java.util.Comparator;
import java.util.NavigableSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


/**
 * @author team
 *
 */
@XmlRootElement(name="crash4j")
public class CrashSession
{
    protected String etag = null;
    protected ConcurrentHashMap<String, Resource> resources = new ConcurrentHashMap<String, Resource>();
    
    /**
     * @return the etag
     */
    @XmlAttribute(name="etag")
    public String getEtag()
    {
        return etag;
    }

    /**
     * Utility class for {@link Stat} ordering.
     * @author team
     *
     */
    class _stat_comparator implements Comparator<Stat>
    {

        @Override
        public int compare(Stat o1, Stat o2)
        {
            return o1.getLastUpdateTime() == o2.getLastUpdateTime() ? 0 : (int)(o1.getLastUpdateTime() - o2.getLastUpdateTime());
        }
        
    }
    
    /**
     * Internal structure to cache stat information about the resource.
     * @author team
     *
     */
    class _resource_ extends Resource
    {
        protected ConcurrentSkipListSet<Stat> stats = new ConcurrentSkipListSet<Stat>(new _stat_comparator());
        
        /**
         * @param r
         */
        public _resource_(ObjectName o, String id)
        {
            super(o, id);
        }
        
        
        /**
         * Adds another set of statistics to this object
         * @param s
         */
        public void addStat(Stat s)
        {
            stats.add(s);
        }
        
        /**
         * Get all {@link Stat} as of a specific time.
         * @param tm
         * @return
         */
        public NavigableSet<Stat> getStatAsOf(long tm)
        {
            Stat s = new Stat();
            s.setLastUpdateTime(tm);
            return this.stats.tailSet(s);
        }
        
        /**
         * 
         * @return get all system stats for this resource
         */
        public NavigableSet<Stat> getAllStats()
        {
            return this.stats;
        }
    }
    
    /**
     * release all resources
     */
    public void release()
    {
    }
    
    /**
     * Process the data through in memory queue
     * @param data
     * @throws JSONException 
     * @throws NullPointerException 
     * @throws MalformedObjectNameException 
     */
    public void submit(JSONArray data) throws IllegalStateException, MalformedObjectNameException, 
                NullPointerException, JSONException
    {
        //Loop around submitted resources and add to the data structure.
        for (int i = 0; i < data.length(); i++)
        {
            JSONObject rr = data.optJSONObject(i);
            ObjectName o = new ObjectName(rr.getString("name"));
            String id = rr.getString("id");

            _resource_ res = (_resource_)this.resources.get(id);
            if (res == null)
            {
                res = new _resource_(o, id);
                this.resources.put(res.getId(), res);
            }
            
            JSONArray js = rr.optJSONArray("stat");
            JSONObject siminfo = rr.optJSONObject("sim");
            
            if (siminfo != null)
            {
                res.setSimulationId(siminfo.getString("simid"));
                res.setCurrentTick(siminfo.getInt("ctick"));
            }
            
            
            for (int k = 0; k < js.length(); k++)
            {
                JSONObject details = js.getJSONObject(k);
                Stat s = new Stat();
                s.setAction(details.getString("action"));
                s.setType(details.getString("type"));
                s.setTimeUnit(TimeUnit.MICROSECONDS);
                s.setCount(details.getLong("count"));
                s.setAverage(details.getDouble("avg"));
                s.setMax(details.getDouble("max"));
                s.setMin(details.getDouble("min"));
                s.setLastUpdateTime(details.getLong("timestamp"));
                s.setLastUpdateValue(details.getDouble("last"));
                res.addStat(s);
            }
            
        }
    }
    
    /**
     * @return new instance of {@link CrashSession}
     */
    public static CrashSession newSession()
    {
        return new CrashSession(UUID.randomUUID().toString());
    }
    
    public CrashSession()
    {
        
    }
    
    /**
     * Instantiate session instance with the tag
     * @param etag
     */
    private CrashSession(String etag)
    {
        this.etag = etag;
    }
    
    /**
     * Check to see if resource exists
     * @param id
     * @return
     */
    @XmlElement(name="resources")
    public  Collection<Resource> getResources()
    {
        return this.resources.values();
    }
    
    /**
     * @param id
     * @return
     */
    public NavigableSet<Stat> getAllStats(String id)
    {
        _resource_ r = (_resource_)resources.get(id);
        if (r != null)
        {
            return r.getAllStats();
        }        
        return null;
    }
    
    /**
     * @param id
     * @return
     */
    public NavigableSet<Stat> getStatAsOf(String id, long tm)
    {
        _resource_ r = (_resource_)resources.get(id);
        if (r != null)
        {
            return r.getStatAsOf(tm);
        }        
        return null;
    }
    
    /**
     * Check to see if resource exists
     * @param id
     * @return
     */
    public boolean hasResource(String id)
    {
        return this.resources.containsKey(id);
    }
    /**
     * get resource....
     * @param id
     * @return
     */
    public Resource getResource(String id)
    {
        _resource_ r = (_resource_)resources.get(id);
        if (r != null)
        {
            return r;
        }        
        return null;
    }
}
