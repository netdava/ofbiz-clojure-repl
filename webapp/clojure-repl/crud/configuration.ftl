<div>
    <div>
    Repl config is ${replConfig}
    </div>
        <label>${uiLabelMap.clojureReplsetReplPort}&nbsp;
            <input type="text" name="replPort" value="${replConfig['clojure.repl.port']}" />
        </label><br/>
        <label>${uiLabelMap.clojureReplsetReplHost}&nbsp;
            <input type="text" name="replHost" value="${replConfig['clojure.repl.host']}" />
        </label>
    <div class="button-bar">
      <input type="submit" value="${uiLabelMap.clojureReplSave}"/>
    </div>
</div>