package org.swarm.gsoc.ontology.model;

import java.util.List;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.BufferedWriter;

/**
 * Created by NixDev.net.
 * Author: Pavel Vinogradov <pavel.vinogradov _at_ nixdev.net>
 * Date: 22.06.2008
 * Time: 2:04:07
 */
public class Clazz {
    
    public String className;
    public List<String> classInterfaces = new ArrayList<String>();
    public String classExtend;
    public List<Method> classMethods = new ArrayList<Method>();
    public List<Variable> classVariables = new ArrayList<Variable>();

    @Override
    public String toString () {
        String result;

        result = "public class " + className;
        if (classInterfaces.size() > 0) {
            result += " imlement ";
        }
        for (String classInterface: classInterfaces) {
            result += " " + classInterface;
        }

        if (classExtend != null) {
            result += " extends " + classExtend + "\n";
        }

        result += "{ \n";

        for (Variable var: classVariables) {
            result += var.toString() + "\n";
        }

        for (Method method: classMethods) {
            result += method.toString() + "\n";
        }

        result += "}\n";

        return result;
    }

    public boolean addMethod(Method method) {
        if (classMethods.contains(method)) {
            return false;
        } else {
            return classMethods.add(method);
        }
    }

    public boolean addVariable(Variable variable) {
        Boolean result = true;

        if (classVariables.contains(variable)) {
            result = false;
        } else {
            result = classVariables.add(variable);
            Method getter = new Method("get"+variable.variableName,
                    "return " + variable.variableName + ";", variable.variableType);
            Method setter = new Method("set"+variable.variableName,
                    "this." + variable.variableName + " = " + variable.variableName + ";", null);
            setter.methodArg.add(variable);
            
            addMethod(getter);
            addMethod(setter);
        }

        return result;
    }

    public boolean toFile (String outPath) {

        try{
            // Create file
            FileWriter fstream = new FileWriter(outPath + className + ".class");
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(this.toString());
            //Close the output stream
            out.close();
         } catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
            return false;
        }
        return true;        
    }
}
