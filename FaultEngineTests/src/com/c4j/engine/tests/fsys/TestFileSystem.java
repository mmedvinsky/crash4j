package com.c4j.engine.tests.fsys;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.SecureRandom;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.c4j.engine.tests.TraceStatsCollector;
import com.crash4j.engine.spi.ResourceManagerSpi;
import com.crash4j.engine.spi.sim.impl.BehaviorImpl;
import com.crash4j.engine.spi.sim.impl.SimulationImpl;
import com.crash4j.engine.types.InstructionTypes;


public class TestFileSystem
{
    protected TraceStatsCollector coll = new TraceStatsCollector();

    
    @Before
    public void setUp() throws Exception
    {
        SimulationImpl s = new SimulationImpl("TestSim", 1);
        String id = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        s.setId(id);
        BehaviorImpl b1 = new BehaviorImpl("delay_delete", InstructionTypes.DELAY);
        b1.addAction("d:*");
        b1.setRotate(true);        
        b1.addInstruction(0, 0.9, 0.1);
        b1.addInstruction(5, 0.9, 0.3);
        b1.addInstruction(10, 0.9, 0.4);
        b1.addInstruction(20, 0.9, 0.2);
        b1.addInstruction(30, 0.9, 1.5);
        b1.addInstruction(40, 0.9, 2.6);
        b1.addInstruction(50, 0.9, 1.7);
        b1.addInstruction(60, 0.9, 2.8);
        b1.addInstruction(80, 0.9, 0.1);
        s.addBehavior(b1);
        ResourceManagerSpi.getSimulationManager().addSimulation(s);
        ResourceManagerSpi.getSimulationManager().addSimulationMapping("fsys:mt=/dev/disk1,resource=*", id);
        ResourceManagerSpi.getSimulationManager().addSimulationMapping("net:lp=*,rp=*,inf=*,la=*,ra=*,mode=*,protocol=*", id2);
        ResourceManagerSpi.getSimulationManager().startAll();
    }

    @After
    public void tearDown() throws Exception
    {
        ResourceManagerSpi.collectStats(coll);
        System.out.println(coll.getReport().toString());
    }
    
    @Test
    public void testFilesystemFunctions() throws IOException
    {
        
        try
        {
            this.fileCreationAndDeltetion();
            this.fileChannel();
            this.fileReadWrite();
            this.fileWithURLConnection();
            
            //Thread.sleep(10000);
        } catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void fileWithURLConnection() throws IOException, InterruptedException
    {
        File f = null;
        f = File.createTempFile("c4j_url", ".txt");
        URLConnection c = f.toURL().openConnection();
        f.delete();
    }
    
    public void fileChannel() throws IOException, InterruptedException
    {
        File f = null;
        f = File.createTempFile("c4j_ch", ".txt");
       
        FileOutputStream fos = new FileOutputStream(f);
        FileInputStream fis = new FileInputStream(f);
        
        final FileChannel fc = fos.getChannel();
        final FileChannel fcr = fis.getChannel();
       
        Runnable writeT = new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    for (int i = 0; i < 10000; i++)
                    {
                        byte[] b = SecureRandom.getSeed((int)(System.currentTimeMillis() % 1024));
                        fc.write(ByteBuffer.wrap(b));
                        TimeUnit.MICROSECONDS.sleep(500);
                    }
                    
                } catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        Runnable readT = new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    for (int i = 0; i < 10000; i++)
                    {
                        fcr.read(ByteBuffer.allocate(1));
                        TimeUnit.MICROSECONDS.sleep(500);
                    }
                    
                } catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        
        
        Thread t[] = new Thread[2];
        t[0] = new Thread(writeT);
        t[1] = new Thread(readT);
        t[0].start();
        t[1].start();
        for (Thread thread : t)
        {
            thread.join();
        }
        f.delete();
    }
    
    
    public void fileReadWrite() throws IOException, InterruptedException
    {
        File f = null;
        f = File.createTempFile("c4j_wr", ".txt");
        FileOutputStream fos = new FileOutputStream(f);
        int tb = 0;
        for (int i = 0; i < 100; i++)
        {
            byte[] b = SecureRandom.getSeed((int)(System.currentTimeMillis() % 1024));
            fos.write(b);
            tb += b.length;
        }
        fos.close();
        FileInputStream fis = new FileInputStream(f);
        int c = 0;
        while ((c = fis.read()) != -1)
        {
            tb--;
        }
        
        f.delete();
        Assert.assertEquals(true, tb == 0);
    }
    
    public void fileCreationAndDeltetion() throws IOException, InterruptedException
    {
        File f = null;
        f = File.createTempFile("c4jtest", ".txt");
        for (int i = 0; i < 100; i++)
        {
            Assert.assertEquals(true, f.exists());
            Assert.assertEquals(false, f.isDirectory());
            Assert.assertEquals(false, f.canExecute());
        }
        f.delete();
    }
}
