package com.netdava.ofbiz.clojure;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import clojure.lang.Keyword;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.DispatchContext;

import java.net.ServerSocket;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Clojure repl service.
 */
public class ClojureREPLService {

    public static final String MODULE = ClojureREPLService.class.getName();
    public static final ConcurrentHashMap<String, Object> CONTEXT = new ConcurrentHashMap<>();

    // nrepl server implements java.util.Map (clojure defrecord)
    private static Optional<Map<Object, Object>> server = Optional.empty();

    public static boolean isStarted() {
        return server.isPresent();
    }

    /**
     * Return the runtime status of the plugin.
     *
     * @param ctx
     * @param context
     * @return
     */
    public static Map<String, ? extends Object> statusRepl(DispatchContext ctx,
                                                           Map<String, Object> context) {
        context.put("status", getServerStats());
        return context;
    }

    @SuppressWarnings("unchecked")
    public static synchronized Map<String, ? extends Object> startRepl(DispatchContext ctx,
                                                                       Map<String, ? extends Object> context) throws GenericEntityException {
        Map<String, String> configs = replConfigMapFromDb(ctx);
        String host = configs.get("clojure.repl.host");
        String port = configs.get("clojure.repl.port");

        if (isStarted()) {
            Debug.logWarning("nRepl is already running on port %s:%s", MODULE, host, port);
        } else {
            Debug.logInfo("Starting nRrepl on %s:%s", MODULE, host, port);
            IFn require = Clojure.var("clojure.core", "require");
            require.invoke(Clojure.read("nrepl.server"));
            IFn start = Clojure.var("nrepl.server", "start-server");

            CONTEXT.put("ofbiz-dispatch-context", ctx);
            Map s = (Map) start
                    .invoke(Clojure.read(":bind"), host, Clojure.read(":port"),
                            Clojure.read(port));
            server = Optional.of(s);
            // https://nrepl.org/nrepl/0.8/building_servers.html#basics
            Debug.logInfo(
                    "nREPL server started on port %s on host %s - nrepl://%s:%s",
                    MODULE, port, host, host, port);
        }

        return context;
    }

    public static Map<String, String> replConfigMapFromDb(DispatchContext ctx) throws GenericEntityException {
        Map<String, String> configs = EntityQuery.use(ctx.getDelegator()).from("SystemProperty").where(
                new HashMap<String, String>() {{
                    put("systemResourceId", "clojureRepl");
                }}).queryList()
                .stream()
                .collect(Collectors.toMap((gv) -> (String) gv.get("systemPropertyId"),
                        (gv) -> (String) gv.get("systemPropertyValue")));
        return configs;
    }

    public static synchronized Map<String, ? extends Object> stopRepl(DispatchContext ctx,
                                                                      Map<String, ? extends Object> context) throws GenericEntityException {
        if (isStarted()) {
            Map<String, String> configs = replConfigMapFromDb(ctx);
            String host = configs.get("clojure.repl.host");
            String port = configs.get("clojure.repl.port");

            Debug.logInfo("Stopping nRrepl on port %s", MODULE, port);
            IFn stop = Clojure.var("nrepl.server", "stop-server");
            Object s = server.get();
            stop.invoke(s);
            server = Optional.empty();
            Debug.logInfo("Stopped nRepl %s", MODULE, port);
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
