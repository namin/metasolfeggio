from mingus.midi.fluidsynth import MidiSequencer
from mingus.containers.Note import Note
import sys

class taSequencer(MidiSequencer):

  def __init__(self, sf2, driver = None):
     MidiSequencer.__init__(self)
     self.start_audio_output(driver)
     self.load_sound_font(sf2)
     self.fs.program_reset()

  def play_Note(self, note, channel = 0, velocity = 100):
      if MidiSequencer.play_Note(self, note, channel, velocity):
         print "(",note,
         sys.stdout.flush()
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
