import org.swarm.gsoc.ontology.model.ModelBuilder;

/**
 * Created at NixDev.net.
 * Author: Pavel Vinogradov <pavel.vinogradov _at_ nixdev.net>
 * Date: 29.06.2008
 * Time: 17:24:35
 */
public class Main {    

    public static void main(String[] args) {

        ModelBuilder builder = new ModelBuilder("file:/home/blaze/devel/IdeaProjects/OwlModelBuilder/examples/heatbug.owl");
        //ModelBuilder builder = new ModelBuilder("file:/home/blaze/pizza.owl");

        builder.generate();
        builder.write("out/generated/");
        builder.clear();
    }

}
