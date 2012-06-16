/**
 * 
 */
package com.crash4j.engine.spi;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.instrument.Instrumentation;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import com.crash4j.engine.Action;
import com.crash4j.engine.StatsCollector;
import com.crash4j.engine.UnknownResourceException;
import com.crash4j.engine.sim.Behavior;
import com.crash4j.engine.sim.Command;
import com.crash4j.engine.sim.Simulation;
import com.crash4j.engine.spi.ResourceSpecLoader._spec;
import com.crash4j.engine.spi.context.ThreadContext;
import com.crash4j.engine.spi.inf.Infrastructure;
import com.crash4j.engine.spi.instrument.EventData;
import com.crash4j.engine.spi.instrument.EventHandler;
import com.crash4j.engine.spi.instrument.delegates.StdStreamInputStream;
import com.crash4j.engine.spi.instrument.delegates.StdStreamOutputStream;
import com.crash4j.engine.spi.log.Log;
import com.crash4j.engine.spi.log.LogFactory;
import com.crash4j.engine.spi.protocol.ProtocolEvent;
import com.crash4j.engine.spi.protocol.ProtocolRecognizer;
import com.crash4j.engine.spi.protocol.http.HttpProtocolRecognizer;
import com.crash4j.engine.spi.protocol.impl.DisabledProtocolRecognizer;
import com.crash4j.engine.spi.protocol.impl.GenericProtocolRecognizer;
import com.crash4j.engine.spi.remote.CrashServiceAdapter;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.resources.impl.ResourceClosureImpl;
import com.crash4j.engine.spi.sim.impl.SimulationManagerSpi;
import com.crash4j.engine.spi.stats.StatsManager;
import com.crash4j.engine.spi.traits.LifecycleHandler;
import com.crash4j.engine.spi.traits.ResourceAware;
import com.crash4j.engine.spi.traits.ResourceBuilder;
import com.crash4j.engine.spi.traits.ResourceClosure;
import com.crash4j.engine.spi.traits.SimulationConsumer;
import com.crash4j.engine.spi.util.BCELUtils;
import com.crash4j.engine.spi.util.ObjectWrapperCreator;
import com.crash4j.engine.spi.util.PropertyOwner;
import com.crash4j.engine.spi.util.Utils;
import com.crash4j.engine.types.ResourceTypes;


/**
 * {@link ResourceManagerSpi} gets events from the instrumented java JVM processes and produces.
 * Each event moves the state machine behind the session for that JVM to the next step in the setup crash chain of events
 * and based on the state the recommendations are produced.  
 * At the time of instrumentation each JVM goes through a registration process, where a unique {@link UUID} os generated for this session and then 
 * used to track communications between {@link ResourceManagerSpi} and the specific JVM.
 * <pre>
 *  [Instrumented JVM] -> http://[server]:[port]/faultengie/register/jvm?uri=[STRING]
 *                     <- id=UUID
 *  
 *  [Instrumented JVM] -> [http://[server]:[port]/faultengie/profile?id=[uuid]&profile=[STRING]
 *                     <- instrumentation specification for a specific profile.  
 * 						   
 *  [Instrumented JVM] -> [http://[server]:[port]/faultengie/register/resource/network/address?v=www.google.com
 *                     <- execution plan
 *                     
 *  </pre>
 *  
 */
public class ResourceManagerSpi 
{
    protected static Log log = LogFactory.getLog(ResourceManagerSpi.class);
    
    /**
     * @see ResourceSpecLoader#load(String)
     */
    protected static ResourceSpecLoader  specLoader = null;
    
    /**
     * @see ConcurrentHashMap
     * Caches relationship between objects and resources
     */
    protected static ConcurrentHashMap<Object, _obj_mgmt_struct_> objectLookupTable = new ConcurrentHashMap<Object, _obj_mgmt_struct_>();
    protected static Thread objectLookupCleanupTh = null;
    /**
     * Caches resources
     */
    protected static ConcurrentHashMap<ResourceSpi, ResourceSpi> resources = new ConcurrentHashMap<ResourceSpi, ResourceSpi>();
    /**
     * Contains items that can not have schema changes during instrumentation
     */
    protected static HashSet<String> restricted = new HashSet<String>();
    
    /**
     * {@link Infrastructure} manages system dependent items, such as mount points and network routes.
     */
    //protected static Infrastructure infrastructure = null;
    
    protected static ByteArrayOutputStream dynaJar = new ByteArrayOutputStream();
    protected static JarOutputStream dynaJarStream = null;
    protected static JarFile jarFile = null;
    protected static ReferenceQueue<Object> referenceQueue = new ReferenceQueue<Object>();
    protected static CrashServiceAdapter csa = null;
    
    /**
     * Manage stats
     */
    protected static StatsManager statsManager = null;
    
    protected static ScheduledThreadPoolExecutor scheduledActions = null;
    /**
     * Collectors registered and running {@link Future}s
     */
    protected static ConcurrentHashMap<String, _stats_collections> collectors = new ConcurrentHashMap<String, _stats_collections>();
    protected static File workingDirectory = null;
    protected static File generatedJarFile = null;
    
    /**
     * {@link SimulationManagerSpi} instance manages all loaded simulations.
     */
    protected static SimulationManagerSpi simulationManager = new SimulationManagerSpi();
    
    protected static boolean protocolDetectionOn = true;
    /**
     * reset all runtime discoveries and tracking,
     */
    public static void reset()
    {
        simulationManager.stopAll();
        simulationManager.clearAll();
        resources.clear();
        statsManager.clearStats();
    }
    
    /**
     * Periodically run stats collections on registered collectors.
     * @author <MM>
     */
    static class _stats_collections implements Runnable
    {
        protected StatsCollector collector = null;
        protected volatile boolean stop = false;
        protected Future<?> future = null;
        
