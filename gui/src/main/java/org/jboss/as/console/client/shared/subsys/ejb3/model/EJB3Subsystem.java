package org.jboss.as.console.client.shared.subsys.ejb3.model;

import org.jboss.as.console.client.widgets.forms.Address;
import org.jboss.as.console.client.widgets.forms.Binding;

@Address("/subsystem=ejb3")
public interface EJB3Subsystem {
    @Binding(detypedName="default-slsb-instance-pool")
    String getDefaultSLSBPool();
    void setDefaultSLSBPool(String name);

    @Binding(detypedName="default-mdb-instance-pool")
    String getDefaultMDBPool();
    void setDefaultMDBPool(String name);
}
