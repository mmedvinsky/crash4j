package com.c4j.engine.util.tests;

import java.net.InetAddress;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.crash4j.engine.spi.inf.Infrastructure;
import com.crash4j.engine.spi.inf.os.mac.SystemAdapterMAC;
import com.crash4j.junit.CrashRunner;

public class TestSystemCalls
{

    @Before
    public void setUp() throws Exception
    {
        Infrastructure.getInstance();
    }

    @Test
    public void testFilesystem() throws Exception
    {
        //SystemAdapterMAC m = new SystemAdapterMAC();
        //m.getFilesystems();
    }
    
    @Test
    public void testNetSys() throws Exception
    {
        //SystemAdapterMAC m = new SystemAdapterMAC();
        //Enumeration<NetworkInterface> iif = NetworkInterface.getNetworkInterfaces();
        //InetAddress[] addresses = m.traceroute(iif.nextElement(), InetAddress.getByName("www.google.com"));
        //double d = m.ping(InetAddress.getByName("www.google.com"));
        //System.out.println(d);
    }
    
    @Test
    public void testInf() throws Exception
    {
        Infrastructure.getInstance().registerAddress(null, InetAddress.getByName("lwlk405"));
        Thread.sleep(20000);
        InetAddress[] result = Infrastructure.getInstance().lookupRoute(null, InetAddress.getByName("lwlk405"));
        Thread.sleep(5000000);
    }    
    
}
