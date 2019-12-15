package me.eddelbuettel.plugins.jira.importer.manager;

import com.atlassian.sal.api.message.I18nResolver;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.DataLocator;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.TemplateImportConfiguration;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.TemplateImportConfiguration.AttributeMapping;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.TemplateImportConfiguration.ObjectTypeMapping;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.model.MissingObjectsType;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.model.TemplateHandleMissingObjects;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.model.ThresholdType;
import me.eddelbuettel.plugins.jira.importer.manager.impl.DomainNameService;
import me.eddelbuettel.plugins.jira.importer.manager.impl.IpAddressService;
import me.eddelbuettel.plugins.jira.importer.model.ModuleSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImportManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private I18nResolver i18n;

    public ImportManager(I18nResolver i18n) {
        this.i18n = i18n;
    }

    public TemplateImportConfiguration templateImportConfiguration() {

        TemplateImportConfiguration templateImportConfiguration = null;

        TemplateHandleMissingObjects removeMissingObjects = new TemplateHandleMissingObjects(MissingObjectsType.REMOVE);
        removeMissingObjects.setThresholdType(ThresholdType.DAYS);
        removeMissingObjects.setThreshold(0);

        List<ObjectTypeMapping> objectTypeMappings = new ArrayList<>();

        /* IP Address Object Type */
        ObjectTypeMapping ipAddressObjectTypeMapping = addObjectTypeMapping(i18n.getText("insight-hosts-file-integration.object-type.ip-addresses.name"), ModuleSelector.IP_ADDRESS.getSelector(), removeMissingObjects, false);
        List<AttributeMapping> ipAddressAttributeMappings = new ArrayList<>();
        ipAddressAttributeMappings.add(addAttributeMapping(IpAddressService.ipAddress, IpAddressService.ipAddress.getLocator(), true));
        ipAddressAttributeMappings.add(addAttributeMapping(IpAddressService.domainNames, IpAddressService.domainNames.getLocator(), false,"\""+ i18n.getText("insight-hosts-file-integration.object-type-attribute.domain-names.name") + "\" IN (${" + IpAddressService.domainNames.getLocator() + "${0}})"));
        ipAddressAttributeMappings.add(addAttributeMapping(IpAddressService.type, IpAddressService.type.getLocator(), false));
        ipAddressObjectTypeMapping.setAttributesMapping(ipAddressAttributeMappings);
        objectTypeMappings.add(ipAddressObjectTypeMapping);

        /* Domain Name Object Type */
        ObjectTypeMapping domainNameObjectTypeMapping = addObjectTypeMapping(i18n.getText("insight-hosts-file-integration.object-type.domain-names.name"), ModuleSelector.DOMAIN_NAME.getSelector(), removeMissingObjects, false);
        List<AttributeMapping> domainNameAttributeMappings = new ArrayList<>();
        domainNameAttributeMappings.add(addAttributeMapping(DomainNameService.domainName, DomainNameService.domainName.getLocator(), true));
        domainNameObjectTypeMapping.setAttributesMapping(domainNameAttributeMappings);
        objectTypeMappings.add(domainNameObjectTypeMapping);

        return TemplateImportConfiguration.createConfigWithMapping(objectTypeMappings);
    }

    private ObjectTypeMapping addObjectTypeMapping(String objectTypeName, String selector, TemplateHandleMissingObjects templateHandleMissingObjects, boolean disabledByDefault) {
        ObjectTypeMapping objectTypeMapping = new ObjectTypeMapping();
        objectTypeMapping.setObjectTypeName(objectTypeName);
        objectTypeMapping.setSelector(selector);
        objectTypeMapping.setHandleMissingObjects(templateHandleMissingObjects);
        if (disabledByDefault) {
            objectTypeMapping.setDisabledByDefault(true);
        }

        return objectTypeMapping;
    }

    private AttributeMapping addAttributeMapping(DataLocator attributeLocator, String attributeName, boolean externalIdPart) {
        AttributeMapping attributeMapping = new AttributeMapping();
        attributeMapping.setAttributeLocators(Collections.singletonList(attributeLocator));
        attributeMapping.setAttributeName(attributeName);
        attributeMapping.setExternalIdPart(externalIdPart);

        return attributeMapping;
    }

    private AttributeMapping addAttributeMapping(List<DataLocator> attributeLocator, String attributeName, boolean externalIdPart, boolean isSuggestedLabel) {
        AttributeMapping attributeMapping = new AttributeMapping();
        attributeMapping.setAttributeLocators(attributeLocator);
        attributeMapping.setAttributeName(attributeName);
        attributeMapping.setExternalIdPart(externalIdPart);
        attributeMapping.setSuggestedLabel(isSuggestedLabel);

        return attributeMapping;
    }

    private AttributeMapping addAttributeMapping(DataLocator attributeLocator, String attributeName, boolean externalIdPart, String objectMappingIql) {
        AttributeMapping attributeMapping = new AttributeMapping();
        attributeMapping.setAttributeLocators(Collections.singletonList(attributeLocator));
        attributeMapping.setAttributeName(attributeName);
        attributeMapping.setObjectMappingIQL(objectMappingIql);
        attributeMapping.setExternalIdPart(externalIdPart);

        return attributeMapping;
    }
}
