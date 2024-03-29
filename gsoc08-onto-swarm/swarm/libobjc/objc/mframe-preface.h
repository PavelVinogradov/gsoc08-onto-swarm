/* General purpose definitions for the GNU Objective-C Library.
   Copyright (C) 1993, 1994, 1995, 1996 Free Software Foundation, Inc.

   Written by:  Andrew Kachites McCallum <mccallum@gnu.ai.mit.edu>
   Created: May 1993

   This file is part of the GNUstep Base Library.

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

#ifndef __preface_h_OBJECTS_INCLUDE
#define __preface_h_OBJECTS_INCLUDE

#undef PACKAGE
#undef VERSION
#include <config.h>
#undef PACKAGE
#undef VERSION

#if defined(__hpux__) && defined(HAVE_SYS_SIGEVENT_H)
#include <sys/sigevent.h>
#endif

#include <stdarg.h>
#include <stdlib.h>
#include <string.h> /* memcpy */

#if 0
#include <base/objc-gnu2next.h>
#include <base/fake-main.h>
#endif


typedef struct  {
  int           offset;
  unsigned      size;
  const char    *type;
#ifndef NO_GNUSTEP
  unsigned      align;
  unsigned      qual;
  BOOL          isReg;
#endif
} NSArgumentInfo;

#define NSCParameterAssert(x) assert(x)

#if (defined(__WIN32__) || defined(_WIN32)) && 0
#include <windows.h>
#define GNUSTEP_BASE_SOCKET_MESSAGE (WM_USER + 1)
#endif

#if NeXT_runtime
 #include <objc/objc.h>
 #include <objc/objc-class.h>
 #include <objc/objc-runtime.h>
 #ifndef _C_ATOM
  #define _C_ATOM '%'
 #endif
 #define _F_CONST    0x01
 #define _F_IN       0x01
 #define _F_OUT      0x02
 #define _F_INOUT    0x03
 #define _F_BYCOPY   0x04
 #define _F_ONEWAY   0x08
 #define _C_CONST    'r'
 #define _C_IN       'n'
 #define _C_INOUT    'N'
 #define _C_OUT      'o'
 #define _C_BYCOPY   'O'
 #define _C_ONEWAY   'V'
#else				/* GNU Objective C Runtime */
 #include <objc/objc.h>
 #include <objc/objc-api.h>
 #include <objc/encoding.h>
 #include <objc/sarray.h>
 /* #include <objc/objc-list.h> */
#endif

#ifndef __WIN32__
#include <sys/param.h> /* Hack to get rid of warning in GNU libc 2.0.3. */
#endif

#if 0
#include <Foundation/NSObject.h>
#include <Foundation/NSObjCRuntime.h>
#endif

#define OBJC_MALLOC(VAR, TYPE, NUM) \
   ((VAR) = (TYPE *) objc_malloc ((unsigned)(NUM)*sizeof(TYPE))) 
#define OBJC_VALLOC(VAR, TYPE, NUM) \
   ((VAR) = (TYPE *) objc_valloc ((unsigned)(NUM)*sizeof(TYPE))) 
#define OBJC_ATOMIC_MALLOC(VAR, TYPE, NUM) \
   ((VAR) = (TYPE *) objc_atomic_malloc ((unsigned)(NUM)*sizeof(TYPE))) 
#define OBJC_REALLOC(VAR, TYPE, NUM) \
   ((VAR) = (TYPE *) objc_realloc ((VAR), (unsigned)(NUM)*sizeof(TYPE)))
#define OBJC_CALLOC(VAR, TYPE, NUM) \
   ((VAR) = (TYPE *) objc_calloc ((unsigned)(NUM), sizeof(TYPE)))
#define OBJC_FREE(PTR) objc_free (PTR)

#ifndef MAX
#define MAX(a,b) \
       ({typeof(a) _MAX_a = (a); typeof(b) _MAX_b = (b);  \
         _MAX_a > _MAX_b ? _MAX_a : _MAX_b; })
#endif

#ifndef MIN
#define MIN(a,b) \
       ({typeof(a) _MIN_a = (a); typeof(b) _MIN_b = (b);  \
         _MIN_a < _MIN_b ? _MIN_a : _MIN_b; })
#endif

#ifndef ABS
#define ABS(a) \
       ({typeof(a) _ABS_a = (a); \
         _ABS_a < 0 ? -_ABS_a : _ABS_a; })
#endif

#ifndef STRINGIFY
#define STRINGIFY(s) XSTRINGIFY(s)
#define XSTRINGIFY(s) #s
#endif

#ifndef OBJC_STRINGIFY
#define OBJC_STRINGIFY(s) OBJC_XSTRINGIFY(s)
#define OBJC_XSTRINGIFY(s) @#s
#endif

#ifndef PTR2LONG
#define PTR2LONG(P) (((char*)(P))-(char*)0)
#endif
#ifndef LONG2PTR
#define LONG2PTR(L) (((char*)0)+(L))
#endif

#if VSPRINTF_RETURNS_LENGTH 
#define VSPRINTF_LENGTH(VSPF_CALL) (VSPF_CALL)
#else
#define VSPRINTF_LENGTH(VSPF_CALL) strlen((VSPF_CALL))
#endif /* VSPRINTF_RETURNS_LENGTH */


#endif /* __preface_h_OBJECTS_INCLUDE */
