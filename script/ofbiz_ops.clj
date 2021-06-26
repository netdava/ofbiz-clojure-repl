(ns ofbiz-ops
  (:import [com.netdava.ofbiz.clojure ClojureREPLService]
           [org.apache.ofbiz.service DispatchContext]))

(def ^DispatchContext ofbiz (.get ClojureREPLService/CONTEXT "ofbiz-dispatch-context"))

(defn list-services []
  (.getAllServiceNames ofbiz))
