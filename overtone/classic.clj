(ns metasolfeggio.classic
  (:use metasolfeggio.chords)
  (:refer-clojure :exclude [==])
  (:require [clojure.core.logic.fd :as fd])
  (:use [clojure.core.logic :exclude [is all log run] :as l]))

(def perfect-consonant [0 5 7])
(def consonant [0 3 4 5 7 8 9])
(def imperfect-consonant [3 4 8 9])

(def my-harmony
  [[1 [3 6 2 4 5]]
   [2 [5 7]]
   [3 [6]]
   [4 [5 7]]
   [5 [1]]
   [6 [2 4]]
   [7 [1]]])

(defn interval-ino [ds note harmony]
  (fresh [d dr]
    (conso d dr ds)
    (fd/in d (fd/interval -10 10))
    (conde
     [(fd/- note harmony d)]
     [(fresh [o]
        (fd/in o (fd/interval -10 10))
        (fd/- note harmony o)
        (fd/!= d o)
        (interval-ino dr note harmony))])))

(defn ino [xs x]
  (fresh [y ys]
    (fd/in y (fd/interval -10 10))
    (conso y ys xs)
    (conde
     [(fd/== x y)]
     [(fd/!= x y)
      (ino ys x)])))

(defn nexto [harmony prev-harmony cur-harmony]
  (fresh [p hs cs]
    (fd/in p (fd/interval 1 8))
    (conso [p cs] hs harmony)
    (conde
     [(fd/== p prev-harmony)
      (ino cs cur-harmony)]
     [(fd/!= p prev-harmony)
      (nexto hs prev-harmony cur-harmony)])))

(defn zico [measure phrase position prev-note cur-note prev-harmony cur-harmony]
  (fresh []
    (nexto my-harmony prev-harmony cur-harmony)
    (fresh [i]
      (fd/in i (fd/interval 0 10))
      (conde
       [(fd/* measure i position)
        (fd/== cur-harmony 1)
        (interval-ino perfect-consonant cur-note cur-harmony)]
       [(fresh [p]
          (fd/in i (fd/interval 0 30))
          (fd/* measure i p)
          (fd/!= p position)
          (interval-ino imperfect-consonant cur-note cur-harmony))]))))

(defn musico [measure phrase position prev-note prev-harmony m]
  (conde
   [(fd/* measure phrase position)
    (== m [])]
   [(fresh [mp]
      (fd/in mp (fd/interval 0 30))
      (fd/* measure phrase mp)
      (fd/< position mp)
      (fresh [position+1 cur-note cur-harmony rest-m]
        (fd/in position+1 (fd/interval 0 30))
        (fd/in cur-note (fd/interval -10 10))
        (fd/in cur-harmony (fd/interval 1 8))
        (conso [cur-note cur-harmony] rest-m m)
        (fd/+ 1 position position+1)
        (zico measure phrase position prev-note cur-note prev-harmony cur-harmony)
        (musico measure phrase position+1 cur-note cur-harmony rest-m)))]))

(l/run 1 [m]
  (musico 1 1 0 5 5 m))

(l/run 1 [m]
  (musico 5 1 0 5 5 m))

(def p
  (first
   (l/run 1 [m]
     (musico 4 2 0 5 5 m))))

(play-progression metro (metro) (progression :C4 :major (map second p)))
