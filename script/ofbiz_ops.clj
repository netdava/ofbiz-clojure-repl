(ns ofbiz-ops
  (:require [clojure.string :as str])
  (:import [com.netdava.ofbiz.clojure ClojureREPLService]
           [org.apache.ofbiz.service DispatchContext]))

(def ^DispatchContext dispatch-ctx (.get ClojureREPLService/CONTEXT "ofbiz-dispatch-context"))

(defn list-services
  "List ofbiz services.
   Can be supplied with a filter function to filter services.

   Example:
   ;; filter services that contain 'repl' in their name
   (ofbiz-ops/list-services (fn [s] (clojure.string/includes? (.toLowerCase s) \"repl\")))"
  ([]
   (.getAllServiceNames dispatch-ctx))
  ([filter-fn]
   (filter filter-fn (list-services))))

(defn get-model-service
  "Gets the ModelService instance that corresponds to given the name.

   Example:
   (ofbiz-ops/get-model-service \"startRepl\" )"

  ([service]
   (-> dispatch-ctx
       (.getModelService service))))

(defn run-sync
  "Calls OFBiz service in sync mode."
  [service ctx]
  (-> dispatch-ctx
      (.getDispatcher)
      (.runSync service ctx)))

(defn run-sync-ignore
  "Calls OFBiz service in sync mode.
   Ignores result value."
  [service ctx]
  (-> dispatch-ctx
      (.getDispatcher)
      (.runSyncIgnore service ctx)))

(defn run-async
  "Calls OFBiz service in async mode."
  [service ctx]
  (-> dispatch-ctx
      (.getDispatcher)
      (.runASync service ctx)))

