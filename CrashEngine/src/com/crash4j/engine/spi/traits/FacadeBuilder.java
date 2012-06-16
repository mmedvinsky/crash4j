/**
 * @copyright
 */
package com.crash4j.engine.spi.traits;

import java.io.FileInputStream;

import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.instrument.EventHandler;
import com.crash4j.engine.spi.instrument.delegates.java_nio_channels_FileChannel;
import com.crash4j.engine.spi.resources.ResourceSpi;


/**
 * {@link FacadeBuilder} is responsible for creating a facade classes specifically related to the {@link EventHandler}
 * end-point in flight.  For example.<p/>
 * {@link FileInputStream#getChannel()} will return and instance of {@link java_nio_channels_FileChannel} that behaves as a delegated facade.
 * @author <crash4j team>
 *
 */
public interface FacadeBuilder
{
    /**
     * Create a facade for the instance and return value.
     * @param spec of the end-point
     * @param args arguments if are any
     * @param instance instance that initiated the facade
     * @param rv the current value of the facade
     * @return facaded instance.
     */
    public Object createFacade(ResourceSpec spec, ResourceSpi res, Object args, Object instance, Object rv, ResourceClosure c);
}
