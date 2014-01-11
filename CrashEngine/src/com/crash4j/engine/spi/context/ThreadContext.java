/**
 * 
 */
package com.crash4j.engine.spi.context;

import java.util.Iterator;
import java.util.Stack;

import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.resources.ResourceSpi;

/**
 * {@link ThreadContext} class is responsible for execution control 
 * and contextual tracking of the execution.
 * 
 * Only a single controller is allowed at a time for the thread heap.
 * 
 * @author <MM>
 *
 */
public class ThreadContext
{
    /** Thread local controller variable */
    static private ThreadLocal<__hread_context> tlh = new ThreadLocal<__hread_context>();
    
    static class __hread_context
    {
        boolean ignore = false;
        Stack<ContextFrame> frames = new Stack<ContextFrame>();
        
        public __hread_context(boolean r)
        {
            this.ignore = r;
        }
    }

    /**
     * @return new {@link ContextFrame} created during this call
     */
    public static ContextFrame createFrame(ResourceSpec res, ResourceSpi rc)
    {
        return new ContextFrame(res, rc);
    }

    /**
     * Returns true if the same resource with the same action is 
     * already in control.
     * @param spec
     * @param res
     * @return
     */
    public static boolean checkOwnership(ResourceSpec spec, ResourceSpi res)
    {
        __hread_context r = tlh.get();
        if (r == null)
        {
            return false;
        }
        
        if (r.frames.isEmpty())
        {
            return false;
        }
        
        Iterator it = r.frames.iterator();
        while (it.hasNext())
        {
            ContextFrame frm = (ContextFrame)it.next();
            if (frm.getResourceSpec().getId() == spec.getId()
                    && res.equals(frm.getResource()))
            {
                return true;
            }
        }
        return false;
    }
    
    public static void push(ContextFrame frame)
    {
        __hread_context r = tlh.get();
        if (r == null)
        {
            r = new __hread_context(false);
            tlh.set(r);
        }
        r.frames.push(frame);
    }

    public static void pop()
    {
        __hread_context r = tlh.get();
        
        if (r == null || r.frames.isEmpty())
        {
        	throw new RuntimeException("Stack frame is out of order!");
        }
        r.frames.pop();
    }

    public static ContextFrame peek()
    {
        __hread_context r = tlh.get();
        if (r == null)
        {
            return null;
        }
        if (!r.frames.isEmpty())
        {
        	return r.frames.peek();
        }
        return null;
    }
    
    public static Iterator<ContextFrame> frameIterator()
    {
        __hread_context r = tlh.get();
        if (r == null)
        {
            throw new RuntimeException("Stack frame is out of order!");
        }
        return r.frames.iterator();
    }
    
    
    public static void beginIgnore()
    {
        __hread_context r = tlh.get();
        if (r == null)
        {
            tlh.set(new __hread_context(true));
        }
        else
        {
            r.ignore = true;
        }
    }
    
    public static void endIgnore()
    {
        tlh.get().ignore = false;
    }
    
    public static boolean shouldIgnore()
    {
        if (tlh.get() == null)
        {
            return false;
        }
        return tlh.get().ignore;
            
    }
}
