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

import com.google.gwt.user.client.ui.Widget;

import org.jboss.as.console.client.shared.dispatch.DispatchAsync;
import org.jboss.as.console.client.shared.subsys.security.model.SecurityDomain;
import org.jboss.as.console.client.shared.viewframework.AbstractEntityView;
import org.jboss.as.console.client.shared.viewframework.Columns;
import org.jboss.as.console.client.shared.viewframework.EntityToDmrBridge;
import org.jboss.as.console.client.shared.viewframework.EntityToDmrBridgeImpl;
import org.jboss.as.console.client.shared.viewframework.FrameworkView;
import org.jboss.as.console.client.widgets.forms.ApplicationMetaData;
import org.jboss.ballroom.client.widgets.forms.Form;
import org.jboss.ballroom.client.widgets.forms.FormAdapter;
import org.jboss.ballroom.client.widgets.tables.DefaultCellTable;

/**
 * @author David Bosschaert
 */
public class DomainsView extends AbstractEntityView<SecurityDomain> implements FrameworkView {
    private final EntityToDmrBridgeImpl<SecurityDomain> bridge;

    public DomainsView(ApplicationMetaData propertyMetaData, DispatchAsync dispatcher) {
        super(SecurityDomain.class, propertyMetaData);
        bridge = new EntityToDmrBridgeImpl<SecurityDomain>(propertyMetaData, SecurityDomain.class, this, dispatcher);
    }

    @Override
    public Widget createWidget() {
        return createEmbeddableWidget();
    }

    @Override
    protected EntityToDmrBridge<SecurityDomain> getEntityBridge() {
        return bridge;
    }

    @Override
    protected DefaultCellTable<SecurityDomain> makeEntityTable() {
        DefaultCellTable<SecurityDomain> table = new DefaultCellTable<SecurityDomain>(5);

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
}