        public _stats_collections(StatsCollector c)
        {
            this.collector = c;
        }
        
        public void start()
        {
            collector.start();
        }
        
        /**
         * @return the future
         */
        public Future<?> getFuture()
        {
            return future;
        }

        /**
         * @param future the future to set
         */
        public void setFuture(Future<?> future)
        {
            this.future = future;
        }

        /**
         * Stop collector
         */
        public void stop()
        {
        	statsManager.collectAllStats(this.collector, this.collector.getLastAccessTime());
            collector.stop();
            stop = true;
        }
        
        @Override
        public void run()
        {       	
            if (stop)
            {
                this.future.cancel(true);
            }
            else
            {
            	statsManager.collectAllStats(this.collector, this.collector.getLastAccessTime());
            }
        }
    }
    
    /**
     * _reference_assoc
     */
    static class _reference_assoc<T> extends WeakReference<T>
    {
        protected Object key = null;

        public _reference_assoc(T referent, ReferenceQueue<? super T> q, Object key) 
        {
            super(referent, q);
            this.key = key;
        }
        
        public _reference_assoc(T r, String key)
        {
            super(r);
            this.key = key;
        }
        
        /**
         * @return the key
         */
        public Object getKey()
        {
            return key;
        }
        
        /**
         * @param key the key to set
         */
        public void setKey(String key)
        {
            this.key = key;
        }
        
    }
    
    /**
     * A static stub class used to rack resource to instance relationships that can not be tracked via {@link ResourceAware}
     * interface association, because the classes are resolved via bootstrap class loader, prior to the 
     * agent resolution.  
     */
    static class _obj_mgmt_struct_ implements ResourceAware
    {
        ResourceSpi resource = null;
        ResourceClosure ud = new ResourceClosureImpl();
        WeakReference<Object> objectWatcher = null;
        /**
         * default constructor.
         * @param res resource pointer
         * @param obj object that we are tracking
         * @param key of the object
         */
        _obj_mgmt_struct_(ResourceSpi res, Object obj, Object key)
        {
            this.resource = res;
            this.objectWatcher = new _reference_assoc<Object>(obj, referenceQueue, key);
        }
        
		@Override
		public ResourceSpi getResource() 
		{
			return this.resource;
		}
		@Override
		public void setResource(ResourceSpi res) 
		{
			this.resource = res;
		}
		
		@Override
		public Object getData() 
		{
			return ud;
		}
		@Override
		public void setData(Object u) 
		{
			this.ud = (ResourceClosure)u;
		}
    }
    
    
    
    /**
     * @return the resources
     */
    public static ConcurrentHashMap<ResourceSpi, ResourceSpi> getResourceSpis()
    {
        return resources;
    }

    
    /**
     * @return the simulationManager
     */
    public static SimulationManagerSpi getSimulationManager()
    {
        return simulationManager;
    }

    /**
     * Add a new simulation.
     * @param sim
     */
    public static void addSimulation(Simulation sim)
    {
        simulationManager.addSimulation(sim);
    }
    
    /**
     * Add a new behavior to existing simulation.
     * @param sim
     */
    public static void addBehavior(String id, Behavior b)
    {
        Simulation sim = simulationManager.getSimulation(id);
        if (sim != null)
        {
            sim.addBehavior(b);
        }
    }
    
