/**
 * Copyright WalletKey Inc.
 */

package com.crash4j.engine.spi.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Iterator;

import com.crash4j.engine.spi.instrument.EventHandler;
import com.crash4j.engine.spi.instrument.bcel.Constants;
import com.crash4j.engine.spi.instrument.bcel.Repository;
import com.crash4j.engine.spi.instrument.bcel.classfile.ClassParser;
import com.crash4j.engine.spi.instrument.bcel.classfile.JavaClass;
import com.crash4j.engine.spi.instrument.bcel.classfile.Method;
import com.crash4j.engine.spi.instrument.bcel.generic.ArrayType;
import com.crash4j.engine.spi.instrument.bcel.generic.ClassGen;
import com.crash4j.engine.spi.instrument.bcel.generic.CodeExceptionGen;
import com.crash4j.engine.spi.instrument.bcel.generic.ConstantPoolGen;
import com.crash4j.engine.spi.instrument.bcel.generic.FieldGen;
import com.crash4j.engine.spi.instrument.bcel.generic.GOTO;
import com.crash4j.engine.spi.instrument.bcel.generic.Instruction;
import com.crash4j.engine.spi.instrument.bcel.generic.InstructionConstants;
import com.crash4j.engine.spi.instrument.bcel.generic.InstructionFactory;
import com.crash4j.engine.spi.instrument.bcel.generic.InstructionHandle;
import com.crash4j.engine.spi.instrument.bcel.generic.InstructionList;
import com.crash4j.engine.spi.instrument.bcel.generic.InstructionTargeter;
import com.crash4j.engine.spi.instrument.bcel.generic.MethodGen;
import com.crash4j.engine.spi.instrument.bcel.generic.ObjectType;
import com.crash4j.engine.spi.instrument.bcel.generic.PUSH;
import com.crash4j.engine.spi.instrument.bcel.generic.ReturnInstruction;
import com.crash4j.engine.spi.instrument.bcel.generic.TargetLostException;
import com.crash4j.engine.spi.instrument.bcel.generic.Type;
import com.crash4j.engine.spi.instrument.bcel.util.BCELifier;
import com.crash4j.engine.spi.log.Log;
import com.crash4j.engine.spi.log.LogFactory;

/**
 * Utility functions that will be used in instrumentation work on the by code
 * with Apache BCEL
 * 
 * @author Michail.Medvinsky
 * 
 */
public final class BCELUtils
{
    protected static final Log log = LogFactory.getLog(BCELUtils.class);
    /**
     * create an instance of {@link JavaClass} from raw bytes.
     * 
     * @param cname
     *            os a {@link String} name of the class to be parsed
     * @param classBytes
     *            raw bytes of the class file
     * @return {@link JavaClass} instance created from bytes
     */
    public static JavaClass fromBytes(String cname, byte[] classBytes) throws IOException
    {
        ClassParser cp = new ClassParser(new ByteArrayInputStream(classBytes), cname);
        return cp.parse();
    }

    /**
     * Encode the method of the class as a string
     * 
     * @param clp
     * @param method
     * @return
     */
    public static String encodeMethodAsString(String cname, Method method)
    {
        StringBuilder sb = new StringBuilder(cname);
        sb.append("#").append(method.getName()).append(method.getSignature());
        return sb.toString();
    }

    /**
     * trace the java class instructions to the output stream
     * 
     * @param pcl
     *            os a {@link JavaClass} instance
     * @param out
     *            os a {@link PrintStream} target
     */
    public static void trace(JavaClass pcl, PrintStream out)
    {
        Method[] methods = pcl.getMethods();
        for (Method method : methods)
        {
            MethodGen gen = new MethodGen(method, pcl.getClassName(), new ConstantPoolGen(pcl.getConstantPool()));
            InstructionList list = gen.getInstructionList();

            out.println(pcl.getClassName());
            if (gen.getInstructionList() != null)
            {
                out.print("\t");
                out.println(method.getName());
                Instruction is[] = gen.getInstructionList().getInstructions();
                for (int i = 0; i < list.size(); i++)
                {
                    StringBuilder sb = new StringBuilder();
                    sb.append("\t\t").append(is[i].getClass().getName()).append("\t").append(is[i].getName());
                    out.println(sb.toString());
                }
            }
            out.println();
        }
    }
    
