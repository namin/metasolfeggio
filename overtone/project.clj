;; add to ~/.lein/profiles.clj the following line
;; {:user {:plugins [[cider/cider-nrepl "0.8.1"]]}}}}

(defproject metasolfeggio "0.1-SNAPSHOT"
  :description "Computer-Aided Harmony and Counterpoint"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/core.logic "0.8.8"]
                 [org.clojure/core.match "0.2.2"]
                 [overtone "0.9.1"]
                 [leipzig "0.7.0" :exclusions [org.clojure/clojure]] ;; not updated to 0.8.0
                 ])
