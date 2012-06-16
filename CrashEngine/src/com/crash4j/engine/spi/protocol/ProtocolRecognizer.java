/**
 * 
 */
package com.crash4j.engine.spi.protocol;

import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.traits.LifecycleHandler;


/**
 * {@link ProtocolRecognizer} trait defines {@link LifecycleHandler}(s) that are able to detect protocol
 * that is transmitted over the stream of events via the handler.
 * <p/>
 * The variety of supported protocols is large, so only a limited number will be supported from the box
 * with the possibility to add custom protocol recognizers
 */
public interface ProtocolRecognizer 
{
	/**
	 * Called by {@link LifecycleHandler} before the <code>write</code> operation proceeds 
	 * @param data to be written
	 * @param offset starting offset
	 * @param len number of bytes to write
	 * 
	 * @return null if no action is detected and and action instance of action is detected.
	 */
	public ProtocolEvent beforeWrite(ResourceSpi spi, byte[] data, int offset, int len);
	
	/**
	 * Called by {@link LifecycleHandler} after the <code>write</code> operation is complete 
	 * @param data to be written
	 * @param offset starting offset
	 * @param len number of bytes tow write
	 * @param written is the number of bytes actually written
	 * @return null if no action is detected and and action instance of action is detected.
	 */
	public ProtocolEvent afterWrite(ResourceSpi spi, byte[] data, int offset, int len, int written);
	
	/**
	 * Called by {@link LifecycleHandler} before the <code>read</code> operation proceeds 
	 * @param data to be read
	 * @param offset starting offset
	 * @param len number of bytes to write
	 * @return null if no action is detected and and action instance of action is detected.
	 */
	public ProtocolEvent beforeRead(ResourceSpi spi, byte[] data, int offset, int len);
	
	/**
	 * Called by {@link LifecycleHandler} after the <code>read</code> operation is complete 
	 * @param data to be read
	 * @param offset starting offset
	 * @param len number of bytes to read
	 * @param written is the number of bytes actually read
	 * @return null if no action is detected and and action instance of action is detected.
	 */
	public ProtocolEvent afterRead(ResourceSpi spi, byte[] data, int offset, int len, int read);
	
	/**
	 * Called by {@link LifecycleHandler} when connection is closed.
	 */
	public ProtocolEvent onClose(ResourceSpi spi);
	
	/**
	 * Called by {@link LifecycleHandler} when error occurs.
	 */
	public ProtocolEvent onError(ResourceSpi spi, Exception e);
	
}
