package org.swarm.gsoc.ontology.model;

import java.util.List;
import java.util.LinkedList;

/**
 * Created by NixDev.net.
 * Author: Pavel Vinogradov <pavel.vinogradov _at_ nixdev.net>
 * Date: 22.06.2008
 * Time: 2:07:39
 */
public class Method {
    private String methodName;
    private String methodBody = "";
    private String methodReturn = "void";
    public List<Variable> methodArg;

    public Method (String name, String body, String type) {
        this.methodName = name;

        if (body != null)
            this.methodBody = body;

        if (type != null)
            this.methodReturn = type;

        methodArg = new LinkedList();
    }

    @Override
    public String toString() {
        String result;

        result = "\tpublic " + methodReturn + " " + methodName + " (";

        for (Variable var: methodArg) {
            result += var.variableType + " " + var.variableName + ",";
        }
        
        if (methodArg.size() > 0)
            result = result.substring(0, result.length()-1);

        result += ") {";

        if (methodBody != null) {
            result += "\n" +
                "\t\t" + methodBody + "\n" +
                "\t}\n";
        } else {
            result += "}\n";
        }

        return result;
    }
}
