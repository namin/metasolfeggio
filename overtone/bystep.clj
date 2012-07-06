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


(defn map-note [f piece]
  (map (fn [line] (map (fn [notes] (map f notes)) line)) piece))

(defn df [x]
  (if (number? x)
    [x 0 :x]
    (if (nil? (rest (rest x)))
      [(first x) (second x) :x]
      x)))

(defn dfr [x]
  (let [[n d _] (df x)] [n d :r]))

(defn adj [delta]
  (fn [x]
    (let [[n d] (df x)]
      (let [nn (+ n delta)]
        (if (< nn 1)
          [(+ 7 nn) (- d 1)]
          [nn d])))))

(def t
  [[[6 5 4 3]]
   [[4] [4 [7 -1] 2 4]]
   [[4] [4 [3 -1] [5 -1] [7 -1] 3]]])

(defn p [t]
  (concat
   [[[[1 -1] [3 -1] [5 -1] 1 3]]]
   [[[5] [5 [4 -1] [6 -1] 1 4]]]
   t
   (map-note (adj -1) t)
   (map-note (comp dfr (adj -2)) t)
   (map-note (comp dfr (adj -3)) t)
   [[[1] [[1 -1] [3 -1] [5 -1] 1]]]))

(defn f [p root]
  (let [s (vec (scale root :minor))]
    (map-note (fn [x]
                (let [[n d kw] (df x)]
                  (let [r (+ (s (dec n)) (* 12 d))]
                    (if (and (= n 7) (= kw :r))
                      (+ r 1)
                      r))))
              p)))

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

(defn change [x]
  (if (<= (note :c5) x (note :b5))
    x
    (if (< x (note :c5))
      (recur (+ x 12))
      (recur (- x 12)))))

(def metro (metronome 32))
;(play-piece metro (metro) (map-note change piece))
;(play-piece metro (metro) (f (p t) :c5))
;(play-piece metro (metro) piece)
;(stop)
