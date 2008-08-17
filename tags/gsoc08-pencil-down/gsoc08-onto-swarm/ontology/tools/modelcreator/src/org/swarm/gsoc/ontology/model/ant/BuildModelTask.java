package org.swarm.gsoc.ontology.model.ant;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;
import org.swarm.gsoc.ontology.model.ModelBuilder;


/**
 * Created at NixDev.net.
 * Author: Pavel Vinogradov <pavel.vinogradov _at_ nixdev.net>
 * Date: 26.06.2008
 * Time: 0:36:04
 */
public class BuildModelTask extends Task {

    private String owlPath;
    private String owlName;
    private String outPath;

    public void execute () throws BuildException {
        
        ModelBuilder builder = new ModelBuilder("file:" + owlPath + owlName + ".owl");

        builder.generate(owlName);
        builder.write(outPath);
        builder.clear();
    }

    public void setOwlPath(String owlPath) {
        this.owlPath = owlPath;
    }

    public void setOwlName(String owlName) {
        this.owlName = owlName;
    }

    public void setOutPath(String outPath) {
        this.outPath = outPath;
    }
}
