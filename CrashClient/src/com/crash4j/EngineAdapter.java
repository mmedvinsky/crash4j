package com.crash4j;

import java.io.Reader;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.crash4j.annotations.Behaviors;

/**
 * Defines {@link EngineAdapter} functions for client programmatic access and
 * isolates the location of the engine (remote|local) from the caller.
 */
public interface EngineAdapter 
{
	/**
	 * Bind to the running engine.
	 */
	public void bind(Map<String, Object> params) throws EngineNotFoundException;

	/**
	 * Get the simulation object from the engine
	 * @param id
	 * @return
	 */
	public Simulation getSimulation(String id) throws EngineMethodException;

	/**
	 * Add new simulation to the engine
	 * @param sim
	 */
	public void addSimulationAsJSON(String sim) throws EngineMethodException;

	/**
	 * Add new simulation to the engine
	 * @param sim
	 */
	public void addBehavior(String simulationId, Behavior behavior)
			throws EngineMethodException;

	/**
	 * Add new simulation to the engine
	 * @param sim
	 */
	public void addBehaviorAsJSON(String simulationId, String behavior)
			throws EngineMethodException;

	/**
	 * Add new simulation to the engine
	 * @param sim
	 */
	public void addSimulation(Simulation sim, Set<String> map)
			throws EngineMethodException;

	/**
	 * @return {@link Simulation} object, loaded from {@link Reader}
	 */
	public Simulation createSimulation(Reader reader) throws EngineException;

	/**
	 * Create {@link Behaviors} object
	 * @param r
	 * @return
	 */
	public Behavior createBehavior(Reader r) throws EngineException;

	/**
	 * Start simulation.
	 * @throws EngineMethodException
	 */
	public void startSimulation(String id) throws EngineMethodException;

	/**
	 * Stop simulation.
	 * @throws EngineMethodException
	 */
	public void stopSimulation(String id) throws EngineMethodException;

	/**
	 * Clear engine statistics
	 * @throws EngineMethodException
	 */
	public void clearStats() throws EngineMethodException;

	/**
	 * Engine method reset
	 * @throws EngineMethodException
	 */
	public void resetEngine() throws EngineMethodException;

	/**
	 * Engine method reset
	 * @throws EngineMethodException
	 */
	public String registerCollector(String klass, Map<String, Object> props,
			long period, TimeUnit unit) throws EngineMethodException;

	/**
	 * 
	 * @param moniker
	 */
	public void unRegisterCollector(String moniker)
			throws EngineMethodException;

	/**
	 * @return {@link Resource} that are currently managed by the runtime
	 * @throws  
	 * @throws  
	 */
	public Set<Resource> getResources() throws EngineDataException;

	/**
	 * @return {@link Resource} that are currently managed by the runtime
	 * @throws  
	 * @throws  
	 */
	public String getResourcesAsJSON() throws EngineDataException;

	/**
	 * @return {@link Resource} that are currently managed by the runtime
	 */
	public Resource getResourceDetails(String id) throws EngineDataException;

	/**
	 * @return {@link Resource} that are currently managed by the runtime
	 */
	public String getResourceDetailsAsJSON(String id)
			throws EngineDataException;

	/**
	 * <pre>
	 * (i) initialization action. i.e. java.io.File constructor.
	 * (o) open connection to the resource, i.e. Socket.connect, FileInputStream() constructor, ...
	 * (c) close connection to the resource  
	 * (s) shutdown the resource operation if it is running
	 * (d) destroy the resource, i.e. file delete.
	 * (n) create new resource, i.e. File.createTempFile 
	 * (m) move/rename/modify resource
	 * (r) read from the resource
	 * (w) write to the resource
	 * (k) get resource information
	 * (t) end-point is performing a test 
	 * (u) user-defined action, to be specified with attributes or other means
	 * </pre>
	 * @return {@link Resource} that are currently managed by the runtime
	 */
	public Resource getResourceDetails(Resource res) throws EngineDataException;

}