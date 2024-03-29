// Swarm library. Copyright � 1996-2000 Swarm Development Group.
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful, but
// WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
// USA
// 
// The Swarm Development Group can be reached via our website at:
// http://www.swarm.org/
/*
Name:            objectbase.h
Description:     support for swarmobjects and probing   
Library:         swarmobject
Authors:         Manor Askenazi
Date:            1996-12-12
*/

//S: Support for Swarm objects and probing

//D: The objectbase library contains the most basic objects users need to
//D: design their agents and swarms. It also serves, at present, as a
//D: repository for the probe machinery, which is provided for every
//D: SwarmObject.

#import <defobj.h>
#import <activity.h> // timeval_t
#include <externvar.h>

@protocol SwarmObject <Create, Drop, CREATABLE>
//S: A superclass of most objects in a Swarm simulation that provides
//S: support for probing.

//D: A SwarmObject is an object that is intended to be a member of a
//D: Swarm.  It's behavior will be perpetuated by messages sent
//D: from the schedule  of events defined in the context of 
//D: Swarm object. 

//D: The SwarmObject is where the models of all the agents of a
//D: simulation will reside. Hence, most of the burden on defining
//D: the messages that can be sent to any agent lies with the user.
//D: SwarmObject inherits its basic functionality from the Create
//D: and Drop object types defined in the defobj library. 

USING
//M: The getProbeMap method returns a pointer to the ProbeMap for
//M: the object if there has been one creaded for that object's class.
//M: If it hasn't been created, then it creates a default ProbeMap.
- getProbeMap;

//M: The getCompleteProbeMap method returns a newly created
//M: CompleteProbeMap for an object. 
- getCompleteProbeMap;

//M: The getProbeForVariable: method returns the VarProbe indexed in the
//M: ProbeMap by the string aVariable.
- getProbeForVariable: (const char *)aVariable;

//M: The getProbeForMessage: method returns the MessageProbe indexed in
//M: the ProbeMap by the string aMessage.
- getProbeForMessage: (const char *)aMessage;
@end

@protocol ProbeConfig
//S: Protocol for configuration of Probes, ProbeMaps, and the
//S: ProbeLibrary.

//D: Protocol for configuration of Probes, ProbeMaps, and the
//D: ProbeLibrary.
USING
- setObjectToNotify: anObject;
- getObjectToNotify;
@end

@protocol Probe <SwarmObject, ProbeConfig>
//S: An abstract superclass of both VarProbe and MessageProbe.

//D: A Probe is simply an object that contains pointers to an element 
//D: (instance variable or message description) of another object.
//D: The Probe contains instance variables that describe the referent's
//D: class and type. 
//D: It's actually an abstract class that is further subdivided into
//D: VarProbe and MessageProbe, which represent the two basic types of 
//D: elements of any object. The Probes are collected into a ProbeMap and
//D: subsequently installed in the ProbeLibrary. 

CREATING
//M: The setProbedClass: method sets the class of the object the probe
//M: points at and must be called at create time.
- setProbedClass: (Class)class_;

- setProbedObject: object;
SETTING
//M: The setSafety method turns on the option of checking the
//M: compatibility of the class of the object before any actions are
//M: performed on the object. 
- setSafety;     // There should be a setSafety: BOOL

//M: The unsetSafety method turns off the option of checking the
//M: compatibility of the class of the object before any actions are
//M: performed on the object.
- unsetSafety;   // and getSafety methods instead...

USING
//M: The clone: method returns a clone of the probe.  If the initial
//M: probe was created by Library Generation or by the default version
//M: of Object generation, the probe should be cloned prior to making
//M: changes to it to avoid having the changes affect the other
//M: potential users of the probe.
- clone: (id <Zone>)aZone;

//M: The getProbedClass method returns the class of the object the
//M: probe points at as a Class pointer.
- (Class)getProbedClass;

//M: The getProbedType method returns the typing of the probed
//M: variable or message. The typing is represented using the
//M: string-format provided by the Objective-C runtime system.
- (const char *)getProbedType;
@end

@protocol VarProbe <Probe, CREATABLE>
//S: A class that allows the user to inspect a given variable in any
//S: candidate that is an instance of, or inherits from, a given class.

//D: This is a specialized subclass of the abstract class Probe.
//D: It completes the specification of a probe that refers to an
//D: instance variable element of an object. 
CREATING
//M: The setProbedVariable: sets the variable being probed.
//M: The aVariable identifier is simply a character string consisting
//M: of the identifier of the variable referent.
//M: This method must be called during the create phase. 
- setProbedVariable: (const char *)aVariable;

