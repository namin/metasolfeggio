(ns metasolfeggio.satie
  (:use [clojure.core.match :only [match]]
        [overtone.live]
        [overtone.inst sampled-piano]))

;;Erik Satie Gnossienne No. 1
(def phrase1a [:iii :v :iv# :iii :iii :ii# :iii :ii#])
(def phrase1b [:iii :v :iv# :iii :v# :vi :v# :vi])
(def phrase1c [:iii :v :iv# :iii :iii :ii# :i :vii- :vi- :vii- :vi- :vii- :i :vii- :vii- :vi-])

(def phrase2 [:i :ii :i :vii- :i :ii :i :vii- :i :vii- :vii- :vi-])

(def phrase3 [:iii :iv# :v# :vi :vii :ii#+ :vii :vi :vii :vi :vii :vi :vi :v# :iv :iii :iii :ii# :i :vii- :vii- :vi-])

(def phrase1a-reprise [:iii :v :iv# :iii :iii :ii#])
(def phrase1b-reprise [:iii :v :iv# :iii :v# :vi])

(def phrase1-bass [:vi--- [:vi- :iii- :i-] [:vi- :iii- :i-]])
(def phrase2-bass [:iii-- [:iii- :vii-- :v--] [:iii- :vii-- :v--]])

(def phrase3-bass [:ii--- [:vi-- :ii- :iv-] [:vi-- :ii- :iv-]])


(def right-hand-degrees (concat phrase1a phrase1b phrase1c
                                phrase1a phrase1b phrase1c
                                phrase2
                                phrase2
                                phrase3
                                phrase3
                                phrase2
                                phrase2
                                phrase1a-reprise
                                phrase1b-reprise
                                phrase1a-reprise
                                phrase1b-reprise
                                phrase2
                                phrase2
                                phrase3
                                phrase3
                                phrase2
                                phrase2))


(def left-hand-degrees (concat (apply concat (repeat 6 phrase1-bass))  ;;A
                               phrase2-bass                            ;;B
                               (apply concat (repeat 8 phrase1-bass))  ;;C
                               phrase2-bass                            ;;D
                               (apply concat (repeat 2 phrase1-bass))  ;;E
                               (apply concat (repeat 2 phrase3-bass))  ;;F
                               (apply concat (repeat 2 phrase1-bass))  ;;G
                               (apply concat (repeat 2 phrase3-bass))  ;;H
                               (apply concat (repeat 14 phrase1-bass)) ;;I
                               (apply concat (repeat 2 phrase3-bass))  ;;J
                               (apply concat (repeat 2 phrase1-bass))  ;;K
                               (apply concat (repeat 2 phrase3-bass))  ;;L
                               (apply concat (repeat 10 phrase1-bass)) ;;M
                               (apply concat (repeat 2 phrase3-bass))  ;;N
                               (apply concat (repeat 2 phrase1-bass))  ;;O
                               (apply concat (repeat 2 phrase3-bass))  ;;P
                               (apply concat (repeat 14 phrase1-bass)) ;;Q
                               (apply concat (repeat 2 phrase3-bass))  ;;R
                               (apply concat (repeat 2 phrase1-bass))  ;;S
                               (apply concat (repeat 2 phrase3-bass))  ;;T
                               phrase1-bass                            ;;U
                               ))

(def lh-pitches (degrees->pitches left-hand-degrees :major :Ab4))
(def rh-pitches (degrees->pitches right-hand-degrees :major :Ab4))

(defn beats [notes]
  (reductions + 0 (map (fn [x] 1) notes)))

(defn play [metro]
  (let [play-note (fn [beat rh lh]
                    (at (metro beat)
                      (sampled-piano rh)
                      (if (sequential? lh)
                        (doseq [p lh] (sampled-piano p))
                        (sampled-piano lh))))]
    (dorun (map play-note (beats rh-pitches) rh-pitches lh-pitches))))

(comment
  (play (metronome 128))
  (stop))
