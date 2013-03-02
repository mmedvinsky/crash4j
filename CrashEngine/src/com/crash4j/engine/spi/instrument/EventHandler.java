/**
 * 
 */
package com.crash4j.engine.spi.instrument;

import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentHashMap;

import com.crash4j.engine.UnknownResourceException;
import com.crash4j.engine.sim.SimulationException;
import com.crash4j.engine.spi.ResourceManagerSpi;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.context.ContextFrame;
import com.crash4j.engine.spi.context.ThreadContext;
import com.crash4j.engine.spi.log.Log;
import com.crash4j.engine.spi.log.LogFactory;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.traits.FacadeBuilder;
import com.crash4j.engine.spi.traits.ResourceAware;
import com.crash4j.engine.spi.util.ArrayUtil;
import com.crash4j.engine.types.ResourceTypes;


/**
 * {@link EventHandler} os responsible for receiving and processing the function events and corresponding faults that
 * need to be executed with them.
 *
 */
public class EventHandler 
{ 
    protected static Log log = LogFactory.getLog(EventHandler.class);
    /**
     * Handle parameter.
     * @param i index of the parameter into the function
     * @param cv
     * @return
     */
    public static Object handleParameter(Object ref, int i, Object cv)
    {
        EventData o = (EventData)ref;
        Object po = o.getParamsOverwrite();
        if (po == null)
        {
            return cv;
        }
        return ArrayUtil.get(po, i);
    }
    
    
    /**
     * @see EventHandler#handleParameter(Object, int, Object)
     */
    public static int handleParameter(Object ref, int i, int cv)
    {
        EventData o = (EventData)ref;
        Object po = o.getParamsOverwrite();
        if (po == null)
        {
            return cv;
        }
        return ArrayUtil.getInt(po, i);
    }
    /**
     * @see EventHandler#handleParameter(Object, int, Object)
     */
    public static long handleParameter(Object ref, int i, long cv)
    {
        EventData o = (EventData)ref;
        Object po = o.getParamsOverwrite();
        if (po == null)
        {
            return cv;
        }
        return ArrayUtil.getLong(po, i);
    }
    /**
     * @see EventHandler#handleParameter(Object, int, Object)
     */
    public static float handleParameter(Object ref, int i, float cv)
    {
        EventData o = (EventData)ref;
        Object po = o.getParamsOverwrite();
        if (po == null)
        {
            return cv;
        }
        return ArrayUtil.getFloat(po, i);
    }
    /**
     * @see EventHandler#handleParameter(Object, int, Object)
     */
    public static double handleParameter(Object ref, int i, double cv)
    {
        EventData o = (EventData)ref;
        Object po = o.getParamsOverwrite();
        if (po == null)
        {
            return cv;
        }
        return ArrayUtil.getDouble(po, i);
    }
    /**
     * @see EventHandler#handleParameter(Object, int, Object)
     */
    public static char handleParameter(Object ref, int i, char cv)
    {
        EventData o = (EventData)ref;
        Object po = o.getParamsOverwrite();
        if (po == null)
        {
            return cv;
        }
        return ArrayUtil.getChar(po, i);
    }
    /**
     * @see EventHandler#handleParameter(Object, int, Object)
     */
    public static byte handleParameter(Object ref, int i, byte cv)
    {
        EventData o = (EventData)ref;
        Object po = o.getParamsOverwrite();
        if (po == null)
        {
            return cv;
        }
        return ArrayUtil.getByte(po, i);
    }
    
    /**
     * @see EventHandler#handleParameter(Object, int, Object)
     */
    public static boolean handleParameter(Object ref, int i, boolean cv)
    {
        EventData o = (EventData)ref;
        Object po = o.getParamsOverwrite();
        if (po == null)
        {
            return cv;
        }
        return ArrayUtil.getBoolean(po, i);
    }
    
