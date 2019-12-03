package me.eddelbuettel.plugins.jira.importer;

import java.util.HashMap;
import java.util.Map;

public enum  Selector {
    IP_ADDRESS("IP_ADDRESS"),
    DOMAIN_NAME("DOMAIN_NAME"),
    UNKNOWN("");

    private String selector;
    private static Map<String, Selector> operatorToEnumMapping;

    private Selector(String selector) {
        this.selector = selector;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public static Selector getInstance(String operator) {
        if (operatorToEnumMapping == null) {
            initMapping();
        }

        Selector selector = operatorToEnumMapping.get(operator.toUpperCase());

        return selector != null ? selector : Selector.UNKNOWN;
    }

    private static void initMapping() {
        operatorToEnumMapping = new HashMap<>();
        for (Selector s : values()) {
            operatorToEnumMapping.put(s.selector, s);
        }
    }
}
