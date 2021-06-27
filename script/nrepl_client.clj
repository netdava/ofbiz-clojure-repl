(ns nrepl-client
  (:require [bencode.core :as b]
            [babashka.fs :as fs]))

(defmacro string-it
  "Convert form to string. Useful in calling ofbiz services via repl string."
  [x]
  (str x))

(def queue (atom ()))

(defn push-msg! [msg]
  (swap! queue conj msg))

(defn reset-queue! []
  (reset! queue ()))

(defn debug []
  (println @queue))

(defn has-id [msg id]
  (= (:id msg) id))

(defn has-value [msg]
  (not (nil? (:value msg))))

(defn response
  "Get the response by filtering messages with supplied id and status "
  [msgs id]
  (filter (fn [msg] (and (has-id msg id) (has-value msg))) msgs))

(defn first-response-value
  "Get the response by filtering messages with supplied id and status "
  [msgs id]
  (->> (response msgs id)
       first
       :value))


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
  "Connect to remote repl and evaluate expression.
   You can provide extra options for nrepl/bencode send via opts map.
   They will be merged and overwrite exiting options."
  ([host port expr]
   (nrepl-eval host port expr {}))
  ([host port expr opts]
   (let [s (java.net.Socket. host port)
         out (.getOutputStream s)
         in (java.io.PushbackInputStream. (.getInputStream s))
         e (merge {"op" "eval"
                   "code" expr
                   "id" "123"} opts)
         _ (b/write-bencode out e)]
     (read-until-done in)
     (deref queue))))

(defn file->str [path]
  (slurp (fs/file (fs/absolutize path))))

(defn ofbiz
  "Connect to OFBiz repl and eval expr code.
   `expr` is a string sent to evaluate on the server."
  [host port expr id]
  (let [ofbiz-ops (file->str "script/ofbiz_ops.clj")]
    (nrepl-eval host port ofbiz-ops)
    (nrepl-eval host port expr {"id" id})))

;; CLI options to use or get inspired to build your own.
(def cli-opts [["-p" "--port PORT" "App repl port."
                :default 7888
                :parse-fn (fn [p] (Integer/parseInt p))
                :validate [(fn [p] (< 0 p 0x10000)) "Must be a number between 0 and 65536"]]
               ["-H" "--host HOST" "App repl host."
                :default "localhost"]
               ["-h" "--help" "Print help information."]])