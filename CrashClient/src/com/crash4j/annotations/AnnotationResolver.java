/**
 * @copyright
 * 
 */
package com.crash4j.annotations;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.crash4j.EngineAdapter;
import com.crash4j.EngineAdapterFactory;
import com.crash4j.EngineException;
import com.crash4j.adapters.LocalEngineAdapter;

/**
 * Resolves annotations and builds {@link Behaviors}s and {@link Simulation}s 
 * @author crash4j team
 *
 */
public class AnnotationResolver
{
    protected EngineAdapter adapter = null;
    
    protected HashMap<String, com.crash4j.Simulation> simulations = new HashMap<String, com.crash4j.Simulation>();
    protected HashMap<String, com.crash4j.Behavior> behaviors = new HashMap<String, com.crash4j.Behavior>();
    protected String outputDir = ".";
    protected long collectionPeriod = 1000; 
    protected boolean retainErrors = false;
    protected boolean collectStats = true;
    /**
     * @param klass
     * @throws EngineException
     */
    public AnnotationResolver(Class<?> klass) throws EngineException
    {
        try
        {
            adapter =  EngineAdapterFactory.getLocalAdapter();       
            Behaviors cwith = klass.getAnnotation(Behaviors.class);
            //Load and resolve all behaviors
            if (cwith == null)
            {
                return;
            }
            String[] ba = cwith.sources();
            for (String behavior : ba)
            {
                loadBehavior(klass, behavior);
            }
            CrashOutput output = klass.getAnnotation(CrashOutput.class);
            if (output != null)
            {
                this.collectionPeriod = output.every();
                this.outputDir = output.dir();
                this.retainErrors = output.retainErrors();
                this.collectStats = output.collectStats();
            }
            
            String val = System.getProperty("com.crash4j.outptut.dir");
            if (val != null)
            {
                this.outputDir = val;
            }
            val = System.getProperty("com.crash4j.outptut.collection.period");
            if (val != null)
            {
                this.collectionPeriod = Long.parseLong(val);
            }
            val = System.getProperty("com.crash4j.outptut.collect.stats");
            if (val != null)
            {
                this.collectStats = Boolean.parseBoolean(val);
            }
        }
        catch (Exception e)
        {
            throw new EngineException(e);
        }
    }
    
    /**
     * @return the adapter
     */
    public EngineAdapter getAdapter()
    {
        return adapter;
    }

    /**
	 * @return the collectStats
	 */
	public boolean isCollectStats() {
		return collectStats;
	}

	/**
	 * @param collectStats the collectStats to set
	 */
	public void setCollectStats(boolean collectStats) {
		this.collectStats = collectStats;
	}

	/**
     * Get the number of iterations that will be run for this test.
     * @param klass
     * @param method
     * @return
     * @throws EngineException
     */
    public int getNumberOfIterations(Class<?> klass, Method method) throws EngineException
    {
        CrashPlan plan = method.getAnnotation(CrashPlan.class);
        if (plan == null)
        {
            return 0;
        }
        return plan.iterations();
    }

    /**
     * Get the number of iterations that will be run for this test.
     * @param klass
     * @param method
     * @return
     * @throws EngineException
     */
    public String getOutputDir() throws EngineException
    {
        return this.outputDir;
    }
    
    /**
     * @return true if you want to retain errors for this test.
     */
    public boolean retainErrors()
    {
        return this.retainErrors;
    }
    /**
     * Get the number of iterations that will be run for this test.
     * @param klass
     * @param method
     * @return
     * @throws EngineException
     */
    public long getCollectionPeriod() throws EngineException
    {
        return this.collectionPeriod;
    }
    
    
    /**
     * Get the number of iterations that will be run for this test.
     * @param klass
     * @param method
     * @return
     * @throws EngineException
     */
    public long getTimeout(Class<?> klass, Method method) throws EngineException
    {
        CrashPlan plan = method.getAnnotation(CrashPlan.class);
        if (plan == null)
        {
        	return 0;
        }
        return plan.timeout();
    }
    
    /**
     * Get the required concurrency that will be used for this test.
     * @param klass
     * @param method
     * @return
     * @throws EngineException
     */
    public int getConcurrency(Class<?> klass, Method method) throws EngineException
    {
        CrashPlan plan = method.getAnnotation(CrashPlan.class);
        if (plan == null)
        {
            return 1;
        }
        return plan.concurrency();
    }

    /**
     * @returns <code>true</code> if this test has the crash plan and <code>false</code> otherwise
     * @param klass
     * @param method
     * @throws EngineException
     */
    public boolean hasPlan(Class<?> klass, Method method) throws EngineException
    {
        CrashPlan plan = method.getAnnotation(CrashPlan.class);
        return (plan != null);
    }

    /**
     * Get the level of declared fault tolerance.
     * @param klass
     * @param method
     * @return
     * @throws EngineException
     */
    public int getToleranceLevel(Class<?> klass, Method method) throws EngineException
    {
        CrashPlan plan = method.getAnnotation(CrashPlan.class);
        if (plan == null)
        {
            return 0;
        }
        return plan.toleranceLevel();
    }
    
