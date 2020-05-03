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

Vizit https://localhost:8443/webtools/control/scheduleServiceSync and