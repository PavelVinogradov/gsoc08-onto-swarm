// jheatbugs-3.0
//
// Java Heatbugs application. Copyright � 1999-2000 Swarm Development Group.
// This library is distributed without any warranty; without even the
// implied warranty of merchantability or fitness for a particular
// purpose.  See file COPYING for details and terms of copying.
//
// Changes (from jheatbugs-2001-03-28) by Timothy Howe. 

package org.swarm.gsoc.ontology.apps.jheatbug;

import java.awt.*;

import org.swarm.ontology.heatbug.model.MHeatBug;

import swarm.Globals;
import swarm.space.Grid2d;
import swarm.gui.Raster;

/**
See HeatbugModelSwarm for an overview of the heatbugs application.

<p>
A Heatbug is an agent in a 2-dimensional world. A Heatbug has the following
behavior:

 <dir>
 Each Heatbug has its own ideal temperature, and a color that indicates the 
 Heatbug's ideal temperature (more green for cool-loving Heatbugs, more yellow 
 for warmth-loving Heatbugs).
 </dir>

 <dir>
 A Heatbug senses the temperature of the cells in its 9-cell neighborhood.
 </dir>

 <dir>
 A Heatbug has an unhappiness, which is the difference between the Heatbug's 
 ideal temperature and the temperature of the cell where it sits.
 We call a Heatbug unhappy if its unhappiness is non-zero.
 </dir>

 <dir>
 With an arbitrary probability (which is a property of the individual
 Heatbug), an unhappy Heatbug will try to move to a randomly-chosen cell in 
 its 8-cell neighborhood.
 </dir>

 <dir>
 If an unhappy Heatbug does not try to move in the arbitrary fashion described 
 in the previous paragraph, it will try to move to the cell in
 its 8-cell neighborhood whose temperature is closest to
 its ideal temperature. If there is more than once such cell, it will 
 choose at random among the cells with that closest-to-ideal temperature.
 </dir>

 <dir>
 If the cell that a Heatbug tries to move to, as described in the previous two
 paragraphs, is not empty, the Heatbug will make 10 attempts to move to a 
 randomly-chosen empty cell in its 8-cell neighborhood. 
 </dir>

 <dir>
 Two or more Heatbugs may not occupy a given cell simultaneously (but at 
 initialization, double occupancy merely generates a warning). 
 </dir>

 <dir>
 If there is no evaporation in the HeatSpace, heat will increase continually
 until it reaches MAX_HEAT times the number of cells. With evaporation, the 
 Heatbugs will typically become happier as they heat their neighborhoods up 
 from the initial zero temperatures; then, after the heat in many cells hits
 its MAX_HEAT ceiling, they will become unhappier as the capped heat diffuses
 to distant cells; then, after the heat homogenizes, their unhappiness will
 stabilize. Check it out: invoke javaswarm -Dr=0 -De=1 [-Di=1] [-Dc=1] [-Dd=0] 
 StartHeatbugs. 
 </dir>

 <dir>
 If there is no evaporation in the HeatSpace, heat will increase continually
 until it reaches MAX_HEAT times the number of cells. With evaporation, 
 the Heatbugs will typically become happier till a certain time (determined by 
 ideal temperatures, "evaporation" rate, and MAX_HEAT), then become unhappier, 
 and then stabilize. Check it out: invoke javaswarm -Dr=0 -De=1 [-Di=1] [-Dc=1] 
 [-Dd=0] StartHeatbugs. 
 </dir>

 <dir>
 A Heatbug produces heat (the amount is a property of the individual Heatbug).
 It deposits the heat at the cell where it was sitting, not at the cell it is 
 going to.
 </dir>

*/
public class Heatbug extends MHeatBug {
	
    // My location in _world as well as in _heatSpace:
    public int x, y;
    
    // The 2-dimensional world of motion:
    private Grid2d _world;

    // The 2-dimensional world of heat:
    private HeatSpace _heatSpace;
    
    // The model I belong to:
    private HeatbugModelSwarm _model;

    public Heatbug (Grid2d world, HeatSpace heatSpace, HeatbugModelSwarm model,
    		int heatbugIndex, int printDiagnostics) {
    	_world = world;
    	_heatSpace = heatSpace;
    	_model = model;
    	setHeatbugIndex(heatbugIndex);
    	setPrintDiagnostics(printDiagnostics);

    	if (_world == null)
    		System.err.println ("Heatbug was created without a world");

    	if (_heatSpace == null)
    		System.err.println ("Heatbug was created without a heatSpace");

    } /// constructor

