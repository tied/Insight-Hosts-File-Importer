package me.eddelbuettel.plugins.jira.importer.manager;

import com.riadalabs.jira.plugins.insight.services.imports.common.external.DataLocator;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.model.ObjectTypeModuleExternal;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.model.external.baseversion.IconExternal;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.model.external.baseversion.InsightSchemaExternal;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.model.external.baseversion.ObjectSchemaExternal;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.model.external.baseversion.ObjectTypeExternal;
import io.riada.StructureUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;

public class StructureManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private static final int OBJECT_SCHEMA_ID = 1;
    private int objectTypeSequenceNumber = 1;
    private int referenceTypeSequenceNumber = 1;
    private static DataLocator ipAddressLocator = new DataLocator("IP Address");
    private static DataLocator domainNamesLocator = new DataLocator("Domain Names");
    private static DataLocator domainNameLocator = new DataLocator("Domain Name");

    public StructureManager() {
    }

    public InsightSchemaExternal getPredefinedStructure() {
        ObjectSchemaExternal objectSchemaExternal = new ObjectSchemaExternal();
        objectSchemaExternal.setId(OBJECT_SCHEMA_ID);

        InsightSchemaExternal insightSchemaExternal = new InsightSchemaExternal();
        insightSchemaExternal.setObjectSchema(objectSchemaExternal);

        /* Get Object Icon */
        IconExternal icon = getIcon("Hosts");
        insightSchemaExternal.getIcons().add(icon);

        /* Create Root Object */
        ObjectTypeExternal rootObjectTypeExternal = new ObjectTypeModuleExternal(null, false, null);
        objectSchemaExternal.getObjectTypes().add(rootObjectTypeExternal);
        rootObjectTypeExternal.setAbstractObjectType(true);
        rootObjectTypeExternal.setId(objectTypeSequenceNumber++);
        rootObjectTypeExternal.setName("Hosts");
        rootObjectTypeExternal.setIcon(icon);

        /* Create IP Address Child Object */
        ObjectTypeExternal ipAddressObjectTypeExternal = new ObjectTypeModuleExternal(null, true, null);
        rootObjectTypeExternal.getObjectTypeChildren().add(ipAddressObjectTypeExternal);
        ipAddressObjectTypeExternal.setId(objectTypeSequenceNumber++);
        ipAddressObjectTypeExternal.setName("IP Address");
        ipAddressObjectTypeExternal.setIcon(icon);

        /* Create Domain Name Child Object */
        ObjectTypeExternal domainNameObjectTypeExternal = new ObjectTypeModuleExternal(null, true, null);
        rootObjectTypeExternal.getObjectTypeChildren().add(domainNameObjectTypeExternal);
        domainNameObjectTypeExternal.setId(objectTypeSequenceNumber++);
        domainNameObjectTypeExternal.setName("Domain Name");
        domainNameObjectTypeExternal.setIcon(icon);

        /* Add IP Address Object Attributes */
        StructureUtils.addTextObjectTypeAttribute("IP Address", ipAddressObjectTypeExternal, ipAddressLocator, true, false, false, "This is the IP Address of the entry", false);
        StructureUtils.addReferenceObjectTypeAttribute(insightSchemaExternal, "Domain Names", ipAddressObjectTypeExternal, domainNameObjectTypeExternal, "Relates to Domain", true, false, domainNamesLocator, false, null, "These are the related Domain Names", referenceTypeSequenceNumber++, OBJECT_SCHEMA_ID);

        /* Add Domain Name Object Attributes */
        StructureUtils.addTextObjectTypeAttribute("Domain Name", domainNameObjectTypeExternal, domainNameLocator, true, false, false, "This is the Domain Name of the entry", false);

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

    public static DataLocator getIpAddressLocator() {
        return ipAddressLocator;
    }

    public static DataLocator getDomainNamesLocator() {
        return domainNamesLocator;
    }

    public static DataLocator getDomainNameLocator() {
        return domainNameLocator;
    }
}
