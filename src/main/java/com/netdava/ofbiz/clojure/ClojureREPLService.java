package com.netdava.ofbiz.clojure;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import clojure.lang.Keyword;
import java.net.ServerSocket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
  // nrepl server implements java.util.Map (clojure defrecord)
  private static Optional<Map<Object, Object>> server = Optional.empty();

  public static boolean isStarted() {
    return server.isPresent();
  }

  public static Map<String, ? extends Object> statusRepl(DispatchContext ctx,
      Map<String, Object> context) {
    context.put("status", getServerStats());
    return context;
  }

  @SuppressWarnings("unchecked")
  public synchronized static Map<String, ? extends Object> startRepl(DispatchContext ctx,
      Map<String, ? extends Object> context) {

    if (isStarted()) {
      Debug.logWarning("nRepl is already running on port " + port, MODULE);
    } else {
      Debug.logInfo("Starting nRrepl on port " + port, MODULE);
      IFn require = Clojure.var("clojure.core", "require");
      require.invoke(Clojure.read("nrepl.server"));
      IFn start = Clojure.var("nrepl.server", "start-server");

      CONTEXT.put("ofbiz-dispatch-context", ctx);
      Map s = (Map) start
          .invoke(Clojure.read(":bind"), Clojure.read("\"localhost\""), Clojure.read(":port"),
              Clojure.read(port));
      server = Optional.of(s);
      // https://nrepl.org/nrepl/0.8/building_servers.html#basics
      Debug.logInfo(
          "nREPL server started on port " + port + " on host 127.0.0.1 - nrepl://127.0.0.1:" + port,
          MODULE);
    }

    return context;
  }

  public synchronized static Map<String, ? extends Object> stopRepl(DispatchContext ctx,
      Map<String, ? extends Object> context) {
    if (isStarted()) {
      Debug.logInfo("Stopping nRrepl on port " + port, MODULE);
      IFn stop = Clojure.var("nrepl.server", "stop-server");
      Object s = server.get();
      stop.invoke(s);
      server = Optional.empty();
      Debug.logInfo("Stoppedn Rrepl " + port, MODULE);
    } else {
      Debug.logInfo("nRepl is not running", MODULE);
    }
    return context;
  }

  static Map<String, Object> getServerStats() {
    if (!server.isPresent()) {
      return Collections.emptyMap();
    }
    ServerSocket ss = (ServerSocket) server.get().get(Keyword.find("server-socket"));

    Map<String, Object> stats = new HashMap<>();
    stats.put("bound", ss.isBound());
    stats.put("closed", ss.isClosed());
    stats.put("address", ss.getInetAddress().toString());
    stats.put("port", ss.getLocalPort());

    return stats;
  }

}