    /**
     * Check the setting to see if we need to replay this method
     */
    public static boolean checkReplay(Object ref)
    {
        EventData ev = (EventData)ref;
        
        boolean rv = (ev.getActions() & 4) == 4;
        return rv;
    }
    
    
	/**
	 * Starts the event as the function is entered.
	 * <p/>  At this point we will try to discover the resource associated with this call, but might have to defer to the {@link EventHandler#end(Object, Object)}
	 * @param integer id of the specification for this event. See {@link ResourceSpec}
	 * @param params list of parameters, as they are passed in into the function that is being controlled by this event
	 * @param instance the actual instance of the object a null if the object is static
	 * @return opaque {@link Object} that holds the reference to the internal data structure - {@link EventData}
	 */
	public static Object begin(int id, Object params, Object instance) throws Throwable
	{
        //discover specification
        ResourceSpec spec = ResourceManagerSpi.getSpecById(id);
    	return begin(spec, params, instance);
	}    
	
	/**
	 * Starts the event as the function is entered.
	 * <p/>  At this point we will try to discover the resource associated with this call, but might have to defer to the {@link EventHandler#end(Object, Object)}
	 * @param integer id of the specification for this event. See {@link ResourceSpec}
	 * @param params list of parameters, as they are passed in into the function that is being controlled by this event
	 * @param instance the actual instance of the object a null if the object is static
	 * @return opaque {@link Object} that holds the reference to the internal data structure - {@link EventData}
	 */
	public static Object begin(ResourceSpec spec, Object params, Object instance) throws Throwable
	{
        EventData o = new EventData();

        //ignore and do nothing.  If the context is set to ignore children then
        //an above controller does not want any additional discoveries to take place.
        if (ThreadContext.shouldIgnore())
        {
            o.setIgnored(true);
            return o;
        }                
        
        //Now check if the parent accepts mask allows for this resource to be discovered and mamanged
        ContextFrame frame = ThreadContext.peek();
        if (frame != null)
        {
        	int mask = spec.getResourceType().toMask();
        	if ((frame.getResourceSpec().getAccepts() & mask)  != mask)
        	{
                o.setIgnored(true);
                return o;
        	}
        }
        
        ResourceSpi res = null;
        //While we are discovering the resource information and applying the behavior changes 
        // we don't want any more events.
        ThreadContext.beginIgnore();
        try
        {
        	//Prepare {@link EventData}
            o.setInstance(instance);
            o.setSpec(spec);
            o.setParams(params);

            //Initiate resource discovery
            res = resolveResource(o, null, null);
            o.setResource(res);
            if (res != null)
            {
            	//Raise onEntry event with the ResourceManagerSpi to perform various tasks that has to take place at this point
                ResourceManagerSpi.onEntry(o, res);
            }
            //Record start time of the actual method to be executed after this change.
            o.setStartTime(System.nanoTime());
        }
        catch (Throwable e)
        {
           log.logTrace("Unexcpected Exception"+e.toString()+" "+e.getClass().getName());
        }
        finally
        {
        	//We are done - stop ignoring the event discovery
            ThreadContext.endIgnore();
            //Stack up the current context 
            ThreadContext.push(ThreadContext.createFrame(spec, res));
            //If this specification does not want any children accepted just ignore everything.
            if (spec.getAccepts() == 0)
            {
                ThreadContext.beginIgnore();
            }
        }
        o.raiseOutgoingException();
        return o;
	}

	/**
	 * @return {@link ResourceSpi} instance after it ha being resolved via available information 
	 */
	