    /**
     * Start the the {@link ResourceManagerSpi}
     * @param uri
     * @param secKey
     * @throws Exception
     */
    public static void start(HashMap<String, String> args, Instrumentation inst) throws Exception
    {                
        //Check if we have a core property overwrite
        String props = args.get("crash4j.properties");
        if (props == null)
        {
            props = "classpath:///com/crash4j/engine/spi/config/crash4j.properties";
        }
        
        Properties config = new Properties();
        config.load(Utils.getResourceAsStream(ResourceManagerSpi.class, props));
        
        //initialize action reaper executer threads.
        int tc = 3;
        String tcs = config.getProperty("com.crash4j.engine.spi.action.threads");
        if (tcs == null)
        {
            tc = 3;
        }
        else
        {
            tc = Integer.parseInt(tcs);
        }
        scheduledActions = new ScheduledThreadPoolExecutor(tc);
        
        //Handle and create working file location .crash4j directory
        String wdir = (String)config.get("com.crash4j.engine.workdir");
        if (wdir == null)
        {
            File base = File.createTempFile("crash4j", ".1");
            wdir = base.getParent();
            base.delete();
        }
        
        //Make working directory
        workingDirectory = new File(wdir, ".crash4j");
        if (!workingDirectory.exists())
        {
            workingDirectory.mkdir();
        }
        
        //create log file, based on configuration.
        File logfile = null;
        String logf = config.getProperty("com.crash4j.engine.log");
        if (logf == null)
        {    
            logfile = new File(workingDirectory, "crash4j.log");
        }
        else
        {
            logfile = new File(logf);
        }
        int mask = 2;
        String lmask = config.getProperty("com.crash4j.engine.log.level");
        if (lmask != null)
        {
            mask = Integer.parseInt(lmask);
        }
        LogFactory.setMask(mask);
        LogFactory.init(logfile);
        
        specLoader = new ResourceSpecLoader();
        //get infrastructure service going
        //infrastructure = Infrastructure.getInstance();
        //generate dynamic classes
        for (int i = 0; i < 3; i++)
        {
            String spec = config.getProperty("com.crash4j.engine.level."+i);
            if (spec != null)
            {
                specLoader.load(Utils.getResourceAsStream(ResourceManagerSpi.class, spec));
            }
        }
        
        try
        {
            startDynamicJarFile();
            generateNativeStubs();
            createObjectWrapperClass();
        }
        finally
        {
            jarFile = endDynamicJarFile();
            inst.appendToBootstrapClassLoaderSearch(jarFile);
            inst.appendToSystemClassLoaderSearch(jarFile);
        }
        
        Utils.initClassDiscovery();
        
        //Start onject table cleanup thread.
        Runnable oCleanup = new Runnable()
        {
            @Override
            public void run()
            {
                while (true)
                {
                    try
                    {
                        Reference<?> ref = null;
                        while ((ref = referenceQueue.remove()) != null)
                        {
                            _reference_assoc<Object> oo = (_reference_assoc<Object>)ref;
                            objectLookupTable.remove(oo.getKey());
                        }
                    } 
                    catch (Exception e)
                    {
                       log.logError("start", e);
                    }
                }
            }
        };
        objectLookupCleanupTh = new Thread(oCleanup);
        objectLookupCleanupTh.start();
        
        StdStreamOutputStream outio = new StdStreamOutputStream(FileDescriptor.out);
        StdStreamOutputStream errio = new StdStreamOutputStream(FileDescriptor.err);
        StdStreamInputStream inio = new StdStreamInputStream(FileDescriptor.in);
        
        resources.put((ResourceSpi)outio.getResource(), (ResourceSpi)outio.getResource());
        resources.put((ResourceSpi)errio.getResource(), (ResourceSpi)errio.getResource());
        resources.put((ResourceSpi)inio.getResource(), (ResourceSpi)inio.getResource());
                        
        System.setOut(new PrintStream(outio));
        System.setErr(new PrintStream(errio));
        System.setIn(inio);
        
        
        //If remote server information is included, initiate connection 
        String host = args.get("crash4j.service.host");
        String port = args.get("crash4j.service.port");
        String apikey = args.get("crash4j.service.apikey");
        
        if (host != null && port != null && apikey != null)
        {
            csa = new CrashServiceAdapter(host, Integer.parseInt(port), apikey);
            if (csa != null)
            {
                csa.connect();
                String stime = config.getProperty("crash4j.service.collector.sweepperiod");
                int st = 200;
                if (stime != null)
                {
                	st = Integer.parseInt(stime);
                }
                registerCollector(csa, st, TimeUnit.MILLISECONDS);
            }
        }
        else
        {
            log.logInfo("Incomplete service specification.  This VM will not connect to the external management services");
        }
        
        statsManager = new StatsManager();
        
        String statsOnS = args.get("com.crash4j.collect.statistics");
        boolean statsOn = (statsOnS == null) || (Boolean.parseBoolean(statsOnS));
        if (statsOn)
        {
            statsManager.start();
        }

        String detectionOn = args.get("com.crash4j.detect.wire.protocols");
        if (detectionOn != null)
        {
        	protocolDetectionOn = Boolean.parseBoolean(detectionOn);
        }
        //Shutdown hook to facilitate exit
        Runtime.getRuntime().addShutdownHook(new Thread() { public void run() { ResourceManagerSpi.stop(); }});
        
    }

    /**
     * @return instance of {@link StatsManager}
     */
    public static StatsManager getStatsManager()
    {
    	return statsManager;
    }
    
    
    /**
     * Assigns active simulation to a new or completed resource
     * @param spi
     */
    public static void assignSimulationForResource(ResourceSpi spi)
    {
        if (!spi.isComplete())
        {
            return;
        }
        //Check to see if we already assigned simulation to this resource as well
        if (spi.getSimulation() != null)
        {
            return;
        }
        if (spi instanceof PropertyOwner && spi instanceof SimulationConsumer)
        {
            Simulation s = simulationManager.selectSimulation((PropertyOwner)spi);
            if (s != null)
            {
                ((SimulationConsumer)spi).setSimulation(s);
            }
        }
    }
    
    /**
     * Create {@link ObjectWrapperCreator} created class that will be able to properly hash
     * and compare objects for uniqueness.
     * @throws IOException
     */
    protected static void createObjectWrapperClass() throws IOException
    {
        ObjectWrapperCreator cre = new ObjectWrapperCreator();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        String cl = cre.create(bos);
        JarEntry entry = new JarEntry(cl+".class");     
        dynaJarStream.putNextEntry(entry);
        dynaJarStream.write(bos.toByteArray());
        dynaJarStream.closeEntry();
    }
    
    
    
    /**
     * @throws IOException 
     * @throws ClassNotFoundException 
     * 
     */
    protected static void generateNativeStubs() throws ClassNotFoundException, IOException
    {
        HashMap<String, ArrayList<ResourceSpec>> classIndex = specLoader.getAllByClass();
        HashMap<String, ArrayList<ResourceSpec>> eps = new HashMap<String, ArrayList<ResourceSpec>>();
        for (Entry<String, ArrayList<ResourceSpec>> cle : classIndex.entrySet())
        {
            ArrayList<ResourceSpec> specs = cle.getValue();
            String cname = cle.getKey();
            
            for (ResourceSpec s : specs)
            {
                if (s.isNative())
                {
                    ArrayList<ResourceSpec> sigs = eps.get(cname);
                    if (sigs == null)
                    {
                        sigs = new ArrayList<ResourceSpec>();
                        eps.put(cname, sigs);        
                    }
                    sigs.add(s);
                }
            }
        }
        
        //We now have a collection of class defs to create
        for (Entry<String, ArrayList<ResourceSpec>> ne : eps.entrySet())
        {
            ArrayList<ResourceSpec> nspecs = ne.getValue();
            String nname = ne.getKey();
            String sigs[] = new String[nspecs.size()];
            int ids[] = new int[nspecs.size()];
            int i = 0;
            for (ResourceSpec s : nspecs)
            {
                sigs[i] = s.getSignature();
                ids[i] = s.getId();
                i++;
            }
            String cnname = nname.replace('.', '_');
            byte[] bytes = BCELUtils.generateNativeStub(nname, 
                    sigs, cnname, "java.lang.Object", ids);
            
            JarEntry entry = new JarEntry(cnname+".class");     
            dynaJarStream.putNextEntry(entry);
            dynaJarStream.write(bytes);
            dynaJarStream.closeEntry();
        }
    }
    
