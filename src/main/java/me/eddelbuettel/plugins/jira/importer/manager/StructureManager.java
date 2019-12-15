package me.eddelbuettel.plugins.jira.importer.manager;

import com.atlassian.sal.api.message.I18nResolver;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.DataLocator;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.model.ObjectTypeModuleExternal;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.model.external.baseversion.IconExternal;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.model.external.baseversion.InsightSchemaExternal;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.model.external.baseversion.ObjectSchemaExternal;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.model.external.baseversion.ObjectTypeExternal;
import io.riada.StructureUtils;
import me.eddelbuettel.plugins.jira.importer.manager.impl.DomainNameService;
import me.eddelbuettel.plugins.jira.importer.manager.impl.IpAddressService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;

public class StructureManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final I18nResolver i18n;
    private static final int OBJECT_SCHEMA_ID = 1;
    private int objectTypeSequenceNumber = 1;
    private int referenceTypeSequenceNumber = 1;

    public StructureManager(I18nResolver i18n) {
        this.i18n = i18n;
    }

    public InsightSchemaExternal getPredefinedStructure() {
        ObjectSchemaExternal objectSchemaExternal = new ObjectSchemaExternal();
        objectSchemaExternal.setId(OBJECT_SCHEMA_ID);

        InsightSchemaExternal insightSchemaExternal = new InsightSchemaExternal();
        insightSchemaExternal.setObjectSchema(objectSchemaExternal);

        /* Get Object Icon */
        IconExternal icon = getIcon(i18n.getText("insight-hosts-file-integration.object-type.hosts.name"));
        insightSchemaExternal.getIcons().add(icon);

        /* Create Root Object */
        ObjectTypeExternal rootObjectTypeExternal = new ObjectTypeModuleExternal(null, false, null);
        objectSchemaExternal.getObjectTypes().add(rootObjectTypeExternal);
        rootObjectTypeExternal.setAbstractObjectType(true);
        rootObjectTypeExternal.setId(objectTypeSequenceNumber++);
        rootObjectTypeExternal.setName(i18n.getText("insight-hosts-file-integration.object-type.hosts.name"));
        rootObjectTypeExternal.setIcon(icon);

        /* Create IP Address Child Object */
        ObjectTypeExternal ipAddressObjectTypeExternal = new ObjectTypeModuleExternal(null, true, null);
        rootObjectTypeExternal.getObjectTypeChildren().add(ipAddressObjectTypeExternal);
        ipAddressObjectTypeExternal.setId(objectTypeSequenceNumber++);
        ipAddressObjectTypeExternal.setName(i18n.getText("insight-hosts-file-integration.object-type.ip-addresses.name"));
        ipAddressObjectTypeExternal.setIcon(icon);

        /* Create Domain Name Child Object */
        ObjectTypeExternal domainNameObjectTypeExternal = new ObjectTypeModuleExternal(null, true, null);
        rootObjectTypeExternal.getObjectTypeChildren().add(domainNameObjectTypeExternal);
        domainNameObjectTypeExternal.setId(objectTypeSequenceNumber++);
        domainNameObjectTypeExternal.setName(i18n.getText("insight-hosts-file-integration.object-type.domain-names.name"));
        domainNameObjectTypeExternal.setIcon(icon);

        /* Add IP Address Object Attributes */
        StructureUtils.addTextObjectTypeAttribute(i18n.getText("insight-hosts-file-integration.object-type-attribute.ip-address.name"), ipAddressObjectTypeExternal, IpAddressService.ipAddress, true, false, false, i18n.getText("insight-hosts-file-integration.object-type-attribute.ip-address.description"), false);
        StructureUtils.addSelectObjectTypeAttribute(i18n.getText("insight-hosts-file-integration.object-type-attribute.type.name"), ipAddressObjectTypeExternal, IpAddressService.type, i18n.getText("insight-hosts-file-integration.object-type-attribute.type.description"));
        StructureUtils.addReferenceObjectTypeAttribute(insightSchemaExternal, i18n.getText("insight-hosts-file-integration.object-type-attribute.domain-names.name"), ipAddressObjectTypeExternal, domainNameObjectTypeExternal, i18n.getText("insight-hosts-file-integration.object-type-attribute.domain-names.reference"), true, false, IpAddressService.domainNames, false, null, i18n.getText("insight-hosts-file-integration.object-type-attribute.domain-names.description"), referenceTypeSequenceNumber++, OBJECT_SCHEMA_ID);

        /* Add Domain Name Object Attributes */
        StructureUtils.addTextObjectTypeAttribute(i18n.getText("insight-hosts-file-integration.object-type-attribute.domain-name.name"), domainNameObjectTypeExternal, DomainNameService.domainName, true, false, false, i18n.getText("insight-hosts-file-integration.object-type-attribute.domain-name.description"), false);

        return insightSchemaExternal;
    }

    private IconExternal getIcon(String objectTypeName) {
        IconExternal iconExternal = new IconExternal();
        iconExternal.setName(objectTypeName);
        iconExternal.setObjectSchemaId(OBJECT_SCHEMA_ID);

        InputStream inputStream = StructureManager.class.getClassLoader()
                .getResourceAsStream("/images/icons/" + objectTypeName + ".png");

        try {
            iconExternal.setImage48(IOUtils.toByteArray(inputStream));
            iconExternal.setImage16(iconExternal.getImage48());
        } catch (Exception e) {
            try {
                inputStream.close();
            } catch (IOException ioe) {
            }
        }

        return iconExternal;
    }

}