    /**
     * COnvert {@link Type} to params
     * 
     * @param t
     * @return
     */
    public static Type basicOrObject(Type t)
    {
        if (t == Type.INT || t == Type.FLOAT || t == Type.BOOLEAN || t == Type.SHORT || t == Type.CHAR || t == Type.BYTE || t == Type.DOUBLE || t == Type.FLOAT
                || t == Type.LONG)
        {
            return t;
        }
        return Type.OBJECT;
    }

    /**
     * Convert {@link Type} to a name of the Type
     * 
     * @param t
     * @return
     */
    public static String getTypeName(Type t)
    {
        if (t == null)
        {
            return null;
        }
        else if (t == Type.INT)
        {
            return "INT";
        }
        else if (t == Type.FLOAT)
        {
            return "FLOAT";
        }
        else if (t == Type.BOOLEAN)
        {
            return "BOOLEAN";
        }
        else if (t == Type.SHORT)
        {
            return "SHORT";
        }
        else if (t == Type.CHAR)
        {
            return "CHAR";
        }
        else if (t == Type.BYTE)
        {
            return "BYTE";
        }
        else if (t == Type.DOUBLE)
        {
            return "DOUBLE";
        }
        else if (t == Type.LONG)
        {
            return "LONG";
        }
        else
        {
            return "OBJECT";
        }
    }

    static class _exception_rtrans_table
    {
        int start = 0;
        int end = 0;
        int handle = 0;
        ObjectType ex;
    }

    /**
     * @param list
     * @param handle
     * @return
     */
    private static int getInstructionOrdByHandle(InstructionList list, InstructionHandle handle)
    {
        Iterator it = list.iterator();
        int i = 0;
        while (it.hasNext())
        {
            InstructionHandle ih = (InstructionHandle) it.next();
            if (ih.equals(handle))
            {
                return i;
            }
            i++;
        }
        return -1;
    }

    /**
     * Layout exception table in order to restore after injection is complete
     * 
     * @param mGen
     * @return
     */
    private static _exception_rtrans_table[] layoutExceptionTable(MethodGen mGen)
    {
        CodeExceptionGen[] eGen = mGen.getExceptionHandlers();
        if (eGen == null || eGen.length == 0)
        {
            return null;
        }

        InstructionList il = mGen.getInstructionList();
        _exception_rtrans_table tt[] = null;
        if (eGen != null)
        {
            tt = new _exception_rtrans_table[eGen.length];

            for (int i = 0; i < eGen.length; i++)
            {
                tt[i] = new _exception_rtrans_table();
                tt[i].start = getInstructionOrdByHandle(il, eGen[i].getStartPC());
                tt[i].end = getInstructionOrdByHandle(il, eGen[i].getEndPC());
                tt[i].handle = getInstructionOrdByHandle(il, eGen[i].getHandlerPC());
                tt[i].ex = eGen[i].getCatchType();
            }
        }
        return tt;
    }

