package org.swarm.gsoc.ontology.model;

/**
 * Created at NixDev.net.
 * Author: Pavel Vinogradov <pavel.vinogradov _at_ nixdev.net>
 * User: blaze
 * Date: 29.06.2008
 * Time: 20:28:36
 */
public class Constant extends Variable {

    public Constant (String name, String type, Object value) {
        super(name, type);
        setDefaultValue(value);
    }

    @Override
    public String toString() {
        String result;

        result = "\tpublic static final " + variableType + " " + variableName + " " + getDefaultValue() + ";\n";

        return result;
    }

}
