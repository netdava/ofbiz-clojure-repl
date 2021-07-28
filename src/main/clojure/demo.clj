;;
;; Code to run in OFBiz clojure repl
;; See https://github.com/matlux/jvm-breakglass
;; For OfBiz service API see https://cwiki.apache.org/confluence/display/OFBIZ/Service+Engine+Guide
;;

;; Usage with Calva:
;; Calva Connect to generic repl port 7888
;; Load current file: ctrl+alt+c  Enter
;; Run forms: move carret on line `(.getAllServis ...` and ctrl+enter

(ns demo
  (:import [org.apache.ofbiz.clojure ClojureREPLService]
           [org.apache.ofbiz.service DispatchContext]))

(def ^DispatchContext ofbiz (.get ClojureREPLService/CONTEXT "ofbiz-dispatch-context"))

(.getAllServiceNames ofbiz)

(comment
  ; We can print one service per line. It's a shame we don't have namespaces for service names
  (doseq [service (.getAllServiceNames ofbiz)]
    (println service))

  0)