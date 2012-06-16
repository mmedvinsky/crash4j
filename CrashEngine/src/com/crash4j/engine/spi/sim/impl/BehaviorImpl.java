/**
 * @copyright
 */
package com.crash4j.engine.spi.sim.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.crash4j.engine.Action;
import com.crash4j.engine.sim.Behavior;
import com.crash4j.engine.sim.Instruction;
import com.crash4j.engine.types.ActionClasses;
import com.crash4j.engine.types.BehaviorTypes;
import com.crash4j.engine.types.InstructionTypes;

/**
 * BehaviorImpl defines a single set of actions that needs to take place from time t()
 * 
 * @author team
 *
 */
public class BehaviorImpl implements Behavior
{
    protected InstructionTypes iType = null;
    protected BehaviorTypes bType = null;
    protected int position = -1;
    protected int tick = 0;
    protected Instruction current = null;
    protected ArrayList<InstructionImpl> simdata = new ArrayList<InstructionImpl>();
    protected boolean rotate = false;
    protected boolean retain = true;
    protected boolean stop = false;
    protected volatile boolean suspended = false;
    protected String name = null;
    protected String id = null;
    
    protected int actions[][] = new int [ActionClasses.values().length][2];

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
     * @return the action
     */
    public Set<Action> getActions()
    {
    	HashSet<Action> a= new HashSet<Action>();
    	for (Object la : this.actions) 
    	{
    		List<Action> list = ((List<Action>)la);
    		if (list != null)
    		{
    			a.addAll(list);
    		}
		}
        return a;
    }


    /**
     * @param action the action to set
     */
    public void addAction(String action)
    {
    	String acts[] = action.split("[:]");    	
    	ActionClasses cla = ActionClasses.valueOf(acts[0].toUpperCase());
    	this.actions[cla.ordinal()][0] |= (cla == null ? 0 : cla.toMask());
    	int m = (cla == null ? 0 : cla.getClassActionTypeMaskFromString(acts[1].toUpperCase()));
    	if (m > 0)
    	{
    		this.actions[cla.ordinal()][1] |= m;
    	}
    }

    /**
     * @return the suspended
     */
    public boolean isSuspended()
    {
        return suspended;
    }

    /**
     * @param suspended the suspended to set
     */
    public void setSuspended(boolean suspended)
    {
        this.suspended = suspended;
    }

    /**
     * @see com.crash4j.engine.sim.Behavior#isStop()
     */
    @Override
    public boolean isStop()
    {
        return stop;
    }

    /**
     * @return the type
     */
    public InstructionTypes getType()
    {
        return iType;
    }

    /**
     * @param type the type to set
     */
    public void setType(InstructionTypes type)
    {
        this.iType = type;
    }

    /**
     * @param stop the stop to set
     */
    public void setStop(boolean stop)
    {
        this.stop = stop;
        this.retain = !stop;
        this.rotate = !stop;
    }

    /**
     * @see com.crash4j.engine.sim.Behavior#getName()
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
     * @see com.crash4j.engine.sim.Behavior#getInstruction()
     */
    @Override
    public Instruction getInstruction()
    {
        return current;
    }
    
    /**
     * @see com.crash4j.engine.sim.Behavior#isRetain()
     */
    @Override
    public boolean isRetain()
    {
        return retain;
    }

    /**
     * @param retain the retain to set
     */
    public void setRetain(boolean retain)
    {
        this.retain = retain;
        this.stop = !retain;
        this.rotate = !retain;
    }

    /**
     * @see com.crash4j.engine.sim.Behavior#isRotate()
     */
    @Override
    public boolean isRotate()
    {
        return rotate;
    }

    /**
     * @param rotate the rotate to set
     */
    public void setRotate(boolean rotate)
    {
        this.rotate = rotate;
        this.retain = !rotate;
        this.stop = !rotate;
    }

    /**
     * Lookahead towards the next instruction and see if the starting tick is not hit yet 
     * 
     * @return next {@link InstructionImpl} for the next tick;
     */
    public Instruction next()
    {
        Instruction nci = null;
        int sz = this.simdata.size();
        
        if (position >= sz)
        {
            if (this.isRotate())
            {
                position = 0;
                current = this.simdata.get(position);
                tick = 1; //restart again
            }
            else if (this.isStop())
            {
                current = null;
            }
            return current;
        }
        
        //One before last
        if ((position + 1) >= sz)
        {
            position++;
        }
        else
        {
            nci = this.simdata.get(position + 1);
            if (nci.getTick() <= tick)
            {
                current = nci;
                position++;
            }
        }
        //advance to next tick in the simulation.
        tick++;
        return current;
    }
    /**
     * @see com.crash4j.engine.sim.Behavior#addInstruction(long, double, double)
     */
    public void addInstruction(long tn, double p, double w)
    {
        if (this.simdata.isEmpty())
        {
            this.simdata.add(new InstructionImpl(tn, p, w));
            return;
        }
        for (int i = 0; i < this.simdata.size(); i++)
        {
            if (this.simdata.get(i).getTick() > tn)
            {
                this.simdata.add(i, new InstructionImpl(tn, p, w));
                return;
            }
        }
        this.simdata.add(new InstructionImpl(tn, p, w));
    }
    /**
     * @return {@link BehaviorTypes} that was selected for this behavior
     */
    public BehaviorTypes getBehaviorType()
    {
        return this.bType;
    }
    /**
     * Set {@link BehaviorTypes} that was selected for this behavior
     */
    public void setBehaviorType(BehaviorTypes t)
    {
        this.bType = t;
    }
    /**
     * @param name of this behavior
     * @param type is a {@link InstructionTypes} of this instance 
     * @param IOTypes enum for at least one action has to be associated with behavior. 
     */
    public BehaviorImpl(String name, InstructionTypes type)
    {
        this.iType = type;
        this.name = name;
        this.bType = BehaviorTypes.STOCHASTIC_RELATIVE;
    }
    
    /**
     * @param name of this behavior
     * @param type is a {@link InstructionTypes} of this instance 
     * @param IOTypes enum for at least one action has to be associated with behavior. 
     */
    public BehaviorImpl(String name, String id, InstructionTypes type)
    {
        this.bType = BehaviorTypes.STOCHASTIC_RELATIVE;
        this.iType = type;
        this.name = name;
        this.id = id;
    }

    /**
     * @param name of this behavior
     * @param type is a {@link InstructionTypes} of this instance 
     * @param IOTypes enum for at least one action has to be associated with behavior. 
     */
    public BehaviorImpl(String name, String id, InstructionTypes type, BehaviorTypes t)
    {
        this.iType = type;
        this.name = name;
        this.id = id;
        this.bType = t;
    }
    
    @Override
    public boolean shouldEffect(Action action)
    {
		int sela = action.getActionClass().ordinal();
    	if ((this.actions[sela][0] & action.getActionClass().toMask()) 
    			== action.getActionClass().toMask())
    	{
    		if (this.actions[sela][1] > 0)
    		{
    			int ms = action.getActionClass().getClassActionTypeMask(action.getActionClassTypes());
    			return ((this.actions[sela][1] & ms) == ms);
    		}
    	}
    	return false;
    }

    @Override
    public void suspend()
    {
        this.setSuspended(true);
    }

    @Override
    public void resume()
    {
        this.setSuspended(false);
    }

    @Override
    public Instruction[] getInstructions()
    {
        return this.simdata.toArray(new Instruction[0]);
    }
}
