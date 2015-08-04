# Introduction #
Current version of Java Swarm examples built with legacy Makefile infrastructure. Most of Java developers not familiar with make tools and use standard
Java solutions like ant or maven to build complex projects. To solve this problem i develop simple and generic ant-based build system for Swarm application.

# How to use #
To use new build system for you app you need:
  * Download and install Ant (>1.6.5).
  * Compile and install Swarm with java support.
  * Organize project tree так чтобы source code of you application was placed in src dir.
  * Create build.properties file at top of your project tree. his file must contain some values adjusted to your project:
```
   project.name=heatbug 		<!--Project name - was used only for information purposes -->
   project.jar=jheatbug.jar	<!--Project jar file name - compiled project classes was packaged to jar file-->
   project.jar.mainclass=org.swarm.gsoc.ontology.apps.jheatbug.StartHeatbugs	<!--Main class name - Full qualifired class name to run as main jar-file class-->
   project.developer=Pavel.Vinogradov	<!--Project developer name - used in jar-file manifest -->
		
   swarm.lib.jar=swarm.jar		<!--Name of jar file with Java Swarm library (without path) -->
   swarm.lib.path=./lib		<!--Path to directory which contain Java Swarm jar-->
   swarm.native.path=/usr/local/lib/	<!--Path to directory with swarm native libs-->
```
  * Create build.xml file with generic ant targets to build application:
```
<?xml version="1.0" encoding="UTF-8"?>
<project default="build_and_run">
	<property file="build.properties" />
			
	<target name="build_and_run" depends="build, run" description="Build and run project">
		<echo>"Build ${project.name}"</echo>
	</target>

	<target name="build" depends="_compile">
		<jar jarfile="${project.jar}" basedir="build" includes="**/*.class">
			<manifest>
  				<attribute name="Build-By" value="${project.developer}" />
				<attribute name="Main-Class" value="${project.jar.mainclass}" />
				<attribute name="Class-Path" value="${swarm.lib.path}/${swarm.lib.jar}"/>
			</manifest>
		</jar>
	</target>    
				
	<target name="run">		
		<java jar="${project.jar}" fork="true" failonerror="true" maxmemory="128m">					
			<jvmarg value="-Djava.library.path=${swarm.native.path}" />
		</java>
	</target>

	<!-- Internal tasks and targets -->
	<target name="_clean">
		<delete dir="build" />
		<delete file="${project.jar}" />
	</target>

	<target name="_init">
		<mkdir dir="build"/>
	</target>
	
	<target name="_compile" depends="_clean, _init">				
		<javac srcdir="src" destdir="build" classpath="${swarm.lib.path}/${swarm.lib.jar}" />
	</target>
</project>
```
  * build.xml file provide three main target
    * build: compile project classes and package it in jar-file with proper Manifest file
    * run:   execute project jar with proper command-line parameters
    * build\_and\_run: build and run your project

This build system very simple but allow use it with most Java Swarm examples without Makefile or editing build.xml. You need just modify
build.properties file and run ANT.

# Limitation #
This build system not support project which depends on some additional third-party libraries. T support such project
we need improve generation of Manifest file for project jar.