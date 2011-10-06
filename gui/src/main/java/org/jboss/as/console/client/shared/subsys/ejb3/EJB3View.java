package org.jboss.as.console.client.shared.subsys.ejb3;

import com.google.inject.Inject;

import org.jboss.as.console.client.shared.dispatch.DispatchAsync;
import org.jboss.as.console.client.shared.subsys.ejb3.model.Pool;
import org.jboss.as.console.client.shared.viewframework.AbstractEntityView;
import org.jboss.as.console.client.shared.viewframework.Columns;
import org.jboss.as.console.client.shared.viewframework.EntityToDmrBridge;
import org.jboss.as.console.client.shared.viewframework.EntityToDmrBridgeImpl;
import org.jboss.as.console.client.widgets.forms.FormMetaData;
import org.jboss.as.console.client.widgets.forms.PropertyMetaData;
import org.jboss.ballroom.client.widgets.forms.Form;
import org.jboss.ballroom.client.widgets.forms.FormAdapter;
import org.jboss.ballroom.client.widgets.tables.DefaultCellTable;

public class EJB3View extends AbstractEntityView<Pool> implements EJB3Presenter.MyView {
    private final FormMetaData formMetaData;
    private final EntityToDmrBridge<Pool> bridge;

    @Inject
    public EJB3View(PropertyMetaData propertyMetaData, DispatchAsync dispatcher) {
        super(Pool.class);
        formMetaData = propertyMetaData.getBeanMetaData(Pool.class).getFormMetaData();
        bridge = new EntityToDmrBridgeImpl<Pool>(propertyMetaData, Pool.class, this, dispatcher);
    }

    @Override
    protected FormMetaData getFormMetaData() {
        return formMetaData;
    }

    @Override
    protected FormAdapter<Pool> makeAddEntityForm() {
        Form<Pool> form = new Form<Pool>(Pool.class);
        form.setNumColumns(1);
        form.setFields(getFormMetaData().findAttribute("name").getFormItemForAdd());
        return form;
    }

    @Override
    protected EntityToDmrBridge<Pool> getEntityBridge() {
        return bridge;
    }

    @Override
    protected DefaultCellTable<Pool> makeEntityTable() {
        DefaultCellTable<Pool> table = new DefaultCellTable<Pool>(10);

        table.addColumn(new Columns.NameColumn(), Columns.NameColumn.LABEL);

        return table;
    }

    @Override
    protected String getPluralEntityName() {
        return "EJB Pools"; // TODO i18n
    }
}
