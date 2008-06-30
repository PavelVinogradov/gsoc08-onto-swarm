package org.swarm.gsoc.ontology.model;

import org.semanticweb.owl.model.*;
import org.semanticweb.owl.apibinding.OWLManager;
import org.swarm.gsoc.ontology.model.writer.ModelWriter;
import org.swarm.gsoc.ontology.model.writer.java.SimpleJavaFileWriter;

import java.net.URI;
import java.util.Set;
import java.util.List;
import java.util.LinkedList;

import uk.ac.manchester.cs.owl.*;

/**
 * Created by NixDev.net.
 * Author: Pavel Vinogradov <pavel.vinogradov _at_ nixdev.net>
 * Date: 21.06.2008
 * Time: 21:44:06
 */

public class ModelBuilder {

    protected List<Clazz> model = new LinkedList<Clazz>();
    protected OWLOntology ontology;
    protected OWLOntologyManager manager;
    protected URI physicalURI;

    public ModelBuilder(String ontologyPath) {
        // A simple example of how to load and save an ontology
        // We first need to obtain a copy of an OWLOntologyManager, which, as the
        // name suggests, manages a set of ontologies.  An ontology is unique within
        // an ontology manager.  To load multiple copies of an ontology, multiple managers
        // would have to be used.
        manager = OWLManager.createOWLOntologyManager();

        // We load an ontology from a physical URI - in this case we'll load the pizza
        // ontology.
        physicalURI = URI.create(ontologyPath);
    }

    protected void loadOntology() {
        try {
            // Now ask the manager to load the ontology
            ontology = manager.loadOntologyFromPhysicalURI(physicalURI);
            // Print out all of the classes which are referenced in the ontology
        } catch (OWLOntologyCreationException e) {
            System.out.println("The ontology could not be created: " + e.getMessage());
        }

    }

    public void generate() {
        if (ontology == null) {
            loadOntology();
        }

        // Traveling through all ontology and generate data model
        for(OWLClass cls : ontology.getReferencedClasses()) {

            System.out.println(cls.toString() + " : ");

            Clazz clazz = new Clazz();
            clazz.className = cls.toString();

            Set<OWLDescription> superClasses = cls.getSuperClasses(ontology);

            // Traveling through all superclass of each class
            for(OWLDescription desc : superClasses) {

                // This is class definition
                if (desc instanceof OWLClassImpl) {

                    System.out.print("\tclass " + desc);

                    if (!desc.toString().equalsIgnoreCase("thing")) {
                        clazz.classExtend = desc.toString();
                    }

                // This is unhandled restriction
                } else if (desc instanceof OWLObjectSomeRestrictionImpl) {
                    System.out.print("\trestriction " + desc + " [unhandled]");

                    //OWLObjectSomeRestrictionImpl restriction = (OWLObjectSomeRestrictionImpl) desc;
                    //System.out.println(restriction.getProperty());
                    //System.out.println(restriction.getFiller());

                // This is class attribute without default value
                } else if (desc instanceof OWLDataAllRestrictionImpl) {
                    System.out.print("\tattribute " + desc);

                    OWLDataAllRestrictionImpl restriction = (OWLDataAllRestrictionImpl) desc;
                    clazz.addVariable(new Variable(restriction.getProperty().toString(), restriction.getFiller().toString()));

                // This is class attribute with object type
                } else if (desc instanceof OWLObjectExactCardinalityRestriction) {
                    System.out.print("\tobject restriction " + desc);

                    OWLObjectExactCardinalityRestrictionImpl restriction = (OWLObjectExactCardinalityRestrictionImpl) desc;

                    OWLObjectPropertyExpression res = restriction.getProperty();
                    Set<OWLDescription> domain = res.getDomains(ontology);

                    //TODO: Implement support for big domain
                    if (domain.size() == 1) {
                        for (OWLDescription value: domain) {
                            clazz.addVariable(new Variable(restriction.getProperty().toString(), value.toString()));
                        }
                    } else {
                        System.out.print("domain too big " + domain.toString() + "[unhandled]");
                    }
                // This is attribute with default value
                } else if (desc instanceof OWLDataValueRestrictionImpl) {
                    System.out.print("\tdata value " + desc);

                    OWLDataValueRestrictionImpl restriction = (OWLDataValueRestrictionImpl) desc;

                    if (restriction.getValue() instanceof OWLTypedConstantImpl) {
                        Boolean isVariable = true;
                        OWLTypedConstantImpl res = (OWLTypedConstantImpl)restriction.getValue();
                        OWLDataProperty prop = restriction.getProperty().asOWLDataProperty();

                        for (OWLDataPropertyRangeAxiom a : ontology.getDataPropertyRangeAxiom(prop) ) {
                            OWLDataRange range = a.getRange();
                            if (range instanceof OWLDataOneOfImpl)
                                isVariable = false;
                        }

                        if (isVariable) {
                            System.out.print(" variable ");
                            Variable variable = new Variable(restriction.getProperty().toString(), res.getDataType().toString());
                            variable.setDefaultValue(res.getLiteral());
                            clazz.addVariable(variable);
                        } else {
                            System.out.print(" constant ");
                            Constant var = new Constant(restriction.getProperty().toString(), res.getDataType().toString(), res.getLiteral());
                            clazz.addConstant(var);
                        }

                    } else {
                        System.err.println (restriction.getValue().getClass());
                        //var.setDefaultValue(restriction.getValue().getLiteral());
                        //clazz.addVariable(var);
                    }
                //I don't know what this!
                } else {
                    //TODO: Implement support for other elements
                    System.out.print("\tunknown " + desc + "[unhandled]");
                    
                    clazz.addMethod(new Method(desc.toString(), null, null));
                }
                System.out.println();
            }

            model.add(clazz);
        }
    }

    public void write(String outPath) {
        ModelWriter writer = new SimpleJavaFileWriter(outPath);
        writer.generate(model);
    }

    public void clear() {
        manager.removeOntology(ontology.getURI());
    }

}