    /**
     * Returns simulation mappings
     * @param klass
     * @param method
     * @param sim
     * @return
     */
    public String[] getSimulationMappings(Class<?> klass, Method method, 
            com.crash4j.Simulation sim) throws EngineException
    {
        CrashPlan plan = method.getAnnotation(CrashPlan.class);
        if (plan == null)
        {
            return null;
        }
        Simulation s[] = plan.simulations();
        for (Simulation sm : s)
        {
            if (sm.id().equals(sim.getId()))
            {
                return sm.mappings();
            }
        }
        return null;
    }
    
    /**
     * 
     * @param klass
     * @param method
     * @return
     * @throws EngineException 
     * @throws IOException 
     * @throws URISyntaxException 
     * @throws MalformedURLException 
     */
    public com.crash4j.Simulation[] getMethodSimulations(Class<?> klass, Method method) 
            throws MalformedURLException, URISyntaxException, IOException, EngineException
    {
        CrashPlan sims = method.getAnnotation(CrashPlan.class);
        if (sims == null)
        {
        	return null;
        }
        Simulation [] sm = sims.simulations();
        ArrayList<com.crash4j.Simulation> ord = new ArrayList<com.crash4j.Simulation>();
        for (Simulation simulation : sm)
        {
            com.crash4j.Simulation cm = this.simulations.get(simulation.id());
            if (cm == null)
            {
                cm = loadSimulation(klass, method, simulation);
            }
            
            if (cm != null)
            {
                ord.add(cm);
            }
        }
        return ord.toArray(new com.crash4j.Simulation[0]);
    }
    
    /**
     * Load {@link Behaviors} annotation.
     * @param klass Class<?> that contains annotated declaration.
     * @param ba
     * @throws IOException 
     * @throws URISyntaxException 
     * @throws MalformedURLException 
     */
    protected com.crash4j.Behavior loadBehavior(Class<?> klass, String source) throws EngineException, MalformedURLException, URISyntaxException, IOException
    {
        com.crash4j.Behavior b = null;
        if (source == null)
        {
            throw new NullPointerException("At least a single behavior confgration needs to be specified.");
        }
        else
        {
            InputStream is = getResourceAsStream(klass, source);
            if (is == null)
            {
                throw new IOException("Unable to discover simulation file");
            }
            b = adapter.createBehavior(new InputStreamReader(is));
            is.close();
        }
        
        //Index behavior
        behaviors.put(b.getId(), b);
        
        return b;
    }
    
    /**
     * Load {@link Simulation} and {@link Behaviors}
     * @throws IOException 
     * @throws URISyntaxException 
     * @throws MalformedURLException 
     * @throws EngineException 
     */
    protected com.crash4j.Simulation loadSimulation(Class<?> klass, Method m, Simulation simulation) 
            throws MalformedURLException, URISyntaxException, IOException, EngineException
    {
        com.crash4j.Simulation sim = null;
        if (!simulation.source().isEmpty())
        {
            //Load from source file
            String src = simulation.source();
            InputStream is = getResourceAsStream(klass, src);
            if (is == null)
            {
                throw new IOException("Unable to discover simulation file");
            }
            sim = adapter.createSimulation(new InputStreamReader(is));
            if (sim != null)
            {
                this.simulations.put(sim.getId(), sim);
            }                
            Set<com.crash4j.Behavior> bs = sim.getBehaviors();
            for (com.crash4j.Behavior behavior : bs)
            {
                this.behaviors.put(behavior.getId(), behavior);
            }
        }
        else
        {
            //Load from annotations.
            sim = new com.crash4j.Simulation();
            sim.setId(simulation.id());
            sim.setName(simulation.name());
            sim.setDescription(simulation.description());
            sim.setFrequency(simulation.frequency());
            
            Behavior[] ba = simulation.behaviors();
            for (Behavior behavior : ba)
            {
                com.crash4j.Behavior b = this.behaviors.get(behavior.id());
                if (b == null)
                {
                    throw new EngineException("No declared behavior with id: "+behavior.id());
                }
                
                String [] actions = behavior.actions();
                if (actions != null && actions.length > 0)
                {
                    b.getActions().clear();
                    for (String a : actions)
                    {
                        b.getActions().add(a);
                    }
                }
                if (behavior.mode() != null)
                {
                    if (behavior.mode().equalsIgnoreCase("stop"))
                    {
                        b.setStop(true);
                    }
                    else if (behavior.mode().equalsIgnoreCase("retain"))
                    {
                        b.setRetain(true);
                    }
                    else
                    {
                       b.setRotate(true); 
                    }
                }
                sim.getBehaviors().add(b);
            }
        }
        return sim;
    }
    
    /**
     * Find resource....
     * 
     * @param base
     * @param resource
     * @return
     * @throws URISyntaxException
     * @throws MalformedURLException
     * @throws IOException
     */
    private static InputStream getResourceAsStream(Class base, String resource) throws URISyntaxException, 
        MalformedURLException, IOException
    {
        InputStream iis = null;
        URI u = new URI(resource);
        if (u.getScheme().startsWith("http"))
        {
            URLConnection c = u.toURL().openConnection();
            iis = c.getInputStream();
        } else if (u.getScheme().startsWith("file"))
        {
            URLConnection c = u.toURL().openConnection();
            iis = c.getInputStream();
        } else if (u.getScheme().startsWith("classpath"))
        {
            String path = u.getPath();
            iis = base.getResourceAsStream(path);
            if (iis == null)
            {
                iis = ClassLoader.getSystemResourceAsStream(path);
                if (iis == null)
                {
                    iis = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
                }
            }
        }
        return iis;
    }
    
}

