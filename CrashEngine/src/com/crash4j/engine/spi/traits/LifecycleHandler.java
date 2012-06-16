/**
 * @copyright
 */
package com.crash4j.engine.spi.traits;

import com.crash4j.engine.Action;
import com.crash4j.engine.sim.Command;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.instrument.EventData;
import com.crash4j.engine.spi.instrument.EventHandler;
import com.crash4j.engine.spi.resources.ResourceSpi;

/**
 * Interface for all class of objects that can handle statistical samples.
 */
public interface LifecycleHandler
{
	/**
	 * Resolve and return the action for this functional event.
	 */
	public Action getAction(ResourceSpi spi, ResourceSpec spec, Object args, Object instance, ResourceClosure c);
	
    /**
     * Consume stats sample
     * @param spec that is submitting the result
     * @param args arguments
     * @param instance owner-ship instance or null for statics
     * @param rv return vale
     * @param dt time spent 
     */
    public void eneter(ResourceSpi spi, ResourceSpec spec, Object args, Object instance, Object rv, ResourceClosure c);

    /**
     * Consume stats sample
     * @param spec that is submitting the result
     * @param args arguments
     * @param instance owner-ship instance or null for statics
     * @param rv return vale
     * @param testStartTime time in nano-seconds on when the test will start
     * @param actualDt time spent on the test 
     * @param dt time spent including simulation 
     */
    public void exit(ResourceSpi spi, ResourceSpec spec, Object args, Object instance, Object rv, ResourceClosure c, Object ex, long testStartTime, long actualDt, long dt);

    
    
    /**
     * Execute behavioral modification, required during {@link EventHandler#end(Object, Object)} message, sent by instrumentation
     * in response to instrumented function call.
     * @param EventData instance for thi call
     * @param res is {@link ResourceSpi} instance in play
     */
    public void modifyBehaviorOnEntry(EventData o, ResourceSpi res, Command c);

    
    /**
     * Execute behavioral modification, required during {@link EventHandler#end(Object, Object)} message, sent by instrumentation
     * in response to instrumented function call.
     * @param EventData instance for thi call
     * @param res is {@link ResourceSpi} instance in play
     */
    public void modifyBehaviorOnExit(EventData o, Object ex, ResourceSpi res, long dt, Command c);
    
    /**
     * Execute behavioral modification, required during {@link EventHandler#end(Object, Object, Object)} message, sent by instrumentation
     * in response to instrumented function call.
     * @param EventData instance for thi call
     * @param res is {@link ResourceSpi} instance in play
     */
    public Object modifyBehaviorOnExit(EventData o, Object ex, ResourceSpi res, Object rv, long dt, Command c);
    
    
    /**
     * Execute behavioral modification, required during {@link EventHandler#end(Object, Object, int)} message, sent by instrumentation
     * in response to instrumented function call.
     * @param EventData instance for thi call
     * @param res is {@link ResourceSpi} instance in play
     */
    public int modifyBehaviorOnExit(EventData o, Object ex, ResourceSpi res, int rv, long dt, Command c);
    
    /**
     * Execute behavioral modification, required during {@link EventHandler#end(Object, Object, long)} message, sent by instrumentation
     * in response to instrumented function call.
     * @param EventData instance for thi call
     * @param res is {@link ResourceSpi} instance in play
     */
    public long modifyBehaviorOnExit(EventData o, Object ex, ResourceSpi res, long rv, long dt, Command c);
    
    
    /**
     * Execute behavioral modification, required during {@link EventHandler#end(Object, Object, float)} message, sent by instrumentation
     * in response to instrumented function call.
     * @param EventData instance for thi call
     * @param res is {@link ResourceSpi} instance in play
     */
    public float modifyBehaviorOnExit(EventData o, Object ex, ResourceSpi res, float rv, long dt, Command c);
    
    /**
     * Execute behavioral modification, required during {@link EventHandler#end(Object, Object, double)} message, sent by instrumentation
     * in response to instrumented function call.
     * @param EventData instance for thi call
     * @param res is {@link ResourceSpi} instance in play
     */
    public double modifyBehaviorOnExit(EventData o, Object ex, ResourceSpi res, double rv, long dt, Command c);
    
    /**
     * Execute behavioral modification, required during {@link EventHandler#end(Object, Object, char)} message, sent by instrumentation
     * in response to instrumented function call.
     * @param EventData instance for thi call
     * @param res is {@link ResourceSpi} instance in play
     */
    public char modifyBehaviorOnExit(EventData o, Object ex, ResourceSpi res, char rv, long dt, Command c);
    
    /**
     * Execute behavioral modification, required during {@link EventHandler#end(Object, Object, short)} message, sent by instrumentation
     * in response to instrumented function call.
     * @param EventData instance for thi call
     * @param res is {@link ResourceSpi} instance in play
     */
    public short modifyBehaviorOnExit(EventData o, Object ex, ResourceSpi res, short rv, long dt, Command c);

    /**
     * Execute behavioral modification, required during {@link EventHandler#end(Object, Object, byte)} message, sent by instrumentation
     * in response to instrumented function call.
     * @param EventData instance for thi call
     * @param res is {@link ResourceSpi} instance in play
     */
    public byte modifyBehaviorOnExit(EventData o, Object ex, ResourceSpi res, byte rv, long dt, Command c);
    
    /**
     * Execute behavioral modification, required during {@link EventHandler#end(Object, Object, boolean)} message, sent by instrumentation
     * in response to instrumented function call.
     * @param EventData instance for thi call
     * @param res is {@link ResourceSpi} instance in play
     */
    public boolean modifyBehaviorOnExit(EventData o, Object ex, ResourceSpi res, boolean rv, long dt, Command c);
    
    
}
