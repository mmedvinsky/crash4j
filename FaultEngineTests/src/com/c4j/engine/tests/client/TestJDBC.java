package com.c4j.engine.tests.client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.crash4j.annotations.CrashOutput;
import com.crash4j.annotations.CrashPlan;
import com.crash4j.engine.spi.ResourceManagerSpi;
import com.crash4j.engine.spi.resources.ResourceSpi;
import com.crash4j.junit.CrashRunner;
import com.mysql.jdbc.Driver;


@CrashOutput( dir=".", every=100)
@RunWith(CrashRunner.class)
public class TestJDBC {
	static Connection conn;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		DriverManager.registerDriver(new Driver());
	    conn = DriverManager.getConnection("jdbc:mysql://localhost/dilemmas", "c4jtest", "abcd123");
	}

	@Test
    @CrashPlan ( iterations=100, concurrency=1 )
	public void testSimpleStatement() 
	{
		try {
			PreparedStatement p = conn.prepareStatement("Select * from dilemmas");
			p.execute();	
			PreparedStatement d = conn.prepareStatement("delete from dilemmas where dilemmaid='8a80818936646f57013664708e292'");
			d.execute();
			PreparedStatement u = conn.prepareStatement("update dilemmas set description='hello' where dilemmaid='8a80818936646f57013664708e290002'");
			u.execute();
			
			
			/*
			Collection<ResourceSpi> rr = ResourceManagerSpi.getResourceSpis().values();
			for (ResourceSpi resourceSpi : rr) {
				if (!resourceSpi.getResourceType().equals("fsys"))
				{
					System.out.println(resourceSpi.getETag()+" "+resourceSpi.getVector().toString());
				}
			}
		    */
			Thread.sleep(100);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
