# Intoduction #

Modeling engines get advantages from using ontology models, but Swarm currently not support ontologies. One version of such support
was developed during GSoC 2008. This version based on OWL ontology language and use owl ontology to create Java model. Currently only limited
subset of OWL-DL language is supported. Supported expression of OWL-DL language include:
  * Classes
  * Restrictions
  * Attributes
  * Cardinality Restriction
  * Data Value Restriction
  * Object All Restriction

# Model Builder #
Such support implemented with use of OwlModelBuilder library. To use owl ontology to model your application world you
must split application development on three phase:
  1. Build owl model of application world. This model will include main concepts and properties of modelling world.
  1. Generate Java model by using ant task BuildModelTask from model-builder.jar
  1. Update your application to use generated model as base classes for implementation of world concepts.

You can repeat this three steps many time to improve owl model. Updating java model not affected other parts of your application
(except cases when you remove some properties or concepts from model).

**Add description of ontology**

# JENA-based solution #
Another approach of ontology use is solution based on JENA library. This solution use JANE library to present modeling wold as
owl ontology with instances of modeling concepts. JENA library present interest functionality like
  * persisting and loading concrete state of modeling world as owl ontology
  * executing SPARQL queries over modeling world
  * provide standard API for querying and updating model

Currently such approach require complete rewrite of modeling application. But in advance we can use integration library
like model-builder to simplify development of new Swarm modeling application.