<div>
<h2>${uiLabelMap.CurrentReplConfig}</h2>
<table>
  <tr>
    <td>${uiLabelMap.StartWithOFBiz}</td>
    <td>${replConfig['clojure.repl.enabled']}</td>
  </tr>
  <tr>
    <td>${uiLabelMap.HostAddress}</td>
    <td>${replConfig['clojure.repl.host']}</td>
  </tr>
  <tr>
    <td>${uiLabelMap.['@component-resource-name@RunOnPort']}</td>
    <td>${replConfig['clojure.repl.port']}</td>
  </tr>
</table><br/><br/>

        <label>${uiLabelMap.clojureReplsetReplPort}&nbsp;
            <input type="text" name="replPort" value="${replConfig['clojure.repl.port']}" />
        </label><br/>
        <label>${uiLabelMap.clojureReplsetReplHost}&nbsp;
            <input type="text" name="replHost" value="${replConfig['clojure.repl.host']}" />
        </label>
    <div>
      <input type="submit" value="${uiLabelMap.clojureReplSave}"/>
    </div>
</div>