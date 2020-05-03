# Clojure repl component for OFBiz

Uses [jvm-breakglass](https://github.com/matlux/jvm-breakglass) library to start an instance of nRepl inside Apache OFBiz.
Once that is done, you can do all sorts of nice things there.

## How does it work?

The plugin exposes services that allow a user to start/stop an instance of nRepl inside OFBiz application.



## Using this plugin

Clone Apache OFBiz trunk project as described in the online [OFBiz Getting started](https://OFBiz.apache.org/developers.html) page.

After that you need to clone this project inside `plugins` directory of OFBiz.

Once that is done you can start the project.

```sh
    # Build the project - download all dependencies
    ./gradlew build
    # Prepare OFBiz for start - load data
    ./gradlew cleanAll loadAll
    # Run OFBiz. You can look for the plugin in the logs.
    ./gradlew OFBiz
    # Open the link in the browser and start clojure repl manually.
    xdg-open https://localhost:8443/accounting
```

Once **OFBiz** has started we need to start the repl inside so we have a port to connect to.

You can do that by using the Web Tools functionality as described on the [OFBiz Web Tools wiki page](https://cwiki.apache.org/confluence/display/OFBiz/Entity+Engine+Guide#EntityEngineGuide-EntityRelationships(relations))

Vizit https://localhost:8443/webtools/control/scheduleServiceSync and use **Run service** functionality to run `startRepl` service.
You can also run `stopRepl` service once you are done.

When starting the repl you should see something like this in the logs:

```sh
May 04, 2020 1:56:26 AM net.matlux.NreplServer <init>
INFO: Creating ReplStartup for Port=1112
May 04, 2020 1:56:27 AM net.matlux.MBeanRegistration registerNreplServerAsMBean
INFO: MBean Registration of JVM-breakglass successful
2020-05-04 01:56:27,294 |jsse-nio-8443-exec-3 |ClojureReplService            |I| Starting nRrepl on port 1112
May 04, 2020 1:56:27 AM net.matlux.NreplServer start
INFO: Repl started successfully on Port = 1112
```

## Connecting to the repl and using it

Once you have a running repl you can use it. Just open your favorite Clojure IDE and connect to the repl, then explore OFBiz.
You can use IntelliJ + Cursive or VS Code with Calva or whatever you wish.

Bellow are some simple commands to list the services inside


```clojure
;;
;; Code to run in OFBiz clojure repl
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
  ; We can print one service per line. It's a shame we don't have namespaces for service names
  (doseq [service (.getAllServiceNames ofbiz)]
    (println service))

  0)
```