    public Object drawSelfOn (Raster raster) {
    	//TODO: Fix Byte to Integer conversion
    	raster.drawPointX$Y$Color (x, y, new Byte((new Integer(getColorIndex())).toString()));
    	return this;
    }

/**
This method defines what the Heatbug does whenever the Schedule triggers it. 

<p>
The method is synchronized, which means the compiler will not let it be 
multi-threaded, which means it cannot be parallelized. It is synchronized 
because to avoid collisions, the Heatbugs must decide one at a time which 
cell to move to. 

<p>
There may be other methods in this simulation that should be synchronized. 

*/
    public synchronized void heatbugStep () {
    	int newX, newY;

    	// Get the heat where I am sitting:
    	int heatHere = _heatSpace.getValueAtX$Y (x, y);

    	// Update my current unhappiness:
    	// int step = _model.getActivity ().getScheduleActivity ().getCurrentTime ();
    	setUnhappiness(Math.abs (getIdealTemperature() - heatHere));

    	if (getUnhappiness() != 0 && ! _model.getImmobile ()) 
    	{

    		double uDR = Globals.env.uniformDblRand.getDoubleWithMin$withMax (0.0, 1.0);
    		if (uDR < getRandomMoveProbability())
    		{
    			if (getPrintDiagnostics() >= 100)
    				System.out.print ("Trying to move randomly ... ");

    			// Pick a random cell within the 9-cell neighborhood, applying
    			// geographic wrap-around:
    			newX =
    				(x + Globals.env.uniformIntRand.getIntegerWithMin$withMax (-1, 1)
    						+ _world.getSizeX ()
    				) % _world.getSizeX ();

    			newY =
    				(y + Globals.env.uniformIntRand.getIntegerWithMin$withMax (-1, 1)
    						+ _world.getSizeY ()
    				) % _world.getSizeY ();
    		} else {
    			if (getPrintDiagnostics() >= 100)
    				System.out.print ("Trying to move rationally ... ");

    			Point scratchPoint = new Point (x, y);
    			
    			// Ask the HeatSpace for a cell in the 9-cell neighborhood
    			// with the closest-to-ideal temperature: 
    			_heatSpace.findExtremeType$X$Y
    			((heatHere < getIdealTemperature() ? HeatSpace.HOT : HeatSpace.COLD),
    					scratchPoint,   // scratchPoint is an inout parameter
    					_world
    			);
            
    			newX = scratchPoint.x;
    			newY = scratchPoint.y;
    		}

    		// ... Whether it chose randomly or rationally, a Heatbug may have
    		// chosen an already-occupied cell -- including the cell it occupies. 
    		// If it chose its own cell, the choice is about
    		// to be rejected, since the code below checks to see whether the cell
    		// is already occupied, without asking which Heatbug is occupying it:
    		if (_world.getObjectAtX$Y (newX, newY) != null)
    		{
    			int tries = 0;
    			int location, xm1, xp1, ym1, yp1;
    			
    			// 10 is an arbitrary choice for the number of tries; it is
    			// *not* implied by the number of cells in the neighborhood:
    			while ((_world.getObjectAtX$Y (newX, newY) != null) && 
    					(tries < 10)
                  	)
    			{
    				// Choose randomly among the 8 cells in the neighborhood
    				location = Globals.env.uniformIntRand.getIntegerWithMin$withMax (1,8);
    				xm1 = (x + _world.getSizeX () - 1) % _world.getSizeX ();
    				xp1 = (x + 1) % _world.getSizeX ();
    				ym1 = (y + _world.getSizeY () - 1) % _world.getSizeY ();
    				yp1 = (y + 1) % _world.getSizeY ();
                
    				switch (location) {
    					case 1:  
    						newX = xm1; newY = ym1;   // NW
    						break;  
    					
    					case 2:
    						newX = x ; newY = ym1;    // N
    						break;  
    					
    					case 3:
    						newX = xp1 ; newY = ym1;  // NE
    						break;  
    					
    					case 4:
    						newX = xm1 ; newY = y;    // W
    						break;  

    					case 5:
    						newX = xp1 ; newY = y;    // E
    						break;  

    					case 6:
    						newX = xm1 ; newY = yp1;  // SW
    						break;  

    					case 7:
    						newX = x ; newY = yp1;    // S
    						break;  
                
    					case 8:
    						newX = xp1 ; newY = yp1;  // SE
    					
    					default:
    						break;
    				}
    				tries++;
    			}
    			if (tries == 10)
    			{
    				if (getPrintDiagnostics() >= 100)
    					System.out.println ("no, staying put ... ");
    				newX = x;
    				newY = y;
    			} else {
    				if (getPrintDiagnostics() >= 100)
    					System.out.println ("no, desperately ... ");
    			}
    		}

    		_heatSpace.addHeat (getOutputHeat(), x, y);
    		_world.putObject$atX$Y (null, x, y);
    		x = newX;
    		y = newY;
    		_world.putObject$atX$Y (this, x, y);

    	} /// if unhappiness != 0 
    	 else {
    		 if (getPrintDiagnostics() >= 100)
    		 {
    			 System.out.println ("Too happy to move ... ");
    		 }
    		 _heatSpace.addHeat (getOutputHeat(), x, y);
    	 }

    	if (getPrintDiagnostics() >= 100)
    		System.out.println ("Heatbug " + this);

    } /// heatbugStep()

    /**
		This method does not check to see whether the target cell is already occupied.
     */
    public Object putAtX$Y (int inX, int inY) {
    	x = inX;
    	y = inY;
    	_world.putObject$atX$Y (this, x, y);
    	return this;
    }

    /**
		The Java compiler will invoke this method whenever we use a Heatbug where the
		compiler is expecting a String. That gives us an easy way to print diagnostics;
		for example, System.out.println ("I initialized Heatbug " + heatbug + ".");.
     */
    public String toString () {
    	return getHeatbugIndex() + " at (" + x + "," + y + "), heat " + 
    	_heatSpace.getValueAtX$Y (x, y);
    }

} /// class Heatbug