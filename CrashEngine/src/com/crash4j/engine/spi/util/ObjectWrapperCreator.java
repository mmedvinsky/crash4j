/**
 * 
 */
package com.crash4j.engine.spi.util;

import java.io.IOException;
import java.io.OutputStream;

import com.crash4j.engine.spi.instrument.bcel.Constants;
import com.crash4j.engine.spi.instrument.bcel.generic.BranchInstruction;
import com.crash4j.engine.spi.instrument.bcel.generic.ClassGen;
import com.crash4j.engine.spi.instrument.bcel.generic.ConstantPoolGen;
import com.crash4j.engine.spi.instrument.bcel.generic.FieldGen;
import com.crash4j.engine.spi.instrument.bcel.generic.InstructionConstants;
import com.crash4j.engine.spi.instrument.bcel.generic.InstructionFactory;
import com.crash4j.engine.spi.instrument.bcel.generic.InstructionHandle;
import com.crash4j.engine.spi.instrument.bcel.generic.InstructionList;
import com.crash4j.engine.spi.instrument.bcel.generic.MethodGen;
import com.crash4j.engine.spi.instrument.bcel.generic.ObjectType;
import com.crash4j.engine.spi.instrument.bcel.generic.PUSH;
import com.crash4j.engine.spi.instrument.bcel.generic.Type;

/**
 * Create {@link Object} wrapper that uses <code>invokespecial</code> to get real references
 * and equal operation from the actual {@link Object}
 */
public class ObjectWrapperCreator implements Constants
{
    private InstructionFactory _factory;
    private ConstantPoolGen _cp;
    private ClassGen _cg;

    /**
     * Default constractor
     */
    public ObjectWrapperCreator()
    {
        _cg = new ClassGen("com_crash4j_engine_ObjectWrapper", "java.lang.Object", "com_crash4j_engine_ObjectWrapper.java", ACC_PUBLIC | ACC_SUPER, new String[] {});

        _cp = _cg.getConstantPool();
        _factory = new InstructionFactory(_cg, _cp);
    }

    /**
     * Create the object and send the data to the {@link OutputStream}
     * @param out
     * @throws IOException
     */
    public String create(OutputStream out) throws IOException
    {
        createFields();
        createInitializationConstructor();
        createEqualsOverwrite();
        createHashCodeOverwrite();
        createToStringOverwrite();
        createStaticGetHashCodeOverwrite();
        _cg.getJavaClass().dump(out);
        return "com_crash4j_engine_ObjectWrapper";
    }

    /**
     * Create member fields (WeakReference)
     */
    private void createFields()
    {
        FieldGen field;

        field = new FieldGen(ACC_PROTECTED, new ObjectType("java.lang.ref.WeakReference"), "ref", _cp);
        _cg.addField(field.getField());
    }

    /**
     * Constructor....
     */
    private void createInitializationConstructor()
    {
        InstructionList il = new InstructionList();
        MethodGen method = new MethodGen(ACC_PUBLIC, Type.VOID, new Type[]
        { Type.OBJECT }, new String[]
        { "arg0" }, "<init>", "com_crash4j_engine_ObjectWrapper", il, _cp);

        il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
        il.append(_factory.createInvoke("java.lang.Object", "<init>", Type.VOID, Type.NO_ARGS, Constants.INVOKESPECIAL));
        il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
        il.append(InstructionConstants.ACONST_NULL);
        il.append(_factory.createFieldAccess("com_crash4j_engine_ObjectWrapper", "ref", new ObjectType("java.lang.ref.WeakReference"), Constants.PUTFIELD));
        il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
        il.append(_factory.createNew("java.lang.ref.WeakReference"));
        il.append(InstructionConstants.DUP);
        il.append(InstructionFactory.createLoad(Type.OBJECT, 1));
        il.append(_factory.createInvoke("java.lang.ref.WeakReference", "<init>", Type.VOID, new Type[]
        { Type.OBJECT }, Constants.INVOKESPECIAL));
        il.append(_factory.createFieldAccess("com_crash4j_engine_ObjectWrapper", "ref", new ObjectType("java.lang.ref.WeakReference"), Constants.PUTFIELD));
        il.append(InstructionFactory.createReturn(Type.VOID));
        method.setMaxStack();
        method.setMaxLocals();
        _cg.addMethod(method.getMethod());
        il.dispose();
    }

