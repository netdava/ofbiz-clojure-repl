import org.apache.ofbiz.entity.GenericValue

replConfig = from('SystemProperty')
        .where('systemResourceId', 'clojureRepl')
        .queryList()
        .collectEntries { GenericValue it -> [ it.get("systemPropertyId"), it.get("systemPropertyValue")] }

context.replConfig = replConfig
