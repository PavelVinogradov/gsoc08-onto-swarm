# Introduction #
JOntoHeatBug is a simple application based on JHeatBug example from Swarm application catalog. Main purpose of this application is demonstration of ontology utilization. Significant changes in JHeatBug application concerned with usage of ant-based build
system (AntBasedBuild) and domain model generated from owl ontology by special library (OwlModelBuilder).

# Building #
Source of this application is available from http://code.google.com/p/gsoc08-onto-swarm/ project (subversion repository

```
  svn checkout http://gsoc08-onto-swarm.googlecode.com/svn/trunk/ gsoc08-onto-swarm-read-only
```

).

Before usinf of this application please read description of Ant-based build system (AntBasedBuild) and model generator library(OwlModelBuilder).

To compile this application you need:
  * swarm engine compiled with java support and installed on local machine
  * ant > 1.6.5
  * adjust values in build.properties file (correct path to swarm lib)

You can build and run all aplication by executing only one command from source tree root:
**ant**

This command refresh world model, build JOntoHeatBug as a single jar file with all dependencies and run with apropriate command line parameters.