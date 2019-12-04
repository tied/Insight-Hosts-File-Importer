package me.eddelbuettel.plugins.jira.importer.manager;

import com.riadalabs.jira.plugins.insight.services.imports.common.external.DataLocator;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.ImportComponentException;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.ImportDataHolder;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.InMemoryDataHolder;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.ModuleOTSelector;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.model.DataEntry;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.model.SimpleDataEntry;
import io.netty.resolver.HostsFileEntries;
import io.netty.resolver.HostsFileParser;
import me.eddelbuettel.plugins.jira.importer.ImportConfiguration;
import me.eddelbuettel.plugins.jira.importer.model.ModuleSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private static DataLocator ipAddressLocator = StructureManager.getIpAddressLocator();
    private static DataLocator typeLocator = StructureManager.getTypeLocator();
    private static DataLocator domainNamesLocator = StructureManager.getDomainNamesLocator();
    private static DataLocator domainNameLocator = StructureManager.getDomainNameLocator();

    public DataManager() {
    }

    public ImportDataHolder dataHolder(ImportConfiguration configuration, ModuleOTSelector moduleOTSelector, @Nullable List<DataLocator> configuredDataLocators, @Nullable List<ModuleOTSelector> enabledModuleOTSelectors) throws ImportComponentException {

        List<DataEntry> dataEntries = new ArrayList<>();
        if (!moduleOTSelector.isEmpty()) {
            try {

                File file = new File(configuration.getHostsFile());
                HostsFileEntries hostsFileEntries = HostsFileParser.parse(file);

                /* Get Unique IPv4 Addresses */
                Collection<Inet4Address> allIp4Addresses = hostsFileEntries.inet4Entries().values();
                List<Inet4Address> uniqueIp4addresses = allIp4Addresses.stream().distinct().collect(Collectors.toList());

                /* Get Unique IPv6 Addresses */
                Collection<Inet6Address> allIp6Addresses = hostsFileEntries.inet6Entries().values();
                List<Inet6Address> uniqueIp6addresses = allIp6Addresses.stream().distinct().collect(Collectors.toList());

                if (ModuleSelector.IP_ADDRESS.getSelector().equals(moduleOTSelector.getSelector())) {

                    for (Inet4Address inet4Address : uniqueIp4addresses) {
                        List<String> domainNames = new ArrayList<>();
                        for (Map.Entry<String, Inet4Address> entry : hostsFileEntries.inet4Entries().entrySet()) {
                            /* Get Domain Names by IPv4 Address */
                            if (entry.getValue().equals(inet4Address)) {
                                domainNames.add(entry.getKey());
                            }
                        }
                        Map<DataLocator, List<String>> dataMapx = new HashMap<>();
                        dataMapx.put(ipAddressLocator, Collections.singletonList(inet4Address.getHostAddress()));
                        dataMapx.put(domainNamesLocator, domainNames);
                        dataMapx.put(typeLocator, Collections.singletonList("IPv4"));
                        dataEntries.add(new SimpleDataEntry(dataMapx));
                    }

                    for (Inet6Address inet6Address : uniqueIp6addresses) {
                        List<String> domainNames = new ArrayList<>();
                        for (Map.Entry<String, Inet6Address> entry : hostsFileEntries.inet6Entries().entrySet()) {
                            /* Get Domain Names by IPv4 Address */
                            if (entry.getValue().equals(inet6Address)) {
                                domainNames.add(entry.getKey());
                            }
                        }
                        Map<DataLocator, List<String>> dataMapx = new HashMap<>();
                        dataMapx.put(ipAddressLocator, Collections.singletonList(inet6Address.getHostAddress()));
                        dataMapx.put(domainNamesLocator, domainNames);
                        dataMapx.put(typeLocator, Collections.singletonList("IPv6"));
                        dataEntries.add(new SimpleDataEntry(dataMapx));
                    }
                } else if (ModuleSelector.DOMAIN_NAME.getSelector().equals(moduleOTSelector.getSelector())) {

                    for (Map.Entry<String, Inet4Address> entry : hostsFileEntries.inet4Entries().entrySet()) {
                        Map<DataLocator, List<String>> dataMap = new HashMap<>();
                        dataMap.put(domainNameLocator, Collections.singletonList(entry.getKey()));
                        dataEntries.add(new SimpleDataEntry(dataMap));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return InMemoryDataHolder.createInMemoryDataHolder(dataEntries);

    }
}
