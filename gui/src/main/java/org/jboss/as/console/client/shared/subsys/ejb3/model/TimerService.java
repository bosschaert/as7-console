package org.jboss.as.console.client.shared.subsys.ejb3.model;

import org.jboss.as.console.client.shared.viewframework.NamedEntity;
import org.jboss.as.console.client.widgets.forms.Address;
import org.jboss.as.console.client.widgets.forms.FormItem;

@Address("/subsystem=ejb3/service=timer-service")
public interface TimerService extends NamedEntity {
    @FormItem(label="Path",
              required=true)
    String getPath();
    void setPath(String path);
}
