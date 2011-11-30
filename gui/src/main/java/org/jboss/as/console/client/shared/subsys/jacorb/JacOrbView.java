package org.jboss.as.console.client.shared.subsys.jacorb;

import java.util.EnumSet;

import com.google.inject.Inject;

import org.jboss.as.console.client.shared.dispatch.DispatchAsync;
import org.jboss.as.console.client.shared.subsys.jacorb.model.JacOrbSubsystem;
import org.jboss.as.console.client.shared.viewframework.AbstractEntityView;
import org.jboss.as.console.client.shared.viewframework.EntityToDmrBridge;
import org.jboss.as.console.client.shared.viewframework.FrameworkButton;
import org.jboss.as.console.client.shared.viewframework.FrameworkView;
import org.jboss.as.console.client.shared.viewframework.SingleEntityToDmrBridgeImpl;
import org.jboss.as.console.client.widgets.forms.ApplicationMetaData;
import org.jboss.ballroom.client.widgets.forms.Form;
import org.jboss.ballroom.client.widgets.forms.FormAdapter;
import org.jboss.ballroom.client.widgets.tables.DefaultCellTable;

public class JacOrbView extends AbstractEntityView<JacOrbSubsystem> implements JacOrbPresenter.MyView, FrameworkView {
    private final EntityToDmrBridge<JacOrbSubsystem> bridge;

    @Inject
    public JacOrbView(ApplicationMetaData applicationMetaData, DispatchAsync dispatcher) {
        super(JacOrbSubsystem.class, applicationMetaData, EnumSet.allOf(FrameworkButton.class));
        bridge = new SingleEntityToDmrBridgeImpl<JacOrbSubsystem>(applicationMetaData, JacOrbSubsystem.class, this, dispatcher);
    }

    @Override
    public EntityToDmrBridge<JacOrbSubsystem> getEntityBridge() {
        return bridge;
    }

    @Override
    protected DefaultCellTable<JacOrbSubsystem> makeEntityTable() {
        DefaultCellTable<JacOrbSubsystem> table = new DefaultCellTable<JacOrbSubsystem>(5);
        table.setVisible(false);
        return table;
    }

    @Override
    protected FormAdapter<JacOrbSubsystem> makeAddEntityForm() {
        return new Form<JacOrbSubsystem>(beanType);
    }

    @Override
    protected String getEntityDisplayName() {
        return "JacORB";
    }
}
