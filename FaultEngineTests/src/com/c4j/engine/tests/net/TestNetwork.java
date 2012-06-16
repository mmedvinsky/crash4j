/**
 * 
 */
package com.c4j.engine.tests.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.SecureRandom;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.c4j.engine.tests.TraceStatsCollector;
import com.crash4j.engine.spi.ResourceManagerSpi;
import com.crash4j.engine.spi.sim.impl.BehaviorImpl;
import com.crash4j.engine.spi.sim.impl.SimulationImpl;
import com.crash4j.engine.types.InstructionTypes;

/**
 * @author <MM>
 *
 */
public class TestNetwork
{
    class _test_server implements Runnable
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
                                int c = -1;
                                while ((c = iis.read()) != -1)
                                {
                                    cli.getOutputStream().write(c);
                                }
                            } 
                            catch (IOException e)
                            {
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
    
    protected _test_server server = new _test_server(6553, 5);
    protected Thread serverThread = new Thread(server);
    protected TraceStatsCollector coll = new TraceStatsCollector();

    
    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception
    {
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        serverThread.start();
        Thread.sleep(1000);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
        serverThread.stop();
        ResourceManagerSpi.collectStats(coll);
        System.out.println(coll.getReport().toString());
    }

    public void urlTest() throws UnknownHostException, IOException
    {
        URL u = new URL("http://localhost:8080/FaultService/profile.xml");
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        InputStreamReader iis = new InputStreamReader(conn.getInputStream());
        char []buf = new char[2];
        int n = 0;
        StringBuffer b = new StringBuffer();
        while ((n = iis.read(buf)) != -1)
        {
            b.append(buf, 0, n);
        }
        iis.close();
        conn.disconnect();
    }
    
    //Simple socket channel test.....
    public void socketChannelTest() throws UnknownHostException, IOException
    {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("www.google.com", 80));
        //sc.connect(new InetSocketAddress("www.google.com", 80));
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        OutputStreamWriter wr = new OutputStreamWriter(bos);
        wr.write("GET / HTTP/1.1\r\nHost: localhost\r\n\r\n");
        wr.flush();
        sc.write(ByteBuffer.wrap(bos.toByteArray()));
        byte[] b = new byte[1024];
        ByteBuffer bbuf = ByteBuffer.wrap(b);
        StringBuilder sb = new StringBuilder();
        while (true)
        {
            int n = sc.read(bbuf);
            for (int i = 0; i < n; i++)
            {
                sb.append((char)bbuf.get(i));
            }
            System.out.println(sb.toString());
            break;
        }
        sc.close();
    }
    
    public void simpleSocketTest() throws UnknownHostException, IOException
    {
        
        Socket s = new Socket("localhost", 6553);
        for (int i = 0; i < 1000; i++)
        {
            s.getOutputStream().write(100);
            s.getInputStream().read();
        }
        s.close();
    }

    
    
    /**
     * Datagram tests
     */
    public void datagramChannelTest()
    {
        Runnable r = new Runnable()
        {
            @Override
            public void run()
            {
                DatagramSocket ds;
                try
                {
                    DatagramChannel dc = DatagramChannel.open();
                    SocketAddress ss = new InetSocketAddress(InetAddress.getLocalHost(), 5557);
                    try
                    {
                        byte[] b = SecureRandom.getSeed((int)(System.currentTimeMillis() % 1024));
                        for (int i = 0; i < 100; i++)
                        {
                            try
                            {
                                dc.send(ByteBuffer.wrap(b), ss);                                
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                        dc.disconnect();
                    } catch (UnknownHostException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } 
                } catch (SocketException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
        };
        
        
        Runnable r2 = new Runnable()
        {

            @Override
            public void run()
            {
                DatagramChannel dc = null;
                try
                {
                    dc = DatagramChannel.open();
                    dc.socket().bind(new InetSocketAddress(5557));
                } catch (IOException e1)
                {
                    e1.printStackTrace();
                }
                try
                {
                    byte[] b = new byte[1024];
                    for (int i = 0; i < 100; i++)
                    {
                        try
                        {
                            dc.receive(ByteBuffer.wrap(b));      
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    dc.close();
                } catch (SocketException e)
                {
                    e.printStackTrace();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            
        };
        
        
        Thread th = new Thread(r2);
        th.start();
        try
        {
            Thread.sleep(2000);
        } catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Thread th2 = new Thread(r);
        th2.start();
        
        try
        {
            th.join();
            th2.join();
        } catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
    
    
    /**
     * Datagram tests
     */
    public void datagramTest()
    {
        Runnable r = new Runnable()
        {

            @Override
            public void run()
            {
                DatagramSocket ds;
                try
                {
                    ds = new DatagramSocket();
                    try
                    {
                        byte[] b = SecureRandom.getSeed((int)(System.currentTimeMillis() % 1024));
                        DatagramPacket p = new DatagramPacket(b, b.length, InetAddress.getLocalHost(), 5556);
                        for (int i = 0; i < 10; i++)
                        {
                            ds.send(p);
                            Thread.sleep(1000);
                        }
                        ds.close();
                    } catch (UnknownHostException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (InterruptedException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } catch (SocketException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
        };
        
        
        Runnable r2 = new Runnable()
        {

            @Override
            public void run()
            {
                DatagramSocket ds;
                try
                {
                    ds = new DatagramSocket(5556);
                    byte[] b = new byte[1024];
                    DatagramPacket p = new DatagramPacket(b, b.length);
                    for (int i = 0; i < 10; i++)
                    {
                        ds.receive(p);
                    }
                    ds.close();
                } catch (SocketException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
        };
        
        
        Thread th = new Thread(r2);
        th.start();
        try
        {
            Thread.sleep(2000);
        } catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Thread th2 = new Thread(r);
        th2.start();
        
        try
        {
            th.join();
            th2.join();
        } catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
    public void serverSocketChannelTest() throws IOException
    {
        ServerSocketChannel ch = ServerSocketChannel.open();
        Runnable t = new Runnable()
        {

            @Override
            public void run()
            {
                try
                {
                    SocketChannel cs  = SocketChannel.open();
                    SocketChannel cs1 = cs.socket().getChannel();
                    cs.socket().connect(new InetSocketAddress("localhost", 5554));
                    byte[] b = {0, 1, 2, 3};
                    cs.write(ByteBuffer.wrap(b));
                } catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            }
            
        };
        
        
        ServerSocket s = ch.socket();
        s.bind(new InetSocketAddress(5554));
        SocketChannel sch = null;
        ByteBuffer b = ByteBuffer.allocate(1024);
        
        (new Thread(t)).start();
        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        while ((sch = ch.accept()) != null)
        {
            sch.read(b);
            break;
        }
        sch.close();
    }
    
    @Test
    public void testNet() throws UnknownHostException, IOException
    {
        try
        {
            serverSocketChannelTest();
            System.out.println("serverSocketChannelTest done");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            socketChannelTest();
            System.out.println("socketChannelTest done");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            simpleSocketTest();
            System.out.println("simpleSocketTest done");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            urlTest();
            System.out.println("urlTest done");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            datagramChannelTest();
            System.out.println("datagramChannelTest done");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            datagramTest();
            System.out.println("datagramTest done");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
