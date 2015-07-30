(ns metasolfeggio.bystep
  (:use
    [overtone.live]
    [overtone.inst.sampled-piano]))

(defn noise []
  (* (rand) 0.0))

(defn chord-n [n chord]
  (let [roots (iterate #(+ 12 %) 0)]
    (take n (flatten (map (fn [root] (map #(+ root %) (sort chord))) roots)))))

(defn play-beat [metro beat notes]
  (let [n (count notes)]
    (dorun
     (map-indexed (fn [i note]
                    (at (metro (+ beat (noise) (/ i n)))
                      (when note
                        (if (number? note)
                          (sampled-piano note)
                          (apply sampled-piano note)))))
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
         [[(note :c5)] (chord-n 6 (chord :c4 :minor))]]))

(defn level [v]
  (fn [note] (if note [note v] note)))

(def piece2
  (let []
    (concat
     (take 2 (repeat [(map note [:c4 nil :eb4 nil :eb4 nil])
                      (map note [:c4 nil :g4 nil :g4 nil])
                      (map (comp (level 2) note) [:g5 :g5 nil :f5 :g5 nil])]))
     (take 2 (repeat [(map (comp (level 2) note) [:ab5 nil nil :f5 :eb5 :d5])
                      (map note [:d4 nil :f4 nil :f4])
                      (map note [:d4 nil :ab4 nil :ab4])]))
     (take 2 (repeat [(map (comp (level 2) note) [:g5 nil nil :eb5 :d5 :c5])
                      (map note [:c4 nil :eb4 nil :eb4])
                      (map note [:c4 nil :g4 nil :g4])]))
     (take 2 (repeat [(map (comp (level 2) note) [:f5 nil nil :d5 :c5 :b4])
                      (map note [:g3 nil :b3 nil :b3])
                      (map note [:g3 nil :d4 nil :d4])]))
     (take 2 (repeat [(map note [:c4 nil :eb4 :eb4 :eb4 :eb4])
                      (map note [:c4 nil :g4 :g4 :g4 :g4])
                      (map (comp (level 2) note) [:c5 nil nil nil nil nil])]))
     (apply concat (take 2 (repeat (concat
                       [[(let [s (map note [:g5 :f5 :eb5 :d5 :c5 :d5])]
                           (cons ((level 2) (first s)) (rest s)))]]
                       [[(map note [:d4 :f4 :f4 :f4 :f4])
                         (map note [:d4 :ab4 :ab4 :ab4 :ab4])]]
                       [[(let [s (map note [:f5 :eb5 :d5 :c5 :b4 :c5])]
                           (cons ((level 2) (first s)) (rest s)))]]
                       [[(map note [:c4 :eb4 :eb4 :eb4 :eb4])
                         (map note [:c4 :eb4 :g4 :g4 :g4])]])))))))

(defn change [x]
  (if (<= (note :c5) x (note :b5))
    x
    (if (< x (note :c5))
      (recur (+ x 12))
      (recur (- x 12)))))

(def s (vec (scale :c5 :minor)))
(defn circle [d vs i n]
  (if (> n 8)
    '()
    (let [v (vs (mod n (count vs)))]
      (cons (map (fn [x]
                   (if x
                     (s (mod (+ (dec i) (dec x)) 8))
                     x)) v)
            (circle d vs (+ i d) (inc n))))))

(def metro (metronome 64))
(comment
  (play-piece metro (metro) (map-note change piece))
  (play-piece metro (metro) (f (p t) :c5))
  (play-piece metro (metro) piece)
  (play-piece metro (metro) piece2)
  (play-piece metro (metro) (map (fn [x] [x]) (circle 5 [[5 3 1] [1 2 3]] 1 0)))
  (stop)
  )