SETTING
//M: The setNonInteractive method sets a VarProbe to be non-interactive.
//M: This ensures that the user will not be able to change the value of
//M: a probe, only observe it.  Setting the VarProbe to be
//M: non-interactive will not interfere with the drag and drop
//M: capability of the objects into the VarProbe field.
//   Currently, setNonInteractive must be used *after* create phase
//   In any case these should also become setInteractive: BOOL etc.
- setNonInteractive; 

//M: The setStringReturnType: method sets the format that will be used
//M: to print the variable.  When the probedVariable is of type
//M: unsigned char or char, the method probeAsString will, by default,
//M: return a string of the format: "'%c' %d".  This is meant to
//M: reflect the commonplace use of an unsigned char as a small int.
- setStringReturnType: returnType;

//M: The setFloatFormat: method sets the floating-point format of a GUI
//M: display widget when given a sprintf-style formatting string.
- setFloatFormat: (const char *)format;

USING
//M: The getProbedVariable method returns a string matching the
//M: identifier of variable being probed.
- (const char *)getProbedVariable;

//M: The getInteractiveFlag method returns the
//M: interactivity state of the VarProbe.
- (BOOL)getInteractiveFlag;

//M: The probeRaw: method returns a pointer to the probed variable.
- (void *)probeRaw: anObject;

//M: The probeAsPointer: method returns a pointer to the probed
//M: variable based on the probeType.
- (void *)probeAsPointer: anObject;

//M: A field probed with probeAsObject: must be an object.
- probeObject: anObject;

//M: The probeAsInt: method returns a pointer to the probed variable as
//M: an integer.
- (int)probeAsInt: anObject;

//M: The probeAsDouble: method returns a pointer to the probed variable
//M: as a double.
- (double)probeAsDouble: anObject;

//M: The probeAsString:Buffer: method prints the value of the
//M: variable into the buffer. The buffer should be pre-allocated.
- (const char *)probeAsString: anObject Buffer: (char *)buffer;

//M: The probeAsString: method prints the value of the
//M: variable into a new String object. 
- (id <String>)probeAsString: anObject;

//M: The probeAsString:Buffer:withFullPrecision: method prints the
//M: value of the variable into the buffer.  The buffer should be
//M: pre-allocated.  This version of probeAsString is used
//M: internally by ObjectSaver to use the "saved as" precision form
//M: which may differ from the "displayed" precision.
- (const char *)probeAsString: anObject
                       Buffer: (char *)buf
            withFullPrecision: (BOOL)precision;

//M: Returns rank of array, or 0 for scalar objects.
- (unsigned)getRank;

//M: In the case of arrays, returns the base type.
- (const char *)getBaseType;

//M: Returns a vector equal to length returned by getRank:
//M: with the dimensions of the array (major to minor).
- (unsigned *)getDims;

//M: Iterates through the elements in an array, calling the argument function
//M: with the rank, position vector, and array element cast as a double.
- iterateAsDouble: anObject using: (void (*) (unsigned rank, unsigned *vec, double val))func;

//M: Iterates through the elements in an array, calling the argument function
//M: with the rank, position vector, and array element cast as an integer.
- iterateAsInteger: anObject using: (void (*) (unsigned rank, unsigned *vec, int val))func;

//M: The setData:To: method sets the probedVariable using the pointer
//M: to the new value.
- (void)setData: anObject To: (void *)newValue;  // pass by reference.

//M: The setData:ToString: sets the probedVariable using a string
//M: which the probe reads and converts appropriately.
//M: When setting the value of an 
//M: unsigned char or a char using this method, the expected format of
//M: the string is always "%i" unless CharString was chosen
//M: (in which case the format should be "'%c'").
- (BOOL)setData: anObject ToString: (const char *)s; 

//M: Sets the probeVariable value using a double.  This requires
//M: that the value is numeric.
- (void)setData: anObject ToDouble: (double)val;

externvar id <Symbol> DefaultString, CharString, IntString;
@end

@protocol MessageProbe <Probe, CREATABLE>
//S: A class that allows the user to call a given message on any
//S: candidate that is an instance of, or inherits from, a given class.

//D: This is a specialized subclass of the abstract class Probe.
//D: It completes the specification of a probe that refers to a
//D: message element of an object. 

