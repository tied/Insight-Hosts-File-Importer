package me.eddelbuettel.plugins.jira.importer.manager;

import com.riadalabs.jira.plugins.insight.services.imports.common.external.model.ObjectTypeModuleExternal;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.model.external.baseversion.IconExternal;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.model.external.baseversion.InsightSchemaExternal;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.model.external.baseversion.ObjectSchemaExternal;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.model.external.baseversion.ObjectTypeExternal;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.model.external.baseversion.ReferenceTypeExternal;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class StructureManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private static final int OBJECT_SCHEMA_ID = 1; /* Just a fake id */
    private int objectTypeSequenceNumber = 1;
    private int referenceTypeSequenceNumber = 1;

    public StructureManager() {
    }

    public InsightSchemaExternal getPredefinedStructure() {
        ObjectSchemaExternal objectSchemaExternal = new ObjectSchemaExternal();
        objectSchemaExternal.setId(OBJECT_SCHEMA_ID);

        InsightSchemaExternal insightSchemaExternal = new InsightSchemaExternal();
        insightSchemaExternal.setObjectSchema(objectSchemaExternal);

        ReferenceTypeExternal relatesToDomainReferenceTypeExternal = new ReferenceTypeExternal();
        relatesToDomainReferenceTypeExternal.setId(referenceTypeSequenceNumber++);
        relatesToDomainReferenceTypeExternal.setName("Relates to Domain");
        relatesToDomainReferenceTypeExternal.setDescription("This object relates to Domain");
        relatesToDomainReferenceTypeExternal.setColor("4C858A");
        insightSchemaExternal.setReferenceTypes(Collections.singletonList(relatesToDomainReferenceTypeExternal));

        IconExternal icon = getIcon("Hosts");
        insightSchemaExternal.getIcons().add(icon);

        /* Root Object */
        ObjectTypeExternal rootObjectTypeExternal = new ObjectTypeModuleExternal(null, false, null);
        objectSchemaExternal.getObjectTypes().add(rootObjectTypeExternal);
        rootObjectTypeExternal.setId(objectTypeSequenceNumber++);
        rootObjectTypeExternal.setName("Hosts");
        rootObjectTypeExternal.setIcon(icon);

        // IP Address Child Object
        ObjectTypeExternal ipAddressObjectTypeExternal = new ObjectTypeModuleExternal(null, true, null);
        rootObjectTypeExternal.getObjectTypeChildren().add(ipAddressObjectTypeExternal);
        ipAddressObjectTypeExternal.setId(objectTypeSequenceNumber++);
        ipAddressObjectTypeExternal.setName("IP Address");
        ipAddressObjectTypeExternal.setIcon(icon);

        // Domainname Child Object
        ObjectTypeExternal domainnameObjectTypeExternal = new ObjectTypeModuleExternal(null, true, null);
        rootObjectTypeExternal.getObjectTypeChildren().add(domainnameObjectTypeExternal);
        domainnameObjectTypeExternal.setId(objectTypeSequenceNumber++);
        domainnameObjectTypeExternal.setName("Domainname");
        domainnameObjectTypeExternal.setIcon(icon);

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
