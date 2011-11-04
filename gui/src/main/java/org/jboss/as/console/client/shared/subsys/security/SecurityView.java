package org.jboss.as.console.client.shared.subsys.security;

import java.util.EnumSet;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import org.jboss.as.console.client.shared.dispatch.DispatchAsync;
import org.jboss.as.console.client.shared.subsys.security.model.SecuritySubsystem;
import org.jboss.as.console.client.shared.viewframework.AbstractEntityView;
import org.jboss.as.console.client.shared.viewframework.EntityDetails;
import org.jboss.as.console.client.shared.viewframework.EntityEditor;
import org.jboss.as.console.client.shared.viewframework.EntityToDmrBridge;
import org.jboss.as.console.client.shared.viewframework.FrameworkButton;
import org.jboss.as.console.client.shared.viewframework.SingleEntityToDmrBridgeImpl;
import org.jboss.as.console.client.widgets.forms.ApplicationMetaData;
import org.jboss.ballroom.client.widgets.forms.FormAdapter;
import org.jboss.ballroom.client.widgets.tables.DefaultCellTable;

public class SecurityView extends AbstractEntityView<SecuritySubsystem> implements SecurityPresenter.MyView {
    private final SingleEntityToDmrBridgeImpl<SecuritySubsystem> bridge;

    @Inject
    public SecurityView(ApplicationMetaData propertyMetaData, DispatchAsync dispatcher) {
        super(SecuritySubsystem.class, propertyMetaData, EnumSet.of(FrameworkButton.ADD, FrameworkButton.REMOVE));
        bridge = new SingleEntityToDmrBridgeImpl<SecuritySubsystem>(propertyMetaData, SecuritySubsystem.class, this, dispatcher);
    }

    @Override
    public Widget createWidget() {
        TabLayoutPanel tabLayoutPanel = new TabLayoutPanel(25, Style.Unit.PX);
        tabLayoutPanel.addStyleName("default-tabpabel");

        tabLayoutPanel.add(createEmbeddableWidget(), getEntityDisplayName());
        tabLayoutPanel.selectTab(0);

        return tabLayoutPanel;
    }

    @Override
    protected EntityEditor<SecuritySubsystem> makeEntityEditor() {
        EntityDetails<SecuritySubsystem> details = new EntityDetails<SecuritySubsystem>(getEntityDisplayName(),
                                                                                        makeEditEntityDetailsForm(),
                                                                                        getEntityBridge(),
                                                                                        getAddress(),
                                                                                        hideButtons);
        return new EntityEditor<SecuritySubsystem>(getEntityDisplayName(), null, makeEntityTable(), details, hideButtons);
    }

    @Override
    protected EntityToDmrBridge<SecuritySubsystem> getEntityBridge() {
        return bridge;
    }

    @Override
    protected DefaultCellTable<SecuritySubsystem> makeEntityTable() {
        DefaultCellTable<SecuritySubsystem> table = new DefaultCellTable<SecuritySubsystem>(5);
        table.setVisible(false);
        return table;
    }

    @Override
    protected FormAdapter<SecuritySubsystem> makeAddEntityForm() {
        return null;
    }

    @Override
    protected String getEntityDisplayName() {
        return "Security";
    }
}
