/**
 * @copyright
 */
package com.crash4j.engine.spi.sim.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.crash4j.engine.sim.Behavior;
import com.crash4j.engine.sim.Command;
import com.crash4j.engine.sim.Instruction;
import com.crash4j.engine.sim.Simulation;
import com.crash4j.engine.types.BehaviorTypes;


/**
 * @see com.crash4j.engine.sim.Simulation
 * @author team
 */
public class SimulationImpl implements Runnable, Simulation
{
    /** The tick counter */
    protected int tick = 0;
    
    /** default to tick/second */
    protected int tickFrequency = 1;
    
    protected String name = null;
    
    protected String id = null;

    /** SimulationImpl thread*/
    protected Thread simulationThread = new Thread(this);
    
    /** Boolean that controls thread's run*/
    volatile boolean exitSimulation = false;
    
    /** @see BehaviorImpl class for more details.*/
    protected ArrayList<Behavior> behaviors = new ArrayList<Behavior>();
    protected HashMap<String, Behavior> behaviorIndex = new HashMap<String, Behavior>();
    
    protected _command [] cCommands = null;
    protected _command [] activeCommands = null;
    
    protected ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    protected ReentrantReadWriteLock swapLock = new ReentrantReadWriteLock();
    
    class _command implements Command
    {
        protected Behavior behavior = null;
        protected Instruction inst = null;
        protected boolean enabled = false;
        
        /**
         * @param behavior
         * @param inst
         */
        public _command(Behavior behavior, Instruction inst, boolean enabled)
        {
            this.behavior = behavior;
            this.inst = inst;
            this.enabled = enabled;
            this.inst = inst;
        }

        /**
         * @return the enabled
         */
        public boolean isEnabled()
        {
            return enabled;
        }

        /**
         * @param enabled the enabled to set
         */
        public void setEnabled(boolean enabled)
        {
            this.enabled = enabled;
        }

        @Override
        public Behavior getBehavior()
        {
            return this.behavior;
        }

        @Override
        public Instruction getInstruction()
        {
            return this.inst;
        }

        /**
         * @param inst the inst to set
         */
        public void setInstruction(Instruction inst)
        {
            this.inst = inst;
        }
        
    }
    
    /**
     * @return the id
     */
    public String getId()
    {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(String id)
    {
        this.id = id;
    }
    /**
     * Default constructor
     */
    public SimulationImpl(String name)
    {
        
    }
    /**
     * Initialize with specific tick frequency.
     * @param tf
     */
    public SimulationImpl(String name, int tf)
    {
        this.tickFrequency = tf;
    }
    
    /**
     * @see com.crash4j.engine.sim.Simulation#getCommands()
     */
    @Override
    public Command[] getCommands()
    {
        if (exitSimulation)
        {
            return null;
        }
        
        this.swapLock.readLock().lock();
        try
        {
            return this.activeCommands;
        }
        finally
        {
            this.swapLock.readLock().unlock();
        }
    }
    
    /**
     * Add behaviors to the {@link SimulationImpl}
     * @param b
     */
    public void addBehavior(Behavior b)
    {
        this.lock.writeLock().lock();
        try
        {
            this.behaviors.add(b);
            this.behaviorIndex.put(b.getId(), b);
        }
        finally
        {
            this.lock.writeLock().unlock();
        }
    }
    
    /**
     * @return the tick
     */
    public int getTick()
    {
        return tick;
    }

    /**
     * @param tick the tick to set
     */
    public void setTick(int tick)
    {
        this.tick = tick;
    }

    /**
     * @return the tickFrequency
     */
    public int getTickFrequency()
    {
        return tickFrequency;
    }

    /**
     * @param tickFrequency the tickFrequency to set
     */
    public void setTickFrequency(int tickFrequency)
    {
        this.tickFrequency = tickFrequency;
    }
    
    /**
     * Start the simulation
     */
    public void start()
    {
        exitSimulation = false;
        simulationThread.start();
    }
    
    public void suspendBehavior(String id)
    {
        this.lock.readLock().lock();
        try
        {
            Behavior b = this.behaviorIndex.get(name);
            if (b != null)
            {
                b.suspend();
            }
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }
    
    public void resumeBehavior(String id)
    {
        this.lock.readLock().lock();
        try
        {
            Behavior b = this.behaviorIndex.get(name);
            if (b != null)
            {
                b.resume();
            }
        }
        finally
        {
            this.lock.readLock().unlock();
        }
    }
    
    /**
     * Stop the simulation....
     */
    public void stop()
    {
        exitSimulation = true;
    }

    @Override
    public void run()
    {
        this.tick = 0;
        while (!exitSimulation)
        {
            _command []active = null;
            this.lock.readLock().lock();
            try
            {
                active = new _command[this.behaviors.size()];                
                int j = 0;
                for (Behavior bh : this.behaviors)
                {  
                    if (bh.isSuspended())
                    {
                        continue;
                    }
                    
                    Instruction si = bh.next();
                    boolean enabled = false;
                    if (si != null)
                    {
                        //We know that P() is 0-1 and so we multiply it by a buffer 100 and then use nanoseconds 
                        //in place of random numbers
                        double nano = System.nanoTime();
                        double factor = (nano % 100);
                        double P = (si.getP()*100);
                        enabled = factor <= P;
                    }
                    active[j++] = new _command(bh, si, enabled);
                }
            }
            finally
            {
                this.lock.readLock().unlock();
            }
            
            this.swapLock.writeLock().lock();
            try
            {
                this.activeCommands = active;
            }
            finally
            {
                this.swapLock.writeLock().unlock();
            }
            
            
            try
            {
                TimeUnit.SECONDS.sleep(this.tickFrequency);
            } 
            catch (InterruptedException e)
            {
                if (!exitSimulation)
                {
                    return;
                }
                continue;
            }
            //Move the next tick
            this.tick++;
        }
    }
    /**
     * @see com.crash4j.engine.sim.Simulation#getName()
     */
    @Override
    public String getName()
    {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }
    /**
     * @see com.crash4j.engine.sim.Simulation#getBehaviors()
     */
    @Override
    public Behavior[] getBehaviors()
    {
        return behaviors.toArray(new Behavior[0]);
    }
    
}
