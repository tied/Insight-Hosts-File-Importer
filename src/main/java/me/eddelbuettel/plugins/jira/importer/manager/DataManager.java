package me.eddelbuettel.plugins.jira.importer.manager;

import com.riadalabs.jira.plugins.insight.services.imports.common.external.DataLocator;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.ImportComponentException;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.ImportDataHolder;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.InMemoryDataHolder;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.ModuleOTSelector;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.model.DataEntry;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.model.SimpleDataEntry;
import me.eddelbuettel.plugins.jira.importer.ImportConfiguration;
import me.eddelbuettel.plugins.jira.importer.Selector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class DataManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private static DataLocator ipAddressLocator = StructureManager.getIpAddressLocator();
    private static DataLocator domainNamesLocator = StructureManager.getDomainNamesLocator();
    private static DataLocator domainNameLocator = StructureManager.getDomainNameLocator();
    private static final Pattern WHITESPACES = Pattern.compile("[ \t]+");

    public DataManager() {
    }

    public ImportDataHolder dataHolder(ImportConfiguration configuration, ModuleOTSelector moduleOTSelector, @Nullable List<DataLocator> configuredDataLocators, @Nullable List<ModuleOTSelector> enabledModuleOTSelectors) throws ImportComponentException {

        // TODO: Refactor Hostfile parser
        List<DataEntry> dataEntries = new ArrayList<>();
        if (!moduleOTSelector.isEmpty()) {
            try {
                Stream<String> lines = Files.lines(Paths.get(configuration.getHostsFile()));

                lines.forEach((line) -> {
                    int commentPosition = line.indexOf("#");
                    if (commentPosition != -1) {
                        line = line.substring(0, commentPosition);
                    }

                    line = line.trim();
                    if (!line.isEmpty()) {
                        List<String> lineParts = new ArrayList<>();
                        String[] var5 = WHITESPACES.split(line);
                        int var6 = var5.length;

                        for (int var7 = 0; var7 < var6; ++var7) {
                            String s = var5[var7];
                            if (!s.isEmpty()) {
                                lineParts.add(s);
                            }
                        }

                        if (Selector.IP_ADDRESS.name().equals(moduleOTSelector.getSelector())) {
                            Map<DataLocator, List<String>> dataMapx = new HashMap<>();
                            dataMapx.put(ipAddressLocator, Collections.singletonList(lineParts.get(0)));
                            dataMapx.put(domainNamesLocator, lineParts);
                            dataEntries.add(new SimpleDataEntry(dataMapx));
                        } else if (Selector.DOMAIN_NAME.name().equals(moduleOTSelector.getSelector())) {
                            lineParts.remove(0);
                            Iterator var10 = lineParts.iterator();

                            while (var10.hasNext()) {
                                String linePart = (String) var10.next();
                                Map<DataLocator, List<String>> dataMap = new HashMap<>();
                                dataMap.put(domainNameLocator, Collections.singletonList(linePart));
                                dataEntries.add(new SimpleDataEntry(dataMap));
                            }
                        }
                    }

                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return InMemoryDataHolder.createInMemoryDataHolder(dataEntries);

    }
}
