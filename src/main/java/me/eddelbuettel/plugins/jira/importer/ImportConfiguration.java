package me.eddelbuettel.plugins.jira.importer;

import com.atlassian.adapter.jackson.ObjectMapper;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.ImportModuleConfiguration;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.insightfield.InsightFieldConfiguration;
import com.riadalabs.jira.plugins.insight.services.imports.common.external.insightfield.text.InsightFieldTextConfiguration;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.Collections;
import java.util.List;

public class ImportConfiguration implements ImportModuleConfiguration {

    private static final long serialVersionUID = -49657992090875844L;
    private InsightFieldConfiguration hostsFile = new InsightFieldTextConfiguration("hostsFile", "Hosts File", "The absolute path to the system hosts file", "/etc/hosts");

    public ImportConfiguration() {
        this.hostsFile.setMandatory(true);
    }

    public String getHostsFile() {
        return (String) this.hostsFile.getValue();
    }

    public void setHostsFile(String hostsFile) {
        this.hostsFile.setValue(hostsFile);
    }

    @Override
    public String toJSON() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }

    @Override
    @JsonIgnore
    public List<InsightFieldConfiguration> getFieldsConfiguration() {
        return Collections.singletonList(this.hostsFile);
    }
}
