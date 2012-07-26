/**
 * Copyright WalletKey Inc.
 */
package com.crash4j.engine.spi.instrument.transformers;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import com.crash4j.engine.spi.ResourceManagerSpi;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.context.ThreadContext;
import com.crash4j.engine.spi.instrument.bcel.Repository;
import com.crash4j.engine.spi.instrument.bcel.classfile.JavaClass;
import com.crash4j.engine.spi.instrument.bcel.classfile.Method;
import com.crash4j.engine.spi.instrument.bcel.generic.ClassGen;
import com.crash4j.engine.spi.instrument.bcel.generic.ConstantPoolGen;
import com.crash4j.engine.spi.instrument.bcel.generic.InstructionFactory;
import com.crash4j.engine.spi.log.Log;
import com.crash4j.engine.spi.log.LogFactory;
import com.crash4j.engine.spi.util.BCELUtils;

/**
 * This transformer os responsible for all class modifications during the class
 * resolution phase.
 * 
 * TODO Implement method replay with a limited number of reiterations.
 * TODO Implement interface instrumentation support.  i.e. java.sql.Connection
 * 
 */
public class MethodInjector implements ClassFileTransformer 
{
    protected final static Log log = LogFactory.getLog(MethodInjector.class);
	/**
	 * @see java.lang.instrument.ClassFileTransformer#transform(java.lang.ClassLoader,
	 *      java.lang.String, java.lang.Class, java.security.ProtectionDomain,
	 *      byte[])
	 */
	@Override
	public byte[] transform(ClassLoader cl, String className,
			Class<?> classBeingRedefined, ProtectionDomain pd, byte[] bytes)
			throws IllegalClassFormatException 
	{
		//log.logError("Processing "+className);
        if (className.contains("crash4j"))
        {
            return bytes;
        }
        
        ThreadContext.beginIgnore();
		try 
		{
			// get the JavaClass instance from bytes
			JavaClass pcl = BCELUtils.fromBytes(className, bytes);
						
			ClassGen cgen = new ClassGen(pcl);
			ConstantPoolGen pgen = cgen.getConstantPool();
			InstructionFactory ifc = new InstructionFactory(cgen);
			byte[] returnBytes = bytes;
			boolean touched = false;

			Method[] m = pcl.getMethods();
			
			for (Method method : m) 
			{
				String enc = BCELUtils.encodeMethodAsString(pcl.getClassName(), method);				
				ResourceSpec spec = ResourceManagerSpi.getByFullSignature(enc);
				
				if (spec == null)
				{
				    //Check interfaces and process the class in question under interface defs
				   
				   JavaClass[] ifs = Repository.getInterfaces(pcl);
				   for (JavaClass javaClass : ifs)
				   {
				       enc = BCELUtils.encodeMethodAsString(javaClass.getClassName(), method);                
				       spec = ResourceManagerSpi.getByFullSignature(enc);
				       if (spec != null)
				       {
				           break;
				       }
				   }
				   
				   //If spec is still null try super classes
				   if (spec == null)
				   {
					   JavaClass[] supercls = Repository.getSuperClasses(pcl);
					   for (JavaClass javaClass : supercls)
					   {
					       enc = BCELUtils.encodeMethodAsString(javaClass.getClassName(), method);                
					       spec = ResourceManagerSpi.getByFullSignature(enc);
					       if (spec != null)
					       {
					           break;
					       }
					   }
				   }
				   
				   if (spec != null && !spec.instrumentSubclasses())
				   {
					   spec = null;
				   }
				}
			
				
				if (spec != null) 
				{			    
				    if (!pcl.isNative())
				    {
				        BCELUtils.injectEventHandler(pcl, method, cgen, pgen, ifc, spec.getId(), false);
				    }
				    touched = true;
				}
			}
			
            if (touched)
            {
                if (!ResourceManagerSpi.getRestricted().contains(cgen.getClassName()))
                {
                    BCELUtils.addResourceTrait(pcl, cgen, pgen, ifc);
                }
                returnBytes = cgen.getJavaClass().getBytes();
            }
            else
            {
                returnBytes = bytes;
            }
            
            /*
            if (pcl.getClassName().equalsIgnoreCase("com.mysql.jdbc.Connection"))
            {
            	FileOutputStream fos = new FileOutputStream(pcl.getClassName());
            	fos.write(returnBytes);
            	fos.close();
            }
            */
			return returnBytes;
		} 
		catch (Throwable e) 
		{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(bos);
			e.printStackTrace(ps);
		    log.logError(new String(bos.toByteArray()));
		    log.logError("Transformation error", e+" "+e.getMessage());
			throw new IllegalClassFormatException("Unable to instrument");
		}
		finally
		{
			ThreadContext.endIgnore();
		}
	}	
}
