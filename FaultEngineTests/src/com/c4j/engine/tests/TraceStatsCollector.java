/**
 * 
 */
package com.c4j.engine.tests;

import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.management.ObjectName;

import org.junit.runner.RunWith;

import com.crash4j.engine.Action;
import com.crash4j.engine.Resource;
import com.crash4j.engine.StatsCollector;
import com.crash4j.engine.client.json.JSONArray;
import com.crash4j.engine.client.json.JSONException;
import com.crash4j.engine.client.json.JSONObject;
import com.crash4j.engine.types.StatTypes;
import com.crash4j.engine.types.UnitTypes;
import com.crash4j.junit.CrashRunner;

/**
 * Trace collector used to track and assert results of the test
 */
@RunWith(CrashRunner.class)
public class TraceStatsCollector implements StatsCollector
{
    protected volatile long lastAccessTime = 0;
    protected volatile long xi = 0;
    protected StringBuffer actions = new StringBuffer();
    protected JSONObject report = new JSONObject();
    protected String filter = new String(".*[.]class.*");
    
    public String getActions()
    {
        return actions.toString();
    }
    
    /**
     * @return the filter
     */
    public String getFilter()
    {
        return filter;
    }

    /**
     * @param filter the filter to set
     */
    public void setFilter(String filter)
    {
        this.filter = filter;
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
    public void submitStats(Resource resource, Action action, StatTypes stype, UnitTypes utype, long ttime, long count, double max, double min, long utime,
            double lastValue, double sample)
    {
        
        JSONObject res = null;
        String url = "Unknown";
        ObjectName resc = resource.asVector();
        if (resc != null)
        {
            url = resc.toString();
        }
        else
        {
            url = "Unknown";
        }
        
        if (url.matches(this.filter))
        {
            return;
        }
        
        try {res= report.getJSONObject(url);}catch (Exception e){}
        try
        {
            if (res == null)
            {
                res = new JSONObject();
                report.put(url, res);
            }
            else
            {
               // String k = Utils.wrappedObjectId(Utils.wrapObject(resource), resource);
               // System.out.println("Double resource:"+url+" "+k);
            }
            
            
            JSONObject act = null;
            try 
            {
                act = res.getJSONObject(action.toString());
            } 
            catch (Exception e) 
            {
            }
            
            if (act == null)
            {
                act = new JSONObject();
                res.put(action.toString(), act);
            }
            
            JSONObject sttype = null;
            try 
            {
                sttype = act.getJSONObject(stype.toString());
            } 
            catch (Exception e) 
            {
            }
            
            if (sttype == null)
            {
                sttype = new JSONObject();
                act.put(stype.toString(), sttype);
                sttype.put("data", new JSONArray());
            }
            
            SimpleDateFormat f =
                    new SimpleDateFormat("dd-mm-yyyy HH:mm:ss:SSS zzz");
            
            
            JSONArray aa = sttype.getJSONArray("data");
            JSONObject inrec = new JSONObject();
            inrec.put("count", count);
            inrec.put("lut", f.format(new Date(utime)));
            inrec.put("min", min);
            inrec.put("max", max);
            inrec.put("value", sample);
                     
            sttype.getJSONArray("data").put(inrec);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        //System.out.println(report);
    }

    /**
     * @return the report
     */
    public JSONObject getReport()
    {
        return report;
    }

    @Override
    public void enterResource(Resource res)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void exitResource(Resource res)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void start()
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void stop()
    {
        // TODO Auto-generated method stub
        
    }
}