	private static ResourceSpi resolveResource(EventData o, Object exec, Object rv)
	{
        ResourceSpi res = null;
        try
        {
            ResourceSpec spec = o.getSpec();
            res = o.getResource();
            if (res != null)
            {
                ResourceManagerSpi.completeResource(o, rv);
                res = o.getResource();
            }
            else
            {
                res = ResourceManagerSpi.resolveResource(o, rv);   
            }
            

            if (rv instanceof ResourceAware)
            {
                ((ResourceAware)rv).setResource(res);
            }
            
            if (res != null)
            {
                ResourceManagerSpi.assignSimulationForResource(res);
            }
        } 
        catch (UnknownResourceException e)
        {
            res = null;
        } catch (URISyntaxException e)
        {
            res = null;
        }
        return res;
	}
	
    
    /**
     * @see EventHandler#end();
     */
    public static Object end(Object ref, Object exec, Object rv) throws Throwable
    {
        //If the spec wants to ignore all children push another ignore frame on the stack
        EventData o = (EventData)ref;
        
        
        //Here we need to check of this was a simulated exception - if yes then we need to get
        //EvenetData instance from the exception cause.
        if (o == null)
        {
	        if (exec instanceof Throwable)
	        {
	        	Throwable t = (Throwable)exec;
	        	Throwable cause = t.getCause();
	        	if (cause instanceof SimulationException)
	        	{
	        		SimulationException sime= (SimulationException)cause;
	        		o = sime.getEventData();
	        	}
	        }
        }
        
        
        if (o.isIgnored())
        {
            return rv;
        }
        
        if (o.isOutgoingExceptionRaised())
        {
        	o.setOutgoingException(null);
        	o.setOutgoingExceptionRaised(false);
        }

        o.setStopTime(System.nanoTime());
        
        ResourceSpi res = null;
        
        try
        {
            ThreadContext.beginIgnore();
            try 
            {
                res = resolveResource(o, exec, rv);
                //Check if delegation os required and apply it.
                FacadeBuilder fb = null;
                ResourceSpec spec = o.getSpec();
                
                if (spec.getHandler() instanceof FacadeBuilder)
                {
                    fb = (FacadeBuilder)spec.getHandler();
                    rv = fb.createFacade(spec, res, o.getParams(), o.getInstance(), rv, o.getClosure()); 
                }
                else
                {
                    if (res != null)
                    {
                        rv = ResourceManagerSpi.onExit(o, exec, res, rv);
                    }
                }
            }
            finally
            {
                ThreadContext.endIgnore();
            }
        }
        finally
        {
            ThreadContext.endIgnore();
            ThreadContext.pop();
            ContextFrame parent = ThreadContext.peek();
            if (res != null && parent != null)
            {
            	res.setParent(parent.getResource());
            }
        }
        o.raiseOutgoingException();
        return rv;
    }
    
    /**
     * ends the event as the function was entered and stops it as it was exited
     * @param className name of the class that initiated the event
     * @param methodName name of the method in the class
     * @param params list of parameters, as they are passed in into the event
     * @param instance the actual instance of the object a null if the object os static
     * @return opaque {@link Object} that holds the reference to the internal data
     */
    public static void end(Object ref, Object exec) throws Throwable
    {
        //If the spec wants to ignore all children push another ignore frame on the stack
        EventData o = (EventData)ref;
        
        //Here we need to check of this was a simulated exception - if yes then we need to get
        //EvenetData instance from the exception cause.
        if (o == null)
        {
	        if (exec instanceof Throwable)
	        {
	        	Throwable t = (Throwable)exec;
	        	Throwable cause = t.getCause();
	        	if (cause instanceof SimulationException)
	        	{
	        		SimulationException sime= (SimulationException)cause;
	        		o = sime.getEventData();
	        	}
	        }
        }
        
        if (o.isIgnored())
        {
            return;
        }

        if (o.isOutgoingExceptionRaised())
        {
        	o.setOutgoingException(null);
        	o.setOutgoingExceptionRaised(false);
        }
        
        o.setStopTime(System.nanoTime());
        ResourceSpi res = null;
        
        ThreadContext.beginIgnore();
        try
        {
            res = resolveResource(o, exec, null);

            if (res != null)
            {
                ResourceManagerSpi.onExit(o, exec, res, null);
            }
        }
        finally
        {
            ThreadContext.endIgnore();
            ThreadContext.pop();
            ContextFrame parent = ThreadContext.peek();
            if (res != null && parent != null)
            {
            	res.setParent(parent.getResource());
            }
        }
        o.raiseOutgoingException();
    }


