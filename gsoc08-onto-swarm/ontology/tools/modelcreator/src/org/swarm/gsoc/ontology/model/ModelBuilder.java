package org.swarm.gsoc.ontology.model;

import org.semanticweb.owl.model.*;
import org.semanticweb.owl.apibinding.OWLManager;
import org.swarm.gsoc.ontology.model.visitors.RestrictionVisitor;

import java.net.URI;
import java.util.Set;
import java.util.List;
import java.util.LinkedList;
import java.util.Collections;

import uk.ac.manchester.cs.owl.OWLClassImpl;
import uk.ac.manchester.cs.owl.OWLObjectSomeRestrictionImpl;
import uk.ac.manchester.cs.owl.OWLDataAllRestrictionImpl;

/**
 * Created by NixDev.net.
 * Author: Pavel Vinogradov <pavel.vinogradov _at_ nixdev.net>
 * Date: 21.06.2008
 * Time: 21:44:06
 */

public class ModelBuilder {

    public static List<Clazz> model = new LinkedList<Clazz>();

    public static void main(String[] args) {
        try {
            // A simple example of how to load and save an ontology
            // We first need to obtain a copy of an OWLOntologyManager, which, as the
            // name suggests, manages a set of ontologies.  An ontology is unique within
            // an ontology manager.  To load multiple copies of an ontology, multiple managers
            // would have to be used.
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

            // We load an ontology from a physical URI - in this case we'll load the pizza
            // ontology.
            URI physicalURI = URI.create("file:/home/blaze/heatbug.owl");
            //URI physicalURI = URI.create("file:/home/blaze/pizza.owl");

            // Now ask the manager to load the ontology
            OWLOntology ontology = manager.loadOntologyFromPhysicalURI(physicalURI);
            // Print out all of the classes which are referenced in the ontology

            for(OWLClass cls : ontology.getReferencedClasses()) {
                Clazz clazz = new Clazz();
                clazz.className = cls.toString();

                Set<OWLDescription> superClasses = cls.getSuperClasses(ontology);                

                for(OWLDescription desc : superClasses) {
                    if (desc instanceof OWLClassImpl) {

                        if (!desc.toString().equalsIgnoreCase("thing")) {
                            clazz.classExtend = desc.toString();
                        }

                    } else if (desc instanceof OWLObjectSomeRestrictionImpl) {

                        OWLObjectSomeRestrictionImpl restriction = (OWLObjectSomeRestrictionImpl) desc;
                        System.out.println(restriction.getProperty());
                        System.out.println(restriction.getFiller());

                    } else if (desc instanceof OWLDataAllRestrictionImpl) {
                        OWLDataAllRestrictionImpl restriction = (OWLDataAllRestrictionImpl) desc;
                        clazz.addVariable(new Variable(restriction.getProperty().toString(), restriction.getFiller().toString()));

                    } else {
                        clazz.addMethod(new Method(desc.toString(), null, null));

                    }
                }

                model.add(clazz);
                clazz.toFile();
            }

            manager.removeOntology(ontology.getURI());
        }
        catch (OWLOntologyCreationException e) {
            System.out.println("The ontology could not be created: " + e.getMessage());
        }
    }
}