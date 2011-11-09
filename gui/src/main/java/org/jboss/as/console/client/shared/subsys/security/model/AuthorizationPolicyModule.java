package org.jboss.as.console.client.shared.subsys.security.model;

import java.util.List;

import org.jboss.as.console.client.shared.properties.PropertyRecord;

public interface AuthorizationPolicyModule {
    String getCode();
    void setCode(String code);

    String getFlag();
    void setFlag(String flag);

    List<PropertyRecord> getProperties();
    void setProperties(List<PropertyRecord> properties);
}