    /**
     * Start building jar cache.
     * @throws IOException
     */
    protected static void startDynamicJarFile() throws IOException
    {
        dynaJarStream = new JarOutputStream(dynaJar);
        dynaJarStream.setComment("Dynamic instrumentation srubs and delegates to support crash4j operations");
    }
    
    /**
     * Start building jar cache.
     * @throws IOException
     */
    protected static JarFile endDynamicJarFile() throws IOException
    {
        dynaJarStream.flush();
        dynaJarStream.finish();
        SecureRandom sr = new SecureRandom();
        generatedJarFile = new File(workingDirectory, "crash4jge.jar");
        generatedJarFile.deleteOnExit();
        FileOutputStream fios = new FileOutputStream(generatedJarFile);
        fios.write(dynaJar.toByteArray());
        fios.flush();
        return new JarFile(generatedJarFile);
    }
        
    
    /**
     * @return instance of the infrastructure.
    public static Infrastructure getInfrastructure()
    {
        return infrastructure;
    }
     */
    
    /**
     * @return the restricted items that can not be modified because they are
     * resolved before premain.
     */
    public static HashSet<String> getRestricted()
    {
        return restricted;
    }



    /**
     * @throws Exception
     */
    public static void stop()
    {
        try
        {
            reset();
            scheduledActions.shutdown();
            if (csa != null)
            {
                csa.disconnect();
            }
            if (statsManager != null)
            {
            	statsManager.stop();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);
        }
    }
    
    public static ResourceSpec[] getSpecs()
    {
        return specLoader.getSpecs();
    }
    

    /**
     * @return new instance of {@link ResourceSpec}
     */
    public static ResourceSpec createSpec(String clz, String method, String full, String sig,  boolean nat, 
    		LifecycleHandler handler, Action defact, ResourceTypes rType, HashMap<String, String> attrs, 
            Set<Class<?>> exceptions, boolean isub, int accepts)
    {
    	return specLoader.createSpec(clz, method, full, sig, nat, handler, defact, rType, attrs, exceptions, isub, accepts);
    }
    
    
    /**
     * Get the {@link ResourceSpec} by ID
     * @param id integer id
     * @return {@link ResourceSpec}
     */
    public static ResourceSpec getSpecById(int id)
    {
        return specLoader.getSpecById(id);
    }
    
    /**
     * Handle entry point
     * @param o
     * @param res
     */
    public static void onEntry(EventData o, ResourceSpi res)
    {
        //Start stat collection.
        LifecycleHandler handler = (LifecycleHandler)o.getSpec().getHandler();
        if (handler == null)
        {
            log.logError("Unable to resolve LifecycleHandler for "+o.getSpec().getKey());
        }
        
        //Apply simulation behavior
        Simulation sim = res.getSimulation();
        if (sim != null)
        {
           Command[] commands = sim.getCommands();
           if (commands != null)
           {
               for (Command c : commands)
               {
                   Behavior bh = c.getBehavior();
                   if (bh.shouldEffect(handler.getAction(res, o.getSpec(), o.getParams(), o.getInstance(), o.getClosure())))
                   {
                       handler.modifyBehaviorOnEntry(o, res, c);
                   }
               }
           }
        }
        
        handler.eneter(res, o.getSpec(), o.getParams(), o.getInstance(), null, o.getClosure());        
    }

    
    
    /**
     * Execute behavioral modification, required during {@link EventHandler#end(Object, Object)} message, sent by instrumentation
     * in response to instrumented function call.
     * @param EventData instance for thi call
     * @param res is {@link ResourceSpi} instance in play
     */
    public static void onExit(EventData o, Object ex, ResourceSpi res)
    {
        LifecycleHandler handler = (LifecycleHandler)o.getSpec().getHandler();
        
        long dt = o.getStopTime() - o.getStartTime();        
        //Apply simulation behavior
        Simulation sim = res.getSimulation();
        if (sim != null)
        {
           Command[] commands = sim.getCommands();
           if (commands != null)
           {
               for (Command c : commands)
               {
                   Behavior bh = c.getBehavior();
                   if (bh.shouldEffect(handler.getAction(res, o.getSpec(), o.getParams(), o.getInstance(), o.getClosure())))
                   {
                       handler.modifyBehaviorOnExit(o, ex, res, dt, c);
                   }
               }
           }
        }
        
        
        handler.exit(res, o.getSpec(),  o.getParams(), 
                o.getInstance(), null, o.getClosure(), ex, o.getStartTime(), dt, System.nanoTime() - o.getStartTime());
    }
    
