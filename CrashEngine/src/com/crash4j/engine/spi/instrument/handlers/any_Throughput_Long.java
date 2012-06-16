/**
 * @copyright
 */
package com.crash4j.engine.spi.instrument.handlers;

import com.crash4j.engine.Action;
import com.crash4j.engine.spi.ResourceManagerSpi;
import com.crash4j.engine.spi.ResourceSpec;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.engine.spi.stats.StatsManager;
import com.crash4j.engine.spi.traits.ResourceBuilder;
import com.crash4j.engine.spi.traits.ResourceClosure;
import com.crash4j.engine.types.StatTypes;
import com.crash4j.engine.types.UnitTypes;

/**
 * Resource factory implementation for java.io.FileInputStream!java.nio.channels.FileChannel#read([Ljava/nio/ByteBuffer;)J#read([Ljava/nio/ByteBuffer;)J
 */
public abstract class any_Throughput_Long extends DefaultHandler implements ResourceBuilder
{
    /**
     * @see com.crash4j.engine.spi.spi.traits.LifecycleHandler#exit(com.crash4j.engine.spi.resources.spi.ResourceSpi, java.lang.Object, java.lang.Object, java.lang.Object, long)
     */
    @Override
    public void exit(ResourceSpi spi, ResourceSpec spec, Object args, Object instance, Object rv, ResourceClosure c, Object ex, long testStartTime, long actualDt, long dt)
    {
        super.exit(spi, spec, args, instance, rv, c, ex, testStartTime, actualDt, dt);
        Action action = this.getAction(spi, spec, args, instance, c);
        if (action == null)
        {
            return;
        }
        StatsManager sMgr = ResourceManagerSpi.getStatsManager();
        sMgr.submit(spi, StatTypes.THROUGHPUT, UnitTypes.MICROSECONDS, action, dt, (Long)rv);
    }
}
