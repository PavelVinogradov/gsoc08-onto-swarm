import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.util.iterator.Filter;


public class OntoHelper {
	
	// Iterator for REAL model classes. All property classes is filtered
	public static ExtendedIterator getClasesIterator (OntModel model) {
		return model.listClasses().filterKeep(new Filter() {
			public boolean accept( Object o ) {
				Boolean result;
				
				if (o instanceof OntClass) {
					result = (((OntClass) o).getNameSpace() != null);
				} else {
					result = false;
				}
				
				return result;
			}
		});

	}

}
