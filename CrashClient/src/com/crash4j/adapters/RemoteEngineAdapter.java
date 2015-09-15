/**
 * 
 */
package com.crash4j.adapters;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.crash4j.Behavior;
import com.crash4j.EngineAdapter;
import com.crash4j.EngineDataException;
import com.crash4j.EngineException;
import com.crash4j.EngineMethodException;
import com.crash4j.EngineNotFoundException;
import com.crash4j.Resource;
import com.crash4j.Simulation;

/**
 */
public class RemoteEngineAdapter implements EngineAdapter 
{
	protected String sessionKey = null;
	protected String host = null;
	protected int port = 0;
	protected Socket socket = null;
	protected OutputStreamWriter output = null;
	/**
	 * @see com.crash4j.EngineAdapter#bind(java.util.Map)
	 */
	@Override
	public void bind(Map<String, Object> params) throws EngineNotFoundException 
	{
		if (params == null)
		{
			throw new EngineNotFoundException();
		}
		this.sessionKey = (String)params.get("key");
		this.host = (String)params.get("host");
		this.port = (Integer)params.get("port");
		
		try {
			this.socket = new Socket(this.host, this.port);
			this.output = new OutputStreamWriter(this.socket.getOutputStream());
		} 
		catch (UnknownHostException e) 
		{
			EngineNotFoundException ee = new EngineNotFoundException();
			ee.initCause(e);
			throw ee;
		} 
		catch (IOException e) 
		{
			EngineNotFoundException ee = new EngineNotFoundException();
			ee.initCause(e);
			throw ee;
		}
	}
	
	
	/**
	 * @see com.crash4j.EngineAdapter#getSimulation(java.lang.String)
	 */
	@Override
	public Simulation getSimulation(String id) throws EngineMethodException 
	{
		return null;
	}

	/* (non-Javadoc)
	 * @see com.crash4j.EngineAdapter#addSimulationAsJSON(java.lang.String)
	 */
	@Override
	public void addSimulationAsJSON(String sim) throws EngineMethodException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.crash4j.EngineAdapter#addBehavior(java.lang.String, com.crash4j.Behavior)
	 */
	@Override
	public void addBehavior(String simulationId, Behavior behavior)
			throws EngineMethodException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.crash4j.EngineAdapter#addBehaviorAsJSON(java.lang.String, java.lang.String)
	 */
	@Override
	public void addBehaviorAsJSON(String simulationId, String behavior)
			throws EngineMethodException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.crash4j.EngineAdapter#addSimulation(com.crash4j.Simulation, java.util.Set)
	 */
	@Override
	public void addSimulation(Simulation sim, Set<String> map)
			throws EngineMethodException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.crash4j.EngineAdapter#createSimulation(java.io.Reader)
	 */
	@Override
	public Simulation createSimulation(Reader reader) throws EngineException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.crash4j.EngineAdapter#createBehavior(java.io.Reader)
	 */
	@Override
	public Behavior createBehavior(Reader r) throws EngineException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.crash4j.EngineAdapter#startSimulation(java.lang.String)
	 */
	@Override
	public void startSimulation(String id) throws EngineMethodException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.crash4j.EngineAdapter#stopSimulation(java.lang.String)
	 */
	@Override
	public void stopSimulation(String id) throws EngineMethodException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.crash4j.EngineAdapter#clearStats()
	 */
	@Override
	public void clearStats() throws EngineMethodException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.crash4j.EngineAdapter#resetEngine()
	 */
	@Override
	public void resetEngine() throws EngineMethodException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.crash4j.EngineAdapter#registerCollector(java.lang.String, java.util.Map, long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public String registerCollector(String klass, Map<String, Object> props,
			long period, TimeUnit unit) throws EngineMethodException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.crash4j.EngineAdapter#unRegisterCollector(java.lang.String)
	 */
	@Override
	public void unRegisterCollector(String moniker)
			throws EngineMethodException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.crash4j.EngineAdapter#getResources()
	 */
	@Override
	public Set<Resource> getResources() throws EngineDataException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.crash4j.EngineAdapter#getResourcesAsJSON()
	 */
	@Override
	public String getResourcesAsJSON() throws EngineDataException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.crash4j.EngineAdapter#getResourceDetails(java.lang.String)
	 */
	@Override
	public Resource getResourceDetails(String id) throws EngineDataException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.crash4j.EngineAdapter#getResourceDetailsAsJSON(java.lang.String)
	 */
	@Override
	public String getResourceDetailsAsJSON(String id)
			throws EngineDataException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.crash4j.EngineAdapter#getResourceDetails(com.crash4j.Resource)
	 */
	@Override
	public Resource getResourceDetails(Resource res) throws EngineDataException {
		// TODO Auto-generated method stub
		return null;
	}

}
