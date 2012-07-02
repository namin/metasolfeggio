(use 'overtone.live)
(use 'overtone.inst.sampled-piano)

(defn noise []
  (* (rand) 0.0))

(defn chord-n [n chord]
  (let [roots (iterate #(+ 12 %) 0)]
    (take n (flatten (map (fn [root] (map #(+ root %) chord)) roots)))))

(defn play-beat [metro beat notes]
  (let [n (count notes)]
    (dorun
     (map-indexed (fn [i note]
                    (at (metro (+ beat (noise) (/ i n)))
                      (sampled-piano note)))
                  notes))))

(defn play-piece [metro beat piece]
  (dorun
   (map-indexed (fn [i s] (dorun (map #(play-beat metro (+ beat i) %) s)))
                piece)))

(def piece
  (let* [c1 (split-at 5 (chord-n 6 (chord :c4 :minor)))
         f (conj (chord-n 4 (chord :f4 :minor)) (first (second c1)))
         b (chord-n 3 (chord :bb4 :major))
         e (conj (chord-n 4 (chord :eb4 :major)) (note :f5))
         a (chord-n 3 (chord :ab4 :major))
         d (conj (chord-n 4 (chord :d4 :dim)) (note :eb5))
         g (chord-n 3 (chord :g4 :major))
         c (conj (chord-n 4 (chord :c4 :minor)) (note :d5))]
        [[(first c1)]
         [(second c1) f]
         [(map note [:ab5 :g5 :f5 :eb5])]
         [(map note [:f5]) (cons (note :f5) b)]
         [[(note :f5)] e]
         [(map note [:g5 :f5 :eb5 :d5])]
         [(map note [:eb5]) (cons (note :eb5) a)]
         [[(note :eb5)] d]
         [(map note [:f5 :eb5 :d5 :c5])]
         [(map note [:d5]) (cons (note :d5) g)]
         [[(note :d5)] c]
         [(map note [:eb5 :d5 :c5 :b4])]
         [[(note :c5)] (chord-n 6 (invert-chord (chord :c4 :minor) 1))]]))

piece
(def metro (metronome 32))
(play-piece metro (metro) piece)
