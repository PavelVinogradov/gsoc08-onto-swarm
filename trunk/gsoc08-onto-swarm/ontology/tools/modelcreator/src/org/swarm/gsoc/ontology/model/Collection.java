package org.swarm.gsoc.ontology.model;

/**
 * Created at NixDev.net.
 * Author: Pavel Vinogradov <pavel.vinogradov _at_ nixdev.net>
 * User: blaze
 * Date: 03.07.2008
 * Time: 7:57:13
 */
public class Collection {

    private String name;
    private TYPE type;
    private String elementType;

    public Collection(String name, TYPE type, String elementTYpe) {
        setName(name);
        this.type = type;
        this.elementType = elementTYpe;
    }

    enum TYPE {
        LIST,
        SET,
        MAP
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String tmpName = name;

        Integer index = tmpName.indexOf('_');


        if (index > 0) {
            tmpName = tmpName.substring(index+1, index+2).toLowerCase() + tmpName.substring(index+2, tmpName.length());
        }

        this.name = tmpName;
    }

    @Override
    public String toString() {
        String result = "\t";

        switch (type) {
            case LIST: default:
                result += "public List<" + elementType + "> " + name + ";";
                break;

            case SET:
                result += "public Set<" + elementType + "> " + name + ";";
                break;

            case MAP:
                System.err.println("Map collection not supported yet");                
        }

        result += "\n";
        return result;
    }
}
