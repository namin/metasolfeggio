(use 'overtone.live)

(def server (osc-server 44100 "osc-clj"))

; (osc-listen server (fn [msg] (println msg)) :debug)

;; {:src-port 49048, :src-host android-292479353f4f4ab.home, :path /1/fader1, :type-tag f, :args (0.0)}

; (osc-rm-listener server :debug)

; (osc-handle server "/1/fader1" (fn [msg] (println msg)))

(definst foo [freq 440] (sin-osc freq))

(defn control-foo
 [val]
 (let [val (scale-range val 0 1 50 1000)]
   (ctl foo :freq val)))

; (osc-handle server "/1/fader1" (fn [msg] (control-foo (first (:args msg)))))

; (foo)
