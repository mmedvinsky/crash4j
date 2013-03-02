package com.c4j.engine.util.tests;

import org.junit.Test;

import com.crash4j.engine.sim.Command;
import com.crash4j.engine.spi.sim.impl.BehaviorImpl;
import com.crash4j.engine.spi.sim.impl.SimulationImpl;
import com.crash4j.engine.types.InstructionTypes;


public class TestSimulation
{

    @Test
    public void testSimulationObject() throws InterruptedException
    {
        SimulationImpl s = new SimulationImpl("Sim1", 2);
        BehaviorImpl b1 = new BehaviorImpl("Test1", InstructionTypes.DELAY);
        b1.addAction("r:*");
        b1.setStop(true);        
        b1.addInstruction(0, 0.9, 0.1);
        b1.addInstruction(10, 0.9, 0.3);
        b1.addInstruction(20, 0.9, 0.4);
        b1.addInstruction(30, 0.9, 0.2);
        b1.addInstruction(40, 0.9, 0.5);
        b1.addInstruction(50, 0.9, 0.6);
        b1.addInstruction(60, 0.9, 0.7);
        b1.addInstruction(70, 0.9, 0.8);
        b1.addInstruction(80, 0.9, 1.0);
        BehaviorImpl b2 = new BehaviorImpl("Test2", InstructionTypes.DELAY);
        b1.addAction("w:*");
        b2.setRetain(true);        
        b2.addInstruction(0, 0.9, 0.13);
        b2.addInstruction(2, 0.2, 0.32);
        b2.addInstruction(10, 0.3, 0.42);
        b2.addInstruction(120, 0.1, 0.24);

        s.addBehavior(b1);
        s.addBehavior(b2);
        s.start();
        for (int i = 0; i < 1000; i++)
        {
            Command[] ins = s.getCommands();
            if (ins != null)
            {
                for (int j = 0; j < ins.length; j++)
                {
                    if (ins[j] != null && ins[j].getInstruction() != null)
                    {
                        if(ins[j].isEnabled())
                        {
                            System.out.println(ins[j].getBehavior().getName()+" "+i+" "+ins[j].getInstruction().getTick()+" "+ins[j].getInstruction().getP()+" "+ins[j].getInstruction().getParameter()+" enabled");
                    
                        }
                        else
                        {
                            System.out.println(ins[j].getBehavior().getName()+" "+i+" "+ins[j].getInstruction().getTick()+" "+ins[j].getInstruction().getP()+" "+ins[j].getInstruction().getParameter()+" disabled");
                        }
                    }
                }
            }
            Thread.sleep(1500);
        }
        Thread.sleep(10000);
    }

}
