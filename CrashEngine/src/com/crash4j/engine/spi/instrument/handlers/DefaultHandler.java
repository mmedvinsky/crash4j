
package com.crash4j.engine.spi.instrument.handlers;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import com.crash4j.engine.Action;
import com.crash4j.engine.UnknownResourceException;
import com.crash4j.engine.sim.Behavior;
import com.crash4j.engine.sim.Command;
import com.crash4j.engine.sim.SimulationException;
import com.crash4j.engine.spi.ResourceManagerSpi;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.instrument.EventData;
import com.crash4j.engine.spi.log.Log;
import com.crash4j.engine.spi.log.LogFactory;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.stats.StatsManager;
import com.crash4j.engine.spi.traits.FacadeBuilder;
import com.crash4j.engine.spi.traits.LifecycleHandler;
import com.crash4j.engine.spi.traits.ResourceBuilder;
import com.crash4j.engine.spi.traits.ResourceClosure;
import com.crash4j.engine.spi.util.HRTime;
import com.crash4j.engine.spi.util.Utils;
import com.crash4j.engine.types.BehaviorTypes;
import com.crash4j.engine.types.InstructionTypes;
import com.crash4j.engine.types.StatTypes;
import com.crash4j.engine.types.UnitTypes;

/**
 * Default handler for every message type.  This class provides shared implementation for 
 * {@link LifecycleHandler}, {@link ResourceBuilder}, {@link FacadeBuilder} and other traits that 
 * are message specific but can share a common implementation.
 * 
 */
public abstract class DefaultHandler implements LifecycleHandler, ResourceBuilder
{
    
    protected Log log = LogFactory.getLog(DefaultHandler.class);
    /**
     * @see com.crash4j.engine.spi.spi.traits.LifecycleHandler#eneter(com.crash4j.engine.spi.resources.spi.ResourceSpi, java.lang.Object, java.lang.Object, java.lang.Object)
     */
    @Override
    public void eneter(ResourceSpi spi, ResourceSpec spec, Object args, Object instance, Object rv, ResourceClosure c)
    {
    	
    }

    /**
	 * @see com.crash4j.engine.spi.traits.LifecycleHandler#getAction()
	 */
	@Override
	public Action getAction(ResourceSpi spi, ResourceSpec spec, Object args, Object instance, ResourceClosure c) 
	{
		return spec.getDefaultAction();
	}

	/**
     * @see com.crash4j.engine.spi.spi.traits.ResourceBuilder#canCache()
     */
    @Override
    public boolean canCache()
    {
        return true;
    }

    /**
     * @see com.crash4j.engine.spi.spi.traits.ResourceBuilder#completeResource(com.crash4j.engine.spi.resources.spi.ResourceSpi, com.crash4j.engine.spi.ResourceSpec, java.lang.Object, java.lang.Object, java.lang.Object)
     */
    @Override
    public void completeResource(ResourceSpi current, ResourceSpec spec, Object args, Object instance, Object rv)
    throws UnknownResourceException
    {
        if (!current.isComplete())
        {
            throw new UnknownResourceException("Resource "+current+" is incomplete, but no completion code is in place!");
        }
    }

    /**
     * @see com.crash4j.engine.spi.spi.traits.ResourceBuilder#getUniqueKey()
     */
    @Override
    public Object getUniqueKey(Object instance, Object args, Object rv)
    {
        return Utils.wrappedObjectId(Utils.wrapObject(instance), instance);
    }

    /**
     * @see com.crash4j.engine.spi.spi.traits.LifecycleHandler#exit(com.crash4j.engine.spi.resources.spi.ResourceSpi, java.lang.Object, java.lang.Object, java.lang.Object, long)
     */
    @Override
    public void exit(ResourceSpi spi, ResourceSpec spec, Object args, Object instance, Object rv, ResourceClosure c, 
    		Object ex, long testStartTime, long actualDt, long dt)
    {        
    	//every spec should have a default action
        Action action = this.getAction(spi, spec, args, instance, c);
        StatsManager sMgr = ResourceManagerSpi.getStatsManager();
        sMgr.submit(spi, StatTypes.TIME, UnitTypes.MICROSECONDS, action, dt);
        sMgr.submit(spi, StatTypes.ACTUALTIME, UnitTypes.MICROSECONDS, action, actualDt);
        if (ex != null)
        {
        	sMgr.submit(spi, StatTypes.ERROR, UnitTypes.NONE, action, 1);
        }
        spi.setLastAccess(System.currentTimeMillis());
    }

    
    /**
     * Handle the {@link InstructionTypes#DELAY} {@link Command}
     */
    protected void applyDefaultSimulationOnEntry(EventData o, ResourceSpi res, Command c)
    {
        if (!c.isEnabled())
        {
            return;
        }
        
        Behavior bh = c.getBehavior();
        InstructionTypes t = bh.getType();
        if (t.equals(InstructionTypes.CLOSE))
        {
            doClose(o, res, c);
        }
        else if (t.equals(InstructionTypes.ERROR))
        {
            doError(o, res, c);
        }
    }    
    
