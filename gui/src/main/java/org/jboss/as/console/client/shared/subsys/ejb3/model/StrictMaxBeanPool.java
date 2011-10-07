package org.jboss.as.console.client.shared.subsys.ejb3.model;

import org.jboss.as.console.client.shared.viewframework.NamedEntity;
import org.jboss.as.console.client.widgets.forms.Address;
import org.jboss.as.console.client.widgets.forms.Binding;
import org.jboss.as.console.client.widgets.forms.FormItem;

@Address("/subsystem=ejb3/strict-max-bean-instance-pool={0}")
public interface StrictMaxBeanPool extends NamedEntity {
    @Override
    @Binding(detypedName="name", key=true)
    @FormItem(defaultValue="",
              localLabel="common_label_name",
              required=true,
              formItemTypeForEdit="TEXT",
              formItemTypeForAdd="TEXT_BOX")
    String getName();
    @Override
    void setName(String name);

    @Binding(detypedName="max-pool-size")
    @FormItem(defaultValue="20",
              label="Max Pool Size",
              required=true,
              formItemTypeForEdit="NUMBER_BOX",
              formItemTypeForAdd="NUMBER_BOX")
    int getMaxPoolSize();
    void setMaxPoolSize(int maxSize);
}
