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
Name:         Symbol.h
Description:  classes to define distinct global id constants
Library:      defobj
*/

#import <defobj/Create.h>

@interface Symbol_c: CreateDrop_s <Symbol>
{
@public
   const char *name;
}
/*** methods in Symbol_c (inserted from .m file by m2h) ***/
- createEnd;
+ create: aZone setName: (const char *)symbolName;
- (const char *)getName;
- (void)describe: outputCharStream;
@end

@interface EventType_c: Symbol_c
/*** methods in EventType_c (inserted from .m file by m2h) ***/
- (void)raiseEvent;
- (void)raiseEvent: (const void *)msgString, ...;
@end

@interface Warning_c: EventType_c
{
@public
   const char *messageString;
}
/*** methods in Warning_c (inserted from .m file by m2h) ***/
- (void) setMessageString: (const char *)messageStr;
- (const char *) getMessageString;
- (void) raiseEvent;
- (void) raiseEvent: (const void *)eventData, ...;
- (void) describe: outputCharStream;
@end

@interface Error_c: Warning_c
/*** methods in Error_c (inserted from .m file by m2h) ***/
- (void) raiseEvent;
- (void) raiseEvent: (const void *)eventData, ...;
@end
