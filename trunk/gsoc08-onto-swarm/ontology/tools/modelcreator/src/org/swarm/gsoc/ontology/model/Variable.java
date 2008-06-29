package org.swarm.gsoc.ontology.model;

/**
 * Created by NixDev.net.
 * Author: Pavel Vinogradov <pavel.vinogradov _at_ nixdev.net>
 * Date: 25.06.2008
 * Time: 21:58:04
 */
public class Variable {
    protected String variableName;
    protected String variableType;
    protected Object defaultValue;

    public Variable (String name, String type) {
        Integer index = name.indexOf('_');

        if (index > 0) {
            name = name.substring(index+1, index+2).toLowerCase() + name.substring(index+2, name.length());
        }

        this.variableName = name;
        this.variableType = type;
    }

    public String getVariableName() {
        return variableName;
    }

    public String getMethodVariableName() {
        return variableName.substring(0, 1).toUpperCase() + variableName.substring(1, variableName.length());
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public String getVariableType() {
        return variableType;
    }

    public void setVariableType(String variableType) {
        this.variableType = variableType;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String toString() {
        String result;

        result = "\tprivate " + variableType + " " + variableName;

        if (getDefaultValue() != null) {
            result += " " + getDefaultValue();
        }

        result += ";\n";

        return result;
    }
}
