/**
 * 
 */
package com.crash4j.engine.spi.protocol.impl;

import com.crash4j.engine.spi.protocol.ProtocolEvent;
import com.crash4j.engine.spi.protocol.ProtocolRecognizer;
import com.crash4j.engine.spi.resources.ResourceSpi;

/**
 * Disabled {@link ProtocolRecognizer} which never raises any {@link ProtocolEvent}s
 * @see ProtocolRecognizer
 */
public class DisabledProtocolRecognizer implements ProtocolRecognizer 
{
	/**
	 * @see com.crash4j.engine.spi.protocol.ProtocolRecognizer#beforeWrite(com.crash4j.engine.spi.resources.ResourceSpi, byte[], int, int)
	 */
	@Override
	public ProtocolEvent beforeWrite(ResourceSpi spi, byte[] data, int offset,
			int len) 
	{
		return null;
	}

	/**
	 * @see com.crash4j.engine.spi.protocol.ProtocolRecognizer#afterWrite(com.crash4j.engine.spi.resources.ResourceSpi, byte[], int, int, int)
	 */
	@Override
	public ProtocolEvent afterWrite(ResourceSpi spi, byte[] data, int offset,
			int len, int written) 
	{
		return null;
	}

	/**
	 * @see com.crash4j.engine.spi.protocol.ProtocolRecognizer#beforeRead(com.crash4j.engine.spi.resources.ResourceSpi, byte[], int, int)
	 */
	@Override
	public ProtocolEvent beforeRead(ResourceSpi spi, byte[] data, int offset,
			int len) 
	{
		return null;
	}

	/**
	 * @see com.crash4j.engine.spi.protocol.ProtocolRecognizer#afterRead(com.crash4j.engine.spi.resources.ResourceSpi, byte[], int, int, int)
	 */
	@Override
	public ProtocolEvent afterRead(ResourceSpi spi, byte[] data, int offset,
			int len, int read) 
	{
		return null;
	}

	/**
	 * @see com.crash4j.engine.spi.protocol.ProtocolRecognizer#onClose(com.crash4j.engine.spi.resources.ResourceSpi)
	 */
	@Override
	public ProtocolEvent onClose(ResourceSpi spi) 
	{
		return null;
	}

	/**
	 * @see com.crash4j.engine.spi.protocol.ProtocolRecognizer#onError(com.crash4j.engine.spi.resources.ResourceSpi, java.lang.Exception)
	 */
	@Override
	public ProtocolEvent onError(ResourceSpi spi, Exception e) 
	{
		return null;
	}

}
