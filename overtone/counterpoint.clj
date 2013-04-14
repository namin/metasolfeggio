(ns metasolfeggio.counterpoint
  (:refer-clojure :exclude [==])
  (:use leipzig.melody
        leipzig.scale
        leipzig.canon
        leipzig.live)
  (:use [clojure.core.logic :exclude [is] :as l])
  (:require [clojure.core.logic.fd :as fd])
  (:require [overtone.live :as overtone]
            [overtone.synth.stringed :as strings]))


;;; Generic Overtone/Leipzig setup
(strings/gen-stringed-synth ektara 1 true)

(defn pick [distort amp {midi :pitch, start :time, length :duration}]
    (let [synth-id (overtone/at start
                     (ektara midi :distort distort :amp amp :gate 1))]
      (overtone/at (+ start length) (overtone/ctl synth-id :gate 0))))

(defmethod play-note :leader [note]
  (pick 0.7 1.0 note))
(defmethod play-note :follower [note]
  (pick 0.3 0.5 note))

(defn play-piece [speed piece]
  (->> piece
       (where :time speed)
       (where :duration speed)
       play))

;;; CLP(FD) for (approximately) Fuxian First Species Counterpoint
(defn noteo-of [key]
  (let [d (apply fd/domain (map key (range -10 10)))]
    (fn [x]
      (fd/in x d))))

(defn check-intervalo-in [ds]
  (let [id (apply fd/domain (concat ds (map #(+ % 12) ds)))]
    (fn [x y]
      (fresh [i]
             (fd/in i id)
             (fd/- x y i)))))

(def perfect-consonanto
  (check-intervalo-in [0 5 7]))

(def consonanto
  (check-intervalo-in [0 3 4 5 7 8 9]))

(def imperfect-consonanto
  (check-intervalo-in [3 4 8 9]))

(defn counterpointitero [key prev melody out]
  (fresh [a d ao dout previ i]
         (conso a d melody)
         (conso ao dout out)
         ((noteo-of key) ao)
         (fd/< ao a)
         (fd/in i (fd/interval 0 10))
         (fd/+ prev 5 previ)
         (fd/- previ ao i)
         (conde
          [(== d ())
           (== dout ())
           (perfect-consonanto a ao)]
          [(imperfect-consonanto a ao)
           (counterpointitero key ao d dout)])))

(defn counterpointo [key melody out]
  (fresh [a d ao dout last]
         (conso a d melody)
         (conso ao dout out)
         ((noteo-of key) ao)
         (fd/< ao a)
         (perfect-consonanto a ao)
         (counterpointitero key ao d dout)))


;;; -- Examples --
(defn create-counterpoint [durs melody counterpoint]
  (->> (phrase durs counterpoint)
       (where :part (is :follower))
       (then (->> (phrase durs melody)
                  (where :part (is :leader))
                  (with (->> (phrase durs counterpoint)
                             (where :part (is :follower))))))))


(defn counterpoint-of [key melody durs]
  (first (run 1 [q] (counterpointo key (map key melody) q))))

(defn counterpoints-of [key melody durs]
  (let [amelody (map key melody)
        cs (run* [q] (counterpointo key amelody q))]
    (letfn [(rec [cs]
              (if (empty? cs)
                ()
                (then (rec (rest cs)) (create-counterpoint durs amelody (first cs)))))]
      (rec cs))))

(def ex-key (comp C major))

(comment
  (play-piece (bpm 120) (counterpoints-of ex-key [0 1 2 3 4 3 2 1 0] (repeat 1)))
  (play-piece (bpm 120) (counterpoints-of ex-key [-3  -3   0   0   1   2   0] [ 1   1   1 1/2 1/2 1/2 1/2]))
  (play-piece (bpm 120) (counterpoints-of ex-key [  4   2   1   2   3   2   1   0] [  2 3/2 1/2 1/2 1/2 1/2 1/2 3/2]))
)

(defn counterphrase [durs melody]
  (->> (phrase durs (map ex-key melody))
       (where :part (is :leader))
       (with (->> (phrase durs (counterpoint-of ex-key melody durs))
                  (where :part (is :follower))))))

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
  (play-piece (bpm 120) shakers-song))
