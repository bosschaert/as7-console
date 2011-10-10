package org.jboss.as.console.client.shared.subsys.ejb3.model;

import org.jboss.as.console.client.widgets.forms.Address;
import org.jboss.as.console.client.widgets.forms.Binding;
import org.jboss.as.console.client.widgets.forms.FormItem;

@Address("/subsystem=ejb3")
public interface EJB3Subsystem {
    @Binding(detypedName="default-slsb-instance-pool")
    @FormItem(label="Default Stateless Session Bean Pool",
              required=true,
              formItemTypeForEdit="COMBO_BOX")
    String getDefaultSLSBPool();
    void setDefaultSLSBPool(String name);

    @Binding(detypedName="default-mdb-instance-pool")
    @FormItem(label="Default Message Driven Bean Pool",
              required=true,
              formItemTypeForEdit="COMBO_BOX")
    String getDefaultMDBPool();
    void setDefaultMDBPool(String name);

    @Binding(detypedName="default-resource-adapter-name")
    @FormItem(label="Default Resource Adapter",
              required=true)
    String getDefaultRA();
    void setDefaultRA(String name);

    @Binding(detypedName="default-singleton-access-timeout")
    @FormItem(label="Default Singleton Access Timeout",
              required=true,
              formItemTypeForEdit="NUMBER_BOX")
    long getDefaultSingletonAccessTimeout();
    void setDefaultSingletonAccessTimeout(long timeout);

    @Binding(detypedName="default-stateful-access-timeout")
    @FormItem(label="Default Stateful Access Timeout",
              required=true,
              formItemTypeForEdit="NUMBER_BOX")
    long getDefaultStatefulAccessTimeout();
    void setDefaultStatefulAccessTimeout(long timeout);
}