    /**
     * injectEventHandler injects {@link EventHandler} around the code of the target function
     * @param pcl is an instance of bcel {@link JavaClass}
     * @param method Method is a {@link Method}
     * @param cgen ClassGen is an instance of {@link ClassGen}
     * @param pgen Constant Page Management
     * @param ifc {@link InstructionFactory} to help generate java assembler
     * @param spec
     * @param nativeStub
     */
    public static void injectEventHandler(JavaClass pcl, Method method, ClassGen cgen, 
            ConstantPoolGen pgen, InstructionFactory ifc, int spec, boolean nativeStub)
    {
        try
        {
            // 1. If this os a constructor then we need to find a safe
            // place to instrument
            // We need to skip the call to super constructor.

            //Check if this is a native stub
            
            MethodGen mGen = new MethodGen(method, pcl.getClassName(), pgen);
            
            //Layout exception table and pre-record postions for future recovery
            _exception_rtrans_table[] exct = layoutExceptionTable(mGen);

            InstructionList il = new InstructionList();

            InstructionList lst = mGen.getInstructionList();

            if (lst == null)
            {
                return;
            }
            
            InstructionList orig = lst.copy();
            
            
            Type[] types = method.getArgumentTypes();

            // make sure our variables start after the original method index
            // ends
            int stackOffset = mGen.getMaxLocals();

            int objRefI = ++stackOffset;

            InstructionHandle restartPoint = il.append(InstructionConstants.ACONST_NULL);
            il.append(InstructionFactory.createStore(Type.OBJECT, objRefI));

            InstructionHandle start = il.append(ifc.createNew("java.lang.Object"));
            il.append(InstructionConstants.DUP);
            il.append(ifc.createInvoke("java.lang.Object", "<init>", Type.VOID, Type.NO_ARGS, Constants.INVOKESPECIAL));
            il.append(ifc.createInvoke("java.lang.Object", "getClass", new ObjectType("java.lang.Class"), Type.NO_ARGS, Constants.INVOKEVIRTUAL));

            int sindex = (nativeStub ? 1 : 0);
            if (types != null && (types.length - sindex) > 0)
            {
                il.append(new PUSH(pgen, types.length - sindex));
            } else
            {
                il.append(new PUSH(pgen, 0));
            }

            int arrayI = ++stackOffset;
            il.append(ifc.createInvoke("java.lang.reflect.Array", "newInstance", Type.OBJECT, new Type[]
            { new ObjectType("java.lang.Class"), Type.INT }, Constants.INVOKESTATIC));
            il.append(InstructionFactory.createStore(Type.OBJECT, arrayI));
            
            //Store the incoming parameters
            //If this is a static method start from 0 and add all
            //and if this is a instance method start from 1 and then add all
            int spoint = (method.isStatic() ? 0 : 1);
            for (int i = sindex; i < types.length - sindex; i++)
            {
                il.append(InstructionFactory.createLoad(Type.OBJECT, arrayI));
                il.append(new PUSH(pgen, i));
                il.append(InstructionFactory.createLoad(types[i], spoint+i));

                Type t = types[i];
                Type vType = BCELUtils.basicOrObject(t);
                il.append(ifc.createInvoke("com.crash4j.engine.spi.util.ArrayUtil", "set", Type.VOID, new Type[]
                { Type.OBJECT, Type.INT, vType }, Constants.INVOKESTATIC));
            }

            // Kick off controller work
            InstructionHandle ih_28 = il.append(new PUSH(pgen, spec));
            il.append(InstructionFactory.createLoad(Type.OBJECT, arrayI));
            if (!method.isStatic() || nativeStub)
            {
                il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
            } 
            else
            {
                il.append(InstructionConstants.ACONST_NULL);
            }
            
            il.append(ifc.createInvoke("com.crash4j.engine.spi.instrument.EventHandler", "begin", Type.OBJECT, new Type[]
            { Type.INT, Type.OBJECT, Type.OBJECT }, Constants.INVOKESTATIC));

            il.append(InstructionFactory.createStore(Type.OBJECT, objRefI));
            
            //BEGIN: parameter overload
            
            //leave a reference of EventData on the stack for the next op
            for (int i = sindex; i < types.length - sindex; i++)
            { 
                Type vType = BCELUtils.basicOrObject(types[i]);
                il.append(InstructionFactory.createLoad(Type.OBJECT, objRefI));
                il.append(new PUSH(pgen, i));
                il.append(InstructionFactory.createLoad(types[i], spoint+i));                
                il.append(ifc.createInvoke("com.crash4j.engine.spi.instrument.EventHandler", "handleParameter", vType, new Type[]
                        { Type.OBJECT, Type.INT, vType }, Constants.INVOKESTATIC));                
                if (types[i] instanceof ObjectType)
                {
                    il.append(ifc.createCheckCast((ObjectType)types[i]));      
                }
                else if (types[i] instanceof ArrayType)
                {
                    il.append(ifc.createCheckCast((ArrayType)types[i]));      
                }
                il.append(InstructionFactory.createStore(types[i], spoint+i));
            }
            //END: parameter overload
            
            int topOffset = il.getLength();

            // Find nd replace all returns with goto to the handler.
            InstructionHandle normalEnd = null;

            int retRefI = ++stackOffset;
            // If return type is not void then store return value in a local
            // variable.
            int saveRetVal = ++stackOffset;
            if (mGen.getReturnType() != Type.VOID)
            {
                normalEnd = il.append(InstructionFactory.createStore(mGen.getReturnType(), retRefI));
                InstructionHandle tmp = il.append(InstructionFactory.createLoad(Type.OBJECT, objRefI));
                normalEnd = (normalEnd == null ? tmp : normalEnd);

                il.append(InstructionConstants.ACONST_NULL);
                il.append(InstructionFactory.createLoad(mGen.getReturnType(), retRefI));

                il.append(ifc.createInvoke("com.crash4j.engine.spi.instrument.EventHandler", "end", BCELUtils.basicOrObject(mGen.getReturnType()), new Type[]
                { Type.OBJECT, Type.OBJECT, BCELUtils.basicOrObject(mGen.getReturnType()) }, Constants.INVOKESTATIC));

                il.append(InstructionFactory.createStore(mGen.getReturnType(), saveRetVal));
                
            } else
            {
                InstructionHandle tmp = il.append(InstructionFactory.createLoad(Type.OBJECT, objRefI));
                normalEnd = (normalEnd == null ? tmp : normalEnd);
                il.append(InstructionConstants.ACONST_NULL);
                il.append(ifc.createInvoke("com.crash4j.engine.spi.instrument.EventHandler", "end", Type.VOID, new Type[]
                { Type.OBJECT, Type.OBJECT }, Constants.INVOKESTATIC));
            }
            
            //Check here if we need to retry this while function!!! 
            //This can be used to simulate packet loss in DatagramSockets or read corruption in
            //file IO.
            //BEGIN(checking for restart action)
            
            //il.append(InstructionFactory.createLoad(Type.OBJECT, objRefI));
            //il.append(ifc.createInvoke("com.crash4j.engine.spi.instrument.EventHandler", "checkReplay", Type.BOOLEAN, new Type[]
            //        { Type.OBJECT }, Constants.INVOKESTATIC));   
            //il.append(new PUSH(pgen, true));   
            
            //brunch to the start of this method
            
            //TODO THIS NEED TO BE FIXED BRUNCHES ARE NOT WORKING.....
            //il.append(InstructionFactory.createBranchInstruction(Constants.IFEQ, restartPoint));
                        
            //END(restart handling)
            
            
            // Here we have a normal exit....
            //InstructionHandle checkReplyTarget = null;
            InstructionHandle stop = null;
            if (mGen.getReturnType() != Type.VOID)
            {
                il.append(InstructionFactory.createLoad(mGen.getReturnType(), saveRetVal));
                stop = il.append(InstructionFactory.createReturn(mGen.getReturnType()));
            }
            else
            {
                stop = il.append(InstructionFactory.createReturn(mGen.getReturnType()));
            }
            
            // Start exception block
            int ex = ++stackOffset;
            InstructionHandle finnallyBlock = il.append(InstructionFactory.createStore(Type.OBJECT, ex));
            il.append(InstructionFactory.createLoad(Type.OBJECT, objRefI));
            il.append(InstructionFactory.createLoad(Type.OBJECT, ex));
            il.append(ifc.createInvoke("com.crash4j.engine.spi.instrument.EventHandler", "end", Type.VOID, new Type[]
            { Type.OBJECT, Type.OBJECT }, Constants.INVOKESTATIC));

            il.append(InstructionFactory.createLoad(Type.OBJECT, ex));
            il.append(InstructionFactory.ATHROW);

            Iterator<InstructionHandle> it = orig.iterator();

            while (it.hasNext())
            {
                InstructionHandle hh = it.next();
                if (hh.getInstruction() instanceof ReturnInstruction)
                {
                    InstructionHandle nh = orig.insert(hh, new GOTO(normalEnd));
                    try
                    {
                        orig.delete(hh);
                    } catch (TargetLostException e)
                    {
                        InstructionHandle[] targets = e.getTargets();
                        for (int i = 0; i < targets.length; i++)
                        {
                            InstructionTargeter[] targeters = targets[i].getTargeters();
                            for (int j = 0; j < targeters.length; j++)
                            {
                                targeters[j].updateTarget(targets[i], nh);
                            }
                        }
                    }
                }
            }

            il.insert(normalEnd, orig);

            il.setPositions();

            mGen.setInstructionList(il);

            if (exct != null)
            {
                // remove all exception handlers and re-insert them in the
                // proper places.
                mGen.removeExceptionHandlers();
                // recalculate exception table
                InstructionHandle[] ih = mGen.getInstructionList().getInstructionHandles();
                for (int ie = 0; ie < exct.length; ie++)
                {
                    mGen.addExceptionHandler(ih[topOffset + exct[ie].start], ih[topOffset + exct[ie].end], ih[topOffset + exct[ie].handle], exct[ie].ex);
                }
            }

            mGen.addExceptionHandler(start, stop, finnallyBlock, null);
            mGen.setMaxStack();
            mGen.setMaxLocals();
            cgen.replaceMethod(method, mGen.getMethod());
            il.dispose();
            
        } catch (Throwable e)
        {
            log.logError("Transformation error", e);
        } 
    }
 
        
    /**
     * Add resource awareness
     * @param pcl
     * @param method
     * @param cgen
     * @param pgen
     * @param ifc
     * @param spec
     * @throws ClassNotFoundException
     */
    public static void addResourceTrait(JavaClass pcl, ClassGen cgen, ConstantPoolGen pgen, InstructionFactory ifc) 
            throws ClassNotFoundException
    {

        if (!Repository.implementationOf(pcl, "com.crash4j.engine.spi.traits.ResourceAware"))
        {
            cgen.addInterface("com.crash4j.engine.spi.traits.ResourceAware");
            FieldGen field = new FieldGen(Constants.ACC_PRIVATE, new ObjectType("com.crash4j.engine.spi.resources.ResourceSpi"), "__res_", pgen);
            FieldGen field2 = new FieldGen(Constants.ACC_PRIVATE, new ObjectType("java.lang.Object"), "__data_", pgen);
            cgen.addField(field.getField());
            cgen.addField(field2.getField());
            addGetResourceMethod(cgen, pgen, ifc);
            addSetResourceMethod(cgen, pgen, ifc);
            addGetDataMethod(cgen, pgen, ifc);
            addSetDataMethod(cgen, pgen, ifc);
        }        
    }
    
