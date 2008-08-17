package org.swarm.gsoc.ontology.model.writer.java;

import org.swarm.gsoc.ontology.model.Clazz;
import org.swarm.gsoc.ontology.model.writer.ModelWriter;

import java.util.List;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.File;

/**
 * Created at NixDev.net.
 * Author: Pavel Vinogradov <pavel.vinogradov _at_ nixdev.net>
 * Date: 29.06.2008
 * Time: 18:17:42
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
            if (clazz.getPackageName() != null) {
                String clazzPath = path + clazz.getPackageName().replace('.', '/') + "/";
                
                if ( !(new File(clazzPath)).exists())
                    (new File(clazzPath)).mkdirs();

                // Create file
                FileWriter fstream = new FileWriter(clazzPath + clazz.className + ".java");
                BufferedWriter out = new BufferedWriter(fstream);
                out.write(clazz.toString());
                //Close the output stream
                out.close();
            }
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
