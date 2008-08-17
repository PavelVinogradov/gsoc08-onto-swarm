package net.nixdev.gsoc08.jena;
/*****************************************************************************
 * Source code information
 * -----------------------
 * Original author    Ian Dickinson, HP Labs Bristol
 * Author email       ian.dickinson@hp.com
 * Package            Jena 2
 * Web                http://sourceforge.net/projects/jena/
 * Created            25-Jul-2003
 * Filename           $RCSfile: PersistentOntology.java.html,v $
 * Revision           $Revision: 1.4 $
 * Release status     $State: Exp $
 *
 * Last modified on   $Date: 2007/01/17 10:44:23 $
 *               by   $Author: andy_seaborne $
 *
 * (c) Copyright 2002, 2003, 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
 * (see footer for full conditions)
 *****************************************************************************/

// Imports
///////////////
import java.util.*;

import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.*;


/**
 * <p>
 * Simple example of using the persistent db layer with ontology models.
 * </p>
 *
 * @author Ian Dickinson, HP Labs
 *         (<a  href="mailto:Ian.Dickinson@hp.com" >email</a>)
 * @version CVS $Id: PersistentOntology.java.html,v 1.4 2007/01/17 10:44:23 andy_seaborne Exp $
 */
public class PersistentOntology {

	// External signature methods
    //////////////////////////////////

    public void load( ModelMaker maker, String source ) {
        // use the model maker to get the base model as a persistent model
        // strict=false, so we get an existing model by that name if it exists
        // or create a new one
        Model base = maker.createModel( source, false );

        // now we plug that base model into an ontology model that also uses
        // the given model maker to create storage for imported models
        OntModel m = ModelFactory.createOntologyModel( getModelSpec( maker ), base );

        // now load the source document, which will also load any imports
        m.read( source );
    }

    public void listClasses( ModelMaker maker, String modelID ) {
        // use the model maker to get the base model as a persistent model
        // strict=false, so we get an existing model by that name if it exists
        // or create a new one
        Model base = maker.createModel( modelID, false );

        // create an ontology model using the persistent model as base
        OntModel m = ModelFactory.createOntologyModel( getModelSpec( maker ), base );

        for (Iterator i = m.listClasses(); i.hasNext(); ) {
            OntClass c = (OntClass) i.next();
            System.out.println( "Class " + c.getURI() );
        }
    }


    public ModelMaker getMaker( String path ) {
        try {
            // Create a model maker object
            return ModelFactory.createFileModelMaker(path);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit( 1 );
        }

        return null;
    }

    public OntModelSpec getModelSpec( ModelMaker maker ) {
        // create a spec for the new ont model that will use no inference over models
        // made by the given maker (which is where we get the persistent models from)
        OntModelSpec spec = new OntModelSpec( OntModelSpec.OWL_MEM );
        spec.setImportModelMaker( maker );

        return spec;
    }
}