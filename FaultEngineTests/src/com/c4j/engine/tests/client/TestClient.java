package com.c4j.engine.tests.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.SecureRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.crash4j.EngineAdapter;
import com.crash4j.EngineAdapterFactory;
import com.crash4j.annotations.Behavior;
import com.crash4j.annotations.Behaviors;
import com.crash4j.annotations.CrashOutput;
import com.crash4j.annotations.CrashPlan;
import com.crash4j.annotations.Simulation;
import com.crash4j.junit.CrashRunner;


@Behaviors ( sources = 
	{"classpath:/com/c4j/engine/tests/client/testbehaviors.json", 
		"classpath:/com/c4j/engine/tests/client/testbehaviors2.json",
		"classpath:/com/c4j/engine/tests/client/errorsbehaviors.json"
	} )
@CrashOutput( dir=".", every=100)
@RunWith(CrashRunner.class)
public class TestClient
{
	
    static class _test_server implements Runnable
    {
        protected ExecutorService exec = null;
        protected int port = 6553;
        protected int threads = 5;
        public _test_server(int port, int threads)
        {
            this.port = port;
            this.threads = threads;
            exec = Executors.newFixedThreadPool(5);
        }
        
        @Override
        public void run()
        {
            try
            {
                ServerSocket sock = new ServerSocket(this.port);
                Socket ssock = null;
                while ((ssock = sock.accept()) != null)
                {
                    final Socket cli = ssock;
                    exec.submit(new Runnable() {
                        @Override
                        public void run()
                        {
                            try
                            {
                                InputStream iis = cli.getInputStream();
                                OutputStream oss = cli.getOutputStream();
                                int c = -1;
                                while (true)
                                {
                                	long n = System.nanoTime();
                                	c = iis.read();
                                	if (c == -1)
                                	{
                                		break;
                                	}
                                	n = System.nanoTime();
                                    oss.write(c);
                                }
                            } 
                            catch (IOException e)
                            {
                            	e.printStackTrace();
                               try
                               {
                            	   cli.close();
                               } catch (IOException e1)
                               {
                            	   // TODO Auto-generated catch block
                            	   e1.printStackTrace();
                               }
                            }
                        }                        
                    });
                }
                sock.close();
            } 
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
	
    protected static _test_server server = new _test_server(6553, 5);
    protected static Thread serverThread = new Thread(server);
	    
    static EngineAdapter rs = null;

    
    @BeforeClass
    public static void setUpClass() throws Exception
    {
        serverThread.start();

    }
    
    @Before
    public void setUp() throws Exception
    {
        rs = EngineAdapterFactory.getLocalAdapter();
    }

    @After
    public void tearDown() throws Exception
    {
    }


       
    @Test
    @CrashPlan
    (
        iterations=200, 
        concurrency=5,
        simulations= 
        { 
            @Simulation (id="3", name="netsim1", mappings={"net:host=*,port=6553,server=false"}, 
                    behaviors = { 
            		@Behavior ( id="com.crash4j.behaviors.test1" )
            		//@Behavior ( id="com.crash4j.behaviors.errors" ) 
                    } )
        }
    )
    public void simpleSocketTest() throws UnknownHostException, IOException
    {
    	try {
			Thread.sleep(1000);
	        Socket s = new Socket("localhost", 6553);
	        OutputStream oss = s.getOutputStream();
	        InputStream iis = s.getInputStream();
	        for (int i = 0; i < 100; i++)
	        {
	            long nnt = System.nanoTime();
	            //System.out.println(i);
	            oss.write(100);
	            nnt = System.nanoTime();
	            int c = iis.read();
	        }
	        s.close();
		} 
    	catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	finally
    	{
    		//System.out.println("NextSocketTest exit");
    	}
    }

    
    @Test
    @CrashPlan
    (
        iterations=100, 
        concurrency=3,
        simulations= 
        { 
            @Simulation (id="1", name="sim1", mappings={"fsys:mt=*,resource={.*[.][t][x][t]}"}, 
                    behaviors = { @Behavior ( id="com.crash4j.behaviors.test1" ), @Behavior (id="com.crash4j.behaviors.test2") } )
        }
    )
    public void testFileInputStream() throws Exception
    {
        File mf = null;
        mf = File.createTempFile("clienttest_", ".txt");
        File f = new File(mf.getParent(), "testfile.txt");
        
        URLConnection c = f.toURL().openConnection();
        FileOutputStream fos = new FileOutputStream(f);
        byte[] b = SecureRandom.getSeed((int)(System.currentTimeMillis() % 1024));
        for (int i = 0; i < 1000; i++)
        {
            fos.write(b);
        }
        
        fos.close();
        FileInputStream fis = new FileInputStream(f);
        byte [] bb = new byte[1024];
        for (int j = 0; j < 1000; j++)
        {
            fis.read(bb);
        }
        fis.close();
        f.delete();
    }
    
    @Test
    @CrashPlan
    (
        iterations=10, 
        concurrency=1,
        simulations= 
        { 
            @Simulation (id="2", name="sim2", mappings={"fsys:mt=*,resource={.*[.][t][x][t]}"}, 
                    behaviors = { @Behavior ( id="com.crash4j.behaviors.test1" ) } )
        }
    )
    public void testFileChannel() throws Exception
    {
        File mf = null;
        mf = File.createTempFile("channel_", ".txt");
        File f = new File(mf.getParent(), "testChannelFile.txt");
        
        URLConnection c = f.toURL().openConnection();
        FileOutputStream fos = new FileOutputStream(f);
        FileChannel ch = fos.getChannel();
        byte[] b = SecureRandom.getSeed((int)(System.currentTimeMillis() % 1024));
        for (int i = 0; i < 1000; i++)
        {
            ch.write(ByteBuffer.wrap(b));
        }        
        ch.close();
        
        FileInputStream fis = new FileInputStream(f);
        FileChannel rch = fis.getChannel();
        byte [] bb = new byte[1024];
        for (int j = 0; j < 1000; j++)
        {
            rch.read(ByteBuffer.wrap(bb));
        }
        rch.close();
        f.delete();
    }
    
    @Test
    @CrashPlan
    (
        iterations=5, 
        concurrency=1
    )
    public void simpleURLTest() throws UnknownHostException, IOException
    {
        HttpURLConnection c = (HttpURLConnection) new URL("http://www.google.com").openConnection();
        c.setRequestMethod("GET");
        InputStream iis = c.getInputStream();
        
        int cc = 0;
        while ((cc = iis.read()) != -1)
        {
        	//System.out.print((char)cc);
        }
        iis.close();
       
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
			
    }
}
