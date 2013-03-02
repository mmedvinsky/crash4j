/**
 * Copyright WalletKey Inc. 
 */
package com.crash4j.agent;

import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;

import com.crash4j.engine.spi.ResourceManagerSpi;
import com.crash4j.engine.spi.instrument.transformers.MethodInjector;
import com.crash4j.engine.spi.instrument.transformers.NativeMethodInjector;


/**
 * Initiates instrumentation of the classes
 */
public class CrashAgent 
{
	/**
	 * Agent initialization function
	 * @param agentArgs os a {@link String} of agent parameters.
	 * @param inst os an instrumentation support from the JVM
	 */
	public static void premain(String agentArgs, Instrumentation inst)
	{		
        try 
        {
            //Read command line and place it into Hash for further use.
            HashMap<String, String> argT = new HashMap<String, String>();
            if (agentArgs != null)
            {
                String[] args = agentArgs.split("[;]");
                for (String n : args)
                {
                    String[] kvp = n.split("[=]");
                    argT.put(kvp[0].trim(), kvp[1].trim());
                }
            }

            //prepare class loader
            if (argT.containsKey("cp"))
            {
            	String cp = argT.get("cp");
            	String pelems[] = cp.split(":");
            	for (String pe : pelems) 
            	{
            		inst.appendToBootstrapClassLoaderSearch(new JarFile(pe));
				}
            }
            
            ResourceManagerSpi.start(argT, inst);
            
    		//add a transformer to the list
            inst.addTransformer(new MethodInjector(), true);
            inst.addTransformer(new NativeMethodInjector(), true);
            
    		//Restart redefinition on all classes included in the list and that are already being instrumented by the
    		//underlying class loading mechanism
            Class<?> []classes = inst.getAllLoadedClasses();
            ArrayList<Class<?>> reformatList = new ArrayList<Class<?>>();
            //ArrayList<ClassDefinition> redefList = new ArrayList<ClassDefinition>();
    		for (Class<?> cla : classes) 
    		{
    			if (ResourceManagerSpi.getByClassName(cla.getName()) != null)
    			{
    				reformatList.add(cla);
    				//Add to the restricted list to exclude from schema modifications
    				ResourceManagerSpi.getRestricted().add(cla.getName());
    			}
    		}    		
			inst.retransformClasses(reformatList.toArray(new Class[0]));		
		} 
		catch (Exception e) 
		{
		    e.printStackTrace(System.err);
			System.err.println(e.getMessage());
		}
	}
}
