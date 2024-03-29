/* Interface for Objective-C Tcl, Tk interpreter object
   Copyright (C) 1993,1994  R. Andrew McCallum

   Written by:  R. Andrew McCallum <mccallum@cs.rochester.edu>
   Dept. of Computer Science, U. of Rochester, Rochester, NY  14627

   This file is part of the Tcl/Objective-C interface library.

   This library is free software; you can redistribute it and/or
   modify it under the terms of the GNU Library General Public
   License as published by the Free Software Foundation; either
   version 2 of the License, or (at your option) any later version.
   
   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Library General Public License for more details.

   You should have received a copy of the GNU Library General Public
   License along with this library; if not, write to the Free
   Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
*/ 

#ifndef _TkInterp_h
#define _TkInterp_h

#include "TclInterp.h"
#include <tk.h>

@interface TkInterp: TclInterp
{
  BOOL stopped;
  @public
  Tcl_DString command;
}

//- (const char *)checkTkLibrary;
- (void)stop;
- (Tk_Window)mainWindow;

@end

#endif /* _TkInterp_h */
