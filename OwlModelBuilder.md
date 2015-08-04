# Introduction #

Model builder is a small Java library to generate Java model source code based on simple OWL-DL ontologies. This library use OWL-API to traveling through ontology tree and parse expressions about classes. Current version of library support limited part of
OWL-DL language:
  * Classes
  * Restrictions
  * Attributes
  * Cardinality Restriction
  * Data Value Restriction
  * Object All Restriction

Such language expressions converted to Java source code with use of custom java generation code. In future this code can be replaced with
cglib library.

# Use #
Library contain two classes to grant access to existing functionality:
  * ModelBuilder to use this class directly from java code
```
        ModelBuilder builder = new ModelBuilder("file:/home/blaze/devel/IdeaProjects/OwlModelBuilder/examples/heatbug.owl");
        //ModelBuilder builder = new ModelBuilder("file:/home/blaze/pizza.owl");

        builder.generate("heatbug");
        builder.write("out/generated/");
        builder.clear();
```
  * BuildModelTask to use library from ant build script
```
	<taskdef name="builder" classname="org.swarm.gsoc.ontology.model.ant.BuildModelTask" classpath="lib/model-builder.jar;lib/owlapi-bin.jar"/>

	<target name="_modelbuild">
		<builder owlName="${model.name}" owlPath="./" outPath="${model.compile.path}/${model.name}/"/>
	</target>
```

In both variants library accept three arguments:
  * owlPath - path to world ontology
  * owlName - ontology name
  * outPath - path to write generated java model