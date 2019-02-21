;; add to ~/.lein/profiles.clj the following line
;; {:user {:plugins [[cider/cider-nrepl "0.21.1"]]}}}}

(defproject metasolfeggio "0.1-SNAPSHOT"
  :description "Computer-Aided Harmony and Counterpoint"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/core.logic "0.8.11"]
                 [org.clojure/core.match "0.3.0-alpha4"]
                 [loco "0.3.1"]
                 [overtone "0.10.3"]
                 ;; cannot upgrade leipzig without changing code?
                 [leipzig "0.8.1" :exclusions [org.clojure/clojure]]
                 ])
