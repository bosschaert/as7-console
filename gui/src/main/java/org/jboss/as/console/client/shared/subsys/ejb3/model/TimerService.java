package org.jboss.as.console.client.shared.subsys.ejb3.model;

import org.jboss.as.console.client.widgets.forms.Address;
import org.jboss.as.console.client.widgets.forms.Binding;
import org.jboss.as.console.client.widgets.forms.FormItem;

@Address("/subsystem=ejb3/service=timer-service")
public interface TimerService {
    @Binding(detypedName="core-threads")
    @FormItem(defaultValue="0",
              label="Core Threads",
              required=true,
              formItemTypeForEdit="NUMBER_BOX")
    int getCoreThreads();
    void setCoreThreads(int threads);

    @Binding(detypedName="max-threads")
    @FormItem(defaultValue="4",
              label="Max Threads",
              required=true,
              formItemTypeForEdit="NUMBER_BOX")
    int getMaxThreads();
    void setMaxThreads(int threads);

    @FormItem(label="Path",
              required=true)
    String getPath();
    void setPath(String path);

    @Binding(detypedName="relative-to")
    @FormItem(label="Relative To",
              required=true)
    String getRelativeTo();
    void setRelativeTo(String location);
}
