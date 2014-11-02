(ns metasolfeggio.satie
  (:use [overtone.live]
        [overtone.inst sampled-piano]))

(defn player
  [t speed notes]
  (when notes
    (let [n     (first (first notes))
          s     (second (first notes))
          notes (next notes)
          t-next (+ t (/ (* 4 speed) s))]
      (when n
        (at t
          (sampled-piano (note n))))
      (apply-at t-next #'player [t-next speed notes]))))

(def shakers-melody
  [[nil 4]         [nil 4]         [:G4 4]         [:G4 4]
   [:C5 4]         [:C5 8] [:D5 8] [:E5 8] [:C5 8] [:E5 8] [:F5 8]
   [:G5 4]         [:G5 8] [:G5 8] [:E5 4]         [:D5 8] [:C5 8]
   [:D5 4]         [:D5 4]         [:D5 4]         [:D5 4]
   [:D5 8] [:E5 8] [:D5 8] [:B4 8] [:G4 4]         [:G4 4]
   [:C5 8] [:B4 8] [:C5 8] [:D5 8] [:E5 4]         [:D5 8] [:D5 8]
   [:E5 4]         [:F5 4]         [:G5 3]                 [:G5 8]
   [:D5 4]         [:D5 8] [:E5 8] [:D5 4]         [:C5 8] [:C5 8]
   [:D5 4]         [:C5 8] [:B4 8] [:C5 2]
   [:G5 2]                         [:E5 3]                 [:D5 8]
   [:E5 8] [:F5 8] [:E5 8] [:D5 8] [:C5 3]                 [:D5 8]
   [:E5 4]         [:E5 8] [:F5 8] [:G5 4]         [:E5 4]
   [:D5 4]         [:D5 8] [:E5 8] [:D5 3]                 [:G4 8]
   [:C5 2]                         [:C5 3]                 [:D5 8]
   [:E5 4]         [:E5 8] [:F5 8] [:G5 4]         [:G5 8] [:G5 8]
   [:D5 4]         [:D5 4]         [:E5 4]         [:E5 8] [:D5 8]
   [:C5 4]         [:C5 4]         [:C5 2]
])

(comment
  (player (now) 380 shakers-melody)
  (stop))