    /**
     * 
     * @param cgen
     * @param pgen
     * @param ifc
     */
    private static void addGetDataMethod(ClassGen cgen, ConstantPoolGen pgen, InstructionFactory ifc)
    {
        InstructionList il = new InstructionList();
        MethodGen method = new MethodGen(Constants.ACC_PUBLIC, new ObjectType("java.lang.Object"), Type.NO_ARGS, new String[] {}, "getData",
                cgen.getClassName(), il, pgen);
        InstructionHandle ih_0 = il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
        il.append(ifc.createFieldAccess(cgen.getClassName(), "__data_", new ObjectType("java.lang.Object"), Constants.GETFIELD));
        InstructionHandle ih_4 = il.append(InstructionFactory.createReturn(Type.OBJECT));
        method.setMaxStack();
        method.setMaxLocals();
        cgen.addMethod(method.getMethod());
        il.dispose();
    }

    /**
     * 
     * @param cgen
     * @param pgen
     * @param ifc
     */
    private static void addSetDataMethod(ClassGen cgen, ConstantPoolGen pgen, InstructionFactory ifc)
    {
        InstructionList il = new InstructionList();
        MethodGen method = new MethodGen(Constants.ACC_PUBLIC, Type.VOID, new Type[]
        { new ObjectType("java.lang.Object") }, new String[]
        { "arg0" }, "setData", cgen.getClassName(), il, pgen);

        InstructionHandle ih_0 = il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
        il.append(InstructionFactory.createLoad(Type.OBJECT, 1));
        il.append(ifc.createFieldAccess(cgen.getClassName(), "__data_", new ObjectType("java.lang.Object"), Constants.PUTFIELD));
        InstructionHandle ih_5 = il.append(InstructionFactory.createReturn(Type.VOID));
        method.setMaxStack();
        method.setMaxLocals();
        cgen.addMethod(method.getMethod());
        il.dispose();
    }
    
    
    /**
     * 
     * @param cgen
     * @param pgen
     * @param ifc
     */
    private static void addGetResourceMethod(ClassGen cgen, ConstantPoolGen pgen, InstructionFactory ifc)
    {
        InstructionList il = new InstructionList();
        MethodGen method = new MethodGen(Constants.ACC_PUBLIC, new ObjectType("com.crash4j.engine.spi.resources.ResourceSpi"), Type.NO_ARGS, new String[] {}, "getResource",
                cgen.getClassName(), il, pgen);
        InstructionHandle ih_0 = il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
        il.append(ifc.createFieldAccess(cgen.getClassName(), "__res_", new ObjectType("com.crash4j.engine.spi.resources.ResourceSpi"), Constants.GETFIELD));
        InstructionHandle ih_4 = il.append(InstructionFactory.createReturn(Type.OBJECT));
        method.setMaxStack();
        method.setMaxLocals();
        cgen.addMethod(method.getMethod());
        il.dispose();
    }