    /**
     * Execute behavioral modification, required during {@link EventHandler#end(Object, Object, Object)} message, sent by instrumentation
     * in response to instrumented function call.
     * @param EventData instance for this call
     * @param res is {@link ResourceSpi} instance in play
     */
    public static Object onExit(EventData o, Object ex, ResourceSpi res, Object rv)
    {
        LifecycleHandler handler = (LifecycleHandler)o.getSpec().getHandler();
        long dt = o.getStopTime() - o.getStartTime();        
        
        //Apply simulation behavior
        Simulation sim = res.getSimulation();
        if (sim != null)
        {
           Command[] commands = sim.getCommands();
           if (commands != null)
           {
               for (Command c : commands)
               {
                   Behavior bh = c.getBehavior();
                   if (bh.shouldEffect(handler.getAction(res, o.getSpec(), o.getParams(), o.getInstance(), o.getClosure())))
                   {
                       rv = handler.modifyBehaviorOnExit(o, ex, res, rv, dt, c);
                   }
               }
           }
        }
        
        handler.exit(res, o.getSpec(),  o.getParams(), 
                o.getInstance(), rv, o.getClosure(), ex, o.getStartTime(), dt, System.nanoTime() - o.getStartTime());
        return rv;
    }
    
    
    /**
     * Execute behavioral modification, required during {@link EventHandler#end(Object, Object, int)} message, sent by instrumentation
     * in response to instrumented function call.
     * @param EventData instance for this call
     * @param res is {@link ResourceSpi} instance in play
     */
    public static int onExit(EventData o, Object ex, ResourceSpi res, int rv)
    {
        LifecycleHandler handler = (LifecycleHandler)o.getSpec().getHandler();
        long dt = o.getStopTime() - o.getStartTime();        
        //Apply simulation behavior
        Simulation sim = res.getSimulation();
        if (sim != null)
        {
           Command[] commands = sim.getCommands();
           if (commands != null)
           {
               for (Command c : commands)
               {
                   Behavior bh = c.getBehavior();
                   if (bh.shouldEffect(handler.getAction(res, o.getSpec(), o.getParams(), o.getInstance(), o.getClosure())))
                   {
                       rv = handler.modifyBehaviorOnExit(o, ex, res, rv, dt, c);
                   }
               }
           }
        }
        
        handler.exit(res, o.getSpec(),  o.getParams(), 
                o.getInstance(), rv, o.getClosure(), ex, o.getStartTime(), dt, System.nanoTime() - o.getStartTime());
        return rv;
    }
    
    /**
     * Execute behavioral modification, required during {@link EventHandler#end(Object, Object, long)} message, sent by instrumentation
     * in response to instrumented function call.
     * @param EventData instance for this call
     * @param res is {@link ResourceSpi} instance in play
     */
    public static long onExit(EventData o, Object ex, ResourceSpi res, long rv)
    {
        LifecycleHandler handler = (LifecycleHandler)o.getSpec().getHandler();
        long dt = o.getStopTime() - o.getStartTime();        
        
        //Apply simulation behavior
        Simulation sim = res.getSimulation();
        if (sim != null)
        {
           Command[] commands = sim.getCommands();
           if (commands != null)
           {
               for (Command c : commands)
               {
                   Behavior bh = c.getBehavior();
                   if (bh.shouldEffect(handler.getAction(res, o.getSpec(), o.getParams(), o.getInstance(), o.getClosure())))
                   {
                       rv = handler.modifyBehaviorOnExit(o, ex, res, rv, dt, c);
                   }
               }
           }
        }
        
        handler.exit(res, o.getSpec(),  o.getParams(), 
                o.getInstance(), rv, o.getClosure(), ex, o.getStartTime(), dt, System.nanoTime() - o.getStartTime());
        return rv;
    }
    
    
    /**
     * Execute behavioral modification, required during {@link EventHandler#end(Object, Object, float)} message, sent by instrumentation
     * in response to instrumented function call.
     * @param EventData instance for this call
     * @param res is {@link ResourceSpi} instance in play
     */
    public static float onExit(EventData o, Object ex, ResourceSpi res, float rv) 
    {
        LifecycleHandler handler = (LifecycleHandler)o.getSpec().getHandler();
        long dt = o.getStopTime() - o.getStartTime();        
        
        //Apply simulation behavior
        Simulation sim = res.getSimulation();
        if (sim != null)
        {
           Command[] commands = sim.getCommands();
           if (commands != null)
           {
               for (Command c : commands)
               {
                   Behavior bh = c.getBehavior();
                   if (bh.shouldEffect(handler.getAction(res, o.getSpec(), o.getParams(), o.getInstance(), o.getClosure())))
                   {
                       rv = handler.modifyBehaviorOnExit(o, ex, res, rv, dt, c);
                   }
               }
           }
        }
        
        handler.exit(res, o.getSpec(),  o.getParams(), 
                o.getInstance(), rv, o.getClosure(), ex, o.getStartTime(), dt, System.nanoTime() - o.getStartTime());
        return rv;
    }
    
    /**
     * Execute behavioral modification, required during {@link EventHandler#end(Object, Object, double)} message, sent by instrumentation
     * in response to instrumented function call.
     * @param EventData instance for this call
     * @param res is {@link ResourceSpi} instance in play
     */
    public static double onExit(EventData o, Object ex, ResourceSpi res, double rv) 
    {
        LifecycleHandler handler = (LifecycleHandler)o.getSpec().getHandler();
        long dt = o.getStopTime() - o.getStartTime();        
        
        //Apply simulation behavior
        Simulation sim = res.getSimulation();
        if (sim != null)
        {
           Command[] commands = sim.getCommands();
           if (commands != null)
           {
               for (Command c : commands)
               {
                   Behavior bh = c.getBehavior();
                   if (bh.shouldEffect(handler.getAction(res, o.getSpec(), o.getParams(), o.getInstance(), o.getClosure())))
                   {
                       rv = handler.modifyBehaviorOnExit(o, ex, res, rv, dt, c);
                   }
               }
           }
        }
        
        handler.exit(res, o.getSpec(),  o.getParams(), 
                o.getInstance(), rv, o.getClosure(), ex, o.getStartTime(), dt, System.nanoTime() - o.getStartTime());
        return rv;
    }
    
