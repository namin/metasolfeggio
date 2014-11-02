// Spanish Romance

Tab tab;

class GuitarPlayer
{
    Mandolin s[6];
    s[0] => JCRev rev => Gain master => dac;
    s[1] => rev;
    s[2] => rev;
    s[3] => rev;
    s[4] => rev;
    s[5] => rev;
    0.03 => rev.mix;

    0.5*(s[1].gain()) => s[1].gain;
    0.5*(s[2].gain()) => s[2].gain;
    0.5*(s[3].gain()) => s[3].gain;

    0.0 => s[4].stringDamping;
    0.02 => s[4].stringDetune;
    0.05 => s[4].bodySize;
    0.0 => s[5].stringDamping;
    0.02 => s[5].stringDetune;
    0.05 => s[5].bodySize;

    fun void play(int n[], int m)
    {
        for (0 => int i; i<6; i++)
        {
            if (n[i]>=0)
            {
                Std.mtof(tab.tuning[i]+n[i]) => s[i].freq;
                Std.rand2f(0.3,1) => s[i].noteOn;
            }
        }
        tab.beat => now;
    }
}

GuitarPlayer p;

now => time start;

for (0 => int i; i < tab.length; i++)
{
    p.play(tab.at(i), i);
}

<<< (now - start)/(1::second) >>>;

