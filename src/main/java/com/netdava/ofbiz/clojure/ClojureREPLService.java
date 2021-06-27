package com.netdava.ofbiz.clojure;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.service.DispatchContext;

/**
 * Clojure repl service.
 */
public class ClojureREPLService {

  public static final String MODULE = ClojureREPLService.class.getName();
  public static final ConcurrentHashMap<String, Object> CONTEXT = new ConcurrentHashMap<>();
  public static String port = "7888";
  private static final boolean isStarted = false;
  private static Object server = null;

  public synchronized static Map<String, ? extends Object> startRepl(DispatchContext ctx,
      Map<String, ? extends Object> context) {

    if (isStarted) {
      Debug.logWarning("nRepl is already running on port " + port, MODULE);
    } else {
      Debug.logInfo("Starting nRrepl on port " + port, MODULE);
      IFn require = Clojure.var("clojure.core", "require");
      require.invoke(Clojure.read("nrepl.server"));
      IFn start = Clojure.var("nrepl.server", "start-server");

      CONTEXT.put("ofbiz-dispatch-context", ctx);
      server = start.invoke(Clojure.read(":bind"), Clojure.read("\"localhost\""), Clojure.read(":port"),
          Clojure.read(port));
      // https://nrepl.org/nrepl/0.8/building_servers.html#basics
      Debug.logInfo("nREPL server started on port " + port + " on host 127.0.0.1 - nrepl://127.0.0.1:" + port, MODULE);
    }

    return context;
  }

  public synchronized static Map<String, ? extends Object> stopRepl(DispatchContext ctx,
      Map<String, ? extends Object> context) {

    if (isStarted) {
      Debug.logInfo("Stopping nRrepl on port " + port, MODULE);
      assert server != null;
      IFn stop = Clojure.var("nrepl.server", "stop-server");
      stop.invoke(server);
      Debug.logInfo("Stoppedn Rrepl " + port, MODULE);
    } else {
      Debug.logInfo("nRepl is not running", MODULE);
    }
    return context;
  }

}
