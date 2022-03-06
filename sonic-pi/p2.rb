use_bpm 200

chords = [:c, :f, :g, :c].map {|n| chord(n, :major)}.ring

define :p1 do |c|
  play c
  sleep 1
  play_pattern_timed c, 0.25
  sleep 0.75
  play c
end

define :p2 do |c|
  use_synth :prophet
  play (pick c)
  sleep 2.5
end

in_thread(name: :t1) do
  loop do
    p1 chords[beat]
  end
end

in_thread(name: :t2) do
  loop do
    p2 chords[beat]
  end
end

