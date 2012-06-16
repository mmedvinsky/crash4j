/**
 * @copyright
 */
package com.crash4j.engine.spi.sim.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import com.crash4j.engine.sim.Command;
import com.crash4j.engine.sim.Simulation;
import com.crash4j.engine.spi.log.Log;
import com.crash4j.engine.spi.log.LogFactory;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.resources.impl.NetworkResourceSpiImpl;
import com.crash4j.engine.spi.util.PropertyOwner;
import com.crash4j.engine.spi.util.PropertyTree;

/**
 * {@link SimulationManagerSpi} loads, unloads and generally manages 
 * simulations that are running within the JVM.
 * <p/>
 * Simulations are loaded from JSON configuration and bound through this object to a set of resources.
 * The simulation machine will bind {@link Command}s to the resource call-backs that are registered with specific running simulations.
 * @author team
 *
 */
public class SimulationManagerSpi
{
    /** Collection, containing all simulations within this runtime, indexed by ID of the simulation. */
    protected HashMap<String, Simulation> sims = new HashMap<String, Simulation>();
    /** {@link PropertyTree} used to index simulations for efficient lookup.*/
    protected PropertyTree<Simulation> simIndex = new PropertyTree<Simulation>();
    protected ConcurrentHashMap<String, Set<String>> mappings = new ConcurrentHashMap<String, Set<String>>();
    
    protected static final Log log = LogFactory.getLog(SimulationManagerSpi.class);
    protected ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    
    /**
     * Select simulation....
     * @param res
     * @return
     */
    public Simulation selectSimulation(PropertyOwner po)
    {
        this.lock.readLock().lock();
        try
        {
            return simIndex.match(po);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
        
    }
    
    /**
     * 
     * @param Simulation id
     * @return {@link Set} of simulation patterns
     */
    public Set<String> getSimulationPatterns(String id)
    {
        return mappings.get(id);
    }
    
    /**
     * The format of the {@link ResourceSpi} objects is similar to the format of simulations.  
     * [/](net|fsys|...)[/]a=b,c=d,...
     * @param pattern
     * @param id
     * @throws  
     * @throws MalformedObjectNameException 
     */
    public void addSimulationMapping(String pattern, String id) throws MalformedObjectNameException
    {
        Set<String> npatterns = new HashSet<String>();
        Set<String> patterns = mappings.putIfAbsent(id, npatterns);
        if (patterns == null)
        {
            patterns = npatterns;
        }
        patterns.add(pattern);
        
        ObjectName name = ObjectName.getInstance(pattern);
        
        ArrayList<String[]> list = new ArrayList<String[]>();
        String kvp[] = new String[2];
        kvp[0] = "type";
        kvp[1] = name.getDomain();
        list.add(kvp);
        
        Set<Map.Entry<String, String>> set = name.getKeyPropertyList().entrySet();
        for (Map.Entry<String, String> entry : set)
        {
            String[] kv = new String[2];
            kv[0] = entry.getKey().toString();
            kv[1] = entry.getValue().toString();
            list.add(kv);
        }
        
        this.lock.writeLock().lock();
        try
        {
            Simulation sim = sims.get(id);
            simIndex.addProperties(list, sim);
        } 
        finally
        {
            this.lock.writeLock().unlock();
        }
    }
    
    /**
     * Add the {@link Simulation} 
     * @param sim
     */
    public void addSimulation(Simulation sim)
    {
        try
        {
            this.lock.writeLock().lock();
            sims.put(sim.getId(), sim);
        }
        finally
        {
            this.lock.writeLock().unlock();
        }
    }
    
    /**
     * Get the {@link Simulation} 
     * @param sim
     */
    public Simulation getSimulation(String id)
    {
        try
        {
            this.lock.readLock().lock();
            return sims.get(id);
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }
    
    
    public void startAll()
    {
        try
        {
            this.lock.readLock().lock();
            for (Simulation sim : sims.values())
            {
                sim.start();
            }
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }
    
    public void stopAll()
    {
        try
        {
            this.lock.readLock().lock();
            for (Simulation sim : sims.values())
            {
                sim.stop();
            }
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }
    
    /**
     * startSimulation with a specific id
     * @param id
     */
    public void startSimulation(String id)
    {
        Simulation sim = getSimulation(id);
        if (sim != null)
        {
            sim.start();
        }
    }
    
    /**
     * Removes all simulations
     */
    public void clearAll()
    {
        try
        {
            this.lock.writeLock().lock();
            sims.clear();
        }
        finally
        {
            this.lock.writeLock().unlock();
        }
    }
    /**
     * stopSimulation with a specific id
     * @param id
     */
    public void stopSimulation(String id)
    {
        Simulation sim = getSimulation(id);
        if (sim != null)
        {
            sim.stop();
        }
    }  
    
    
}