    /**
     * Overload {@link Object#equals(Object)} with INVOKESPECIAL directive
     */
    private void createEqualsOverwrite()
    {
        InstructionList il = new InstructionList();
        MethodGen method = new MethodGen(ACC_PUBLIC, Type.BOOLEAN, new Type[]
        { Type.OBJECT }, new String[]
        { "arg0" }, "equals", "com_crash4j_engine_ObjectWrapper", il, _cp);

        il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
        il.append(_factory.createFieldAccess("com_crash4j_engine_ObjectWrapper", "ref", new ObjectType("java.lang.ref.WeakReference"), Constants.GETFIELD));
        il.append(_factory.createInvoke("java.lang.ref.WeakReference", "get", Type.OBJECT, Type.NO_ARGS, Constants.INVOKEVIRTUAL));
        il.append(InstructionFactory.createStore(Type.OBJECT, 2));
        il.append(InstructionFactory.createLoad(Type.OBJECT, 2));
        BranchInstruction ifnonnull_9 = InstructionFactory.createBranchInstruction(Constants.IFNONNULL, null);
        il.append(ifnonnull_9);
        il.append(new PUSH(_cp, 0));
        il.append(InstructionFactory.createReturn(Type.INT));
        InstructionHandle ih_14 = il.append(InstructionFactory.createLoad(Type.OBJECT, 2));
        il.append(InstructionFactory.createLoad(Type.OBJECT, 1));
        il.append(_factory.createInvoke("java.lang.Object", "equals", Type.BOOLEAN, new Type[]
        { Type.OBJECT }, Constants.INVOKESPECIAL));
        il.append(InstructionFactory.createReturn(Type.INT));
        ifnonnull_9.setTarget(ih_14);
        method.setMaxStack();
        method.setMaxLocals();
        _cg.addMethod(method.getMethod());
        il.dispose();
    }

    
    /**
     * Overwrite {@link Object#hashCode()} with INVOKESPECIAL directive
     */
    private void createStaticGetHashCodeOverwrite()
    {
        InstructionList il = new InstructionList();
        MethodGen method = new MethodGen(ACC_PUBLIC | ACC_STATIC, Type.INT, new Type[]
                { Type.OBJECT }, new String[] {"o"}, "getBaseHashCode", "com_crash4j_engine_ObjectWrapper", il, _cp);

        il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
        il.append(_factory.createInvoke("java.lang.Object", "hashCode", Type.INT, Type.NO_ARGS, Constants.INVOKESPECIAL));
        il.append(InstructionFactory.createReturn(Type.INT));
        method.setMaxStack();
        method.setMaxLocals();
        _cg.addMethod(method.getMethod());
        il.dispose();
    }

    
    
    /**
     * Overwrite {@link Object#hashCode()} with INVOKESPECIAL directive
     */
    private void createHashCodeOverwrite()
    {
        InstructionList il = new InstructionList();
        MethodGen method = new MethodGen(ACC_PUBLIC, Type.INT, Type.NO_ARGS, new String[] {}, "hashCode", "com_crash4j_engine_ObjectWrapper", il, _cp);

        il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
        il.append(_factory.createFieldAccess("com_crash4j_engine_ObjectWrapper", "ref", new ObjectType("java.lang.ref.WeakReference"), Constants.GETFIELD));
        il.append(_factory.createInvoke("java.lang.ref.WeakReference", "get", Type.OBJECT, Type.NO_ARGS, Constants.INVOKEVIRTUAL));
        il.append(InstructionFactory.createStore(Type.OBJECT, 1));
        il.append(InstructionFactory.createLoad(Type.OBJECT, 1));
        BranchInstruction ifnonnull_9 = InstructionFactory.createBranchInstruction(Constants.IFNONNULL, null);
        il.append(ifnonnull_9);
        il.append(new PUSH(_cp, 0));
        il.append(InstructionFactory.createReturn(Type.INT));
        InstructionHandle ih_14 = il.append(InstructionFactory.createLoad(Type.OBJECT, 1));
        il.append(_factory.createInvoke("java.lang.Object", "hashCode", Type.INT, Type.NO_ARGS, Constants.INVOKESPECIAL));
        il.append(InstructionFactory.createReturn(Type.INT));
        ifnonnull_9.setTarget(ih_14);
        method.setMaxStack();
        method.setMaxLocals();
        _cg.addMethod(method.getMethod());
        il.dispose();
    }

    /**
     * Overload {@link Object#toString()} 
     */
    private void createToStringOverwrite()
    {
        InstructionList il = new InstructionList();
        MethodGen method = new MethodGen(ACC_PUBLIC, Type.STRING, Type.NO_ARGS, new String[] {}, "toString", "com_crash4j_engine_ObjectWrapper", il, _cp);

        il.append(InstructionFactory.createLoad(Type.OBJECT, 0));
        il.append(_factory.createFieldAccess("com_crash4j_engine_ObjectWrapper", "ref", new ObjectType("java.lang.ref.WeakReference"), Constants.GETFIELD));
        il.append(_factory.createInvoke("java.lang.ref.WeakReference", "get", Type.OBJECT, Type.NO_ARGS, Constants.INVOKEVIRTUAL));
        il.append(InstructionFactory.createStore(Type.OBJECT, 1));
        il.append(InstructionFactory.createLoad(Type.OBJECT, 1));
        BranchInstruction ifnonnull_9 = InstructionFactory.createBranchInstruction(Constants.IFNONNULL, null);
        il.append(ifnonnull_9);
        il.append(new PUSH(_cp, ""));
        il.append(InstructionFactory.createReturn(Type.OBJECT));
        InstructionHandle ih_15 = il.append(InstructionFactory.createLoad(Type.OBJECT, 1));
        il.append(_factory.createInvoke("java.lang.Object", "toString", Type.STRING, Type.NO_ARGS, Constants.INVOKESPECIAL));
        il.append(InstructionFactory.createReturn(Type.OBJECT));
        ifnonnull_9.setTarget(ih_15);
        method.setMaxStack();
        method.setMaxLocals();
        _cg.addMethod(method.getMethod());
        il.dispose();
    }
}
