package org.jboss.as.console.client.shared.subsys.ejb3;

import java.util.Collections;
import java.util.List;

import org.jboss.as.console.client.shared.dispatch.DispatchAsync;
import org.jboss.as.console.client.shared.dispatch.impl.DMRAction;
import org.jboss.as.console.client.shared.subsys.Baseadress;
import org.jboss.as.console.client.shared.viewframework.DmrCallback;
import org.jboss.as.console.client.shared.viewframework.EntityToDmrBridge;
import org.jboss.as.console.client.shared.viewframework.FrameworkView;
import org.jboss.as.console.client.widgets.forms.AddressBinding;
import org.jboss.as.console.client.widgets.forms.BeanMetaData;
import org.jboss.as.console.client.widgets.forms.EntityAdapter;
import org.jboss.as.console.client.widgets.forms.FormMetaData;
import org.jboss.as.console.client.widgets.forms.PropertyMetaData;
import org.jboss.ballroom.client.widgets.forms.FormAdapter;
import org.jboss.dmr.client.ModelDescriptionConstants;
import org.jboss.dmr.client.ModelNode;

public class SingleEntityToDmrBridgeImpl<T> implements EntityToDmrBridge<T> {
    protected final AddressBinding address;
    protected final FormMetaData attributes;
    protected final DispatchAsync dispatcher;
    protected final EntityAdapter<T> entityAdapter;
    protected final PropertyMetaData propertyMetaData;
    protected final Class<? extends T> type;
    protected final FrameworkView view;
    protected T entity;

    public SingleEntityToDmrBridgeImpl(PropertyMetaData propertyMetaData, Class<? extends T> type,
        FrameworkView view, DispatchAsync dispatcher) {
        BeanMetaData beanMetaData = propertyMetaData.getBeanMetaData(type);

        this.propertyMetaData = propertyMetaData;
        this.address = beanMetaData.getAddress();
        this.attributes = beanMetaData.getFormMetaData();
        this.entityAdapter = new EntityAdapter<T>(type, propertyMetaData);
        this.type = type;
        this.view = view;
        this.dispatcher = dispatcher;
    }

    @Override
    public FormMetaData getEntityAttributes() {
        return attributes;
    }

    @Override
    public void loadEntities(String nameEditedOrAdded) {
        ModelNode operation = address.asResource(Baseadress.get());
        operation.get(ModelDescriptionConstants.OP).set(ModelDescriptionConstants.READ_RESOURCE_OPERATION);

        dispatcher.execute(new DMRAction(operation), new DmrCallback() {
            @Override
            public void onDmrSuccess(ModelNode response) {
                entity = entityAdapter.fromDMR(response.get(ModelDescriptionConstants.RESULT));
                view.refresh();
            }
        });
    }

    @Override
    public String getNameOfLastEdited() {
        return type.getName();
    }

    @Override
    public List<T> getEntityList() {
        return Collections.singletonList(entity);
    }

    @Override
    public T findEntity(String name) {
        return entity;
    }

    @Override
    public void onAdd(FormAdapter<T> form) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onEdit() {
        view.setEditingEnabled(true);
    }

    @Override
    public void onSaveDetails(FormAdapter<T> form) {
        view.setEditingEnabled(false);
        // TODO
    }

    @Override
    public void onRemove(FormAdapter<T> form) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getName(T entity) {
        return type.getName();
    }

    @Override
    public T newEntity() {
        throw new UnsupportedOperationException();
    }
}
