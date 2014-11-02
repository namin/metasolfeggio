// Spanish Romance

Tab tab;

// sound chain
SndBuf hihat => dac;

// me.dirUp 
me.dir(-1) + "/audio/hihat_01.wav" => hihat.read;

// parameter setup
.5 => hihat.gain;

for (0 => int i; i < tab.length; i++)
{
    tab.swing(i);
    if ((i % 9) == 0) {
        Math.random2f(0.1,.3) => hihat.gain;
        Math.random2f(.9,1.2) => hihat.rate;
        0 => hihat.pos;
    } else if ((i % 3) == 0) {
        Math.random2f(0.0,0.1) => hihat.gain;
        Math.random2f(.9,1.2) => hihat.rate;
        0 => hihat.pos;        
    }
    tab.beat => now;
 }
