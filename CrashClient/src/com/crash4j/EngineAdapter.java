/**
 * @copyright
 */
package com.crash4j;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.management.ObjectName;

import com.crash4j.annotations.Behaviors;
import com.crash4j.engine.client.json.JSONArray;
import com.crash4j.engine.client.json.JSONException;
import com.crash4j.engine.client.json.JSONObject;
import com.crash4j.engine.client.json.JSONTokener;

/**
 * TODO Need to write an extensive javadoc here with usage examples.
 */
public class EngineAdapter
{
    protected Class<?> fe;
    protected Method getResources;
    protected Method getResource;
    protected Method addSimulation;
    protected Method addBehavior;
    protected Method getSimulation;
    protected Method startSimulation;
    protected Method stopSimulation;
    protected Method clearStats;
    protected Method reset;
    protected Method registerCollector;
    protected Method unRegisterCollector;
    protected Object instance = null;
    
    /**
     * {@link EngineAdapter} is a proxy class that is able to connect and communicate to the 
     * underlying engine.
     * @throws ClassNotFoundException 
     * @throws EngineNotFoundException 
     */
    public EngineAdapter() 
    {
    }
    
    /**
     * Bind to the running engine.
     */
    public void bind() throws EngineNotFoundException
    {
        try
        {
            this.fe = Class.forName("com.crash4j.engine.ResourceService");
            if (this.fe == null)
            {
                throw new EngineNotFoundException();
            }
            this.instance = this.fe.newInstance();
            this.getResources = this.fe.getDeclaredMethod("getResources");
            this.getResource = this.fe.getDeclaredMethod("getResource", String.class);
            this.addSimulation = this.fe.getDeclaredMethod("addSimulation", String.class);
            this.addBehavior = this.fe.getDeclaredMethod("addBehavior", String.class, String.class);
            this.getSimulation = this.fe.getDeclaredMethod("getSimulation", String.class);
            this.startSimulation = this.fe.getDeclaredMethod("startSimulation", String.class);
            this.stopSimulation = this.fe.getDeclaredMethod("stopSimulation", String.class);
            this.clearStats = this.fe.getDeclaredMethod("clearStats");
            this.registerCollector = this.fe.getDeclaredMethod("registerCollector", String.class, Map.class, long.class, TimeUnit.class);
            this.unRegisterCollector = this.fe.getDeclaredMethod("unRegisterCollector", String.class);
            this.reset = this.fe.getDeclaredMethod("reset");
        }
        catch (Exception e)
        {
            EngineNotFoundException ex = new EngineNotFoundException();
            ex.initCause(e);
            throw ex;
        }
        
    }

    /**
     * Get the simulation object from the engine
     * @param id
     * @return
     */
    public Simulation getSimulation(String id) throws EngineMethodException
    {
        try
        {
            String sim = (String)this.getSimulation.invoke(this.instance, id);
            return createSimulation(new StringReader(sim));
        }
        catch (Exception e)
        {
            EngineMethodException ee = new EngineMethodException();
            ee.initCause(e);
            throw ee;
        }
    }
    
    
    /**
     * Add new simulation to the engine
     * @param sim
     */
    public void addSimulationAsJSON(String sim) throws EngineMethodException
    {
        try
        {
            this.addSimulation.invoke(this.instance, sim);
        }
        catch (Exception e)
        {
            EngineMethodException ee = new EngineMethodException();
            ee.initCause(e);
            throw ee;
        }
    }

    
    /**
     * Add new simulation to the engine
     * @param sim
     */
    public void addBehavior(String simulationId, Behavior behavior) throws EngineMethodException
    {
        try
        {
            JSONObject bh = this._generateBehaviorJSON(behavior);
            this.addBehavior.invoke(this.instance, simulationId, bh.toString());
        }
        catch (Exception e)
        {
            EngineMethodException ee = new EngineMethodException();
            ee.initCause(e);
            throw ee;
        }
    }
    
