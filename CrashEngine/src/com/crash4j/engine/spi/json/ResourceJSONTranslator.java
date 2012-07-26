/**
 * 
 */
package com.crash4j.engine.spi.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.management.ObjectName;

import com.crash4j.engine.Resource;
import com.crash4j.engine.sim.Behavior;
import com.crash4j.engine.sim.Instruction;
import com.crash4j.engine.sim.Simulation;
import com.crash4j.engine.spi.ResourceManagerSpi;
import com.crash4j.engine.spi.log.Log;
import com.crash4j.engine.spi.log.LogFactory;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.sim.impl.BehaviorImpl;
import com.crash4j.engine.spi.sim.impl.SimulationImpl;
import com.crash4j.engine.types.BehaviorTypes;
import com.crash4j.engine.types.InstructionTypes;

/**
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
public class ResourceJSONTranslator
{
	protected static Log log = LogFactory.getLog(ResourceJSONTranslator.class);
    /**
     * @return resources that are currently managed by the runtime as JSON
     *         String, for exanple [ {"resource" :
     *         "net:protocol=tcp, la=localhost, lp=8080, rp=localhost, rp=6567"
     *         }, ...]
     */
    public JSONArray resourcesAsJSON(Collection<ResourceSpi> spis) throws Exception
    {
        JSONArray jRs = new JSONArray();
        for (ResourceSpi res : spis)
        {
            JSONObject jr = new JSONObject();
            jr.put("name", res.asVector().toString());
            jr.put("id", res.getETag());
            jRs.put(jr);
        }
        return jRs;
    }

    
    /**
     * @return information about the resource, specified by the name [
     *         {"resource" :
     *         "net:protocol=tcp, la=localhost, lp=8080, rp=localhost, rp=6567"
     *         }, ...]
     */
    public JSONObject resourceAsJSON(ResourceSpi spi, Object stat) throws Exception
    {
        if (spi != null)
        {
            JSONObject o = _getResourceDetails(spi, stat);
            return o;
        }
        return null;
    }

    /**
     * @return information about the resource, specified by the name [
     *         {"resource" :
     *         "net:protocol=tcp, la=localhost, lp=8080, rp=localhost, rp=6567"
     *         }, ...]
     */
    protected JSONObject _getResourceDetails(ResourceSpi spi, Object stat) throws Exception
    {
        Simulation sim = spi.getSimulation();
        JSONObject jr = new JSONObject();
        ObjectName o = spi.asVector();
        jr.put("name", o.toString());
        jr.put("id", spi.getETag());
        jr.put("stat", stat);
        JSONObject siminfo = new JSONObject();
        if (sim != null)
        {
            siminfo.put("simid", sim.getId());
            siminfo.put("ctick", sim.getTick());
            jr.put("sim", siminfo);
        }
        return jr;
    }

    /**
     * Add a simulation
     * 
     * @param sim
     */
    public Simulation simulationFromJSON(JSONObject jSim, ArrayList<String> maps) throws Exception
    {
        String name = jSim.getString("name");
        if (name == null)
        {
            throw new NullPointerException("Missing simulation name.");
        }
        SimulationImpl s = new SimulationImpl(name);
        String id = jSim.getString("id");
        if (id == null)
        {
        	log.logError("Null simulation id");
            throw new NullPointerException("Missing simulation id.");
        }
        s.setId(id);

        int frequency = -1;
        try
        {
            frequency = jSim.getInt("frequency");
        } catch (Exception e)
        {
        }
        s.setTickFrequency(frequency == -1 ? 1 : frequency);
        try
        {
            JSONArray jbs = jSim.getJSONArray("behaviors");
            if (jbs != null)
            {
                for (int j = 0; j < jbs.length(); j++)
                {
                    Behavior b = _readBehavior(jbs.getJSONObject(j));
                    s.addBehavior(b);
                }
            }
            JSONArray mappings = jSim.getJSONArray("mappings");
            if (mappings != null)
            {
                for (int k = 0; k < mappings.length(); k++)
                {
                    maps.add(mappings.getString(k));
                }
            }
        } catch (Exception e)
        {
            // no behaviors are included in original submission
        	log.logError("No behaviors added", e);
        }
        return s;
    }

    public Behavior behaviorFromJSON(String id, JSONObject bh) throws Exception
    {
        return _readBehavior(bh);
    }

    /**
     * Adds a simulation
     * 
     * @param s
     * @param behavior
     */
    protected Behavior _readBehavior(JSONObject jb) throws Exception
    {
        String name = jb.getString("name");
        if (name == null)
        {
            throw new NullPointerException("Missing behavior name.");
        }
        String id = jb.getString("id");
        if (id == null)
        {
            throw new NullPointerException("Missing behavior id.");
        }

        String type = jb.getString("itype");
        if (type == null)
        {
            throw new NullPointerException("Missing instruction type.");
        }
        String btype = jb.getString("btype");
        if (btype == null)
        {
            throw new NullPointerException("Missing behavior type.");
        }

        BehaviorImpl b = new BehaviorImpl(name, id, InstructionTypes.fromString(type), BehaviorTypes.fromString(btype));

        JSONArray actions = jb.getJSONArray("actions");
        for (int i = 0; i < actions.length(); i++)
        {
            String action = (String) actions.get(i);
            b.addAction(action);
        }

        String mode = jb.optString("mode");
        if (mode == null)
        {
            b.setRotate(true);
        } else
        {
            if (mode.equalsIgnoreCase("stop"))
            {
                b.setStop(true);
            } else if (mode.equalsIgnoreCase("retain"))
            {
                b.setRetain(true);
            } else
            {
                b.setRotate(true);
            }
        }
        JSONArray jInsts = jb.getJSONArray("instructions");
        for (int k = 0; k < jInsts.length(); k++)
        {
            JSONArray inst1 = jInsts.getJSONArray(k);
            int tn = inst1.getInt(0);
            double prob = inst1.getDouble(1);
            double weight = 0;
            if (inst1.length() > 2)
            {
                weight = inst1.getDouble(2);
            }
            b.addInstruction(tn, prob, weight);
        }
        return b;
    }

    /**
     * get simulation y id
     * 
     * @param id
     */
    public JSONObject simulationAsJSON(Simulation sim) throws Exception
    {
        if (sim == null)
        {
            return null;
        }

        JSONObject jSim = new JSONObject();
        jSim.put("id", sim.getId());
        jSim.put("name", sim.getName());
        jSim.put("tick", sim.getTick());
        jSim.put("frequency", sim.getTickFrequency());
        JSONArray behaviors = new JSONArray();
        Behavior[] b = sim.getBehaviors();
        for (Behavior behavior : b)
        {
            JSONObject bh = new JSONObject();
            bh.put("id", behavior.getId());
            bh.put("name", behavior.getName());
            bh.put("itype", behavior.getType());
            bh.put("btype", behavior.getType());
            if (behavior.isRetain())
            {
                bh.put("mode", "retain");
            } else if (behavior.isStop())
            {
                bh.put("mode", "stop");
            } else
            {
                bh.put("mode", "replay");
            }
            JSONArray ji = new JSONArray();
            Instruction[] insts = behavior.getInstructions();
            for (Instruction instruction : insts)
            {
                JSONArray singleI = new JSONArray();
                singleI.put(instruction.getTick());
                singleI.put(instruction.getP());
                singleI.put(instruction.getWeight());
                ji.put(singleI);
            }
            bh.put("instructions", ji);
            behaviors.put(bh);
        }

        jSim.put("behaviors", behaviors);

        JSONArray mappings = new JSONArray();
        Set<String> pats = ResourceManagerSpi.getSimulationManager().getSimulationPatterns(sim.getId());
        for (String p : pats)
        {
            mappings.put(p);
        }
        jSim.put("mappings", mappings);
        return jSim;
    }
}
