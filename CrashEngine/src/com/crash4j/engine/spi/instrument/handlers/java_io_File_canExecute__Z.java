package com.crash4j.engine.spi.instrument.handlers;
import com.crash4j.engine.spi.traits.ResourceBuilder;


/**
 * 
 */

/**
 * Resource factory implementation for java.io.File#canExecute()Z
 * <p/>
 * Supported Behaviors:
 * <p/>
 * <ul>
 * <li>Delay</li>
 * <li>Error (SecurityException)</li>
 * </ul>
 * 
 */
public class java_io_File_canExecute__Z extends java_io_File_HandleWithInstance implements ResourceBuilder
{
}