    /**
     * Add new simulation to the engine
     * @param sim
     */
    public void addBehaviorAsJSON(String simulationId, String behavior) throws EngineMethodException
    {
        try
        {
            this.addBehavior.invoke(this.instance, simulationId, behavior);
        }
        catch (Exception e)
        {
            EngineMethodException ee = new EngineMethodException();
            ee.initCause(e);
            throw ee;
        }
    }
    
    
    /**
     * Add new simulation to the engine
     * @param sim
     */
    public void addSimulation(Simulation sim, Set<String> map) throws EngineMethodException
    {
        try
        {
            JSONObject jSim = new JSONObject();
            jSim.put("id", sim.getId());
            jSim.put("name", sim.getName());
            jSim.put("tick", sim.getTick());
            jSim.put("frequency", sim.getFrequency());
            JSONArray behaviors = new JSONArray();
            Set<Behavior> b = sim.getBehaviors();
            for (Behavior behavior : b)
            {
                JSONObject bh = this._generateBehaviorJSON(behavior);
                behaviors.put(bh);
            }
            jSim.put("behaviors", behaviors);
            JSONArray mappings = new JSONArray();
            for (String p : map)
            {
                mappings.put(p);
            }
            jSim.put("mapping", mappings);
            this.addSimulation.invoke(this.instance, jSim.toString());
        }
        catch (Exception e)
        {
            EngineMethodException ee = new EngineMethodException();
            ee.initCause(e);
            throw ee;
        }
    }
    
    /**
     * @return {@link Simulation} object, loaded from {@link Reader}
     */
    public Simulation createSimulation(Reader reader) throws EngineException
    {
        try
        {
            JSONObject jSim = new JSONObject(new JSONTokener(reader));
            String name = jSim.getString("name");
            if (name == null)
            {
                throw new NullPointerException("Missing simulation name.");
            }
            Simulation s = new Simulation();
            s.setName(name);
            s.setId(jSim.getString("id"));
            int frequency = -1;
            try {frequency = jSim.getInt("frequency");} catch (Exception e) {}
            s.setFrequency(frequency == -1 ? 1 : frequency);
            try
            {
                JSONArray jbs = jSim.getJSONArray("behaviors");
                if (jbs != null)
                {
                    for (int j = 0; j < jbs.length(); j++)
                    {
                        Behavior b = _readBehavior(jbs.getJSONObject(j));
                        s.getBehaviors().add(b);
                    }
                }
            }
            catch (Exception e)
            {
            }
            return s;
        } 
        catch (JSONException e)
        {
            throw new EngineException(e);
        }
    }

    /**
     * Create {@link Behaviors} object
     * @param r
     * @return
     */
    public Behavior createBehavior(Reader r) throws EngineException
    {
        try
        {
            return _readBehavior(new JSONObject(new JSONTokener(r)));
        } 
        catch (JSONException e)
        {
            throw new EngineException(e);
        } 
        catch (Exception e)
        {
            throw new EngineException(e);
        }
    }
    