    /**
     * @see EventHandler#end();
     */
    public static int end(Object ref, Object exec, int rv)  throws Throwable
    {
        EventData o = (EventData)ref;
        //Here we need to check of this was a simulated exception - if yes then we need to get
        //EvenetData instance from the exception cause.
        if (o == null)
        {
	        if (exec instanceof Throwable)
	        {
	        	Throwable t = (Throwable)exec;
	        	Throwable cause = t.getCause();
	        	if (cause instanceof SimulationException)
	        	{
	        		SimulationException sime= (SimulationException)cause;
	        		o = sime.getEventData();
	        	}
	        }
        }
        if (o.isIgnored())
        {
            return rv;
        }
        
        ResourceSpi res = null;
        if (o.isOutgoingExceptionRaised())
        {
        	o.setOutgoingException(null);
        	o.setOutgoingExceptionRaised(false);
        }
        o.setStopTime(System.nanoTime());
        
        ThreadContext.beginIgnore();
        try
        {
            res = resolveResource(o, exec, rv);
            if (res != null)
            {
                rv = ResourceManagerSpi.onExit(o, exec, res, rv);
            }
        }
        finally
        {
            ThreadContext.endIgnore();
            ThreadContext.pop();
            ContextFrame parent = ThreadContext.peek();
            if (res != null && parent != null)
            {
            	res.setParent(parent.getResource());
            }
        }
        o.raiseOutgoingException();
        return rv;
    }
    
    /**
     * @see EventHandler#end();
     */
    public static long end(Object ref, Object exec, long rv)  throws Throwable
    {
        EventData o = (EventData)ref;
        //Here we need to check of this was a simulated exception - if yes then we need to get
        //EvenetData instance from the exception cause.
        if (o == null)
        {
	        if (exec instanceof Throwable)
	        {
	        	Throwable t = (Throwable)exec;
	        	Throwable cause = t.getCause();
	        	if (cause instanceof SimulationException)
	        	{
	        		SimulationException sime= (SimulationException)cause;
	        		o = sime.getEventData();
	        	}
	        }
        }
        if (o.isIgnored())
        {
            return rv;
        }
        if (o.isOutgoingExceptionRaised())
        {
        	o.setOutgoingException(null);
        	o.setOutgoingExceptionRaised(false);
        }
        o.setStopTime(System.nanoTime());
        
        ResourceSpi res = null;
        ThreadContext.beginIgnore();
        try
        {
            res = resolveResource(o, exec, rv);
            if (res != null)
            {
                rv = ResourceManagerSpi.onExit(o, exec, res, rv);
            }
        }
        finally
        {
            ThreadContext.endIgnore();
            ThreadContext.pop();
            ContextFrame parent = ThreadContext.peek();
            if (res != null && parent != null)
            {
            	res.setParent(parent.getResource());
            }
        }
        o.raiseOutgoingException();
        return rv;
    }
    
    /**
     * @see EventHandler#end();
     */
    public static short end(Object ref, Object exec, short rv)  throws Throwable
    {
        EventData o = (EventData)ref;
        //Here we need to check of this was a simulated exception - if yes then we need to get
        //EvenetData instance from the exception cause.
        if (o == null)
        {
	        if (exec instanceof Throwable)
	        {
	        	Throwable t = (Throwable)exec;
	        	Throwable cause = t.getCause();
	        	if (cause instanceof SimulationException)
	        	{
	        		SimulationException sime= (SimulationException)cause;
	        		o = sime.getEventData();
	        	}
	        }
        }
        if (o.isIgnored())
        {
            return rv;
        }
        if (o.isOutgoingExceptionRaised())
        {
        	o.setOutgoingException(null);
        	o.setOutgoingExceptionRaised(false);
        }
        o.setStopTime(System.nanoTime());
        ResourceSpi res = null;
        
        ThreadContext.beginIgnore();
        try
        {
            res = resolveResource(o, exec, rv);
            if (res != null)
            {
                rv = ResourceManagerSpi.onExit(o, exec, res, rv);
            }
        }
        finally
        {
            ThreadContext.endIgnore();
            ThreadContext.pop();
            ContextFrame parent = ThreadContext.peek();
            if (res != null && parent != null)
            {
            	res.setParent(parent.getResource());
            }
        }
        o.raiseOutgoingException();
        return rv;
    }
    
