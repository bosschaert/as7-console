package org.jboss.as.console.client.shared.subsys.ejb3;

import java.util.EnumSet;

import com.google.gwt.user.client.ui.Widget;

import org.jboss.as.console.client.shared.dispatch.DispatchAsync;
import org.jboss.as.console.client.shared.subsys.ejb3.model.StrictMaxBeanPool;
import org.jboss.as.console.client.shared.subsys.ejb3.model.TimerService;
import org.jboss.as.console.client.shared.viewframework.AbstractEntityView;
import org.jboss.as.console.client.shared.viewframework.EntityDetails;
import org.jboss.as.console.client.shared.viewframework.EntityEditor;
import org.jboss.as.console.client.shared.viewframework.EntityToDmrBridge;
import org.jboss.as.console.client.shared.viewframework.FrameworkButton;
import org.jboss.as.console.client.widgets.forms.FormMetaData;
import org.jboss.as.console.client.widgets.forms.PropertyMetaData;
import org.jboss.ballroom.client.widgets.forms.Form;
import org.jboss.ballroom.client.widgets.forms.FormAdapter;
import org.jboss.ballroom.client.widgets.tables.DefaultCellTable;

public class TimerServiceView extends AbstractEntityView<TimerService> {
    private final FormMetaData formMetaData;
    private final EntityToDmrBridge<TimerService> bridge;

    public TimerServiceView(PropertyMetaData propertyMetaData, DispatchAsync dispatcher) {
        super(TimerService.class, EnumSet.of(FrameworkButton.ADD));
        formMetaData = propertyMetaData.getBeanMetaData(TimerService.class).getFormMetaData();
        bridge = new SingleEntityToDmrBridgeImpl<TimerService>(propertyMetaData, TimerService.class, this, dispatcher);
    }

    @Override
    public Widget createWidget() {
        entityEditor = makeEntityEditor();
        return entityEditor.asWidget();
    }

    @Override
    protected EntityEditor<TimerService> makeEntityEditor() {
        EntityDetails<TimerService> details = new EntityDetails<TimerService>(getPluralEntityName(), makeEditEntityDetailsForm(), getEntityBridge(), hideButtons);
        return new EntityEditor<TimerService>(getPluralEntityName(), null, makeEntityTable(), details, hideButtons);
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
        DefaultCellTable<TimerService> table = new DefaultCellTable<TimerService>(5);
//        table.addColumn(new Columns.NameColumn(), Columns.NameColumn.LABEL);
        table.setVisible(false);
        return table;
    }

    @Override
    protected FormAdapter<TimerService> makeAddEntityForm() {
        // TODO delete this!
        Form<TimerService> form = new Form<TimerService>(StrictMaxBeanPool.class);
        form.setNumColumns(1);
        form.setFields(getFormMetaData().findAttribute("path").getFormItemForAdd());
        return form;
    }

    @Override
    protected String getPluralEntityName() {
        return "Timer Service"; // needed?
    }
}
