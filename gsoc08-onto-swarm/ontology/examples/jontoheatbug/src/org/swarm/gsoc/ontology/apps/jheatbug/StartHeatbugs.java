// jheatbugs-3.0
//
// Java Heatbugs application. Copyright � 1999-2000 Swarm Development Group.
// This library is distributed without any warranty; without even the
// implied warranty of merchantability or fitness for a particular
// purpose.  See file COPYING for details and terms of copying.
//
// Changes (from jheatbugs-2001-03-28) by Timothy Howe. 

package org.swarm.gsoc.ontology.apps.jheatbug;

import swarm.Globals;
import swarm.objectbase.Swarm;
import org.swarm.gsoc.ontology.apps.Utils;

/**

<p>
See HeatbugModelSwarm for an overview of the jheatbugs program.

<p>
The remainder of this discussion is confined to the issue of command-line
parameters. 

<p> <b> Controlling this program </b>

<p>
The following Java properties are recognized by this program.

 <dir>
 c=&lt;boolean&gt;: Start the Heatbugs all in a contiguous cluster
 </dir>

 <dir>
 d=&lt;double&gt;:  Specify the diffusion rate (0..1)
 </dir>

 <dir>
 e=&lt;double&gt;:  Specify the "evaporation" (really retention) rate (0..1)
 </dir>

 <dir>
 i=&lt;boolean&gt;: Make Heatbugs immobile
 </dir>

 <dir>
 n=&lt;int&gt;:     Specify the number of heatbugs
 </dir>

 <dir>
 p=&lt;int&gt;:     Specify level of diagnostic messages (try 10 or 100)
 </dir>

 <dir>
 r=&lt;double&gt;:  Specify the random-move probability
 </dir>

<p>
You can set Java properties on the java command line; for example, invoke

 <dir>
 <xmp>
  javaswarm -Dn=300 StartHeatbugs
 </xmp>
 </dir>

<p>
to start jheatbugs in GUI mode with 300 Heatbugs, or invoke

 <dir>
 <xmp>
  javaswarm -Dn=300 StartHeatbugs -b
 </xmp>
 </dir>

<p>
to start jheatbugs in batch mode with 300 Heatbugs.

<p> <b> Precedence of controls </b>

<p>
In batch mode, the Java properties mechanism herein 
takes precedence over all other setting of variables, 
overriding the SCM file, 
which itself overrides the Java initializers and constructors. 

<p>
In GUI mode, the probe display 
takes precedence over all other setting of variables, 
overriding the Java properties mechanism herein, 
which itself overrides the Java initializers and constructors. 

<p> <b> Modifying this program's properties </b>

<p>
To modify this program to accept an additional boolean property:

 <dir>
 Add to this documentation section a line analogous to 
  <dir>
  <xmp>
    c=&lt;boolean&gt;: Start the Heatbugs all in a contiguous cluster
  </xmp>
  </dir>
 </dir>

 <dir>
 and add to the code a statement analogous to 
  <dir>
  <xmp>
    model.setStartInOneCluster (getBooleanProperty ("c", false));
  </xmp>
  </dir>
 </dir>

<p>
To modify this program to accept an additional double property:

 <dir>
 Add to this documentation section a line analogous to 
  <dir>
  <xmp>
    r=&lt;double&gt;:  Specify the random-move probability
  </xmp>
  </dir>
 </dir>

 <dir>
 If you do not want this file to apply a default
 overriding the model's default, 
 add to the code a statement analogous to 
  <dir>
  <xmp>
    model.setRandomMoveProbability 
     (getDoubleProperty ("r", model.getRandomMoveProbability ()));
  </xmp>
  </dir>

 If you do want this file to apply a default
 overriding the model's default,
 add a statement analogous to 

  <dir>
  <xmp>
    model.setRandomMoveProbability (getDoubleProperty ("r", 0.5));
  </xmp>
  </dir>
 </dir>

<p>
Similarly for int properties, String properties, etc.

*/
public class StartHeatbugs
{

public static void main (String[] args)
{
    // Swarm initialization: all Swarm apps must call this first:
	System.out.println ("This is StartHeatbugs.main().");
    Globals.env.initSwarm
     ("jheatbugs", "2.1", "bug-swarm@swarm.org", args);
    HeatbugModelSwarm model;
    
    if (Globals.env.guiFlag)
    {
        // We want graphics, so make an ObserverSwarm to get GUI objects:
        HeatbugObserverSwarm topLevelSwarm 
         = new HeatbugObserverSwarm (Globals.env.globalZone);
        Globals.env.setWindowGeometryRecordName (topLevelSwarm, "topLevelSwarm");

        model = topLevelSwarm.getHeatbugModelSwarm ();
        build (topLevelSwarm, model);
        topLevelSwarm.go ();
        unbuild (topLevelSwarm, model);
    }
    else
    {
        // We do not want graphics, so make a BatchSwarm for writing to files: 
        HeatbugBatchSwarm topLevelSwarm
         = (HeatbugBatchSwarm) Globals.env.lispAppArchiver.getWithZone$key
         (Globals.env.globalZone, "batchSwarm");
        
        model = topLevelSwarm.getHeatbugModelSwarm ();
        build (topLevelSwarm, model);
        topLevelSwarm.go ();
        unbuild (topLevelSwarm, model);
    }
} /// main()

private static void build (Swarm swarm, HeatbugModelSwarm model)
{
    // ... We'd like to move as much code as possible from the if/then sections
    // in main() into this common method build(), but if we move 
    // getHeatbugModelSwarm(), we get this compile error: *** Error: No method 
    // named "getHeatbugModelSwarm" was found in type "swarm/objectbase/Swarm". 
    // Similarly when we try to move go(). So we moved the calls we could
    // move and left getHeatbugModelSwarm() and go() behind. 

    // We could pass immobile to the batch buildObjects(), but we get a compile
    // error if we try to pass it to the GUI buildObjects(). So instead we use 
    // setImmobile() for both:
    model.setImmobile (Utils.getBooleanProperty ("i", false));

    model.setPrintDiagnostics (Utils.getIntProperty ("p", 0));

    model.setStartInOneCluster (Utils.getBooleanProperty ("c", false));

    model.setRandomMoveProbability 
     (Utils.getDoubleProperty ("r", model.getRandomMoveProbability ()));
      // ... In cases such as this, when we happen not to want to override any 
      // default set by HeatbugModelSwarm, we apply that value as our own 
      // default. 

    model.setDiffusionConstant 
     (Utils.getDoubleProperty ("d", model.getDiffusionConstant ()));

    model.setEvaporationRate 
     (Utils.getDoubleProperty ("e", model.getEvaporationRate ()));

    model.setNumBugs (Utils.getIntProperty ("n", -1));
    // ... The special value -1 tells HeatbugModelSwarm to use its own default
    // value for numBugs. Use of Integer rather than int would avoid the need
    // for this special value. 

    swarm.buildObjects ();
    swarm.buildActions ();
    swarm.activateIn (null);
} /// build()

private static void unbuild (Swarm swarm, HeatbugModelSwarm model)
{
    swarm.drop ();
}

}
