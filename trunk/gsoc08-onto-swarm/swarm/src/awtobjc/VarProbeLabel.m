// Swarm library. Copyright (C) 1996-1998, 2000 Swarm Development Group.
// This library is distributed without any warranty; without even the
// implied warranty of merchantability or fitness for a particular purpose.
// See file LICENSE for details and terms of copying.

#import <awtobjc/VarProbeLabel.h>

@implementation VarProbeLabel

- createEnd
{
  [super createEnd];
  
  printf ("VarProbeLabel\n");

  return self;
}

@end

