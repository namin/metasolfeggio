#!/usr/bin/env python
from mingus.core import progressions, intervals
from mingus.core import chords as ch
from mingus.containers import NoteContainer, Note
from mingus.midi import fluidsynth
import time, sys
from random import random


SF2 = "ChoriumRevA.SF2"

key = 'C'

rules = [
   ["I", "vi", "ii", "iii7", "I7", "viidom7", "iii7", "V7", "I"],
   ["I", "IV", "V", "vi", "ii", "IV", "V", "I"],
   ["I", "V", "I"]]

def init():
   if not fluidsynth.init(SF2):
      print "Couldn't load soundfont", SF2
      sys.exit(1)

def play_basic_chord(chord):
   c = NoteContainer(chord)
   l = Note(c[0].name)
   l.octave_down()
   print ch.determine(chord)[0]
	
   # Play chord and lowered first note
   fluidsynth.play_NoteContainer(c)
   fluidsynth.play_Note(l)
   time.sleep(1.0)
	
   return c

def play_chord(chord):
   c = play_basic_chord(chord)

   # Play highest note in chord
   fluidsynth.play_Note(c[-1])

   # 50% chance on a bass note
   if random() > 0.5:
      p = Note(c[1].name)
      p.octave_down()
      fluidsynth.play_Note(p)
   time.sleep(0.50)
		
   # 50% chance on a ninth
   if random() > 0.5:
      p = Note(intervals.second(c[0].name, key))
      p.octave_up()
      fluidsynth.play_Note(p)
   time.sleep(0.25)

   # 50% chance on the second highest note
   if random() > 0.5:
      fluidsynth.play_Note(c[-2])
   time.sleep(0.25)

def play_progression(chords):
   for chord in chords:
      play_chord(chord)
      print "-" * 20

def main():
   init()
   progression = []
   for p in rules:
      progression.extend(p[:-1])
   chords = progressions.to_chords(progression, key)
   play_progression(chords)
   play_basic_chord(ch.I(key))

if __name__ == '__main__':
   main()