    /**
     * Execute behavioral modification, required during {@link EventHandler#end(Object, Object, char)} message, sent by instrumentation
     * in response to instrumented function call.
     * @param EventData instance for this call
     * @param res is {@link ResourceSpi} instance in play
     */
    public static char onExit(EventData o, Object ex, ResourceSpi res, char rv) 
    {
        LifecycleHandler handler = (LifecycleHandler)o.getSpec().getHandler();
        long dt = o.getStopTime() - o.getStartTime();        
        
        //Apply simulation behavior
        Simulation sim = res.getSimulation();
        if (sim != null)
        {
           Command[] commands = sim.getCommands();
           if (commands != null)
           {
               for (Command c : commands)
               {
                   Behavior bh = c.getBehavior();
                   if (bh.shouldEffect(handler.getAction(res, o.getSpec(), o.getParams(), o.getInstance(), o.getClosure())))
                   {
                       rv = handler.modifyBehaviorOnExit(o, ex, res, rv, dt, c);
                   }
               }
           }
        }
        
        handler.exit(res, o.getSpec(),  o.getParams(), 
                o.getInstance(), rv, o.getClosure(), ex, o.getStartTime(), dt, System.nanoTime() - o.getStartTime());
        return rv;
    }
    
    /**
     * Execute behavioral modification, required during {@link EventHandler#end(Object, Object, short)} message, sent by instrumentation
     * in response to instrumented function call.
     * @param EventData instance for this call
     * @param res is {@link ResourceSpi} instance in play
     */
    public static short onExit(EventData o, Object ex, ResourceSpi res, short rv) 
    {
        LifecycleHandler handler = (LifecycleHandler)o.getSpec().getHandler();
        long dt = o.getStopTime() - o.getStartTime();        
        
        //Apply simulation behavior
        Simulation sim = res.getSimulation();
        if (sim != null)
        {
           Command[] commands = sim.getCommands();
           if (commands != null)
           {
               for (Command c : commands)
               {
                   Behavior bh = c.getBehavior();
                   if (bh.shouldEffect(handler.getAction(res, o.getSpec(), o.getParams(), o.getInstance(), o.getClosure())))
                   {
                       rv = handler.modifyBehaviorOnExit(o, ex, res, rv, dt, c);
                   }
               }
           }
        }
        
        handler.exit(res, o.getSpec(),  o.getParams(), 
                o.getInstance(), rv, o.getClosure(), ex, o.getStartTime(), dt, System.nanoTime() - o.getStartTime());
        return rv;
    }
    /**
     * Execute behavioral modification, required during {@link EventHandler#end(Object, Object, byte)} message, sent by instrumentation
     * in response to instrumented function call.
     * @param EventData instance for this call
     * @param res is {@link ResourceSpi} instance in play
     */
    public static byte onExit(EventData o, Object ex, ResourceSpi res, byte rv) 
    {
        LifecycleHandler handler = (LifecycleHandler)o.getSpec().getHandler();
        long dt = o.getStopTime() - o.getStartTime();        
        
        //Apply simulation behavior
        Simulation sim = res.getSimulation();
        if (sim != null)
        {
           Command[] commands = sim.getCommands();
           if (commands != null)
           {
               for (Command c : commands)
               {
                   Behavior bh = c.getBehavior();
                   if (bh.shouldEffect(handler.getAction(res, o.getSpec(), o.getParams(), o.getInstance(), o.getClosure())))
                   {
                       rv = handler.modifyBehaviorOnExit(o, ex, res, rv, dt, c);
                   }
               }
           }
        }
        
        handler.exit(res, o.getSpec(),  o.getParams(), 
                o.getInstance(), rv, o.getClosure(), ex, dt, o.getStartTime(), System.nanoTime() - o.getStartTime());
        return rv;
    }
    
    /**
     * Execute behavioral modification, required during {@link EventHandler#end(Object, Object, boolean)} message, sent by instrumentation
     * in response to instrumented function call.
     * @param EventData instance for this call
     * @param res is {@link ResourceSpi} instance in play
     */
    public static boolean onExit(EventData o, Object ex, ResourceSpi res, boolean rv)
    {
        LifecycleHandler handler = (LifecycleHandler)o.getSpec().getHandler();
        long dt = o.getStopTime() - o.getStartTime();        
        
        //Apply simulation behavior
        Simulation sim = res.getSimulation();
        if (sim != null)
        {
           Command[] commands = sim.getCommands();
           if (commands != null)
           {
               for (Command c : commands)
               {
                   Behavior bh = c.getBehavior();
                   if (bh.shouldEffect(handler.getAction(res, o.getSpec(), o.getParams(), o.getInstance(), o.getClosure())))
                   {
                       rv = handler.modifyBehaviorOnExit(o, ex, res, rv, dt, c);
                   }
               }
           }
        }
        
        handler.exit(res, o.getSpec(),  o.getParams(), 
                o.getInstance(), rv, o.getClosure(), ex, o.getStartTime(), dt, System.nanoTime() - o.getStartTime());
        return rv;
    }
    
    
    
