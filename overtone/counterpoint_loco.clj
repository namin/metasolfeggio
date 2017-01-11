(ns metasolfeggio.counterpoint_loco
  (:use leipzig.melody
        leipzig.scale
        leipzig.canon
        leipzig.live)
  (:use [loco.core]
        [loco.constraints])
  (:require [overtone.live :as overtone]
            [overtone.inst.sampled-piano :as piano]))

;;; Generic Overtone/Leipzig setup
(defn pick [a b {midi :pitch}]
  (piano/sampled-piano midi))

(defmethod play-note :leader [note]
  (pick 0.7 1.0 note))
(defmethod play-note :follower [note]
  (pick 0.3 0.5 note))

(defn play-piece [speed piece]
  (->> piece
       (where :time speed)
       (where :duration speed)
       play))

;;; helpers for music interval constraints
(defn check-interval-in [ds]
  (let [id (concat ds (map #(+ % 12) ds))]
    (fn [x y]
      (apply $or (map #($= ($- x y) %) id)))))

(def $perfect-consonant
  (check-interval-in [0 5 7]))

(def $consonant
  (check-interval-in [0 3 4 5 7 8 9]))

(def $imperfect-consonant
  (check-interval-in [3 4 8 9]))

;;; (approximately) Fuxian First Species Counterpoint
(defn counterpoint-model [key melody]
  (let [m (vec melody)
        n (count m)
        l (dec n)
        d (map key (range -10 10))]
    (concat
     (for [i (range n)]
       ($in [:x i] d))
     (for [i (range n)]
       ($< [:x i] (m i)))
     [($perfect-consonant (m 0) [:x 0])
      ($perfect-consonant (m l) [:x l])]
     (for [i (range 1 l)]
       ($imperfect-consonant (m i) [:x i])))))
(defn counterpoint-restrict [n]
  (for [i (range 0 (dec n))]
    ($< ($abs ($- [:x i] [:x (inc i)])) 3)))
(defn counterpoint-minimize [n]
  (apply $+
         (for [i (range 0 (dec n))]
           ($abs ($- [:x i] [:x (inc i)])))))


(defn solution->melody [sol]
  (for [i (range (count sol))]
    (sol [:x i])))

(comment
  (solution->melody (solution (counterpoint-model ex-key (map ex-key [0 1 2 3 4 3 2 1 0]))))
  (map solution->melody (solutions (counterpoint-model ex-key (map ex-key [0 1 2 3 4 3 2 1 0]))))
)

(defn counterpoint-of [key melody durs]
  (solution->melody (solution (counterpoint-model key (map key melody))
                              :minimize (counterpoint-minimize (count melody)))))

(defn create-counterpoint [durs melody counterpoint]
  (->> (phrase durs counterpoint)
       (where :part (is :follower))
       (then (->> (phrase durs melody)
                  (where :part (is :leader))
                  (with (->> (phrase durs counterpoint)
                             (where :part (is :follower))))))))

(defn counterpoints-of [key melody durs]
  (let [amelody (map key melody)
        cs (map solution->melody (solutions (concat (counterpoint-model key amelody)
                                                    (counterpoint-restrict (count amelody)))))]
    (letfn [(rec [cs]
              (if (empty? cs)
                ()
                (then (rec (rest cs)) (create-counterpoint durs amelody (first cs)))))]
      (rec cs))))

(def ex-key (comp C major))

(comment
  (play-piece (bpm 120) (counterpoints-of ex-key [0 1 2 3 4 3 2 1 0] (repeat 1)))
  (play-piece (bpm 120) (create-counterpoint (repeat 1) (map ex-key [0 1 2 3 4 3 2 1 0]) (counterpoint-of ex-key [0 1 2 3 4 3 2 1 0] (repeat 1))))
  (play-piece (bpm 120) (counterpoints-of ex-key [-3  -3   0   0   1   2   0] [ 1   1   1 1/2 1/2 1/2 1/2]))
  (play-piece (bpm 120) (counterpoints-of ex-key [  4   2   1   2   3   2   1   0] [  2 3/2 1/2 1/2 1/2 1/2 1/2 3/2]))
  (stop)
)

(defn counterphrase [durs melody]
  (->> (phrase durs (map ex-key melody))
       (where :part (is :leader))
       (with (->> (phrase durs (counterpoint-of ex-key melody durs))
                  (where :part (is :follower))))))

(comment
  (play-piece (bpm 120) (counterphrase (repeat 1) [0 1 2 3 4 3 2 1 0]))
  (play-piece (bpm 120) (counterphrase [ 1   1   1 1/2 1/2 1/2 1/2] [-3  -3   0   0   1   2   0]))
  (play-piece (bpm 120) (counterphrase  [  2 3/2 1/2 1/2 1/2 1/2 1/2 3/2] [  4   2   1   2   3   2   1   0]))
  (stop)
)
(def shakers-song
       ;;;  it is a gift to be simple
  (->> (counterphrase [ 1   1   1 1/2 1/2 1/2 1/2]
                      [-3  -3   0   0   1   2   0])
       ;;; it's a  gift to be free
       (then (counterphrase [1/2 1/2   1 1/2 1/2   1]
                            [  2   3   4   4   4   2]))
       ;;; it's a gift to come round where we ought to be
       (then (counterphrase [1/2 1/2   1   1   1   1 1/2 1/2 1/2 1/2   1]
                            [  1   0   1   1   1   1   1   2   1  -1  -3]))
       ;;; and when we find ourselves in the place just right
       (then (counterphrase [  1 1/2 1/2 1/2 1/2   1 1/2 1/2   1   1]
                            [ -3   0  -1   0   1   2   1   1   2   3]))
       ;;; it will be in the valley of love and delight
       (then (counterphrase [3/2 1/2   1 1/2 1/2   1 1/2 1/2   1 1/2 1/2  2]
                            [  4   4   1   1   2   1   0   0   1   0  -1  0]))
       ;;; when true simplicity is gained
       (then (counterphrase [  2 3/2 1/2 1/2 1/2 1/2 1/2 3/2]
                            [  4   2   1   2   3   2   1   0]))
       ;;; to bow and to bend we shan't be ashamed
       (then (counterphrase [1/2   1 1/2 1/2   1   1   1 1/2 1/2 3/2]
                            [  1   2   2   3   4   2   1   1   2   1]))
       ;;; to turn, turn will be our delight
       (then (counterphrase [1/2   2 3/2 1/2   1 1/2 1/2   1]
                            [ -3   0   0   1   2   2   3   4]))
       ;;; till by turning, turning we come round right
       (then (counterphrase [1/2 1/2   1   1   1 1/2 1/2   1   1   1]
                            [  4   4   1   1   2   2   1   0   0   0]))))
(comment
  (play-piece (bpm 120) shakers-song)
  (stop))
