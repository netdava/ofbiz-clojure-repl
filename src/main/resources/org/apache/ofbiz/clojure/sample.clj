(ns org.apache.ofbiz.clojure.sample
  (:gen-class)
  (:import org.apache.ofbiz.service.DispatchContext))

(defn clojure-test-service [^DispatchContext dctx ^java.util.Map ctx]
  (println "test " dctx " " ctx)
  {"status" {"hello" "nurse"}})