CREATING
//M: The setProbedSelector: method sets the message to be probed given
//M: the selector. 
- setProbedSelector: (SEL)aSel;

//M: The setProbedMessage: method sets the message to be probed given
//M: the message name.  In dynamically-typed languages like JavaScript
//M: selectors don't make sense, since method argument types aren't
//M: fixed.
- setProbedMethodName: (const char *)methodName;

//M: Convenience factory method for common case.
+ create: (id <Zone>)aZone setProbedSelector: (SEL)aSel;

SETTING
//M: The setHideResult: method is used to set the visibility of the
//M: result field.  When set to 1, the user is indicating that
//M: the result field in a graphical representation of the message
//M: probe should not be shown.
- setHideResult: (BOOL)val;

USING
//M: The isResultId method returns 1 if the return value of the message is of
//M: type object, and returns 0 otherwise.
- (BOOL)isResultId;

//M: The isArgumentId: method returns 1 if a given argument of the message
//M: is of type object, and returns 0 otherwise.
- (BOOL)isArgumentId: (unsigned)which;

//M: The getProbedMessage method returns the string matching the identifier of
//M: the message being probed.
- (const char *)getProbedMessage;

- (unsigned)getArgCount;

//M: The getArg: method returns the argument type for a given index.
- (val_t)getArg: (unsigned)which;

//M: The getArgName: method returns a string representation of the argument
//M: key with the given index.
- (const char *)getArgName: (unsigned)which;

//M: The getHideResult method returns 1 if the result field is "hidden".
- (BOOL)getHideResult;

//M: The setArg:ToString: method sets the nth argument of the message. 
//M: The argument must be provided in string form.
- setArg: (unsigned)which ToString: (const char *)what;

//M: The setArg:ToUnsigned: method sets the nth argument of the message 
//M: used by the probe to an unsigned integer value.  The user is 
//M: responsible for matching the unsigned integer type of this 
//M: argument with the argument type of the method being probed.
- setArg: (unsigned)which ToUnsigned: (unsigned)x;

//M: The dynamicCallOn: method generates a dynamic message call on the target
//M: object. 
- (val_t)dynamicCallOn: target;

//M: The doubleDynamicCallOn: method generates a dynamic message call on the 
//M: target object. This method assumes the user knows the type to be
//M: numeric and would like a direct translation into type double.
- (double)doubleDynamicCallOn: target;

//M: The longDynamicCallOn: method generates a dynamic message call on the 
//M: target object. This method assumes the user knows the return 
//M: type to be numeric and would like a direct translation into type logn.
- (long)longDynamicCallOn: target;

//M: The stringDynamicCallOn: method generates a dynamic message call on the 
//M: target object. This method assumes the user knows the return type to be 
//M: const char *.
- (const char *)stringDynamicCallOn: target;

//M: The objectDynamicCallOn: method generates a dynamic message call on the 
//M: target object. This method assumes the user knows the return type to be 
//M: id.
- objectDynamicCallOn: target;
@end

@protocol ProbeMap <SwarmObject, ProbeConfig, CREATABLE>
//S: A container class for Probes used to specify the contents of a 
//S: ProbeDisplay.

//D: A ProbeMap is a Map-type collection of Probes. They are used to gather 
//D: several Probes, who usually have a common referent, into a single
//D: bundle. For example, all the instance variables of a ModelSwarm might be 
//D: gathered into a single ProbeMap. Each ProbeMap is then installed
//D: into the global ProbeLibrary. 

CREATING
//M: The setProbedClass: method sets the class of the object that the set of 
//M: probes that constitute the probe map points at. This message must be sent 
//M: before createEnd. 
- setProbedClass: (Class)class_;

- setProbedObject: object;
USING
//M: The getCount method returns the number of probes in the ProbeMap.
- (unsigned)getCount;

//M: The getProbedClass method returns the class of the object that the set of 
//M: probes that constitute the probe map points at.
- (Class)getProbedClass;

//M: The addProbe: method adds a probe to the contents of the ProbeMap.
//M: The ProbeMap will always make sure that the probedClass of the Probe being
//M: added corresponds to its own probedClass.
- addProbe: (id <Probe>)aProbe;

//M: The addProbeMap: method is used to tailor the contents of a ProbeMap by
//M: performing "set inclusion" with another ProbeMap. The typing is verified 
//M: prior to inclusion.
- addProbeMap: aProbeMap;

