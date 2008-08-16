import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.rdf.model.ModelMaker;


public class OntPersister {
	    // Constants
	    //////////////////////////////////

	    public static final String ONT1 = "urn:x-hp-jena:test1";
	    public static final String ONT2 = "urn:x-hp-jena:test2";

	    // Static variables
	    //////////////////////////////////

	    // if true, reload the data
	    private static boolean s_reload = false;

	    // source URL to load data from; if null, use default
	    private static String s_source;

	    // External signature methods
	    //////////////////////////////////

	    public static void main( String[] args ) {

	        // check for default sources
	        if (s_source == null) {
	            s_source = getDefaultSource();
	        }

	        // create the helper class we use to handle the persistent ontologies
	        PersistentOntology po = new PersistentOntology();

	        // are we re-loading the data this time?
	        if (s_reload) {

	            // we pass cleanDB=true to clear out existing models
	            // NOTE: this will remove ALL Jena models from the named persistent store, so
	            // use with care if you have existing data stored
	            ModelMaker maker = po.getMaker("/tmp");

	            // now load the source data into the newly cleaned db
	            po.load( maker, s_source );
	        }

	        // now we list the classes in the database, to show that the persistence worked
	        ModelMaker maker = po.getMaker("/tmp");
	        po.listClasses( maker, s_source );
	    }

	    /**
	     * Answer the default source document, and set up the document manager
	     * so that we can find it on the file system
	     *
	     * @return The URI of the default source document
	     */
	    private static String getDefaultSource() {
	        // use the ont doc mgr to map from a generic URN to a local source file
	    	OntDocumentManager.getInstance().addAltEntry( ONT1, "file:heatbug.owl" );
//	        OntDocumentManager.getInstance().addAltEntry( ONT1, "file:src-examples/data/test1.owl" );
//	        OntDocumentManager.getInstance().addAltEntry( ONT2, "file:src-examples/data/test2.owl" );

	        return ONT1;
	    }
}