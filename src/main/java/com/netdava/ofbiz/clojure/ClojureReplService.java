package com.netdava.ofbiz.clojure;

import java.util.HashMap;
import java.util.Map;
import net.matlux.NreplServer;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.service.DispatchContext;

/**
 * Clojure repl service.
 */
public class ClojureReplService {

  public static final String MODULE = ClojureReplService.class.getName();

  private static final NreplServer NREPL_SERVER = new NreplServer(NreplServer.DEFAULT_PORT, false,
      true,
      false, true);


  public synchronized static Map<String, ? extends Object> startRepl(DispatchContext ctx,
      Map<String, ? extends Object> context) {

    if (NREPL_SERVER.isStarted()) {
      Debug.logWarning("nRepl is already running on port " + NREPL_SERVER.getPort(), MODULE);
    } else {
      Debug.logInfo("Starting nRrepl on port " + NREPL_SERVER.getPort(), MODULE);
      Map<String, Object> replContext = new HashMap<>();
      replContext.put("ofbiz-dispatch", ctx);

      NREPL_SERVER.setObjMap(replContext);
      NREPL_SERVER.start();
      Debug.logInfo("Started nRrepl", MODULE);
    }

    return context;
  }

  public synchronized static Map<String, ? extends Object> stopRepl(DispatchContext ctx,
      Map<String, ? extends Object> context) {

    if (NREPL_SERVER.isStarted()) {
      Debug.logInfo("Stopping nRrepl on port " + NREPL_SERVER.getPort(), MODULE);
      NREPL_SERVER.stop();
      Debug.logInfo("Stoppedn Rrepl", MODULE);
    } else {
      Debug.logInfo("nRepl is not running", MODULE);
    }
    return context;
  }

}