    /**
     * Adds a simulation
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
            throw new NullPointerException("Missing behavior id.");
        }
        String btype = jb.getString("btype");
        if (btype == null)
        {
            throw new NullPointerException("Missing behavior id.");
        }
        
        Behavior b = new Behavior();
        b.setId(id);
        b.setName(name);
        b.setType(type);
        b.setBehaviorType(btype);
        JSONArray actions = jb.getJSONArray("actions");
        for (int i = 0; i < actions.length(); i++)
        {
            String action = (String)actions.get(i);
            b.getActions().add(action);
        }
        
        String mode = jb.getString("mode");
        if (mode.equalsIgnoreCase("stop"))
        {
            b.setStop(true);
        }
        else if (mode.equalsIgnoreCase("retain"))
        {
            b.setRetain(true);
        }
        else
        {
            b.setRotate(true);
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
            b.getInstructions().add(new Instruction(tn, prob, weight));
        }
        return b;
    }
    
    /**
     * Generate JSON behavior
     * @param b
     * @return
     */
    protected JSONObject _generateBehaviorJSON(Behavior behavior) throws EngineMethodException
    {
        try
        {
            JSONObject bh = new JSONObject();
            bh.put("id", behavior.getId());
            bh.put("name", behavior.getName());
            bh.put("itype", behavior.getType());
            bh.put("btype", behavior.getBehaviorType());
            if (behavior.isRetain())
            {
                bh.put("mode", "retain");
            }
            else if (behavior.isStop())
            {
                bh.put("mode", "stop");
            }
            else
            {
                bh.put("mode", "rotate");
            }
            JSONArray acts = new JSONArray();
            Set<String> actions = behavior.getActions();
            for (String act : actions)
            {
                acts.put(act);
            }
            bh.put("actions", acts);
            
            JSONArray ji =new JSONArray();
            List<Instruction> insts = behavior.getInstructions();
            for (Instruction instruction : insts)
            {
                JSONArray singleI = new JSONArray();
                singleI.put(instruction.getTick());
                singleI.put(instruction.getP());
                singleI.put(instruction.getWeight());
                ji.put(singleI);
            }
            bh.put("instructions", ji);
            return bh;
        }
        catch (Exception e)
        {
            EngineMethodException ee = new EngineMethodException();
            ee.initCause(e);
            throw ee;
        }
            
    }
    
    /**
     * Start simulation.
     * @throws EngineMethodException
     */
    public void startSimulation(String id) throws EngineMethodException
    {
        try
        {
            this.startSimulation.invoke(this.instance, id);
        } 
        catch (Exception e)
        {
            EngineMethodException ee = new EngineMethodException();
            ee.initCause(e);
            throw ee;
        } 
    }
    
    /**
     * Stop simulation.
     * @throws EngineMethodException
     */
    public void stopSimulation(String id) throws EngineMethodException
    {
        try
        {
            this.stopSimulation.invoke(this.instance, id);
        } 
        catch (Exception e)
        {
            EngineMethodException ee = new EngineMethodException();
            ee.initCause(e);
            throw ee;
        } 
    }
    
    /**
     * Clear engine statistics
     * @throws EngineMethodException
     */
    public void clearStats() throws EngineMethodException
    {
        try
        {
            this.clearStats.invoke(this.instance);
        } 
        catch (Exception e)
        {
            EngineMethodException ee = new EngineMethodException();
            ee.initCause(e);
            throw ee;
        } 
    }
    
    
    /**
     * Engine method reset
     * @throws EngineMethodException
     */
    public void resetEngine() throws EngineMethodException
    {
        try
        {
            this.reset.invoke(this.instance);
        } 
        catch (Exception e)
        {
            EngineMethodException ee = new EngineMethodException();
            ee.initCause(e);
            throw ee;
        } 
    }
    
    /**
     * Engine method reset
     * @throws EngineMethodException
     */
    public String registerCollector(String klass, Map<String, Object> props, 
            long period, TimeUnit unit) throws EngineMethodException
    {
        try
        {
            return (String)this.registerCollector.invoke(this.instance, klass, props, period, unit);
        } 
        catch (Exception e)
        {
            EngineMethodException ee = new EngineMethodException();
            ee.initCause(e);
            throw ee;
        } 
    }

    /**
     * 
     * @param moniker
     */
    public void unRegisterCollector(String moniker) throws EngineMethodException
    {
        try
        {
            this.unRegisterCollector.invoke(this.instance, moniker);
        } 
        catch (Exception e)
        {
            EngineMethodException ee = new EngineMethodException();
            ee.initCause(e);
            throw ee;
        } 
    }
    
    
    
