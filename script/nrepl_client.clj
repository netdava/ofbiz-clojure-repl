(ns nrepl-client
  (:require [bencode.core :as b]
            [babashka.fs :as fs]))

(def queue (atom ()))

(defn push-msg! [msg]
  (swap! queue conj msg))

(defn reset-queue! []
  (reset! queue ()))

(defn value-filter [f]
  (some? (:value f)))

(defn filter-values [msgs]
  (first (filter value-filter msgs)))

(defn read-reply [x]
  (cond (map? x) (zipmap (map keyword (keys x)) (map read-reply (vals x)))
        (bytes? x) (String. x "UTF-8")
        (vector? x) (mapv read-reply x)
        (seq? x) (map read-reply x)
        :else x))

(defn read-until-done [in]
  (loop []
    (let [msg (read-reply (b/read-bencode in))]
      (push-msg! msg)
      (if-let [status (:status msg)]
        (when-not (contains? (set status) "done")
          (recur))
        (recur)))))

(defn nrepl-eval
  "Connect to remote repl and evaluate expression."
  [host port expr]
  (let [s (java.net.Socket. host port)
        out (.getOutputStream s)
        in (java.io.PushbackInputStream. (.getInputStream s))
        _ (b/write-bencode out {"op" "eval" "code" expr "id" "123"})]
    (read-until-done in)))


(defn file->str [path]
  (slurp (fs/file (fs/absolutize path))))

(defn ofbiz-run
  [host port expr]
  (let [ofbiz-ops (file->str "script/ofbiz_ops.clj")]
    (nrepl-eval host port ofbiz-ops)
    (reset-queue!)
    (nrepl-eval host port expr)))