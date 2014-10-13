/**
 * 
 */
package com.crash4j;

import java.util.HashMap;

import com.crash4j.adapters.LocalEngineAdapter;
import com.crash4j.adapters.RemoteEngineAdapter;

/**
 */
public abstract class EngineAdapterFactory 
{
	/**
	 * @return the instance of {@link EngineAdapter} bound to the local engine
	 */
	public static EngineAdapter getLocalAdapter() throws EngineNotFoundException
	{
		LocalEngineAdapter le = new LocalEngineAdapter();
		le.bind(null);
		return le;
	}
	
	/**
	 * Produce the remote adapter
	 * @param akey
	 * @param host
	 * @param port
	 * @return
	 * @throws EngineNotFoundException
	 */
	public static EngineAdapter getRemoteAdapter(String akey, String host, int port)  throws EngineNotFoundException
	{
		RemoteEngineAdapter rea = new RemoteEngineAdapter();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("key", akey);
		params.put("host", host);
		params.put("port", new Integer(port));
		rea.bind(params);
		return rea;
	}
}
