(ns ofbiz-clojure-repl
  (:require [nrepl.server :refer [start-server stop-server]])
  (:gen-class))

(println "Hello from clojure")

(def server (atom nil))

(defn hello []
  (println "nurse"))

(defn safe-stop-repl 
  [server]
  (when-not (nil? server)
    (stop-server server)))

(defn start-repl
  "Start nrepl server with host and port.   
   Stops the old server before starting the new one"
  ([host port]
   (println "Start server " host " " port)
   (swap! server (fn [old-server]
                   (safe-stop-repl old-server)
                   (start-server :bind host :port port)))))

(defn stop-repl []
  (swap! server (fn [old-server]
                  (safe-stop-repl old-server)
                  nil)))