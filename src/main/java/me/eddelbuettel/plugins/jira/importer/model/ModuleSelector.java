package me.eddelbuettel.plugins.jira.importer.model;

import java.util.HashMap;
import java.util.Map;

public enum ModuleSelector {
    IP_ADDRESS("IP_ADDRESS"),
    DOMAIN_NAME("DOMAIN_NAME"),
    UNKNOWN("");

    private String selector;
    private static Map<String, ModuleSelector> operatorToEnumMapping;

    private ModuleSelector(String selector) {
        this.selector = selector;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public static ModuleSelector getInstance(String operator) {
        if (operatorToEnumMapping == null) {
            initMapping();
        }

        ModuleSelector moduleSelector = operatorToEnumMapping.get(operator.toUpperCase());

        return moduleSelector != null ? moduleSelector : ModuleSelector.UNKNOWN;
    }

    private static void initMapping() {
        operatorToEnumMapping = new HashMap<>();
        for (ModuleSelector s : values()) {
            operatorToEnumMapping.put(s.selector, s);
        }
    }
}
