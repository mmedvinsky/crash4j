/**
 * @copyright
 */
package com.crash4j.engine.spi.json;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import com.crash4j.engine.Action;
import com.crash4j.engine.Resource;
import com.crash4j.engine.spi.log.Log;
import com.crash4j.engine.spi.log.LogFactory;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.types.StatTypes;
import com.crash4j.engine.types.UnitTypes;

/**
 * 
 * @author team
 *
 */
public abstract class JSONBufferedStatsCollector extends JSONStatsCollector
{
    protected Log log = LogFactory.getLog(JSONBufferedStatsCollector.class);
    protected ResourceSpi current = null;
    protected ResourceJSONTranslator translator = new ResourceJSONTranslator();
    protected AtomicReference<JSONArray> messages = new AtomicReference<JSONArray>(new JSONArray());
    
    
    /**
     * Initialization constructor
     * @param host
     * @param port
     * @param apikey
     */
    public JSONBufferedStatsCollector()
    {
    }
    
    
    /**
     * @see com.crash4j.engine.spi.json.JSONStatsCollector#submitStats(com.crash4j.engine.Resource, com.crash4j.engine.types.ActionThemes, com.crash4j.engine.types.StatTypes, com.crash4j.engine.types.UnitTypes, long, double, double, long, double, double)
     */
    @Override
    public void submitStats(Resource resource, Action action, StatTypes stype, UnitTypes utype, 
    		long ttime, long count, double max, double min, long utime,
    		
            double lastValue, double sample)
    {
        super.submitStats(resource, action, stype, utype, ttime,  count, max, min, utime, lastValue, sample);
    }


    /**
     * @see com.crash4j.engine.spi.json.JSONStatsCollector#enterResource(com.crash4j.engine.Resource)
     */
    @Override
    public void enterResource(Resource res)
    {
        super.enterResource(res);
        this.current = (ResourceSpi)res;
    }


    /**
     * @see com.crash4j.engine.spi.json.JSONStatsCollector#exitResource(com.crash4j.engine.Resource)
     */
    @Override
    public void exitResource(Resource res)
    {
        JSONObject st = super.getStats();
        try
        {
            JSONObject o = translator.resourceAsJSON(current, st);
            //System.out.println(o);
            this.messages.get().put(o);
        } 
        catch (Exception e)
        {
            log.logError("", e);
        }
    }


    /**
     * @see com.crash4j.engine.spi.json.JSONStatsCollector#end()
     */
    @Override
    public void end()
    {
        super.end();
        JSONArray arr = this.messages.getAndSet(new JSONArray());
        try
        {
            sendMessages(arr);
        } 
        catch (IOException e)
        {
            log.logError("", e);
        }
    }
    
    /**
     * Send the current payload to be processed by the implementing subclass
     * @param arr
     * @throws IOException 
     */
    protected abstract void sendMessages(JSONArray arr) throws IOException;
 }
