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

#import <swarmgstep/Value2dDisplay.h>
#import <space.h>
#import <space/Discrete2d.h>
#ifndef GNUSTEP
#import <gui.h>
#else
#include <AppKit/AppKit.h>
#endif
#import <defobj.h> // raiseEvent

// This should be subclassed to fill in the colormap for your CA.
// for now, we expect your colormap to be allocated already.

@implementation Value2dDisplay

PHASE(Creating)

#ifndef GNUSTEP
+ create: aZone setDisplayWidget: (id <Raster>)r colormap: (id <Colormap>)c setDiscrete2dToDisplay: (id <GridData>)d
#else
+ create: aZone setDisplayWidget: (id)r colormap: (id)c setDiscrete2dToDisplay: (id <GridData>)d
#endif
{
  Value2dDisplay *obj = [self createBegin: aZone];

  [obj setDisplayWidget: r colormap: c];
  [obj setDiscrete2dToDisplay: d];

  return [obj createEnd];
}

#ifndef GNUSTEP
- setDisplayWidget: (id <Raster>)r colormap: (id <Colormap>)c
#else
- setDisplayWidget: (id)r colormap: (id)c
#endif
{
  displayWidget = r;
  colormap = c;
  
  drawPointImp = [(Object *)r methodFor: @selector (drawPointX:Y:Color:)];
  return self;
}

- setDiscrete2dToDisplay: (id <GridData>)c
{
  discrete2d = c;
  return self;
}

- createEnd
{
  [super createEnd];

#if 0
  if (displayWidget == nil || discrete2d == nil)
    raiseEvent (InvalidCombination, "Value display improperly initialized\n");
#endif
  
  if (modFactor == 0)
    modFactor = 1;

  return self;
}

PHASE(Using)

- (id <GridData>)discrete2d
{
  return discrete2d;
}
     
// linear transform between values and colours. Good enough?
- setDisplayMappingM: (int)m C: (int)c
{
  modFactor = m;
  colorConstant = c;
  return self;
}

- displayX:(int)xPos Y:(int)yPos inRect:(NSRect)aRect;
{
  long color;
  float redValue;
  NSColor *aColor;
  id *lattice;
  long *offsets;

  lattice = [discrete2d getLattice];
  offsets = [discrete2d getOffsets];
  color = (long) *(discrete2dSiteAt(lattice, offsets, xPos, yPos));
  redValue = (float)color / (float)modFactor;
  aColor = [NSColor colorWithDeviceRed: redValue green: 0.0
		    blue: 0.0 alpha: 1.0];
  [aColor set];
  PSrectfill(aRect.origin.x, aRect.origin.y,
	     aRect.size.width, aRect.size.height);

  return self;
}

- (void)displayOn: (NSImage *)anImage
{
  int x, y;
  id *lattice;
  long *offsets;
  int xsize, ysize;

  lattice = [discrete2d getLattice];
  offsets = [discrete2d getOffsets];
  xsize = [discrete2d getSizeX];
  ysize = [discrete2d getSizeY];

  for (y = 0; y < ysize; y++)
    for (x = 0; x < xsize; x++)
      {
	long color;
	float redValue;
	NSColor *aColor;

        color = (long) *(discrete2dSiteAt(lattice, offsets, x, y));
        redValue = (float)color / (float)modFactor;
	aColor = [NSColor colorWithDeviceRed: redValue green: 0.0
			  blue: 0.0 alpha: 1.0];
	[aColor set];
	PSrectfill(3*x, 3*y, 3, 3);
#if 0
        long color;

        color = (long) *(discrete2dSiteAt(lattice, offsets, x, y));
        color = color / modFactor + colorConstant;
        if (color < 0 || color > 255)
          raiseEvent (WarningMessage,
                      "Value2dDisplay: found colour %d not in [0,255].\n",
                      color);
        
        if (drawPointImp)
          // cache method lookup.
          (void) (*drawPointImp) (displayWidget,
                                  @selector (drawPointX:Y:Color:),
                                  x, y, color);
        else
          [displayWidget drawPointX: x Y: y Color: (unsigned char)color];
#endif
      }
}

- display
{
  int x, y;
  id *lattice;
  long *offsets;
  int xsize, ysize;

  lattice = [discrete2d getLattice];
  offsets = [discrete2d getOffsets];
  xsize = [discrete2d getSizeX];
  ysize = [discrete2d getSizeY];

  for (y = 0; y < ysize; y++)
    for (x = 0; x < xsize; x++)
      {
        long color;

        color = (long) *(discrete2dSiteAt(lattice, offsets, x, y));
        color = color / modFactor + colorConstant;
        if (color < 0 || color > 255)
          raiseEvent (WarningMessage,
                      "Value2dDisplay: found colour %d not in [0,255].\n",
                      color);
        
        if (drawPointImp)
          // cache method lookup.
          (void) (*drawPointImp) (displayWidget,
                                  @selector (drawPointX:Y:Color:),
                                  x, y, color);
        else
          [displayWidget drawPointX: x Y: y Color: (unsigned char)color];
      }
  return self;
}

@end



