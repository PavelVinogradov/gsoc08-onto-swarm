package org.swarm.gsoc.ontology.model.writer.java;

import org.swarm.gsoc.ontology.model.Clazz;
import org.swarm.gsoc.ontology.model.writer.ModelWriter;

import java.util.List;
import java.io.FileWriter;
import java.io.BufferedWriter;

/**
 * Created by IntelliJ IDEA.
 * User: blaze
 * Date: 29.06.2008
 * Time: 18:17:42
 * To change this template use File | Settings | File Templates.
 */
public class SimpleJavaFileWriter implements ModelWriter {

    private String path = "./";

    public SimpleJavaFileWriter() {
    }

    public SimpleJavaFileWriter(String path) {
        setPath(path);
    }
    
    public void setPath(String path) {
        this.path = path;
    }

    public boolean toFile (Clazz clazz) {

        try{
            // Create file
            FileWriter fstream = new FileWriter(path + clazz.className + ".class");
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(clazz.toString());
            //Close the output stream
            out.close();
         } catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
            return false;
        }
        return true;
    }

    public void generate(List<Clazz> model) {
        for (Clazz clazz: model) {
            toFile(clazz);
        }
    }
}
