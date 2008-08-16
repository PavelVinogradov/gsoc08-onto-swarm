import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class HeatWorld {

	public final String SOURCE = "http://www.nixdev.net/ontology/heatbug.owl";
	public final String NS = SOURCE + "#";
	public final String OWL_SOURCE = "heatbug.owl";
	public final String OWL_DEST = "heatbug_live.owl";

	public final String ONTOLOGY_WORLD = NS + "MWorld";
	public final String ONTOLOGY_AGENT = NS + "MHeatBug";
	
	protected OntModel model;
	protected Map<String, Object> modelProps = new HashMap<String, Object>();
	
	
	public HeatWorld() {
		model = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
		modelProps.put("numBugs", 100);
		modelProps.put("minIdealTemp", 17000);
		modelProps.put("maxIdealTemp", 31000);
		modelProps.put("worldXSize", 80);
		modelProps.put("worldYSize", 80);
		
	}
	
	public void worldLoad() {
		InputStream in = FileManager.get().open(OWL_SOURCE);
		model.read(in, "");	
	}

	protected void initIndividual(Individual individ) {
		ExtendedIterator propList = individ.getOntClass().listDeclaredProperties(true);
		while (propList.hasNext()) {
			OntProperty prop = (OntProperty)propList.next();
			
			if (modelProps.containsKey(prop.getLocalName())) {
				individ.addProperty(prop, modelProps.get(prop.getLocalName()).toString());
				System.out.println("Set " + prop.getLocalName() + " to " + modelProps.get(prop.getLocalName()).toString());
			}
		}		
	}
	
	public void worldShow() {
		
		ExtendedIterator worldIndivIter = model.listIndividuals();
		
		while (worldIndivIter.hasNext()) {
				Individual individ = (Individual)worldIndivIter.next();
				System.out.println(individ.getOntClass(true).getLocalName() + " [" + individ.getOntClass(true).listInstances(true) + "]");				
				
				ExtendedIterator propList = individ.getOntClass().listDeclaredProperties(true);
				while (propList.hasNext()) {
					OntProperty prop = (OntProperty)propList.next();
					
					System.out.println("\t" + prop.getLocalName() + " = " + individ.getPropertyValue(prop));			
			}		
		}
	}
	
	public void worldInit() {

		//Use special to exclude property classes 
		ExtendedIterator worldClassIter = OntoHelper.getClasesIterator(model);		
		
		//Iterate over all classes in model
		while (worldClassIter.hasNext()) {
			OntClass cls = (OntClass) worldClassIter.next();

			//Create World instance
			if (cls.getURI().equals(ONTOLOGY_WORLD))
				initIndividual(cls.createIndividual("HeatWorld"));
		}								
	}
	
	public void worldPersist() {
		model.write(System.out);
	}
	
	public void worldPopulate() {
		Long numBugs = 2L;//model.getIndividual(NS + "HeatWorld").getPropertyValue(new Property(NS + "numBugs")).getLong();
		for (Integer i = 1; i <= numBugs; i++) {
			createIndividualBug("MHeatBug", "HeatBug", i);
		}		
	}
	
	public void createIndividualBug(String parentName, String indivName, Integer index) {	
		OntClass parent = model.getOntClass( NS + parentName);
		Individual child = model.createIndividual( NS + indivName + index, parent );
		
		Property prop = model.getProperty(NS + "heatbugIndex");		
		child.addLiteral(prop, index);
		
		Property x = model.getProperty(NS + "x");		
		child.addLiteral(x, index);

		Property y = model.getProperty(NS + "y");		
		child.addLiteral(y, index);

		Property outputHeat = model.getProperty(NS + "outputHeat");		
		child.addLiteral(outputHeat, index);

		//Property propDebug = model.getProperty(NS + "heatbug_printDiagnostics");
		//child.addLiteral(propDebug, model.getProperty( NS + "heatbug_printDiagnostics").getProperty(OWL.hasValue).getLong());
	}
	
	public static void main(String[] args) {
		
		HeatWorld main = new HeatWorld();
		
		main.worldLoad();
		main.worldInit();
		main.worldPopulate();
		main.worldShow();
		//main.worldPersist();			
	}

}
