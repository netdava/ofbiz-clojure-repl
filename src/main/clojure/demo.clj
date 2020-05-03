;; 
;; Code to run in OfBiz clojure repl
;; See https://github.com/matlux/jvm-breakglass
;; For OfBiz service API see https://cwiki.apache.org/confluence/display/OFBIZ/Service+Engine+Guide 
;; 

(ns com.netdava.ofbiz.clojure.demo
  (:require [cl-java-introspector.core :as introspector]
            [me.raynes.fs :as raynes]))

; See what objects we have in the nrepl context
(introspector/get-objs)

; Bind the ofbiz-dispatch context to ofbiz global
(def ofbiz (introspector/get-obj "ofbiz-dispatch"))

; list all the methods of the ofbiz instance
(introspector/methods-info ofbiz)

(.getAllServiceNames ofbiz)


(comment
  
  (doseq [service (.getAllServiceNames ofbiz)]
    (println service))
  
  )