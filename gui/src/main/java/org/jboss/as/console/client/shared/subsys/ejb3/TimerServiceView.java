package org.jboss.as.console.client.shared.subsys.ejb3;

import com.google.gwt.user.client.ui.Widget;

import org.jboss.as.console.client.shared.dispatch.DispatchAsync;
import org.jboss.as.console.client.shared.subsys.ejb3.model.TimerService;
import org.jboss.as.console.client.shared.viewframework.AbstractEntityView;
import org.jboss.as.console.client.shared.viewframework.EntityToDmrBridge;
import org.jboss.as.console.client.shared.viewframework.EntityToDmrBridgeImpl;
import org.jboss.as.console.client.widgets.forms.FormMetaData;
import org.jboss.as.console.client.widgets.forms.PropertyMetaData;
import org.jboss.ballroom.client.widgets.forms.FormAdapter;
import org.jboss.ballroom.client.widgets.tables.DefaultCellTable;

public class TimerServiceView extends AbstractEntityView<TimerService>{
    private final FormMetaData formMetaData;
    private final EntityToDmrBridgeImpl<TimerService> bridge;

    public TimerServiceView(PropertyMetaData propertyMetaData, DispatchAsync dispatcher) {
        super(TimerService.class);
        formMetaData = propertyMetaData.getBeanMetaData(TimerService.class).getFormMetaData();
        bridge = new EntityToDmrBridgeImpl<TimerService>(propertyMetaData, TimerService.class, this, dispatcher);
    }

    @Override
    public Widget createWidget() {
        FormAdapter<TimerService> details = makeEditEntityDetailsForm();
        return details.asWidget();
    }

    @Override
    protected FormMetaData getFormMetaData() {
        return formMetaData;
    }

    @Override
    protected EntityToDmrBridge<TimerService> getEntityBridge() {
        return bridge;
    }

    @Override
    protected DefaultCellTable<TimerService> makeEntityTable() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected FormAdapter<TimerService> makeAddEntityForm() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String getPluralEntityName() {
        return "Timer Service"; // needed?
    }
}