    /**
     * 
     * @param cgen
     * @param pgen
     * @param ifc
     */
    private static void addSetResourceMethod(ClassGen cgen, ConstantPoolGen pgen, InstructionFactory ifc)
    {
        InstructionList il = new InstructionList();
        MethodGen method = new MethodGen(Constants.ACC_PUBLIC, Type.VOID, new Type[]
        { new ObjectType("com.crash4j.engine.spi.resources.ResourceSpi") }, new String[]
        { "arg0" }, "setResource", cgen.getClassName(), il, pgen);

        InstructionHandle ih_0 = il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
        il.append(InstructionFactory.createLoad(Type.OBJECT, 1));
        il.append(ifc.createFieldAccess(cgen.getClassName(), "__res_", new ObjectType("com.crash4j.engine.spi.resources.ResourceSpi"), Constants.PUTFIELD));
        InstructionHandle ih_5 = il.append(InstructionFactory.createReturn(Type.VOID));
        method.setMaxStack();
        method.setMaxLocals();
        cgen.addMethod(method.getMethod());
        il.dispose();
    }

    /**
     * Take a class names and list of signatures and generate a native stab that
     * can be used to instrument native method call inline.
     * 
     * @param cl
     * @param sigs
     * @return
     * @throws ClassNotFoundException
     * @throws IOException 
     */
    static class _method_id_pair
    {
        String sig;
        String method;
        int id;
    }
    
