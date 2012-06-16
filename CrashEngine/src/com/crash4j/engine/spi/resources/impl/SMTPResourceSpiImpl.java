/**
 * 
 */
package com.crash4j.engine.spi.resources.impl;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.regex.Pattern;

import javax.management.ObjectName;

import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.log.Log;
import com.crash4j.engine.spi.log.LogFactory;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.types.ResourceTypes;

/**
 * Describes http resource
 * @see ResourceSpi
 */
public class SMTPResourceSpiImpl extends EndpointBasedResourceSpiImpl
{
	/**
	 * SMTP resource desc
	 * @param spec
	 * @param host
	 * @param port
	 * @throws MalformedURLException
	 */
	public SMTPResourceSpiImpl(ResourceSpec spec, InetAddress host, int port)
			throws MalformedURLException 
	{
		super(spec, host, port);
	}
}
