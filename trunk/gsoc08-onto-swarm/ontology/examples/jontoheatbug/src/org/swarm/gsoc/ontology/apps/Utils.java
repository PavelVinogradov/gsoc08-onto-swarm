package org.swarm.gsoc.ontology.apps;

public class Utils {
	/**
	This method and the other get...Property() methods are generic convenience
	methods that would normally be defined in some utility library.
	*/
	public static boolean getBooleanProperty (String propertyName, boolean dflt)
	{
	    String property = System.getProperty (propertyName);
	    if (property == null || property.equals ("")) return dflt;
	    else return property.equals ("true") || property.equals ("1");
	}
	
	public static double getDoubleProperty (String propertyName, double dflt)
	{
	    String property = System.getProperty (propertyName);
	    if (property == null || property.equals ("")) return dflt;
	    else return Double.parseDouble (property);
	}
	
	public static int getIntProperty (String propertyName, int dflt)
	{
	    String property = System.getProperty (propertyName);
	    if (property == null || property.equals ("")) return dflt;
	    else return Integer.parseInt (property);
	}
	
	public static String getStringProperty (String propertyName, String dflt)
	{
	    String property = System.getProperty (propertyName);
	    if (property == null) return dflt;
	    return property;
	}
}