    /**
     * @see EventHandler#end();
     */
    public static boolean end(Object ref, Object exec, boolean rv)  throws Throwable
    {
        EventData o = (EventData)ref;
        //Here we need to check of this was a simulated exception - if yes then we need to get
        //EvenetData instance from the exception cause.
        if (o == null)
        {
	        if (exec instanceof Throwable)
	        {
	        	Throwable t = (Throwable)exec;
	        	Throwable cause = t.getCause();
	        	if (cause instanceof SimulationException)
	        	{
	        		SimulationException sime= (SimulationException)cause;
	        		o = sime.getEventData();
	        	}
	        }
        }
        if (o.isIgnored())
        {
            return rv;
        }
        if (o.isOutgoingExceptionRaised())
        {
        	o.setOutgoingException(null);
        	o.setOutgoingExceptionRaised(false);
        }
        o.setStopTime(System.nanoTime());
        
        ResourceSpi res = null;
        ThreadContext.beginIgnore();
        try
        {
            res = resolveResource(o, exec, rv);
            if (res != null)
            {
                rv = ResourceManagerSpi.onExit(o, exec, res, rv);
            }
        }
        finally
        {
            ThreadContext.endIgnore();
            ThreadContext.pop();
            ContextFrame parent = ThreadContext.peek();
            if (res != null && parent != null)
            {
            	res.setParent(parent.getResource());
            }
        }
        o.raiseOutgoingException();
        return rv;
    }
    
    /**
     * @see EventHandler#end();
     */
    public static byte end(Object ref, Object exec, byte rv)  throws Throwable
    {
        EventData o = (EventData)ref;
        //Here we need to check of this was a simulated exception - if yes then we need to get
        //EvenetData instance from the exception cause.
        if (o == null)
        {
	        if (exec instanceof Throwable)
	        {
	        	Throwable t = (Throwable)exec;
	        	Throwable cause = t.getCause();
	        	if (cause instanceof SimulationException)
	        	{
	        		SimulationException sime= (SimulationException)cause;
	        		o = sime.getEventData();
	        	}
	        }
        }
        if (o.isIgnored())
        {
            return rv;
        }
        if (o.isOutgoingExceptionRaised())
        {
        	o.setOutgoingException(null);
        	o.setOutgoingExceptionRaised(false);
        }
        o.setStopTime(System.nanoTime());
        
        ResourceSpi res = null;
        ThreadContext.beginIgnore();
        try
        {
            res = resolveResource(o, exec, rv);
            if (res != null)
            {
                rv = ResourceManagerSpi.onExit(o, exec, res, rv);
            }
        }
        finally
        {
            ThreadContext.endIgnore();
            ThreadContext.pop();
            ContextFrame parent = ThreadContext.peek();
            if (res != null && parent != null)
            {
            	res.setParent(parent.getResource());
            }
        }
        o.raiseOutgoingException();
        return rv;
    }
    
