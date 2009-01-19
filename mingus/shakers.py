from mingus.containers.Bar import Bar
from mingus.containers.Track import Track
from taSequencer import taSequencer

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

def toTrack(key, meter, melody):
    _, base = meter
    t = Track()
    for mb in melody:
        b = Bar(key, meter)
        for mn in mb.split():
            nr = mn.split(':')
            n = nr[0]
            r = base
            if len(nr) > 1:
                r = int(nr[1])
            ok = b.place_notes(n, r)
        t.add_bar(b)
    return t

melody = [                        'G-4         G-4',
          'C-5         C-5:8 D-5:8 E-5:8 C-5:8 E-5:8 F-5:8',
          'G-5         G-5:8 G-5:8 E-5         D-5:8 C-5:8',
          'D-5         D-5         D-5         D-5'        ,
          'D-5:8 E-5:8 D-5:8 B-4:8 G-4         G-4'        ,
          'C-5:8 B-4:8 C-5:8 D-5:8 E-5         D-5:8 D-5:8',
          'E-5         F-5         G-5:3             G-5:8',
          'D-5         D-5:8 E-5:8 D-5         C-5:8 C-5:8',
          'D-5         C-5:8 B-4:8 C-5:2'                  ,
          'G-5:2             E-5:3                   D-5:8',
          'E-5:8 F-5:8 E-5:8 D-5:8 C-5:3             D-5:8',
          'E-5         E-5:8 F-5:8 G-5         E-5'        , 
          'D-5         D-5:8 E-5:8 D-5:3             G-4:8',
          'C-5:2                   C-5:3             D-5:8',
          'E-5         E-5:8 F-5:8 G-5         G-5:8 G-5:8',
          'D-5         D-5         E-5         E-5:8 D-5:8',
          'C-5         C-5         C-5:2']

track = toTrack("C", (4,4), melody)

def playTrack(track=track, bpm=60):
    import sf2
    m = taSequencer(sf2.default)
    m.play_Track(track=track, bpm=bpm)

playTrack()