// These should use the word remove not drop.
//M: The dropProbeForVariable: method is used to drop a Probe from the 
//M: ProbeMap. No class verification takes place since the probe is dropped
//M: based on its variableName, not its actual id value.
- (void)dropProbeForVariable: (const char *)aVariable; 

//M: The dropProbeForMessage: method is used to drop a Probe from the ProbeMap.
//M: No class verification takes place since the probe is dropped based on its
//M: messageName, not its actual id value.
- (void)dropProbeForMessage: (const char *)aMessage;  

//M: The dropProbeMap: method is used to drop a probe from a probe map. It is
//M: equivalent to callling dropProbeForVariable for each variable name present
//M: in the ProbeMap being dropped, followed by a call to dropProbeForMessage
//M: for each message name present in the ProbeMap being dropped.
- dropProbeMap: aProbeMap;

//M: The begin: method returns an iterator (index) over the ProbeMap. This 
//M: index is used in the exact same way any Map index is used. 
- (id <Index>)begin: (id <Zone>)aZone; // returns an index to the underlying Map.

//M: The clone: method returns a clone of the probe map. If the initial probe
//M: map created by Library Generation or by the default version of Object 
//M: generation, the probe map should be cloned prior to making changes to it 
//M: to avoid having the changes affect the other potential users of the 
//M: probe map.
- clone: (id <Zone>)aZone;

@end

@protocol Swarm <SwarmProcess, CREATABLE>
//S: A temporal container.

//D: A Swarm is a community of agents sharing a common timescale as
//D: well as common memory pool.

USING
//M: Override this to let your Swarm create the objects that it 
//M: contains. 
- buildObjects;

//M: Override this to let your Swarm build its actions.
- buildActions;

#ifndef IDL
//M: Override this to activate any actions you built in buildActions.
//M: Note, you must activate yourself first before you can activate
//M: actions inside you. 

//E: [super activateIn: swarmContext];
//E: [fancySchedule activateIn: self];
//E: return [self getSwarmActivity];    
- (id <Activity>)activateIn: swarmContext;
#endif

//M: Needed to support probing of Swarms.
- (id <ProbeMap>)getProbeMap;

//M: Needed to support probing of Swarms.
- (id <ProbeMap>)getCompleteProbeMap;

//M: Needed to support probing of Swarms.
- (id <VarProbe>)getProbeForVariable: (const char *)aVariable;    
@end

@protocol DefaultProbeMap <ProbeMap, CREATABLE>
//S: A subclass of ProbeMap, whose initial state contains all the VarProbes 
//S: of the requested target class and also those of all its
//S: superclasses.

//D: A subclass of ProbeMap, whose initial state contains all the VarProbes 
//D: of the requested target class and also those of all its
//D: superclasses.
@end

@protocol CustomProbeMap <ProbeMap, CREATABLE> 
//S: A subclass of ProbeMap, whose initial state is empty unlike the default
//S: probeMap initial state which contains all the VarProbes of the requested
//S: target class.

//D: This subclass of the ProbeMap is used to create probe maps which are 
//D: initialised in an emtpy state or with the VarProbes and MessageProbes 
//D: intended. In other words, the probed class is set, as is the case with 
//D: the normal ProbeMap class but upon createEnd no VarProbes or 
//D: MessageProbes will be present within it. This feature is useful when 
//D: creating a probe map from scratch (e.g. to be used in conjunction with 
//D: the setProbeMap:For: message of the ProbeLibrary). 
CREATING

//M: Convenience method for creating a CustomProbeMap in which the
//M: user specifies the list of variables and methods to be probed
//M: this by passing a delimited list of strings of the form: "var1",
//M: "var2", ..., ":", "method1", "method2",..., NULL
+ create: (id <Zone>)aZone forClass: (Class)aClass withIdentifiers: (const char *)vars, ...;
SETTING
//M: Allows further probes specified in the delimited string list to
//M: be added *after* the CustomProbeMap has been created
- addProbesForClass: (Class) aClass 
    withIdentifiers:  (const char *)vars, ...;
USING
@end

@protocol EmptyProbeMap <CustomProbeMap, CREATABLE>
//S: A CustomProbeMap to be used for building up ProbeMaps from scratch.

//D: A CustomProbeMap to be used for building up ProbeMaps from scratch.

CREATING
//M: Convenience method for creating an EmptyProbeMap 
+ create: (id <Zone>)aZone forClass: (Class)aClass;
@end

@protocol CompleteProbeMap <ProbeMap, CREATABLE>
//S: A subclass of ProbeMap whose initial state contains the VarProbes and
//S: MessageProbes of the requested target class but also those of all its
//S: subclasses.

