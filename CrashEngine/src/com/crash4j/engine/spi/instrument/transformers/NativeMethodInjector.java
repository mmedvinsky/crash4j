/**
 * Copyright WalletKey Inc.
 */
package com.crash4j.engine.spi.instrument.transformers;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Array;
import java.security.ProtectionDomain;
import java.util.Iterator;

import com.crash4j.engine.spi.ResourceManagerSpi;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.instrument.bcel.Constants;
import com.crash4j.engine.spi.instrument.bcel.classfile.JavaClass;
import com.crash4j.engine.spi.instrument.bcel.classfile.Method;
import com.crash4j.engine.spi.instrument.bcel.generic.ClassGen;
import com.crash4j.engine.spi.instrument.bcel.generic.ConstantPoolGen;
import com.crash4j.engine.spi.instrument.bcel.generic.Instruction;
import com.crash4j.engine.spi.instrument.bcel.generic.InstructionFactory;
import com.crash4j.engine.spi.instrument.bcel.generic.InstructionHandle;
import com.crash4j.engine.spi.instrument.bcel.generic.InstructionList;
import com.crash4j.engine.spi.instrument.bcel.generic.InvokeInstruction;
import com.crash4j.engine.spi.instrument.bcel.generic.MethodGen;
import com.crash4j.engine.spi.instrument.bcel.generic.Type;
import com.crash4j.engine.spi.log.Log;
import com.crash4j.engine.spi.log.LogFactory;
import com.crash4j.engine.spi.util.BCELUtils;

/**
 * This transformer os responsible for all class modifications during the class
 * resolution phase.
 */
public class NativeMethodInjector implements ClassFileTransformer
{
    protected final static Log log = LogFactory.getLog(NativeMethodInjector.class);
    /**
     * Default constructor
     */
    public NativeMethodInjector()
    {
    }

    /**
     * @see java.lang.instrument.ClassFileTransformer#transform(java.lang.ClassLoader,
     *      java.lang.String, java.lang.Class, java.security.ProtectionDomain,
     *      byte[])
     */
    @Override
    public byte[] transform(ClassLoader cl, String className, Class<?> classBeingRedefined, ProtectionDomain pd, byte[] bytes)
            throws IllegalClassFormatException
    {
        try
        {
            if (className.contains("crash4j"))
            {
                return bytes;
            }
            
            // get the JavaClass instance from bytes
            JavaClass pcl = BCELUtils.fromBytes(className, bytes);
            
            String[] infs = pcl.getInterfaceNames();
            for (String in : infs)
            {
                if (in.equalsIgnoreCase("com.crash4j.engine.spi.traits.NativeStub"))
                {
                    return bytes;
                }
            }
            
            ClassGen cgen = new ClassGen(pcl);
            ConstantPoolGen pgen = cgen.getConstantPool();
            InstructionFactory ifc = new InstructionFactory(cgen);

            Method[] m = pcl.getMethods();
            boolean touched = false;
            for (Method method : m)
            {
                MethodGen mGen = new MethodGen(method, className, pgen);
                InstructionList il = mGen.getInstructionList();
                if (il != null)
                {
                    Iterator<InstructionHandle> it = il.iterator();
                    while (it.hasNext())
                    {
                        InstructionHandle h = it.next();
                        Instruction ci = h.getInstruction();
                        if (ci instanceof InvokeInstruction)
                        {
                            InvokeInstruction ii = (InvokeInstruction) ci;
                            
                            String cname = ii.getClassName(pgen);
                            StringBuilder cb = new StringBuilder(cname);
                            cb.append("#").append(ii.getMethodName(pgen)).append(ii.getSignature(pgen));
                            String mSig = cb.toString();

                            ResourceSpec sp = ResourceManagerSpi.getByFullSignature(mSig);
                            if (sp == null)
                            {
                                continue;
                            }

                            if (sp.isNative())
                            {
                                // System.out.println("Found "+mSig);
                                Type[] args = ii.getArgumentTypes(pgen);
                                Type[] params = new Type[args == null ? 1 : args.length + 1];
                                for (int i = 1; i < Array.getLength(params); i++)
                                {
                                    params[i] = args[i - 1];
                                }
                                params[0] = Type.OBJECT;
                                //String clz = "com.crash4j.engine.spi.spi.instrument.nativestubs." + cname.replace('.', '_');
                                String clz = cname.replace('.', '_');
                                InvokeInstruction newI = ifc.createInvoke(clz, ii.getMethodName(pgen), ii.getReturnType(pgen), params, Constants.INVOKESTATIC);
                                h.swapInstruction(newI);
                                touched = true;
                            }
                        }
                    }
                }

                if (touched)
                {
                    il.setPositions();
                    mGen.setInstructionList(il);
                    mGen.setMaxStack();
                    mGen.setMaxLocals();
                    cgen.replaceMethod(method, mGen.getMethod());
                    il.dispose();
                }
            }

            if (touched)
            {
                return cgen.getJavaClass().getBytes();
            }

            return bytes;
        } 
        catch (Throwable e)
        {
            log.logError("Transformation error "+className, e);
            throw new IllegalClassFormatException("Unable to instrument "+className);
        }
        finally
        {                    
        }
    }
}
