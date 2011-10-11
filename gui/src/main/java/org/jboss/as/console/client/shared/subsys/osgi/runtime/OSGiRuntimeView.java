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
package org.jboss.as.console.client.shared.subsys.osgi.runtime;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.inject.Inject;

import org.jboss.as.console.client.shared.dispatch.DispatchAsync;
import org.jboss.as.console.client.shared.dispatch.impl.DMRAction;
import org.jboss.as.console.client.shared.subsys.Baseadress;
import org.jboss.as.console.client.shared.subsys.osgi.runtime.model.Bundle;
import org.jboss.as.console.client.shared.viewframework.AbstractEntityView;
import org.jboss.as.console.client.shared.viewframework.Columns;
import org.jboss.as.console.client.shared.viewframework.DmrCallback;
import org.jboss.as.console.client.shared.viewframework.EntityToDmrBridge;
import org.jboss.as.console.client.shared.viewframework.EntityToDmrBridgeImpl;
import org.jboss.as.console.client.shared.viewframework.FrameworkButton;
import org.jboss.as.console.client.widgets.forms.FormMetaData;
import org.jboss.as.console.client.widgets.forms.PropertyMetaData;
import org.jboss.ballroom.client.widgets.forms.Form;
import org.jboss.ballroom.client.widgets.forms.FormAdapter;
import org.jboss.ballroom.client.widgets.tables.DefaultCellTable;
import org.jboss.dmr.client.ModelDescriptionConstants;
import org.jboss.dmr.client.ModelNode;

/**
 * @author David Bosschaert
 */
public class OSGiRuntimeView extends AbstractEntityView<Bundle> implements OSGiRuntimePresenter.MyView {
    private final FormMetaData formMetaData;
    private final EntityToDmrBridgeImpl<Bundle> bridge;

    @Inject
    public OSGiRuntimeView(PropertyMetaData propertyMetaData, DispatchAsync dispatcher) {
        super(Bundle.class, EnumSet.allOf(FrameworkButton.class));
        formMetaData = propertyMetaData.getBeanMetaData(Bundle.class).getFormMetaData();
        bridge = new EntityToDmrBridgeImpl<Bundle>(propertyMetaData, Bundle.class, this, dispatcher) {
            @Override
            public void loadEntities(String name) {
                nameOfLastEdited = name;

                ModelNode operation = address.asSubresource(Baseadress.get());
                operation.get(ModelDescriptionConstants.OP).set(ModelDescriptionConstants.READ_CHILDREN_RESOURCES_OPERATION);
                operation.get(ModelDescriptionConstants.INCLUDE_RUNTIME).set(true);

                dispatcher.execute(new DMRAction(operation), new DmrCallback() {

                    @Override
                    public void onDmrSuccess(ModelNode response) {
                        List<Bundle> entities = entityAdapter.fromDMRList(response.get(ModelDescriptionConstants.RESULT).asList());
                        // EntityToDmrBridgeImpl.this.entityList = sortEntitties(entities);
                        // set enabled in entities
                        for (Bundle b : entities) {
                            b.setEnabled("ACTIVE".equals(b.getState()));
                        }
                        entityList = entities;
                        view.refresh();
                    }

                    /*
                    private List<T> sortEntitties(List<T> entities) {
                        Collections.sort(entities, entityComparator);
                        return entities;
                    }
                    */
                });
            }
        };
    }

    @Override
    protected FormMetaData getFormMetaData() {
        return formMetaData;
    }

    @Override
    protected EntityToDmrBridge<Bundle> getEntityBridge() {
        return bridge;
    }

    @Override
    protected DefaultCellTable<Bundle> makeEntityTable() {
        DefaultCellTable<Bundle> table = new DefaultCellTable<Bundle>(15);

        table.addColumn(new Columns.NameColumn(), "Bundle ID");
        TextColumn<Bundle> symbolicNameColumn = new TextColumn<Bundle>() {
            @Override
            public String getValue(Bundle record) {
                return record.getSymbolicName();
            }
        };
        table.addColumn(symbolicNameColumn, "Symbolic Name");

        TextColumn<Bundle> versionColumn = new TextColumn<Bundle>() {
            @Override
            public String getValue(Bundle record) {
                return record.getVersion();
            }
        };
        table.addColumn(versionColumn, "Version");

        table.addColumn(new Columns.EnabledColumn(), "Active");

        class MyColumn extends Column<Bundle,Bundle> {
            public MyColumn(Cell<Bundle> cell) {
                super(cell);
            }

            @Override
            public Bundle getValue(Bundle record) {
                return record;
            }
        };
        ActionCell<Bundle> ac = new ActionCell<Bundle>("Start", new ActionCell.Delegate<Bundle>() {
            @Override
            public void execute(Bundle id) {
                // TODO Auto-generated method stub
                System.out.println("Start Execute called; " + id);
            }
        });

        final ActionCell<Bundle> ac2 = new ActionCell<Bundle>("Stop", new ActionCell.Delegate<Bundle>() {
            @Override
            public void execute(Bundle id) {
                // TODO Auto-generated method stub
                System.out.println("Stop Execute called; " + id);
            }
        });
        List<HasCell<Bundle,Bundle>> hasCells = new ArrayList<HasCell<Bundle,Bundle>>();
        hasCells.add(new MyColumn(ac));
        hasCells.add(new MyColumn(ac2));
        CompositeCell cc = new CompositeCell(hasCells);
        MyColumn myColumn = new MyColumn(cc);

        table.addColumn(myColumn, "Action");

        return table;
    }

    @Override
    // TODO remove!
    protected FormAdapter<Bundle> makeAddEntityForm() {
        Form<Bundle> form = new Form<Bundle>(Bundle.class);
        form.setNumColumns(1);
        form.setFields(formMetaData.findAttribute("name").getFormItemForAdd());
        return form;
        // Cannot add a Bundle here
        // return null;
    }

    @Override
    protected String getPluralEntityName() {
        return "Bundles";
    }
}
