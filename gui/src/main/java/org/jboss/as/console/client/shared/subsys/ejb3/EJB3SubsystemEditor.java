package org.jboss.as.console.client.shared.subsys.ejb3;

import java.util.Map;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import org.jboss.as.console.client.shared.help.FormHelpPanel;
import org.jboss.as.console.client.shared.subsys.Baseadress;
import org.jboss.as.console.client.shared.subsys.ejb3.model.EJB3Subsystem;
import org.jboss.as.console.client.widgets.forms.FormToolStrip;
import org.jboss.ballroom.client.widgets.ContentGroupLabel;
import org.jboss.ballroom.client.widgets.forms.Form;
import org.jboss.ballroom.client.widgets.forms.TextBoxItem;
import org.jboss.dmr.client.ModelNode;

public class EJB3SubsystemEditor {
    private final EJB3Presenter presenter;
    private Form<EJB3Subsystem> form;

    public EJB3SubsystemEditor(EJB3Presenter presenter) {
        this.presenter = presenter;
    }

    public Widget asWidget() {
        VerticalPanel panel = new VerticalPanel();
        panel.setStyleName("fill-layout-width");

        panel.add(new ContentGroupLabel("Default Pools"));
        form = new Form<EJB3Subsystem>(EJB3Subsystem.class);
        form.setNumColumns(1);

        TextBoxItem slsbItem = new TextBoxItem("defaultSLSBPool", "Default Stateless Session Bean Pool");
        form.setFields(slsbItem);

        FormToolStrip<EJB3Subsystem> toolStrip = new FormToolStrip<EJB3Subsystem>(form,
            new FormToolStrip.FormCallback<EJB3Subsystem>() {
                @Override
                public void onSave(Map<String, Object> changeset) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onDelete(EJB3Subsystem entity) {
                    // not possible
                }
            });
        toolStrip.providesDeleteOp(false);
        panel.add(toolStrip.asWidget());

        FormHelpPanel helpPanel = new FormHelpPanel(new FormHelpPanel.AddressCallback() {
            @Override
            public ModelNode getAddress() {
                ModelNode address = Baseadress.get();
                address.add("subsystem", "ejb3");
                return address;
            }
        }, form);
        panel.add(helpPanel.asWidget());
        panel.add(form.asWidget());

        return panel;
    }

    public void setProviderDetails(EJB3Subsystem provider) {
        form.edit(provider);
        form.setEnabled(false);
    }
}
