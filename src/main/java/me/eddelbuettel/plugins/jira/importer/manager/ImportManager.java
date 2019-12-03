package me.eddelbuettel.plugins.jira.importer.manager;

import com.riadalabs.jira.plugins.insight.services.imports.common.external.DataLocator;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.TemplateImportConfiguration;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.TemplateImportConfiguration.AttributeMapping;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.TemplateImportConfiguration.ObjectTypeMapping;
import me.eddelbuettel.plugins.jira.importer.Selector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImportManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private static DataLocator ipAddressLocator = StructureManager.getIpAddressLocator();
    private static DataLocator domainNamesLocator = StructureManager.getDomainNamesLocator();
    private static DataLocator domainNameLocator = StructureManager.getDomainNameLocator();

    public ImportManager() {
    }

    public TemplateImportConfiguration templateImportConfiguration() {

        TemplateImportConfiguration templateImportConfiguration = null;

        List<ObjectTypeMapping> objectTypeMappings = new ArrayList<>();
        ObjectTypeMapping objectTypeMapping = new ObjectTypeMapping();
        objectTypeMapping.setObjectTypeName("IP Address");
        objectTypeMapping.setSelector(Selector.IP_ADDRESS.getSelector());

        List<AttributeMapping> attributeMappings = new ArrayList<>();

        AttributeMapping attributeMapping = new AttributeMapping();
        attributeMapping.setAttributeName("IP Address");
        attributeMapping.setAttributeLocators(Collections.singletonList(ipAddressLocator));
        attributeMapping.setExternalIdPart(true);
        attributeMappings.add(attributeMapping);

        attributeMapping = new AttributeMapping();
        attributeMapping.setAttributeName("Domain Names");
        attributeMapping.setAttributeLocators(Collections.singletonList(domainNamesLocator));
        attributeMapping.setObjectMappingIQL("\"Domain Name\" IN (${" + domainNamesLocator.getLocator() + "${0}})");
        attributeMappings.add(attributeMapping);

        objectTypeMapping.setAttributesMapping(attributeMappings);

        objectTypeMappings.add(objectTypeMapping);
        objectTypeMapping = new ObjectTypeMapping();
        objectTypeMapping.setObjectTypeName("Domain Name");
        objectTypeMapping.setSelector(Selector.DOMAIN_NAME.getSelector());

        attributeMapping = new AttributeMapping();
        attributeMapping.setAttributeName("Name");
        attributeMapping.setAttributeLocators(Collections.singletonList(domainNameLocator));
        attributeMapping.setExternalIdPart(true);

        attributeMappings = new ArrayList<>();
        attributeMappings.add(attributeMapping);
        objectTypeMapping.setAttributesMapping(attributeMappings);
        objectTypeMappings.add(objectTypeMapping);

        return TemplateImportConfiguration.createConfigWithMapping(objectTypeMappings);

    }

}
