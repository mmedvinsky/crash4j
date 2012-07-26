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

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.xml.bind.annotation.XmlAttribute;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


/**
 * @author team
 *
 */
public class CrashSession
{
    protected String etag = null;
    protected ConcurrentHashMap<String, JSONObject> resources = new ConcurrentHashMap<String, JSONObject>();
    protected ConcurrentHashMap<String, _sim_tracker> simulations = new ConcurrentHashMap<String, _sim_tracker>();
    /**
     * @return the etag
     */
    @XmlAttribute(name="etag")
    public String getEtag()
    {
        return etag;
    }

    /**
     * Simple wrapper object that will track stages of simulation 
     * download and runtime
     */
    class _sim_tracker
    {
    	public JSONObject sim;
    	public boolean distributed = false;
    	public boolean started = false;
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
     * @return list of update commands
     */
    public JSONArray updateCommands()
    {
    	JSONArray cmds = new JSONArray();
    	for (_sim_tracker t : this.simulations.values()) 
    	{
    		if (!t.distributed)
    		{
    			cmds.put("sim");
    		}
		}
    	return cmds;
     }
    
    /**
     * Adds simulation to session.
     * @param obj
     */
    public void addSimulation(JSONObject obj)
    {
    	_sim_tracker t = new _sim_tracker();
    	t.sim = obj;
    	this.simulations.put(obj.optString("name"), t);
    }
    
    /**
     * Adds simulation to session.
     * @param obj
     */
    public void getSimulationForDistribution(JSONArray cont)
    {
    	for (_sim_tracker t : this.simulations.values()) 
    	{
    		if (!t.distributed)
    		{
    			t.distributed = true;
    			cont.put(t.sim);
    		}
		}
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

            JSONObject res = (JSONObject)this.resources.get(id);
            if (res == null)
            {
                res = new JSONObject();
                res.put("id", id);
                res.put("name", o.toString());                
                res.put("stat", new JSONArray());                
                this.resources.put(id, res);
            }
            else
            {
            	System.out.println("Duplicate");
            }
            JSONObject js = rr.optJSONObject("stat");
            JSONObject siminfo = rr.optJSONObject("sim");
            JSONObject srec = new JSONObject();
            srec.put("stat", js);
            if (siminfo != null)
            {
                srec.put("sim", siminfo);
            }
            res.getJSONArray("stat").put(srec);
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
    public  Collection<JSONObject> getResources()
    {
        return this.resources.values();
    }
    
    /**
     * @param id
     * @return
     */
    public JSONObject getResourceById(String id)
    {
        return (JSONObject)resources.get(id);
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
}
