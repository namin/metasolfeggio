use_synth :piano
use_bpm 260

define :d do |x|
  m = x % 7
  if m==0 then m=7 end
  m
end

define :l do |i|
  play_pattern (chord_degree d(1-i), :c, :minor, 3)
  play_pattern (chord_degree d(1-i), :c, :minor, 3, invert: 3)
  play_pattern (chord_degree d(6-i), :c, :minor, 1, invert: 1)
  play_pattern (chord_degree d(5-i), :c, :minor, 1, invert: 1)
  play_pattern (chord_degree d(4-i), :c, :minor, 1, invert: 1)
  play_pattern (chord_degree d(3-i), :c, :minor, 1, invert: 1)
  play_pattern (chord_degree d(4-i), :c, :minor, 1, invert: 1)
  play_pattern (chord_degree d(4-i), :c, :minor, 3, invert: 1)
end

l 0
l 1
l 2

i = 3
play_pattern (chord_degree 1, :g, :major, 3)
play_pattern (chord_degree 1, :g, :major, 3, invert: 3)
play_pattern (chord_degree d(6-i), :c, :minor, 1, invert: 1)
play_pattern (chord_degree d(5-i), :c, :minor, 1, invert: 1)
play_pattern (chord_degree d(4-i), :c, :minor, 1, invert: 1)
play_pattern (chord_degree 3, :g, :major, 1, invert: 0)
play_pattern (chord_degree d(4-i), :c, :minor, 1, invert: 1)
play_pattern (chord_degree d(4-i), :c, :minor, 3, invert: 1)
