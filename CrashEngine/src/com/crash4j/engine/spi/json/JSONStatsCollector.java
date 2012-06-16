/**
 * @copyright
 */
package com.crash4j.engine.spi.json;

import java.util.Locale;

import com.crash4j.engine.Action;
import com.crash4j.engine.Resource;
import com.crash4j.engine.StatsCollector;
import com.crash4j.engine.spi.log.Log;
import com.crash4j.engine.spi.log.LogFactory;
import com.crash4j.engine.spi.util.Utils;
import com.crash4j.engine.types.StatTypes;
import com.crash4j.engine.types.UnitTypes;

/**
 * JSON stats collection.
 * Collects stats into a json object.
 * @author team
 *
 */
public class JSONStatsCollector implements StatsCollector
{
    protected JSONObject stats = null;
    protected static Log log = LogFactory.getLog(JSONStatsCollector.class);
    protected long lastAccessTime = System.currentTimeMillis();
    /**
     * Create a {@link JSONStatsCollector} with a {@link JSONObject} parent
     */
    public JSONStatsCollector()
    {
    }

    /**
     * @see com.crash4j.engine.StatsCollector#start()
     */
    @Override
    public void start()
    {
    }

    /**
     * @see com.crash4j.engine.StatsCollector#stop()
     */
    @Override
    public void stop()
    {
    }

    /**
     * @return the stats
     */
    public JSONObject getStats()
    {
        return stats;
    }

    /**
     * @see com.crash4j.engine.StatsCollector#getLastAccessTime()
     */
    @Override
    public long getLastAccessTime()
    {
        return lastAccessTime;
    }

    /**
     * @see com.crash4j.engine.StatsCollector#begin()
     */
    @Override
    public void begin()
    {
        //log.logError("net Starting:"+lastAccessTime);
    }

    /**
     * @see com.crash4j.engine.StatsCollector#end()
     */
    @Override
    public void end()
    {
        lastAccessTime = System.currentTimeMillis();
    }

    /**
     * @see com.crash4j.engine.StatsCollector#submitStats(com.crash4j.engine.Resource, com.crash4j.engine.types.ActionThemes, com.crash4j.engine.types.StatTypes, com.crash4j.engine.types.UnitTypes, long, double, double, long, double, double)
     */
    @Override
    public void submitStats(Resource resource, Action action, StatTypes stype, 
    		UnitTypes utype, long totaltime, long count, double max, double min, long utime,
            double lastValue, double sample)
    {
        try
        {
            JSONObject st = null;
            st = this.stats.optJSONObject(action.toString());
            if (st == null)
            {
                st = new JSONObject();
                this.stats.put(action.toString(), st);
            }
            
            
            JSONObject styp = new JSONObject();
            st.put(stype.toString().toString(), styp);
            styp.put("count", count);
            styp.put("totaltime", totaltime);
            styp.put("max", Utils.formatDouble(max, Locale.getDefault()));
            styp.put("min", Utils.formatDouble(min, Locale.getDefault()));
            styp.put("timestamp", utime);
            styp.put("last", Utils.formatDouble(lastValue, Locale.getDefault()));
            styp.put("avg", Utils.formatDouble(sample, Locale.getDefault()));
            styp.put("units", utype.toString());
            
        } 
        catch (JSONException e)
        {
            log.logError("Error generating statistics", e);
        }
        
    }

    @Override
    public void enterResource(Resource res)
    {
        stats = new JSONObject();
    }

    @Override
    public void exitResource(Resource res)
    {
        stats = new JSONObject();
    }
}
