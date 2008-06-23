package org.coode.owlapi.examples;

/**
 * Created by NixDev.net.
 * Author: Pavel Vinogradov <pavel.vinogradov _at_ nixdev.net>
 * Date: 22.06.2008
 * Time: 2:07:39
 */
public class Method {
    public String methodName;
    public String methodBody;
    public String methodReturn = "void";

    @Override
    public String toString() {
        String result;

        result = "\tpublic " + methodReturn + " " + methodName + " {";
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
