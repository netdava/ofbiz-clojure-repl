package com.netdava.ofbiz.clojure;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.ofbiz.base.container.Container;
import org.apache.ofbiz.base.container.ContainerException;
import org.apache.ofbiz.base.start.StartupCommand;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.DelegatorFactoryImpl;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceDispatcher;

/**
 * ClojureREPLContainer - container to start the clojure REPL service.
 */
public class ClojureREPLContainer implements Container {

  public static final ConcurrentHashMap<String, Object> CONTEXT = new ConcurrentHashMap<>();
  private static final String MODULE = ClojureREPLContainer.class.getName();
  private final AtomicBoolean loaded = new AtomicBoolean(false);
  private String name;

  @Override
  public void init(List<StartupCommand> ofbizCommands, String name, String configFile)
      throws ContainerException {
    if (!loaded.compareAndSet(false, true)) {
      throw new ContainerException("Container already loaded, cannot start");
    }
    this.name = name;
    Debug.logInfo("Container initialized", MODULE);
  }

  @Override
  public boolean start() throws ContainerException {
    try {
      Delegator delegator = DelegatorFactoryImpl.getDelegator("default");
      LocalDispatcher dispatcher = ServiceDispatcher.getLocalDispatcher("default", delegator);
      dispatcher.runAsync("startRepl", Collections.emptyMap());
      loaded.set(true);
    } catch (Exception e) {
      Debug.logError("Error init container", MODULE);
    }
    return loaded.get();
  }

  @Override
  public void stop() throws ContainerException {
    try {
      Delegator delegator = DelegatorFactoryImpl.getDelegator("default");
      LocalDispatcher dispatcher = ServiceDispatcher.getLocalDispatcher("default", delegator);
      dispatcher.runAsync("stopRepl", Collections.emptyMap());
      loaded.set(true);
    } catch (Exception e) {
      Debug.logError("Error stopping REPL service", MODULE);
    }
  }

  @Override
  public String getName() {
    return name;
  }
}