//D: Upon creation, this subclass of the ProbeMap will contain all the 
//D: variables and all the messages of a given class (including the inherited
//D: ones). 
@end

@protocol CompleteVarMap <ProbeMap, CREATABLE>
//S: A subclass of ProbeMap, whose initial state contains no MessageProbes.

//D: A subclass of ProbeMap, whose initial state contains no MessageProbes,
//D: but does contain all the VarProbes of the requested target class and 
//D: those of all its superclasses.
@end

@protocol ProbeLibrary <Create, Drop, ProbeConfig, CREATABLE> 
//S: A (singleton) Class, whose instance is used as a container for a global
//S: mapping between classnames and their 'default' ProbeMaps. These defaults
//S: can be changed by the user, thus allowing him/her to customize the default
//S: contents of the ProbeDisplays generated when probing objects.

//D: The normal Swarm simulation will probably only ever contain one instance 
//D: of this class, namely the probeLibrary object. This object is used
//D: for Library Generation of Probes and ProbeMaps: its role is to cache one 
//D: unique "official" ProbeMap for every Class ever probed during a
//D: run of Swarm. These ProbeMaps are generated as they are requested. 

USING
//M: The setDisplayPrecision: method sets the number of significant digits for 
//M: floating point and double floating point numbers displayed on GUI widgets.
//M: This method is currently only implemented for VarProbes. It has not been 
//M: implemented for MessageProbes yet. 
//M: The setDisplayPrecision method allows all probes checked out from the 
//M: global ProbeLibrary instance to access this displayed precision. However, 
//M: individual probes can vary from this global default, by using the 
//M: setFloatFormat method on a exisiting probe. 
- setDisplayPrecision: (unsigned)nSigDisplay;

//M: The getDisplayPrecision method gets the current display precision set in 
//M: the ProbeLibrary instance.
- (unsigned)getDisplayPrecision;

//M: The setSavedPrecision: method sets the number of significant digits saved 
//M: for floating-point and double floating-point numbers through ObjectSaver. 
//M: This function sets the global default precision for all floating point 
//M: numbers, including double floating point numbers. This floating point 
//M: precision affects all numbers saved via the ObjectSaver class. There is 
//M: currently no way to override this global default for an individual probe. 
- setSavedPrecision: (unsigned)nSigSaved;

//M: The getSavedPrecision method gets the current saved precision set in the 
//M: ProbeLibrary instance.
- (unsigned)getSavedPrecision;

//M: The isProbeMapDefinedFor: method returns True if there is a non-nil value 
//M: in the ProbeLibrary for that class and False otherwise.
- (BOOL)isProbeMapDefinedFor: (Class)aClass;
- (BOOL)isProbeMapDefinedForObject: anObject;

//M: The getProbeMapFor: method returns a ProbeMap for the aClass class. If a 
//M: specific ProbeMap has been designed and installed in the ProbeLibrary for 
//M: that class, then that specific ProbeMap is returned. If a custom ProbeMap 
//M: was not designed and installed, then a CompleteProbeMap is created and 
//M: returned.
- (id <ProbeMap>)getProbeMapFor: (Class)aClass;
- (id <ProbeMap>)getProbeMapForObject: anObject;

//M: The getCompleteProbeMapFor: method returns a ProbeMap containing Probes 
//M: for all the instance variables and messages of the given Class (including 
//M: inherited variables and messages). The current implementation of 
//M: ProbeLibrary does not cache CompleteProbeMaps. 
- (id <ProbeMap>)getCompleteProbeMapFor: (Class)aClass;
- (id <ProbeMap>)getCompleteProbeMapForObject: anObject;

//M: The getCompleteVarMapFor: method returns a ProbeMap containing Probes for 
//M: all the instance variables of the given Class (including inherited 
//M: variables) but does not include any MessageProbes. 
- (id <ProbeMap>)getCompleteVarMapFor: (Class)aClass;
- (id <ProbeMap>)getCompleteVarMapForObject: anObject;

//M: The getProbeForVariable:inClass: method returns a probe that has been 
//M: "checked out" from the appropriate Probes in the probe library. 
//M: Note: The returned probe will be cached so to avoid affecting the results 
//M:       of future requests for the same probes, clone the probe prior to 
//M:       making modifications to the probe.
- (id <VarProbe>)getProbeForVariable: (const char *)aVar inClass: (Class)aClass;
- (id <VarProbe>)getProbeForVariable: (const char *)aVar inObject: anObject;

