package org.jboss.as.console.client.shared.subsys.ejb3.model;

import org.jboss.as.console.client.widgets.forms.Address;
import org.jboss.as.console.client.widgets.forms.Binding;
import org.jboss.as.console.client.widgets.forms.FormItem;

@Address("/subsystem=ejb3")
public interface EJB3Subsystem {
    @Binding(detypedName="default-slsb-instance-pool")
    @FormItem(label="Default Stateless Session Bean Pool")
    String getDefaultSLSBPool();
    void setDefaultSLSBPool(String name);

    @Binding(detypedName="default-mdb-instance-pool")
    @FormItem(label="Default Message Driven Bean Pool")
    String getDefaultMDBPool();
    void setDefaultMDBPool(String name);
}