    /**
     * Generate native stub from class information
     * @param cl
     * @param sigs
     * @param newClass
     * @param superType
     * @param spec
     * @return
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static byte[] generateNativeStub(String cl, String sigs[], String newClass, String superType, int spec[]) throws ClassNotFoundException, IOException
    {
        HashSet<_method_id_pair> mlist = new HashSet<_method_id_pair>();
        for (int i = 0; i < sigs.length; i++)
        {
            _method_id_pair pair = new _method_id_pair();
            String rawSig[] = sigs[i].split("[(]");
            pair.id = spec[i];
            pair.sig = "("+rawSig[1];
            pair.method = rawSig[0];
            mlist.add(pair);
        }
        
        // Generate new class
        String fname = "/"+newClass.replace('.', '/')+".class";
        ClassGen cgen = new ClassGen(newClass, superType, fname, Constants.ACC_PUBLIC, new String[]{"com.crash4j.engine.spi.traits.NativeStub"});
        ConstantPoolGen pgen = cgen.getConstantPool();
        InstructionFactory ifc = new InstructionFactory(cgen);
        
        // Create default constructor
        InstructionList il = new InstructionList();
        MethodGen method = new MethodGen(Constants.ACC_STATIC, Type.VOID, Type.NO_ARGS, new String[] {}, "<clinit>", newClass, il, pgen);
        il.append(InstructionFactory.createReturn(Type.VOID));
        method.setMaxStack();
        method.setMaxLocals();
        cgen.addMethod(method.getMethod());
        il.dispose();
        
        // Set super class and remove interfaces
        for (_method_id_pair mpi : mlist)
        {
            il = new InstructionList();
            // Create method gen with the same semantic as a source method
            Type[] itypes = Type.getArgumentTypes(mpi.sig);
            Type[] types = new Type[itypes == null ? 1 : itypes.length + 1];
            System.arraycopy(itypes, 0, types, 1, itypes.length);
            types[0] = Type.OBJECT;
            
            String args[] = new String[types.length];
            for (int i = 0; i < args.length; i++)
            {
                args[i] = "arg"+i;
            }
            
            Type rtype = Type.getReturnType(mpi.sig);
            
            MethodGen mgen = new MethodGen(Constants.ACC_PUBLIC | Constants.ACC_STATIC, rtype, types, args, mpi.method, newClass, il, pgen);

            il.append(InstructionFactory.createLoad(types[0], 0));
            il.append(ifc.createCheckCast(new ObjectType(cl)));
            for (int i = 1; i < types.length; i++)
            {
                il.append(InstructionFactory.createLoad(types[i], i));
            }
            
            il.append(ifc.createInvoke(cl, mpi.method, rtype, itypes, Constants.INVOKESPECIAL));
            il.append(InstructionFactory.createReturn(mgen.getReturnType()));
            
            mgen.setMaxStack();
            mgen.setMaxLocals();
            Method newm = mgen.getMethod();
            cgen.addMethod(mgen.getMethod());

            injectEventHandler(cgen.getJavaClass(), newm, cgen, pgen, ifc, mpi.id, true);        
        
        }
        return cgen.getJavaClass().getBytes();
    }

    /**
     * @param cl
     * @throws IOException 
     */
    public static void dumpInstructions(String name, byte[] cl, PrintStream out) throws IOException
    {
        BCELifier b = new BCELifier(fromBytes(name, cl), out);
        b.start();
    }
    
    /**
     * Visit every instruction in the list and replace all return instructions
     * with store and goto instructions to finish final processing
     * 
     * @param gotoPoint
     */
    public static InstructionList overloadReturns(InstructionHandle gotoPoint, InstructionList iL) throws TargetLostException
    {
        Instruction[] insts = iL.getInstructions();
        for (int i = 0; i < iL.getLength(); i++)
        {
            if (insts[i] instanceof ReturnInstruction)
            {
                iL.insert(insts[i], new GOTO(gotoPoint));
                iL.delete(insts[i]);
            }
        }
        return new InstructionList();
    }
}