//M: The getProbeForMessage:inClass: method returns a probe that has been 
//M: "checked out" from the appropriate Probes in the probe library. 
//M: Note: The returned probe will be cached so to avoid affecting the results 
//M:       of future requests for the same probes, clone the probe prior to 
//M:       making modifications to the probe.
- (id <MessageProbe>)getProbeForMessage: (const char *)aMessage inClass: (Class)aClass;
- (id <MessageProbe>)getProbeForMessage: (const char *)aMessage inObject: anObject;

//M: The setProbeMap:For: method sets the standard probe map as the probe map.
//M: The returned Probe will be cached as though it was produced by the
//M: library itself.
- setProbeMap: (id <ProbeMap>)aMap For: (Class)aClass; 
- setProbeMap: (id <ProbeMap>)aMap ForObject: anObject;

@end

//G: The global librarian for ProbeMaps.
externvar id <ProbeLibrary> probeLibrary;

@protocol ActivityControl <SwarmObject, CREATABLE>
//S: A class that provides an object interface to an activity.

//D: The ActivityControl class specifies an object that can be attached to 
//D: an activity (regardless of how or where that activity is created) for
//D: the purpose of explicitly controlling the execution of actions on that 
//D: activity's action plan. There is nothing that available through this 
//D: class that is not already available through the variables or messages 
//D: available via the activity itself. However, it packages up the main 
//D: control messages and makes them available to other objects that may need 
//D: control over an activity, thereby shielding the activity from directly
//D: receiving messages from outside objects and saving the user from having 
//D: to parse the more complex interface to the activity. 

CREATING

USING

//M: The attachToActivity: method sets an instance variable inside the 
//M: ActivityControl object that points to the Activity to be controlled. 
//M: It then creates a Schedule upon which it places a message to itself to
//M: update its own variables.
- (void)attachToActivity: (id <ScheduleActivity>)anActivity;

//M: The run method sends a run message to the activity if the conditions are
//M: appropriate.  This message causes the activity to continue executing the
//M: actions on its schedule until either no other actions are waiting, or 
//M: until the execution of actions is stopped by a subactivity or stopped by
//M: a stop message to the activity.  If the activity completes executing all 
//M: the actions on its schedule, the run method returns Completed.
- (id <Symbol>)runActivity;

//M: The stop method sends a stop message to the activity if the conditions 
//M: are appropriate. This message causes the control to move back up the 
//M: run-stack and resume at the place in the code where the run was first 
//M: executed. The next action on the super-activity will begin without  
//M: finishing the rest of the current activity's actions. 
- (id <Symbol>)stopActivity;

//M: The next method sends a next message to the activity if the conditions 
//M: are appropriate. It runs an activity forward through as many actions as 
//M: necessary until it hits a breakFunction, at which point it walks back up 
//M: the tree of activities and returns Stopped. In most cases, this means
//M: that an entire action or action group on the activity under control 
//M: will be executed, including completion of all subactivities. 
- (id <Symbol>)nextAction;

//M: The step method sends a step message to the activity if the conditions
//M: are appropriate. It causes the execution of a single action. 
- (id <Symbol>)stepAction;

//M: The stepUntil: method sends a stepUntil: message to the activity if 
//M: conditions are appropriate. This causes all actions on the activity's 
//M: schedule, including any actions on subactivities' schedules, to be 
//M: executed until the activity's relative time is equal to stopTime - 1. 
- (id <Symbol>)stepUntil: (timeval_t)stopTime;

//M: The updateStateVar method updates the ActivityControl instance variables
//M: and tests for the continued existence of the activity that is being 
//M: controlled. This message is sent on each cycle of the schedule for the 
//M: activity being controlled.
- (void)updateStateVar;

//M: The getStatus method returns the status of the activity.
- (id <Symbol>)getStatus;

//M: Recursively removes all subactivities.
- (void)terminate;

//M: Return the controlled activity.
- (id <ScheduleActivity>)getActivity;
@end

@class Probe;
@class VarProbe;
@class MessageProbe;
@class ProbeMap;
@class DefaultProbeMap;
@class CustomProbeMap;
@class EmptyProbeMap;
@class CompleteProbeMap;
@class CompleteVarMap;
@class ProbeLibrary;
@class ActivityControl;
@class Swarm;
@class SwarmObject;

//G: The version of Swarm being used.
externvar const char *swarm_version;
