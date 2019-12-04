package me.eddelbuettel.plugins.jira.importer.manager;

import com.riadalabs.jira.plugins.insight.services.imports.common.external.DataLocator;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.TemplateImportConfiguration;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.TemplateImportConfiguration.AttributeMapping;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.TemplateImportConfiguration.ObjectTypeMapping;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.model.MissingObjectsType;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.model.TemplateHandleMissingObjects;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.model.ThresholdType;
import me.eddelbuettel.plugins.jira.importer.model.ModuleSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImportManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private static DataLocator ipAddressLocator = StructureManager.getIpAddressLocator();
    private static DataLocator typeLocator = StructureManager.getTypeLocator();
    private static DataLocator domainNamesLocator = StructureManager.getDomainNamesLocator();
    private static DataLocator domainNameLocator = StructureManager.getDomainNameLocator();

    public ImportManager() {
    }

    public TemplateImportConfiguration templateImportConfiguration() {

        TemplateImportConfiguration templateImportConfiguration = null;

        TemplateHandleMissingObjects removeMissingObjects = new TemplateHandleMissingObjects(MissingObjectsType.REMOVE);
        removeMissingObjects.setThresholdType(ThresholdType.DAYS);
        removeMissingObjects.setThreshold(0);

        List<ObjectTypeMapping> objectTypeMappings = new ArrayList<>();
        ObjectTypeMapping objectTypeMapping = new ObjectTypeMapping();
        objectTypeMapping.setObjectTypeName("IP Address");
        objectTypeMapping.setSelector(ModuleSelector.IP_ADDRESS.getSelector());
        objectTypeMapping.setHandleMissingObjects(removeMissingObjects);

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

        attributeMapping = new AttributeMapping();
        attributeMapping.setAttributeName("Type");
        attributeMapping.setAttributeLocators(Collections.singletonList(typeLocator));
        attributeMappings.add(attributeMapping);

        objectTypeMapping.setAttributesMapping(attributeMappings);

        objectTypeMappings.add(objectTypeMapping);
        objectTypeMapping = new ObjectTypeMapping();
        objectTypeMapping.setObjectTypeName("Domain Name");
        objectTypeMapping.setSelector(ModuleSelector.DOMAIN_NAME.getSelector());
        objectTypeMapping.setHandleMissingObjects(removeMissingObjects);

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
