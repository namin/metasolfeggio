(ns metasolfeggio.harmonizer
  (:refer-clojure :exclude [==])
  (:use [overtone.live]
        [overtone.inst sampled-piano])
  (:use [clojure.core.logic :exclude [is all log run] :as l]))

(def harmony-degrees
  (vec (map #(map dec %)
            [[3 6 2 4 5]
             [5 7]
             [6]
             [5 7]
             [1]
             [2 4]
             [1]])))

(defn degree-chord [d]
  (map #(mod (+ d %) 7)
       (let [ds [0 2 4]]
         (if (contains? #{5 7} (inc d))
           (conj ds 6)
           ds))))

(defn degree-contains? [a b]
  (boolean (some #{b} (degree-chord a))))

(defn harmony-tab [ds]
  (vec (map (fn [d] (vec (filter #(degree-contains? % d) ds)))
            (range 0 87))))

(def harmony
  (vec (map harmony-tab harmony-degrees)))

(defn harmonize-rec [notes d]
  (if (empty? notes)
    '()
    (let [ds (if (= d -1) [(first notes)] (nth (nth harmony d) (first notes)))
          next-d (if (empty? ds) -1 (choose ds))]
      (cons next-d
            (harmonize-rec (rest notes) (if (= -1 next-d) d next-d))))))

(defn harmonize [notes]
  (harmonize-rec notes -1))

(defmacro picko [od om oo]
  `(conde
    ~@(apply concat
             (for [d (range 0 7) m (range 0 7)]
               (map (fn [o] `[(== ~d ~od) (== ~m ~om) (== ~o ~oo)])
                    (nth (nth harmony d) m))))))

(defn harmonize-rec-o [notes d out]
  (conde
   [(== notes '())
    (== out '())]
   [(fresh [na nd oa od]
      (conso na nd notes)
      (conso oa od out)
      (conde
       [(== -1 d) (== oa na)]
       [(picko d na oa)])
      (harmonize-rec-o nd oa od))]))

(defn harmonizeo [notes out]
  (harmonize-rec-o notes -1 out))

(comment
  (l/run 10 [q]
    (harmonizeo '(0 1 2 0 0 1 2 3 0 2 3 4 0 2 3 4) q))
  )
