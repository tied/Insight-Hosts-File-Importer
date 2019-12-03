package me.eddelbuettel.plugins.jira.importer;

import com.riadalabs.jira.plugins.insight.services.imports.common.external.AbstractInsightImportModule;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.DataLocator;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.ImportComponentException;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.ImportDataHolder;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.InsightImportModule;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.ModuleOTSelector;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;

public class ImportModule extends AbstractInsightImportModule<ImportConfiguration> implements InsightImportModule<ImportConfiguration> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public ImportModule() {
    }

    @Override
    public ImportDataHolder dataHolder(ImportConfiguration importConfiguration, ModuleOTSelector moduleOTSelector) throws ImportComponentException {
        return null;
    }

    @Override
    public ImportConfiguration importModuleConfigurationTemplate() {
        return new ImportConfiguration();
    }

    @Override
    public List<DataLocator> fetchDataLocators(ImportConfiguration importConfiguration, ModuleOTSelector moduleOTSelector) throws ImportComponentException {
        return null;
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
    public ImportDataHolder dataHolder(ImportConfiguration configuration, ModuleOTSelector moduleOTSelector, @Nullable List<DataLocator> configuredDataLocators, @Nullable List<ModuleOTSelector> enabledModuleOTSelectors) throws ImportComponentException {
        return null;
    }
}
