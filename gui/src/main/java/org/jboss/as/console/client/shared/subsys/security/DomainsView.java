/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.jboss.as.console.client.shared.subsys.security;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import org.jboss.as.console.client.shared.dispatch.DispatchAsync;
import org.jboss.as.console.client.shared.subsys.security.model.SecurityDomain;
import org.jboss.as.console.client.shared.viewframework.AbstractEntityView;
import org.jboss.as.console.client.shared.viewframework.Columns;
import org.jboss.as.console.client.shared.viewframework.EntityToDmrBridge;
import org.jboss.as.console.client.shared.viewframework.EntityToDmrBridgeImpl;
import org.jboss.as.console.client.shared.viewframework.FrameworkView;
import org.jboss.as.console.client.shared.viewframework.TabbedFormLayoutPanel;
import org.jboss.as.console.client.widgets.forms.ApplicationMetaData;
import org.jboss.ballroom.client.widgets.forms.Form;
import org.jboss.ballroom.client.widgets.forms.FormAdapter;
import org.jboss.ballroom.client.widgets.tables.DefaultCellTable;

/**
 * @author David Bosschaert
 */
public class DomainsView extends AbstractEntityView<SecurityDomain> implements FrameworkView {
    private final EntityToDmrBridgeImpl<SecurityDomain> bridge;

    AuthorizationEditor authorizationEditor;
    private DefaultCellTable<SecurityDomain> table;
    private TabbedFormLayoutPanel tabBottomPanel;
    private SecurityPresenter presenter;

    public DomainsView(ApplicationMetaData propertyMetaData, DispatchAsync dispatcher) {
        super(SecurityDomain.class, propertyMetaData);
        bridge = new EntityToDmrBridgeImpl<SecurityDomain>(propertyMetaData, SecurityDomain.class, this, dispatcher) {

            @Override
            public void onEdit() {
                super.onEdit();
                authorizationEditor.onEdit();
            }

            @Override
            public void onCancel() {
                super.onCancel();
                authorizationEditor.onCancel();
            }

            @Override
            public void onSaveDetails(FormAdapter<SecurityDomain> form) {
                super.onSaveDetails(form);
                authorizationEditor.onSave();
            }
        };
    }

    @Override
    public Widget createWidget() {
        Widget w = createEmbeddableWidget();

        authorizationEditor = new AuthorizationEditor(presenter);
        tabBottomPanel.add(new VerticalPanel(), "ACL");
        tabBottomPanel.add(new VerticalPanel(), "Authentication");
        tabBottomPanel.add(new VerticalPanel(), "Audit");
        tabBottomPanel.add(authorizationEditor.asWidget(), "Authorization");
        tabBottomPanel.add(new VerticalPanel(), "Identity Trust");
        tabBottomPanel.add(new VerticalPanel(), "JSSE");
        tabBottomPanel.add(new VerticalPanel(), "Mapping");
        tabBottomPanel.add(new VerticalPanel(), "Vault");

        table.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                SingleSelectionModel<SecurityDomain> ssm = (SingleSelectionModel<SecurityDomain>) table.getSelectionModel();
                presenter.updateDomainSelection(ssm.getSelectedObject());
            }
        });

        return w;
    }

    @Override
    protected FormAdapter<SecurityDomain> makeEditEntityDetailsForm() {
        tabBottomPanel = new TabbedFormLayoutPanel(beanType, getFormMetaData(), this);
        return tabBottomPanel;
    }

    @Override
    protected EntityToDmrBridge<SecurityDomain> getEntityBridge() {
        return bridge;
    }

    @Override
    protected DefaultCellTable<SecurityDomain> makeEntityTable() {
        table = new DefaultCellTable<SecurityDomain>(5);

        table.addColumn(new Columns.NameColumn(), Columns.NameColumn.LABEL);

        return table;
    }

    @Override
    protected FormAdapter<SecurityDomain> makeAddEntityForm() {
        Form<SecurityDomain> form = new Form(SecurityDomain.class);
        form.setNumColumns(1);
        form.setFields(formMetaData.findAttribute("name").getFormItemForAdd(),
                       formMetaData.findAttribute("cacheType").getFormItemForAdd());
        return form;
    }

    @Override
    protected String getEntityDisplayName() {
        return "Security Domains";
    }

//    @Override
//    public void setEditingEnabled(boolean isEnabled) {
//        super.setEditingEnabled(isEnabled);
//        authorizationEditor.setEditingEnabled(isEnabled);
//    }
//
    void setPresenter(SecurityPresenter presenter) {
        this.presenter = presenter;
    }
}
