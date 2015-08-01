(ns metasolfeggio.lsystem
  (:use leipzig.melody
        leipzig.scale
        leipzig.canon
        leipzig.live)
  (:require [overtone.live :as overtone]
            [overtone.inst.sampled-piano :as piano]))

(def examples
  [{:start "X"
    :rules {\X "F-[[X]+X]+F[+FX]-X"
            \F "FF"}
    :iter 5}
   {:start "X"
    :rules {\X "F[+X][-X]FX"
            \F "FF"}
    :iter 5}
   {:start "X"
    :rules {\X "F[+X]-X"
            \F "FF"}
    :iter 6}
   {:start "F"
    :rules {\F "F[+F]F[-F]F"}
    :iter 4}
   ])

(defn lsys1 [rules s]
  (mapcat
   (fn [c] (let [r (get rules c)]
            (if r r (str c))))
   s))

(defn lsys [rules s n]
  (if (= n 0) s (lsys rules (lsys1 rules s) (dec n))))


(def init-turtle
  {:dur 0 :note 0 :notes '() :durs '() :note-stack '() :dur-stack '()})

;; Schenkerian rendering
;; from http://www-users.cs.york.ac.uk/susan/bib/ss/nonstd/eurogp05.pdf
(defn exec-cmd [t c]
  (case c
    \F (assoc t :dur (inc (:dur t)))
    \+ (assoc t :note (inc (:note t)))
    \- (assoc t :note (dec (:note t)))
    \[ (assoc t :dur 0
              :note-stack (cons (:note t) (:note-stack t))
              :dur-stack (cons (:dur t) (:dur-stack t)))
    \] (assoc t
              :notes (if (> (:dur t) 0) (cons (:note t) (:notes t)) (:notes t))
              :durs (if (> (:dur t) 0) (cons (:dur t) (:durs t)) (:durs t))
              :note (first (:note-stack t))
              :dur (first (:dur-stack t))
              :note-stack (rest (:note-stack t))
              :dur-stack (rest (:dur-stack t)))
    t))

(defn turtle [s]
  (reduce exec-cmd init-turtle s))

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

;;; Putting it all together
(defn run-ex [ex]
  (let [t (turtle (lsys (:rules ex) (:start ex) (:iter ex)))]
    (play-piece (bpm 300)
                (->> (phrase (:durs t) (:notes t))
                     (where :part (is :leader))
                     (where :pitch (comp C major))))))

(comment
  (run-ex (examples 0))
  (run-ex (examples 1))
  (run-ex (examples 2))
  (run-ex (examples 3))
  (stop))
