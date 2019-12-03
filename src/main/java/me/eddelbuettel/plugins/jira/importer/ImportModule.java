package me.eddelbuettel.plugins.jira.importer;

import com.riadalabs.jira.plugins.insight.services.imports.common.external.AbstractInsightImportModule;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.DataLocator;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.ImportComponentException;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.ImportDataHolder;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.InsightImportModule;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.ModuleOTSelector;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.TemplateImportConfiguration;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.model.external.baseversion.InsightSchemaExternal;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.model.validation.ValidationResult;
import me.eddelbuettel.plugins.jira.importer.manager.DataManager;
import me.eddelbuettel.plugins.jira.importer.manager.ImportManager;
import me.eddelbuettel.plugins.jira.importer.manager.StructureManager;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImportModule extends AbstractInsightImportModule<ImportConfiguration> implements InsightImportModule<ImportConfiguration> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private static DataLocator ipAddressLocator = StructureManager.getIpAddressLocator();
    private static DataLocator domainNamesLocator = StructureManager.getDomainNamesLocator();
    private static DataLocator domainNameLocator = StructureManager.getDomainNameLocator();

    public ImportModule() {
    }

    @Override
    public ImportDataHolder dataHolder(ImportConfiguration configuration, ModuleOTSelector moduleOTSelector) throws ImportComponentException {
        return this.dataHolder(configuration, moduleOTSelector);
    }

    @Override
    public ImportConfiguration importModuleConfigurationTemplate() {
        return new ImportConfiguration();
    }

    @Override
    public InsightSchemaExternal predefinedStructure(ImportConfiguration configuration) {
        return (new StructureManager()).getPredefinedStructure();
    }

    @Override
    public TemplateImportConfiguration templateImportConfiguration(ImportConfiguration configuration) {
        try {
            return (new ImportManager().templateImportConfiguration());
        } catch (Exception e) {
            this.logger.error("Unable to prepare insight external data for template configuration " + configuration, e);
            throw new ImportComponentException(e);
        }
    }

    @Override
    public List<DataLocator> fetchDataLocators(ImportConfiguration importConfiguration, ModuleOTSelector moduleOTSelector) throws ImportComponentException {
        if (Selector.IP_ADDRESS.name().equals(moduleOTSelector.getSelector())) {
            return Arrays.asList(ipAddressLocator, domainNamesLocator);
        } else {
            return Selector.DOMAIN_NAME.name().equals(moduleOTSelector.getSelector()) ? Collections.singletonList(domainNameLocator) : Collections.emptyList();
        }
    }

    @Override
    public ImportConfiguration convertConfigurationFromJSON(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return (ImportConfiguration) objectMapper.readValue(jsonString, ImportConfiguration.class);
        } catch (IOException e) {
            this.logger.warn("Unable to transform config to ImportConfiguration " + jsonString, e);
            throw new IllegalArgumentException("Unable to parse configuration as ImportConfiguration " + jsonString, e);
        }
    }

    @Override
    public ValidationResult validateAndTestConfiguration(ImportConfiguration configuration) {
        File file = new File(configuration.getHostsFile());
        if (file.exists()) {
            return ValidationResult.OK();
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("hosts", "Can't find the hosts file");
            return ValidationResult.error(error);
        }
    }

    @Override
    public ImportDataHolder dataHolder(ImportConfiguration configuration, ModuleOTSelector moduleOTSelector, @Nullable List<DataLocator> configuredDataLocators, @Nullable List<ModuleOTSelector> enabledModuleOTSelectors) throws ImportComponentException {
        try {
            return (new DataManager().dataHolder(configuration, moduleOTSelector, configuredDataLocators, enabledModuleOTSelectors));
        } catch (Exception e) {
            this.logger.error("Unable to fetch data holder using conf " + configuration, e);
            throw new ImportComponentException("Unable to fetch data holder from Module", e);
        }
    }
}