    /**
     * Complete resource with more data
     * @param res
     * @param spec
     * @param args
     * @param instance
     * @param rv
     * @throws UnknownResourceException
     */
    public static void completeResource(EventData o, Object rv) 
            throws UnknownResourceException
    {
        ResourceSpi spi = o.getResource();
        if (spi.isComplete())
        {
            return;
        }
        ResourceSpec spec = o.getSpec();
        ResourceBuilder resourceBuilder = (ResourceBuilder)spec.getHandler();
        if (resourceBuilder == null)
        {
            log.logError("Unknwon resource builder for"+spec.getKey());
            return;
        }
        
        //Establish closure with object
        Object instance = o.getInstance();
        
        //int s0 = resources.size();
        ResourceSpi pk = resources.remove(spi);
        //int s1 = resources.size();
        //reinsert resaource here as we are going to be changing its hashcode
        resourceBuilder.completeResource(spi, 
                spec, o.getParams(), o.getInstance(), rv); 
        putIfAbsent(spi);
        
        if (rv instanceof ResourceAware)
        {
            ((ResourceAware)rv).setResource(spi);
        }
        
        if (instance instanceof ResourceAware)
        {
            ((ResourceAware)instance).setResource(spi);
        }         
        else
        {
            Object hkey = null;
            Object assoc = instance;
            if (instance != null)
            {
                hkey = resourceBuilder.getUniqueKey(instance, o.getParams(), rv);
            }
            else if (rv != null)
            {
                hkey = resourceBuilder.getUniqueKey(instance, o.getParams(), rv);
                assoc = rv;
            }
            
            if (hkey != null)
            {
                objectLookupTable.putIfAbsent(hkey, new _obj_mgmt_struct_(spi, assoc, hkey));
            }
        }
    }

    /**
     * @param spi resource to include
     * @return {@link ResourceSpi} from the collection or add one.
     */
    private static ResourceSpi putIfAbsent(ResourceSpi spi)
    {
        ResourceSpi ospi = resources.putIfAbsent(spi, spi);
        if (ospi == null)
        {
           ospi = spi; 
        }
        return ospi;
    }
    
    /**
     * lookup resource when event occurs.
     * @param spec
     * @param args
     * @param instance
     * @return
     * @throws UnknownResourceException
     * @throws URISyntaxException 
     */
    public static ResourceSpi resolveResource(EventData o, Object rv) 
            throws UnknownResourceException, URISyntaxException
    {
    	
    	ResourceSpec spec = o.getSpec();
    	Object args = o.getParams();
    	Object instance = o.getInstance();
    	
        ResourceSpi mr = null;
        
        ResourceBuilder resourceBuilder = (ResourceBuilder)spec.getHandler();
        if (resourceBuilder == null)
        {
            log.logError("Unknwon resource builder for"+spec.getKey());
            return null;
        }
        
        
        //If we can not cache the resource then create the new copy and match with
        //runtime table.
        //Not caching means that we are creating a new unique resource each time a call is made and are not associating
        //a resource with the instance of the call.  This transient situation happens when DatagramSocket is sending and receiving 
        //data to an unspecified end-points.  Under this conditions we are unable to keep any state.
        if (!resourceBuilder.canCache())
        {
            ResourceSpi spi = resourceBuilder.createResource(spec, args, instance, rv);
            if (spi != null)
            {
                //Check to see if this resource  already being created and if yes then establish the relationship
                //between resource client object and ResourceSpi
                return putIfAbsent(spi);
            }
            return null;
        }
        
        
        ResourceClosure closure = null;
        
        // This is an instrumented instance generated by 
        // crash4j after the bootstrap class loader has finished and it is aware of its resource
        if (instance instanceof ResourceAware)
        {
        	closure = (ResourceClosure)((ResourceAware)instance).getData();
        	if (closure == null)
        	{
        		closure = new ResourceClosureImpl();
        		((ResourceAware)instance).setData(closure);
        	}
        	
            ResourceSpi res = ((ResourceAware)instance).getResource();
            if (res != null)
            {
            	mr = res;
            	o.setResource(res);
            	o.setClosure(closure);
            	completeResource(o, rv);
            }
            else
            {
                res = resourceBuilder.createResource(spec, args, instance, rv);
                if (res != null)
                {
                    mr = putIfAbsent(res);
                    mr = (mr == null ? res : mr);
                	o.setResource(mr);
                	o.setClosure(closure);
                    //Check to see if this resource  already being created and if yes then establish the relationship
                    //between resource client object and ResourceSpi                    
                    if (instance instanceof ResourceAware)
                    {
                        ((ResourceAware)instance).setResource(mr);
                    }         
                }
            }
            
            //If we could not get resource here , oo well.......
            if (mr == null)
            {
            	return null;
            }
            
            //associate the produced return value with the resource.
        	//as well
            if (rv instanceof ResourceAware)
            {
                ((ResourceAware)rv).setResource(mr);
        		((ResourceAware)rv).setData(closure);
            }
            
            return mr;
        }
        
        //If we are here then we are dealing with lower level objects that are not ResourceAware and 
        //so the association will have to be manual;
        
        ResourceSpi res = null;
        Object hkey = null;
        //Get Unique Key from the resource, used for further indexing
        if (instance != null)
        {
            hkey = resourceBuilder.getUniqueKey(instance, args, rv);
        }
        
        if (hkey != null)
        {
            _obj_mgmt_struct_ omgm = objectLookupTable.get(hkey);
            if (omgm != null)
            {
            	closure = (ResourceClosure)(omgm.getData());
            }
            res = (omgm == null ? null : omgm.resource);
        }
        
        //Lookup the resource by the instance.
        
        if (res == null)
        {
            ResourceSpi spi = resourceBuilder.createResource(spec, args, instance, rv);
            if (spi != null)
            {
                //Check to see if this resource  already being created and if yes then establish the relationship
                //between resource client object and ResourceSpi
                mr = putIfAbsent(spi);
                if (mr == null)
                {
                    mr = spi;
                }
                if (hkey != null)
                {
                	_obj_mgmt_struct_ mg = new _obj_mgmt_struct_(mr, instance, hkey);
                	closure = (ResourceClosure)mg.getData();
                    objectLookupTable.putIfAbsent(hkey, mg);
                }
            	o.setResource(mr);
            	o.setClosure(closure);
            }
        }
        else
        {
        	mr = res;
        	o.setResource(mr);
        	o.setClosure(closure);
            completeResource(o, rv);
        }
        
        return mr;        
    }

