# background tune
# IAM - Un cri court dans la nuit feat. Daddy Nuttea
# https://www.youtube.com/watch?v=7pjLV2-pnbY (Audio officiel)
#

use_bpm 120

def tin(first, third=79)
  play first
  sleep 1
  play 75
  sleep 1
  play third
  sleep 1
  play 75
  sleep 1
end

loop do
  tin 72
  tin 71
  tin 70
  tin 69, 77
end
