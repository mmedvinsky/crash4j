/**
 * @copyright
 */
package com.crash4j.engine;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.crash4j.engine.sim.Behavior;
import com.crash4j.engine.sim.Simulation;
import com.crash4j.engine.spi.ResourceManagerSpi;
import com.crash4j.engine.spi.json.JSONArray;
import com.crash4j.engine.spi.json.JSONObject;
import com.crash4j.engine.spi.json.JSONStatsCollector;
import com.crash4j.engine.spi.json.ResourceJSONTranslator;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.resources.impl.TagKey;

/**
 * {@link ResourceService} is a bridge class that provides public access abstraction to the underlying engine
 * With this class you can:
 * <ol>
 * <li>Get {@link Resource} information</li>
 * <li>Collect statistics from running {@link Resource} instances</li>
 * <li>Add, Remove, Change, Start, Stop {@link Simulation}</li>
 * <li>Start, Stop, Suspend the engine</li>
 * </ol>
 * 
 * @author <MM>
 * 
 */
public class ResourceService
{
    protected ResourceJSONTranslator translator = new ResourceJSONTranslator();
    /**
     * @return resources that are currently managed by the runtime as JSON
     *         String, for exanple [ {"resource" :
     *         "net:protocol=tcp, la=localhost, lp=8080, rp=localhost, rp=6567"
     *         }, ...]
     */
    public String getResources() throws Exception
    {
        Map<ResourceSpi, ResourceSpi> spis = ResourceManagerSpi.getResourceSpis();
        JSONArray arr =  translator.resourcesAsJSON(spis.values());
        if (arr != null)
        {
            return arr.toString();
        }
        return null;
    }

    /**
     * @return information about the resource, specified by the name [
     *         {"resource" :
     *         "net:protocol=tcp, la=localhost, lp=8080, rp=localhost, rp=6567"
     *         }, ...]
     */
    public String getResource(String etag) throws Exception
    {
        Map<ResourceSpi, ResourceSpi> spis = ResourceManagerSpi.getResourceSpis();
        ResourceSpi spi = spis.get(new TagKey(etag));
        if (spi != null)
        {
            JSONObject o = _getResourceDetails(spi);
            return o.toString();
        }
        return null;
    }

    /**
     * @return information about the resource, specified by the name [
     *         {"resource" :
     *         "net:protocol=tcp, la=localhost, lp=8080, rp=localhost, rp=6567"
     *         }, ...]
     */
    protected JSONObject _getResourceDetails(ResourceSpi spi) throws Exception
    {
        Simulation sim = spi.getSimulation();
        JSONStatsCollector jcollector = new JSONStatsCollector();
        ResourceManagerSpi.collectStats(jcollector, spi);
        return translator.resourceAsJSON(spi, jcollector.getStats());
    }

    /**
     * Add a simulation
     * 
     * @param sim
     */
    public void addSimulation(String sim) throws Exception
    {
        JSONObject jSim = new JSONObject(sim);
        ArrayList<String> maps = new ArrayList<String>();
        Simulation s = translator.simulationFromJSON(jSim, maps);
        ResourceManagerSpi.addSimulation(s);
        try
        {
            for (int k = 0; k < maps.size(); k++)
            {
                ResourceManagerSpi.getSimulationManager().addSimulationMapping(maps.get(k), s.getId());
            }
        } catch (Exception e)
        {
            // no behaviors are included in original submission
        }

    }

    public void addBehavior(String id, String bh) throws Exception
    {
        ResourceManagerSpi.addBehavior(id, _readBehavior(new JSONObject(bh)));
    }

    /**
     * Adds a simulation
     * 
     * @param s
     * @param behavior
     */
    protected Behavior _readBehavior(JSONObject jb) throws Exception
    {
        return translator.behaviorFromJSON(jb.getString("id"), jb);
    }

    /**
     * get simulation y id
     * 
     * @param id
     */
    public String getSimulation(String id) throws Exception
    {
        Simulation sim = ResourceManagerSpi.getSimulationManager().getSimulation(id);
        if (sim == null)
        {
            return null;
        }
        return translator.simulationAsJSON(sim).toString();
    }

    /**
     * Start {@link Simulation}
     */
    public void startSimulation(String id)
    {
        Simulation s = ResourceManagerSpi.getSimulationManager().getSimulation(id);
        if (s == null)
        {
            return;
        }
        s.start();
    }

    /**
     * Stop {@link Simulation}
     */
    public void stopSimulation(String id)
    {
        Simulation s = ResourceManagerSpi.getSimulationManager().getSimulation(id);
        if (s == null)
        {
            return;
        }
        s.stop();
    }

    /**
     * Stop {@link Simulation}
     */
    public void stopAllSimulation()
    {
        ResourceManagerSpi.getSimulationManager().stopAll();
    }

    /**
     * Start {@link Simulation}
     */
    public void startAllSimulation()
    {
        ResourceManagerSpi.getSimulationManager().startAll();
    }

    /**
     * Register collector.
     * @param klass
     * @param props
     * @param period
     * @param unit
     */
    public String registerCollector(String klass, Map<String, Object> props, long period, TimeUnit unit) throws Exception
    {
        return ResourceManagerSpi.registerCollector(klass, props, period, unit);
    }
    
    /**
     * 
     * @param moniker
     */
    public void unRegisterCollector(String moniker)
    {
        ResourceManagerSpi.unRegisterCollector(moniker);
    }
    
    /**
     * Clear stats infromation from the engine.
     */
    public void clearStats()
    {
        ResourceManagerSpi.clearStats();
    }

    /**
     * Clear stats infromation from the engine.
     */
    public void reset()
    {
        ResourceManagerSpi.reset();
    }
}