    /**
     * Execute a single (Full) collection of all the stats
     * that are currently initiated by the running system
     * 
     * @param collector
     */
    public static void collectStats(StatsCollector collector)
    {
        collector.start();
        collector.begin();
        for (ResourceSpi rSpi : resources.keySet())  //For each resource spi in the list
        {
            _collectStats(collector, rSpi, -1);
        }
        collector.end();
        collector.stop();
    }

    /**
     * Execute a single (Full) collection of all the stats
     * that are currently initiated by the running system
     * 
     * @param collector
     */
    public static void collectStats(StatsCollector collector, 
            ResourceSpi rSpi)
    {
        collector.start();
        collector.begin();
        _collectStats(collector, rSpi, -1);
        collector.end();
        collector.stop();
    }

    /**
     * Collect Stats from a single resource.
     * @param collector
     * @param rSpi
     */
    protected static void _collectStats(StatsCollector collector, ResourceSpi rSpi, long after)
    {
        //Check last access time. if did not change then skip
        //if (true)
        //Get all monitors
        collector.enterResource(rSpi);
        try
        {
        	statsManager.collectStats(collector, rSpi, after);
        }
        finally
        {
            collector.exitResource(rSpi);
        }
    }
    
    /**
     * Execute a single (Full) collection of all the stats
     * that are currently initiated by the running system
     * 
     * @param collector
     */
    public static void clearStats()
    {
    	statsManager.clearStats();
    }
    
    
    
	/**
     * @return the specLoader
     */
    public static ResourceSpecLoader getSpecLoader()
    {
        return specLoader;
    }

    
	/**
	 * 
	 * @param string
	 * @return
	 */
    public static ResourceSpec getByFullSignature(String string)
    {
        return specLoader.getByFullSignature(string);
    }

    public static List<ResourceSpec> getByClassName(String className)
    {
        return specLoader.getByClassName(className);
    }
    /**
     * Register collector
     * @param c
     * @param period
     * @param unit
     * @return
     */
    public static String registerCollector(StatsCollector c, long period, TimeUnit unit)
    {
        _stats_collections r = new _stats_collections(c);
        r.start();
        ScheduledFuture<?> future = scheduledActions.scheduleAtFixedRate(r, 0, period, unit);
        r.setFuture(future);
        int h = Utils.getBaseHashCode(future);
        StringBuilder b = new StringBuilder("collector@");
        b.append(h);
        collectors.put(b.toString(), r);
        return b.toString();        
    }
    /**
     * unRegisterCollector
     * @param moniker
     */
    public static void unRegisterCollector(String moniker)
    {
        _stats_collections r = collectors.remove(moniker);
        if (r != null)
        {
            r.stop();
        }
    }
    
    /**
     * Register collector by name of the implementation
     * @param klass
     * @param props
     * @param period
     * @param unit
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IntrospectionException
     * @throws InvocationTargetException 
     * @throws IllegalArgumentException 
     */
    public static String registerCollector(String klass, Map<String, Object> props, long period, TimeUnit unit) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, IntrospectionException, 
            IllegalArgumentException, InvocationTargetException
    {
        Class<StatsCollector> ck = (Class<StatsCollector>)Utils.findClass(ResourceManagerSpi.class, klass);
        BeanInfo binfo = Introspector.getBeanInfo(ck);
        PropertyDescriptor pdesc[] = binfo.getPropertyDescriptors();
        StatsCollector sc = ck.newInstance();
        for (Entry<String, Object> entry : props.entrySet())            
        {
            for (PropertyDescriptor pd : pdesc)
            {
                if (pd.getName().equalsIgnoreCase(entry.getKey()))
                {
                    pd.getWriteMethod().invoke(sc, entry.getValue());
                }
            }
        }
        return registerCollector(sc, period, unit);
    }
    
    /**
     * Raises protocol event, {@link ProtocolEvent} as a result of {@link ProtocolRecognizer} yilding 
     * an event through the {@link EventHandler} API
     * @param pe is an event identified.
     */
    public static void raiseProtocolEvent(ProtocolEvent pe)
    {
    	if (pe == null)
    	{
    		return;
    	}
    	EventData ref = null;
    	Throwable newe = null;
    	boolean ignoring = false;
    	try
    	{
            ignoring = ThreadContext.shouldIgnore();  
            ThreadContext.endIgnore();
            ResourceSpi si = pe.getTargetResource();
            ThreadContext.push(ThreadContext.createFrame(si.getResourceSpec(), pe.getResource()));
        	ref = (EventData)EventHandler.begin(si.getResourceSpec(), null, pe);
        	ref.setStartTime(pe.getEventStartTime());
        	ref.setStopTime(pe.getEventStopTime());
    	}
        catch (Throwable e)
        {
        	newe = e;
        }
    	finally
    	{
        	try 
        	{
        		EventHandler.end(ref, newe); 
                ThreadContext.pop();
        		if (ignoring)
        		{
        			ThreadContext.beginIgnore();  
        		}
        	} 
        	catch (Throwable t) 
        	{
        		log.logError("", t);
        	}
    	}
    } 
    
    /**
     * @param type indicates the type of resource that we are going to attach the recognition to.
     * @return creates a new instance of protocol recognizer
     */
    public static ProtocolRecognizer newProtocolRecognizer(ResourceTypes type)
    {
    	if (!protocolDetectionOn)
    	{
        	return new DisabledProtocolRecognizer();
    	}
    	if (type.equals(ResourceTypes.NET))
    	{
    		return new GenericProtocolRecognizer();
    	}
    	return new DisabledProtocolRecognizer();
    }
}
