/**
 * 
 */
package com.crash4j.engine.spi.context;

/**
 * {@link ThreadContextOld} class is responsible for execution control 
 * and contextual tracking of the execution.
 * 
 * Only a single controller is allowed at a time for the thread heap.
 * 
 * @author <MM>
 *
 */
public class ThreadContextOld
{
    /** Thread local controller variable */
    static private ThreadLocal<__hread_context> tlh = new ThreadLocal<__hread_context>();
    
    static class __hread_context
    {
        int refc = 0;
        boolean ignore = false;
        public __hread_context(int r)
        {
            this.refc = r;
        }
        public __hread_context(boolean r)
        {
            this.ignore = r;
        }
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
    
    /**
     * @return the number of calls before this addReff is applied. Once returned the number within tls is return value + 1
     */
    public static int addRef()
    {
        __hread_context r = tlh.get();
        if (r == null)
        {
            tlh.set(new __hread_context(1));
            return 0;
        }        
        return r.refc++;
    }
   
    /**
     * @return int value that is currently on the thread
     */
    public static int count()
    {
        __hread_context r = tlh.get();
        if (r == null)
        {
            return 0;
        }
        return r.refc;
    }
   
    
    
    
    /**
     * @return int value after the release is applied.  So the number is representative of current state
     */
    public static int release()
    {
        __hread_context r = tlh.get();
        return --r.refc;
    }

}
