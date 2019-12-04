package me.eddelbuettel.plugins.jira.importer.manager.impl;

import com.riadalabs.jira.plugins.insight.services.imports.common.external.DataLocator;
import me.eddelbuettel.plugins.jira.importer.model.DomainName;

import java.util.List;

public class IpAddressService {

    public static DataLocator ipAddress = new DataLocator("IP Address");
    public static DataLocator domainNames = new DataLocator("Domain Names");
    public static DataLocator type = new DataLocator("Type");

    public IpAddressService() {
    }
}
