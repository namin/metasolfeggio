#!/usr/bin/env python

__doc__ = """usage: tab2chuck.py <tab.txt>
converts guitar tab to chuck 2-dimensional array
"""

numStrings = 6

def clean(bar, i):
  if bar[i]=='-' or (i>0 and bar[i-1] in '0123456789'):
    return -1
  else:
    s = bar[i]
    n = len(bar)
    while (i+1)<n and bar[i+1] in '0123456789':
      s += bar[i+1]
      i += 1
    if len(s)==1:
      s = " "+s
    return s

def go(txt):
  print 'public class Partition'
  print '{'
  print '['
  p = gr(txt)
  first = True
  c = 0
  bars = []
  for l in p:
    n = len(l[0])
    for i in range(0, n):
      if l[0][i] in '0123456789-':
        if first:
          print
        else:
          print ','
        first = False
        c += 1
        print '[',
        for j in range(0, numStrings):
          print clean(l[j], i),
          if j+1<numStrings:
            print ",",
        print ']',
      else:
        print
        if c not in bars:
          bars.append(c)
  print '] @=> static int tab[][];'
  print
  print bars, '@=> static int bars[];'
  print '}'

def gr(txt):
  lines = txt.split()
  p = []
  while (lines != []):
    g = []
    for i in range(0, numStrings):
      g.append(lines[0])
      lines = lines[1:]
    p.append(g)
  return p

if __name__ == '__main__':
  import sys
  argc = len(sys.argv)
  if argc!=2:
    print __doc__
  else:
    with open(sys.argv[1], 'r') as tab_file:
      tab_txt = tab_file.read()
      go(tab_txt)
