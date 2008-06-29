package org.swarm.gsoc.ontology.model;

import java.util.List;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.BufferedWriter;

/**
 * Created at NixDev.net.
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

    public boolean addMethod(Method method) {
        if (classMethods.contains(method)) {
            return false;
        } else {
            return classMethods.add(method);
        }
    }

    public boolean addConstant(Constant variable) {
        Boolean result;

        if (classVariables.contains(variable)) {
            result = false;
        } else {
            result = classVariables.add(variable);
        }

        return result;
    }

    public boolean addVariable(Variable variable) {
        Boolean result;

        if (classVariables.contains(variable)) {
            result = false;
        } else {
            result = classVariables.add(variable);
            Method getter = new Method("get"+variable.getMethodVariableName(),
                    "return " + variable.getVariableName() + ";", variable.getVariableType());
            Method setter = new Method("set"+variable.getMethodVariableName(),
                    "this." + variable.getVariableName() + " = " + variable.getVariableName() + ";", null);
            setter.methodArg.add(variable);
            
            addMethod(getter);
            addMethod(setter);
        }

        return result;
    }

    @Override
    public String toString () {
        String result;

        result = "public class " + className;
        if (classInterfaces.size() > 0) {
            result += " imlement ";

            for (String classInterface: classInterfaces) {
                result += " " + classInterface;
            }
        }

        if (classExtend != null) {
            result += " extends " + classExtend + " ";
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
}