    /**
     * Handle the {@link InstructionTypes#DELAY} {@link Command}
     */
    protected void applyDefaultSimulationOnExit(EventData o, ResourceSpi res, long dt, Command c)
    {
        if (!c.isEnabled())
        {
            return;
        }
        Behavior bh = c.getBehavior();
        InstructionTypes t = bh.getType();
        if (t.equals(InstructionTypes.DELAY))
        {
            doDelay(o, res, dt, c);
        }
    }
    
    /**
     * This methods should be overwritten by {@link DefaultHandler} subclasses to provide specific behaviors
     * @param o an instance of {@link EventData}
     * @param res {@link ResourceSpi} instance
     * @param dt delta time from start of action to its end without modification 
     * @param c command to handle (DELAY in this case.)
     */
    protected void doDelay(EventData o, ResourceSpi res, long dt, Command c)
    {
        BehaviorTypes bt = c.getBehavior().getBehaviorType();
        double rt = c.getInstruction().getWeight();
        if (bt.equals(BehaviorTypes.STATIC_RELATIVE))
        {
            rt = dt >= rt ? 0 : rt - dt;
        }
        else if (bt.equals(BehaviorTypes.WEIGHTED_ABSOLUTE))
        {
        	//System.err.println(dt+" "+rt);
            rt = dt * rt;
        }
        else if (bt.equals(BehaviorTypes.WEIGHTED_RELATIVE))
        {
            rt = dt * rt;
            rt = dt >= rt ? 0 : rt - dt;
        }
        else if (bt.equals(BehaviorTypes.STOCHASTIC_ABSOLUTE))
        {
            rt = System.nanoTime() % rt;
        }
        else if (bt.equals(BehaviorTypes.STOCHASTIC_RELATIVE))
        {
            rt = System.nanoTime() % rt;
            rt = dt >= rt ? 0 : rt - dt;
        }
        //Extract the time we spend on processing from delay.
        long ddt = (long)rt - (System.nanoTime() - o.getStopTime());
        if (ddt > 0)
        {
            HRTime.delay((long)ddt);
        }
    }
    
    /**
     * This methods should be overwritten by {@link DefaultHandler} subclasses to provide specific behaviors
     * @param o an instance of {@link EventData}
     * @param res {@link ResourceSpi} instance
     * @param dt delta time from start of action to its end without modification 
     * @param c command to handle (DELAY in this case.)
     */
    protected void doClose(EventData o, ResourceSpi res, Command c)
    {
        if (o.getSpec().isConstructor())
        {
            return;
        }
        
        Object instance = o.getInstance();
        if (instance == null)
        {
            return;
        }
        try
        {
            if (instance instanceof InputStream)
            {
                ((InputStream)instance).close();
            }
            else if (instance instanceof OutputStream)
            {
                ((OutputStream)instance).close();              
            }
            else if (instance instanceof Socket)
            {
                ((Socket)instance).close();              
            }
            else if (instance instanceof ServerSocket)
            {
                ((ServerSocket)instance).close();              
            }
            else if (instance instanceof DatagramSocket)
            {
                ((DatagramSocket)instance).close();              
            }
            else if (instance instanceof SocketChannel)
            {
                ((SocketChannel)instance).close();              
            }
            else if (instance instanceof FileChannel)
            {
                ((FileChannel)instance).close();              
            }
            else if (instance instanceof ServerSocketChannel)
            {
                ((ServerSocketChannel)instance).close();              
            }
        }
        catch (Exception e)
        {
            log.logError("", e);
        }
    }
    
    /**
     * This methods should be overwritten by {@link DefaultHandler} subclasses to provide specific behaviors
     * @param o an instance of {@link EventData}
     * @param res {@link ResourceSpi} instance
     * @param dt delta time from start of action to its end without modification 
     * @param c command to handle (ERROR in this case.)
     */
    protected void doError(EventData o, ResourceSpi res, Command c)
    {
        try
        {
            Set<Class<?>> excs = o.getSpec().getExceptions();
            // in this case we will use a nano time as a random number if there are more then one exception
            //and will flip a coint and raise the one the get selected
            if (excs == null || excs.isEmpty())
            {
                return;  //no errors means that we will not raise any
            }
            
            Iterator<Class<?>> it = excs.iterator();
            int sz = excs.size();
            Class selc = null;
            if (sz == 1)
            {
                selc = it.next();
            }
            else
            {
                long ord = System.nanoTime() % sz;
                for (int i = 0; i < ord; i++)
                {
                    selc = it.next();
                }
                if (selc == null)
                {
                    selc = it.next();
                }
            }
            Throwable t = (Throwable)selc.newInstance();
            t.initCause(new SimulationException(o, res));
            o.setOutgoingException(t);
        }
        catch (Exception e)
        {
            log.logError("Error raising simulated error ", e);
        }
    }
    
    
    /**
     * @see com.crash4j.engine.spi.traits.LifecycleHandler#modifyBehaviorOnEntry(com.crash4j.engine.spi.instrument.EventData, com.crash4j.engine.spi.resources.ResourceSpi)
     */
    @Override
    public void modifyBehaviorOnEntry(EventData o, ResourceSpi res, Command c)
    {
        applyDefaultSimulationOnEntry(o, res, c);
    }

