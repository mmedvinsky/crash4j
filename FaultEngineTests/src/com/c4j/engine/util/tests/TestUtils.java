/**
 * @copyright
 */
package com.c4j.engine.util.tests;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.crash4j.engine.spi.util.HRTime;

/**
 * @author team
 *
 */
public class TestUtils
{

    @Test
    public void testHRTime() throws Exception
    {
        Object u = new Object();
        HRTime.callibrate(Short.MAX_VALUE);
        long n = System.nanoTime();
        HRTime.delay(1000);
        long r = System.nanoTime() - n;
        System.out.println(r);
    }

}
