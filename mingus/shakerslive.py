from mingus.containers.Note import Note
from mingus.core import progressions
import mingus.core.notes as notes
import play
import time

doc = """
    Simple Gifts
 an 1848 Shaker song

'Tis the gift to be simple, 'tis the gift to be free,
'Tis the gift to come down where you ought to be,
And when we find ourselves in the place just right,
'Twill be in the valley of love and delight.
When true simplicity is gain'd,
To bow and to bend we shan't be asham'd,
To turn, turn will be our delight,
Till by turning, turning we come round right.
"""

harmonies = "I ii ii iii7 iii7 IV V vi vi viidim viidom7".split()

score  = [                        'G-4         G-4        ',
                                  'I:2                    ',
          'C-5         C-5:8 D-5:8 E-5:8 C-5:8 E-5:8 F-5:8',
          '0:1',
          'G-5         G-5:8 G-5:8 E-5         D-5:8 C-5:8',
          '0:1',
          'D-5         D-5         D-5         D-5'        ,
          '0:1',
          'D-5:8 E-5:8 D-5:8 B-4:8 G-4         G-4'        ,
          '0:1',
          'C-5:8 B-4:8 C-5:8 D-5:8 E-5         D-5:8 D-5:8',
          '0:1',
          'E-5         F-5         G-5:3             G-5:8',
          '0:1',
          'D-5         D-5:8 E-5:8 D-5         C-5:8 C-5:8',
          '0:1',
          'D-5         C-5:8 B-4:8 C-5:2'                  ,
          '0:1',
          'G-5:2             E-5:3                   D-5:8',
          '0:1',
          'E-5:8 F-5:8 E-5:8 D-5:8 C-5:3             D-5:8',
          '0:1',
          'E-5         E-5:8 F-5:8 G-5         E-5'        , 
          '0:1',
          'D-5         D-5:8 E-5:8 D-5:3             G-4:8',
          '0:1',
          'C-5:2                   C-5:3             D-5:8',
          '0:1',
          'E-5         E-5:8 F-5:8 G-5         G-5:8 G-5:8',
          '0:1',
          'D-5         D-5         E-5         E-5:8 D-5:8',
          '0:1',
          'C-5         C-5         C-5:2',
          '0:1']

def go(key='C', meter=(4,4), score=score, mul=1.0):
    _, base = meter
    for i in xrange(0, len(score)/2):
        for mn in score[2*i].split():
            nr = mn.split(':')
            n = nr[0]
            r = base
            if len(nr) > 1:
                r = int(nr[1])
            play.play_high_note(n)
            time.sleep(1.0*mul*base/r)

def main():
    play.init()
    [chord] = progressions.to_chords(['I'], 'C')
    play.play_chord(chord)
    go()

if __name__ == '__main__':
    main()
