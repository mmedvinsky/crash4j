/**
 * @copyright
 */
package com.crash4j.junit;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import com.crash4j.Behavior;
import com.crash4j.EngineException;
import com.crash4j.Simulation;
import com.crash4j.annotations.AnnotationResolver;
import com.crash4j.engine.client.json.JSONArray;
import com.crash4j.engine.client.json.JSONObject;
import com.crash4j.junit.report.CrashReport;


/**
 * {@link CrashRunner} is used to execute crash4j analysis and simulations from within a junit framework
 * In order to be able to properly configure the test runtime this package also adds additional annotations.
 * </p>
 * 
 */
public class CrashRunner extends BlockJUnit4ClassRunner
{
    protected AnnotationResolver resolver = null;
    protected List<Future<Long>> waiters = null;
    protected File reportLoc = null;
    protected Class<?> jklass = null;
    
    
    /**
     * Initialize the runner.....
     * @param klass
     * @throws InitializationError
     */
    public CrashRunner(Class<?> klass) throws InitializationError
    {
        super(klass);
        this.jklass = klass;
        try
        {
            resolver = new AnnotationResolver(klass);
            
            String outdir = resolver.getOutputDir();
            System.out.println(outdir);
            if (resolver.isCollectStats())
            {            
	            File reportloc = new File(outdir);
	            if (!reportloc.canWrite())
	            {
	            	throw new InitializationError("Unable to write to output directory "+outdir);
	            }
	            reportLoc = new File(reportloc, jklass.getName());
	            if (reportLoc.exists())
	            {
	            	reportLoc.delete();
	            }
	            reportLoc.mkdir();
            }
        } 
        catch (EngineException e)
        {
            throw new InitializationError(e);
        }
    }
    
    /**
     * @see org.junit.runners.BlockJUnit4ClassRunner#methodBlock(org.junit.runners.model.FrameworkMethod)
     */
    @Override
    protected Statement methodBlock(final FrameworkMethod method)
    {
        return new Statement() 
        {  
            @Override  
            public void evaluate() throws Throwable 
            {  
                Long beforeTime = 0L;
                Long afterTime = 0L;
                               
                //0. Prepare the engine for the run
                com.crash4j.Simulation []sims = resolver.getMethodSimulations(jklass, method.getMethod());
                int iterations = resolver.getNumberOfIterations(jklass, method.getMethod());
                int concrrency = resolver.getConcurrency(jklass, method.getMethod());
                long tm = resolver.getTimeout(jklass, method.getMethod());
                final long tLevel = resolver.getToleranceLevel(jklass, method.getMethod());

                                
                // setup baseline stat collector
                try
                {
                    //1. Run once to establish success baseline 
                    //   If exception is thrown it will be thrown by this statement up.
                    long bt = System.nanoTime();
                    try
                    {
                        CrashRunner.super.methodBlock(method).evaluate();  
                    }
                    catch (Throwable e)
                    {
                        throw new CrashUnitException("Crash test failed. Unable to get a baseline.", e);
                    }
                    beforeTime = new Long(System.nanoTime() - bt);
                }
                finally
                {
                }
                
                System.out.println("Before Plan");
                //If there is no plan then just exit.
                if (!resolver.hasPlan(jklass, method.getMethod()))
                {
                	return;
                }
                
                //2. Prepare concurrency runtime.
                String reportName = method.getName();
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("outputDir", CrashRunner.this.reportLoc.getAbsolutePath());
                map.put("output", reportName);
                
                String moniker = null;
                if (resolver.isCollectStats())
                {
                	moniker = resolver.getAdapter().registerCollector("com.crash4j.engine.spi.json.StatsJSONFileCollector", 
                			map, resolver.getCollectionPeriod(), TimeUnit.MILLISECONDS);
                }
                
                System.out.println("After Plan");
                
                ExecutorService executor = Executors.newFixedThreadPool(concrrency);

                
                JSONObject jsims = new JSONObject();
                for (Simulation simulation : sims)
                {
                    HashSet<String> set = new HashSet<String>();
                    String [] maps = resolver.getSimulationMappings(jklass, method.getMethod(), simulation);
                    if (maps == null)
                    {
                        throw new EngineException("Simulation is not mapped to resources.");
                    }
                    for (String m : maps)
                    {
                        set.add(m);
                        simulation.getMappings().add(m);
                    }
                    
                    //Record JSON details of the test....
                    JSONObject sim = new JSONObject();
                    sim.put("name", simulation.getName());
                    sim.put("description", simulation.getDescription());
                    sim.put("frequency", simulation.getFrequency());
                    JSONArray jmaps = new JSONArray();
                    for (String m : maps)
                    {
                        jmaps.put(m);
                    }               
                    
                    jsims.put(simulation.getId(), sim);
                    JSONObject jbhs = new JSONObject();
                    Set<Behavior> behs = simulation.getBehaviors();
                    for (Behavior bs : behs)
                    {
                        JSONObject jbh = new JSONObject();
                        jbh.put("name", bs.getName());
                        jbh.put("itype", bs.getType());
                        jbh.put("btype", bs.getBehaviorType());                        
                        jbhs.put(bs.getId(), jbh);
                    }
                    sim.put("behaviors", jbhs);
                    
                    resolver.getAdapter().addSimulation(simulation, set);
                }

                try 
                {  
                    HashSet<Callable<Long>> workers = new HashSet<Callable<Long>>();
                    for (int j = 0; j < iterations; j++)
                    {
                        Callable<Long> call = new Callable<Long>()
                        {
                            @Override
                            public Long call() throws Exception
                            {
                                long start = System.nanoTime();
                                try
                                {
                                    CrashRunner.super.methodBlock(method).evaluate();
                                } 
                                catch (Throwable e)
                                {
                                	//e.printStackTrace();
                                	
                                	if (tLevel >= 2)
                                	{
                                		throw (Exception)e;
                                	}
                                	
                                }  
                                
                                long t = System.nanoTime() - start;
                                return new Long(t);
                            }
                        };
                        workers.add(call);
                    }
                    
                    for (Simulation simulation : sims)
                    {
                        resolver.getAdapter().startSimulation(simulation.getId());
                    }
                    
                    //Invoke all tests     
                    
                    waiters = executor.invokeAll(workers);                    
                    
                    if (tm == -1)
                    {
                        for (Future<Long> future : waiters)
                        {
                            future.get();
                        }
                    }
                    else
                    {
                        for (Future<Long> future : waiters)
                        {
                            future.get(tm, TimeUnit.MILLISECONDS);
                        }
                    }
                    
                    //Terminate all simulation activities.
                    for (Simulation simulation : sims)
                    {
                        resolver.getAdapter().stopSimulation(simulation.getId());
                    }
                    
                    if (resolver.isCollectStats())
                    {
                    	resolver.getAdapter().unRegisterCollector(moniker);
                    }

                    //3. Run test again to make sure that everything is ok.
                    long at = System.nanoTime();
                    try
                    {
                        CrashRunner.super.methodBlock(method).evaluate();  
                    }
                    catch (Throwable e)
                    {
                        throw new CrashUnitException("Crash test failed. System instability detected.", e);
                    }
                    afterTime = new Long(System.nanoTime() - at);
                    
                } 
                finally 
                {  
                	CrashReport rep = new CrashReport(sims, iterations, concrrency, tm, CrashRunner.this.reportLoc.getAbsolutePath(), reportName);
                	rep.prepare();
                }  
            } 
        };    
    }
}
