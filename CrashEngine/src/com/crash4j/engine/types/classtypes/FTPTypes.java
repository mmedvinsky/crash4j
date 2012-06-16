/**
 * @copyright
 */
package com.crash4j.engine.types.classtypes;

import java.io.IOException;
import java.util.HashSet;
import java.util.Stack;

import com.crash4j.engine.spi.ResourceManagerSpi;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.actions.ActionImpl;
import com.crash4j.engine.spi.instrument.handlers.RecognizedProtocolHandler;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.resources.impl.FTPResourceSpiImpl;
import com.crash4j.engine.spi.resources.impl.NetworkResourceSpiImpl;
import com.crash4j.engine.spi.traits.LifecycleHandler;
import com.crash4j.engine.spi.traits.ProtocolBasedResourceFacotry;
import com.crash4j.engine.types.ActionClasses;
import com.crash4j.engine.types.ResourceTypes;

/**
 * SMTP protocol types
 * @author <crash4j team>
 * 
 */
public enum FTPTypes  implements ProtocolBasedResourceFacotry
{
	ABOR,
	CDUP,
	CWD,
	DELE,
	LIST,
	MKD,
	MLSD,
	MLST,
	MODE,
	NLST,
	OPTS,
	PWD,
	REIN,
	REST,
	RETR,
	RMD,
	RNFR,
	RNTO,
	SITE,
	SIZE,
	SMNT,
	STAT,
	STOR,
	STOU,
	STRU,
	SYST,
	TYPE,
	FTPCMD;
	
	/** mul^2 mask supports combinations */
	private int mask = 0;
	private ResourceSpec spec = null;

	/**
	 * @param action
	 */
	private FTPTypes() 
	{
		this.mask = (int) Math.pow(2, (this.ordinal() + 1));
        this.spec = ResourceManagerSpi.createSpec(ActionClasses.FTP.name(), name(), "", "", false, (LifecycleHandler)new RecognizedProtocolHandler(), 
				new ActionImpl(name(), ActionClasses.FTP, this), ResourceTypes.SERVICE, null, new HashSet<Class<?>>(), false, 0xFF);;
	}

	public static FTPTypes fromString(String s) 
	{
		return FTPTypes.valueOf(s.toUpperCase());
	}

	public int toInt() {
		return this.ordinal();
	}

	public int toMask() {
		return mask;
	}

	@Override
	public ResourceSpi createResource(ResourceSpi owner, Stack<String> params)
			throws IOException 
	{
		if (!(owner instanceof NetworkResourceSpiImpl))
		{
			return null;
		}
		
		// All of this should be hidden below specific filter.
		NetworkResourceSpiImpl netr = (NetworkResourceSpiImpl)owner;
		try
		{
			return new FTPResourceSpiImpl(this.spec, netr.getHost(), netr.getPort());
		}
		catch (Exception e)
		{
			throw new IOException(e);
		}
	}

	@Override
	public ResourceSpec getSpec() 
	{
		return this.spec;
	}

}