    /**
     * @see com.crash4j.engine.spi.traits.LifecycleHandler#modifyBehaviorOnExit(com.crash4j.engine.spi.instrument.EventData, java.lang.Object, com.crash4j.engine.spi.resources.ResourceSpi)
     */
    @Override
    public void modifyBehaviorOnExit(EventData o, Object ex, ResourceSpi res, long dt, Command c)
    {
        applyDefaultSimulationOnExit(o, res, dt, c);
    }

    /**
     * @see com.crash4j.engine.spi.traits.LifecycleHandler#modifyBehaviorOnExit(com.crash4j.engine.spi.instrument.EventData, java.lang.Object, com.crash4j.engine.spi.resources.ResourceSpi, java.lang.Object)
     */
    @Override
    public Object modifyBehaviorOnExit(EventData o, Object ex, ResourceSpi res, Object rv, long dt, Command c)
    {
        applyDefaultSimulationOnExit(o, res, dt, c);
        return rv;
    }

    /**
     * @see com.crash4j.engine.spi.traits.LifecycleHandler#modifyBehaviorOnExit(com.crash4j.engine.spi.instrument.EventData, java.lang.Object, com.crash4j.engine.spi.resources.ResourceSpi, int)
     */
    @Override
    public int modifyBehaviorOnExit(EventData o, Object ex, ResourceSpi res, int rv, long dt, Command c)
    {
        applyDefaultSimulationOnExit(o, res, dt, c);
        return rv;
    }

    /**
     * @see com.crash4j.engine.spi.traits.LifecycleHandler#modifyBehaviorOnExit(com.crash4j.engine.spi.instrument.EventData, java.lang.Object, com.crash4j.engine.spi.resources.ResourceSpi, long)
     */
    @Override
    public long modifyBehaviorOnExit(EventData o, Object ex, ResourceSpi res, long rv, long dt, Command c)
    {
        applyDefaultSimulationOnExit(o, res, dt, c);
        return rv;
    }

    /**
     * @see com.crash4j.engine.spi.traits.LifecycleHandler#modifyBehaviorOnExit(com.crash4j.engine.spi.instrument.EventData, java.lang.Object, com.crash4j.engine.spi.resources.ResourceSpi, float)
     */
    @Override
    public float modifyBehaviorOnExit(EventData o, Object ex, ResourceSpi res, float rv, long dt, Command c)
    {
        applyDefaultSimulationOnExit(o, res, dt, c);
        return rv;
    }

    /**
     * @see com.crash4j.engine.spi.traits.LifecycleHandler#modifyBehaviorOnExit(com.crash4j.engine.spi.instrument.EventData, java.lang.Object, com.crash4j.engine.spi.resources.ResourceSpi, double)
     */
    @Override
    public double modifyBehaviorOnExit(EventData o, Object ex, ResourceSpi res, double rv, long dt, Command c)
    {
        applyDefaultSimulationOnExit(o, res, dt, c);
        return rv;
    }

    /**
     * @see com.crash4j.engine.spi.traits.LifecycleHandler#modifyBehaviorOnExit(com.crash4j.engine.spi.instrument.EventData, java.lang.Object, com.crash4j.engine.spi.resources.ResourceSpi, char)
     */
    @Override
    public char modifyBehaviorOnExit(EventData o, Object ex, ResourceSpi res, char rv, long dt, Command c)
    {
        applyDefaultSimulationOnExit(o, res, dt, c);
        return rv;
    }

    /**
     * @see com.crash4j.engine.spi.traits.LifecycleHandler#modifyBehaviorOnExit(com.crash4j.engine.spi.instrument.EventData, java.lang.Object, com.crash4j.engine.spi.resources.ResourceSpi, short)
     */
    @Override
    public short modifyBehaviorOnExit(EventData o, Object ex, ResourceSpi res, short rv, long dt, Command c)
    {
        applyDefaultSimulationOnExit(o, res, dt, c);
        return rv;
    }

    /**
     * @see com.crash4j.engine.spi.traits.LifecycleHandler#modifyBehaviorOnExit(com.crash4j.engine.spi.instrument.EventData, java.lang.Object, com.crash4j.engine.spi.resources.ResourceSpi, byte)
     */
    @Override
    public byte modifyBehaviorOnExit(EventData o, Object ex, ResourceSpi res, byte rv, long dt, Command c)
    {
        applyDefaultSimulationOnExit(o, res, dt, c);
        return rv;
    }

    /**
     * @see com.crash4j.engine.spi.traits.LifecycleHandler#modifyBehaviorOnExit(com.crash4j.engine.spi.instrument.EventData, java.lang.Object, com.crash4j.engine.spi.resources.ResourceSpi, boolean)
     */
    @Override
    public boolean modifyBehaviorOnExit(EventData o, Object ex, ResourceSpi res, boolean rv, long dt, Command c)
    {
        applyDefaultSimulationOnExit(o, res, dt, c);
        return rv;
    }
}