    /**
     * @see EventHandler#end();
     */
    public static char end(Object ref, Object exec, char rv)  throws Throwable
    {
        EventData o = (EventData)ref;
        //Here we need to check of this was a simulated exception - if yes then we need to get
        //EvenetData instance from the exception cause.
        if (o == null)
        {
	        if (exec instanceof Throwable)
	        {
	        	Throwable t = (Throwable)exec;
	        	Throwable cause = t.getCause();
	        	if (cause instanceof SimulationException)
	        	{
	        		SimulationException sime= (SimulationException)cause;
	        		o = sime.getEventData();
	        	}
	        }
        }
        if (o.isIgnored())
        {
            return rv;
        }
        if (o.isOutgoingExceptionRaised())
        {
        	o.setOutgoingException(null);
        	o.setOutgoingExceptionRaised(false);
        }
        o.setStopTime(System.nanoTime());
        
        ResourceSpi res = null;
        ThreadContext.beginIgnore();
        try
        {
            res = resolveResource(o, exec, rv);
            if (res != null)
            {
                rv = ResourceManagerSpi.onExit(o, exec, res, rv);
            }
        }
        finally
        {
            ThreadContext.endIgnore();
            ThreadContext.pop();
            ContextFrame parent = ThreadContext.peek();
            if (res != null && parent != null)
            {
            	res.setParent(parent.getResource());
            }
        }
        o.raiseOutgoingException();
        return rv;
    }
    
    /**
     * @see EventHandler#end();
     */
    public static float end(Object ref, Object exec, float rv)  throws Throwable
    {
        EventData o = (EventData)ref;
        //Here we need to check of this was a simulated exception - if yes then we need to get
        //EvenetData instance from the exception cause.
        if (o == null)
        {
	        if (exec instanceof Throwable)
	        {
	        	Throwable t = (Throwable)exec;
	        	Throwable cause = t.getCause();
	        	if (cause instanceof SimulationException)
	        	{
	        		SimulationException sime= (SimulationException)cause;
	        		o = sime.getEventData();
	        	}
	        }
        }
        if (o.isIgnored())
        {
            return rv;
        }
        if (o.isOutgoingExceptionRaised())
        {
        	o.setOutgoingException(null);
        	o.setOutgoingExceptionRaised(false);
        }
        o.setStopTime(System.nanoTime());
        
        ResourceSpi res = null;
        ThreadContext.beginIgnore();
        try
        {
            res = resolveResource(o, exec, rv);
            if (res != null)
            {
                rv = ResourceManagerSpi.onExit(o, exec, res, rv);
            }
        }
        finally
        {
            ThreadContext.endIgnore();
            ThreadContext.pop();
            ContextFrame parent = ThreadContext.peek();
            if (res != null && parent != null)
            {
            	res.setParent(parent.getResource());
            }
        }
        o.raiseOutgoingException();
        return rv;
    }
    
    /**
     * @see EventHandler#end();
     */
    public static double end(Object ref, Object exec, double rv) throws Throwable
    {
        EventData o = (EventData)ref;
        //Here we need to check of this was a simulated exception - if yes then we need to get
        //EvenetData instance from the exception cause.
        if (o == null)
        {
	        if (exec instanceof Throwable)
	        {
	        	Throwable t = (Throwable)exec;
	        	Throwable cause = t.getCause();
	        	if (cause instanceof SimulationException)
	        	{
	        		SimulationException sime= (SimulationException)cause;
	        		o = sime.getEventData();
	        	}
	        }
        }
        if (o.isIgnored())
        {
            return rv;
        }
        
        if (o.isOutgoingExceptionRaised())
        {
        	o.setOutgoingException(null);
        	o.setOutgoingExceptionRaised(false);
        }
        o.setStopTime(System.nanoTime());
        ResourceSpi res = null;
        ThreadContext.beginIgnore();
        try
        {
            res = resolveResource(o, exec, rv);
            if (res != null)
            {
                rv = ResourceManagerSpi.onExit(o, exec, res, rv);
            }
        }
        finally
        {
            ThreadContext.endIgnore();
            ThreadContext.pop();
            ContextFrame parent = ThreadContext.peek();
            if (res != null && parent != null)
            {
            	res.setParent(parent.getResource());
            }
        }
        o.raiseOutgoingException();
        return rv;
    }
}
