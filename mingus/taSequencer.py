from mingus.midi.fluidsynth import MidiSequencer
from mingus.containers.Note import Note
import mingus.core.notes as notes
import sys
# TODO add piano

movable = "do di ra re ri me mi fa fi se sol si le la li te ti".split()
fixed = "do ra re me mi fa se sol le la te si".split()

class taSequencer(MidiSequencer):

  def __init__(self, sf2, driver = None):
     MidiSequencer.__init__(self)
     self.start_audio_output(driver)
     self.load_sound_font(sf2)
     self.fs.program_reset()
     self.i = 0

  def play_Note(self, note, channel = 0, velocity = 100):
      if MidiSequencer.play_Note(self, note, channel, velocity):
         print self.i, "(",note,'--',fixed[notes.note_to_int(note.name)],
         sys.stdout.flush()
         self.i += 1
         return True

  def stop_Note(self, note, channel = 0):
      if MidiSequencer.stop_Note(self, note, channel):
         print ")"
         sys.stdout.flush()
         return True

def testTaSequencer():
   import sf2
   m = taSequencer("ChoriumRevA.SF2")
   m.play_Note(Note("C-5"))

if __name__ == '__main__':
  testTaSequencer()