    /**
     * @return {@link Resource} that are currently managed by the runtime
     * @throws  
     * @throws  
     */
    public  Set<Resource> getResources() throws EngineDataException
    {
        try
        {
            HashSet<Resource> set = new HashSet<Resource>();
            String resources = (String)this.getResources.invoke(this.instance);
            if (resources == null)
            {
                throw new EngineDataException();
            }
            JSONArray jr = new JSONArray(resources);
            for (int i = 0; i < jr.length(); i++)
            {
                JSONObject res = jr.getJSONObject(i);
                ObjectName nm = new ObjectName(res.getString("name"));
                Resource r = new Resource(nm, res.getString("id"));
                set.add(r);
            }
            return set;
        } 
        catch (Exception e)
        {
            EngineDataException ee = new EngineDataException();
            ee.initCause(e);
            throw ee;
        } 
    }

    /**
     * @return {@link Resource} that are currently managed by the runtime
     * @throws  
     * @throws  
     */
    public  String getResourcesAsJSON() throws EngineDataException
    {
        try
        {
            return (String)this.getResources.invoke(this.instance);
        } 
        catch (Exception e)
        {
            EngineDataException ee = new EngineDataException();
            ee.initCause(e);
            throw ee;
        } 
    }
    
    
    /**
     * @return {@link Resource} that are currently managed by the runtime
     */
    public  Resource getResourceDetails(String id) throws EngineDataException
    {
        Resource res = new Resource(null, id);
        return this.getResourceDetails(res);
    }
    

    /**
     * @return {@link Resource} that are currently managed by the runtime
     */
    public  String getResourceDetailsAsJSON(String id) throws EngineDataException
    {
        try
        {
            return (String)this.getResource.invoke(this.instance, id);
        } 
        catch (Exception e)
        {
            EngineDataException ee = new EngineDataException();
            ee.initCause(e);
            throw ee;
        } 
    }
    
    
    /**
     * <pre>
     * (i) initialization action. i.e. java.io.File constructor.
     * (o) open connection to the resource, i.e. Socket.connect, FileInputStream() constructor, ...
     * (c) close connection to the resource  
     * (s) shutdown the resource operation if it is running
     * (d) destroy the resource, i.e. file delete.
     * (n) create new resource, i.e. File.createTempFile 
     * (m) move/rename/modify resource
     * (r) read from the resource
     * (w) write to the resource
     * (k) get resource information
     * (t) end-point is performing a test 
     * (u) user-defined action, to be specified with attributes or other means
     * </pre>
     * @return {@link Resource} that are currently managed by the runtime
     */
    public  Resource getResourceDetails(Resource res) throws EngineDataException
    {
        try
        {
            String resourceDetails = (String)this.getResource.invoke(this.instance, res.getId());
            if (resourceDetails == null)
            {
                throw new EngineDataException();
            }
            JSONObject obj = new JSONObject(resourceDetails);
            res.setName(new ObjectName(obj.getString("name")));
            JSONObject siminfo = obj.optJSONObject("sim");
            if (siminfo != null)
            {
                res.setSimulationId(siminfo.getString("simid"));
                res.setCurrentTick(siminfo.getInt("ctick"));
            }

            JSONArray js = obj.getJSONArray("stat");
            
            for (int i = 0; i < js.length(); i++)
            {
                JSONObject details = js.getJSONObject(i);
                Stat s = new Stat();
                s.setAction(details.getString("action"));
                s.setType(details.getString("type"));
                s.setTimeUnit(TimeUnit.valueOf(details.getString("units")));
                s.setCount(details.getLong("count"));
                s.setAverage(details.getDouble("avg"));
                s.setMax(details.getDouble("max"));
                s.setMin(details.getDouble("min"));
                s.setTotalTime(details.getLong("totaltime"));
                s.setLastUpdateTime(details.getLong("timestamp"));
                s.setLastUpdateValue(details.getDouble("last"));
                res.getStat().add(s);
            }
            return res;
        } 
        catch (Exception e)
        {
            EngineDataException ee = new EngineDataException();
            ee.initCause(e);
            throw ee;
        } 
    }
    
}
