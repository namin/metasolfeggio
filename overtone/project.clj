;; add to ~/.lein/profiles.clj the following line
;; {:user {:plugins [[cider/cider-nrepl "0.8.1"]]}}}}

(defproject metasolfeggio "0.1-SNAPSHOT"
  :description "Computer-Aided Harmony and Counterpoint"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/core.logic "0.8.10"]
                 [org.clojure/core.match "0.3.0-alpha4"]
                 [loco "0.3.0"]
                 [overtone "0.10-SNAPSHOT"]
                 [leipzig "0.8.1" :exclusions [org.clojure/clojure]] ;; ;; issues with nothing playing in 0.9.0
                 ])
