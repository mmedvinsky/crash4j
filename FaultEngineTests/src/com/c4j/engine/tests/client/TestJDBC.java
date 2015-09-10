package com.c4j.engine.tests.client;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.crash4j.annotations.Behavior;
import com.crash4j.annotations.Behaviors;
import com.crash4j.annotations.CrashOutput;
import com.crash4j.annotations.CrashPlan;
import com.crash4j.annotations.Simulation;
import com.crash4j.junit.CrashRunner;
import com.mysql.jdbc.Driver;


@Behaviors ( sources = 
{"classpath:/com/c4j/engine/tests/client/dberrors.json", 
	"classpath:/com/c4j/engine/tests/client/dbclose.json"
} )
@CrashOutput( dir=".", every=100)
@RunWith(CrashRunner.class)
public class TestJDBC 
{
	static Connection conn;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		DriverManager.registerDriver(new Driver());
	    conn = DriverManager.getConnection("jdbc:mysql://localhost/plastiq_trans", "puser", "OR4+0ry");
	}

	@Test
    @CrashPlan
    (
        iterations=40, 
        concurrency=1,
        simulations= 
        { 
            @Simulation (id="100", name="dbclosecrush", mappings={"db:url=*,driver=*,vendor=*"}, 
                    behaviors = { 
            		//@Behavior ( id="com.crash4j.behaviors.sql.test.errors" )
            		//@Behavior ( id="com.crash4j.behaviors.sql.close" )
                    } )
        }
    )
	public void testSimpleStatement() 
	{
		try {
			PreparedStatement p = conn.prepareStatement("Select * from trans");
			p.execute();	

			CallableStatement cp = conn.prepareCall("CALL plastiq_provider_get_settlement_rule('dmwedwedmwedmwemd')");
			cp.execute();
			
			PreparedStatement d = conn.prepareStatement("delete from tjournal");
			d.execute();
			
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
