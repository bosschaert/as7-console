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
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.dom.client.Style;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import org.jboss.as.console.client.shared.dispatch.DispatchAsync;
import org.jboss.as.console.client.shared.subsys.osgi.runtime.model.OSGiBundle;
import org.jboss.as.console.client.shared.viewframework.AbstractEntityView;
import org.jboss.as.console.client.shared.viewframework.Columns;
import org.jboss.as.console.client.shared.viewframework.EntityToDmrBridge;
import org.jboss.as.console.client.shared.viewframework.EntityToDmrBridgeImpl;
import org.jboss.as.console.client.shared.viewframework.FrameworkButton;
import org.jboss.as.console.client.widgets.forms.PropertyMetaData;
import org.jboss.ballroom.client.widgets.forms.Form;
import org.jboss.ballroom.client.widgets.forms.FormAdapter;
import org.jboss.ballroom.client.widgets.icons.Icons;
import org.jboss.ballroom.client.widgets.tables.DefaultCellTable;

/**
 * @author David Bosschaert
 */
public class OSGiRuntimeView extends AbstractEntityView<OSGiBundle> implements OSGiRuntimePresenter.MyView {
    private final EntityToDmrBridgeImpl<OSGiBundle> bridge;
    private OSGiRuntimePresenter presenter;
    private FrameworkRuntimeView framework;

    @Inject
    public OSGiRuntimeView(PropertyMetaData propertyMetaData, DispatchAsync dispatcher) {
        super(OSGiBundle.class, propertyMetaData, EnumSet.allOf(FrameworkButton.class));
        bridge = new BundleToDmrBridge(propertyMetaData, OSGiBundle.class, this, dispatcher);

        framework = new FrameworkRuntimeView(propertyMetaData, dispatcher);
    }

    @Override
    public Widget createWidget() {
        entityEditor = makeEntityEditor();

        // overall layout
        TabLayoutPanel tabLayoutPanel = new TabLayoutPanel(25, Style.Unit.PX);
        tabLayoutPanel.addStyleName("default-tabpanel");

        Widget entityEditorWidget = entityEditor.asWidget();
        entityEditorWidget.addStyleName("rhs-content-panel");

        tabLayoutPanel.add(framework.asWidget(), "Framework");
        tabLayoutPanel.add(entityEditorWidget, "Bundles");
        tabLayoutPanel.selectTab(1);

        return tabLayoutPanel;
    }

    @Override
    protected EntityToDmrBridge<OSGiBundle> getEntityBridge() {
        return bridge;
    }

    @Override
    protected DefaultCellTable<OSGiBundle> makeEntityTable() {
        DefaultCellTable<OSGiBundle> table = new DefaultCellTable<OSGiBundle>(15);

        table.addColumn(new Columns.NameColumn(), "Bundle ID");
        TextColumn<OSGiBundle> symbolicNameColumn = new TextColumn<OSGiBundle>() {
            @Override
            public String getValue(OSGiBundle record) {
                return record.getSymbolicName();
            }
        };
        table.addColumn(symbolicNameColumn, "Symbolic Name");

        TextColumn<OSGiBundle> versionColumn = new TextColumn<OSGiBundle>() {
            @Override
            public String getValue(OSGiBundle record) {
                return record.getVersion();
            }
        };
        table.addColumn(versionColumn, "Version");

        Column<OSGiBundle, ImageResource> startedColumn = new Column<OSGiBundle, ImageResource>(new ImageResourceCell()) {
            @Override
            public ImageResource getValue(OSGiBundle bundle) {
                if ("ACTIVE".equals(bundle.getState()))
                    return Icons.INSTANCE.statusGreen_small();
                if ("STARTING".equals(bundle.getState()))
                    return Icons.INSTANCE.statusYellow_small();
                if ("RESOLVED".equals(bundle.getState()))
                    return Icons.INSTANCE.statusBlue_small();
                return Icons.INSTANCE.statusRed_small();
            }
        };
        table.addColumn(startedColumn, "State");

        class BundleColumn extends Column<OSGiBundle,OSGiBundle> {
            public BundleColumn(Cell<OSGiBundle> cell) {
                super(cell);
            }

            @Override
            public OSGiBundle getValue(OSGiBundle record) {
                return record;
            }
        };
        ActionCell<OSGiBundle> startCell = new ActionCell<OSGiBundle>("Start", new ActionCell.Delegate<OSGiBundle>() {
            @Override
            public void execute(OSGiBundle bundle) {
                if ("fragment".equals(bundle.getType())) {
                    Window.alert("Can't start a fragment (" + bundle.getSymbolicName() + ").");
                } else {
                    presenter.startBundle(bundle);
                }
            }
        });

        final ActionCell<OSGiBundle> stopCell = new ActionCell<OSGiBundle>("Stop", new ActionCell.Delegate<OSGiBundle>() {
            @Override
            public void execute(OSGiBundle bundle) {
                if ("fragment".equals(bundle.getType())) {
                    Window.alert("Can't stop a fragment (" + bundle.getSymbolicName() + ").");
                } else {
                    presenter.stopBundle(bundle);
                }
            }
        });
        List<HasCell<OSGiBundle,OSGiBundle>> hasCells = new ArrayList<HasCell<OSGiBundle,OSGiBundle>>();
        hasCells.add(new BundleColumn(startCell));
        hasCells.add(new BundleColumn(stopCell));
        BundleColumn myColumn = new BundleColumn(new CompositeCell(hasCells));

        table.addColumn(myColumn, "Action");

        return table;
    }

    @Override
    // TODO remove!
    protected FormAdapter<OSGiBundle> makeAddEntityForm() {
        Form<OSGiBundle> form = new Form<OSGiBundle>(OSGiBundle.class);
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

    @Override
    public void loadFramework() {
        framework.initialLoad();
    }

    @Override
    public void setPresenter(OSGiRuntimePresenter presenter) {
        this.presenter = presenter;
    }
}
