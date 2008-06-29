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

        setVariableName(name);
        setVariableType(type);
    }

    public String getVariableName() {
        return variableName;
    }

    public String getMethodVariableName() {
        return variableName.substring(0, 1).toUpperCase() + variableName.substring(1, variableName.length());
    }

    public void setVariableName(String variableName) {
        String tmpName = variableName;

        Integer index = tmpName.indexOf('_');


        if (index > 0) {
            tmpName = tmpName.substring(index+1, index+2).toLowerCase() + tmpName.substring(index+2, tmpName.length());
        }

        this.variableName = tmpName;
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
