package org.jboss.as.console.client.shared.subsys.ejb3;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.Proxy;

import org.jboss.as.console.client.core.NameTokens;
import org.jboss.as.console.client.shared.dispatch.DispatchAsync;
import org.jboss.as.console.client.shared.subsys.RevealStrategy;
import org.jboss.as.console.client.shared.subsys.ejb3.model.EJB3Subsystem;
import org.jboss.as.console.client.shared.viewframework.FrameworkView;
import org.jboss.as.console.client.widgets.forms.BeanMetaData;
import org.jboss.as.console.client.widgets.forms.PropertyMetaData;

public class EJB3Presenter extends Presenter<EJB3Presenter.MyView, EJB3Presenter.MyProxy>{
    private final BeanMetaData ejb3MetaData;
    private final DispatchAsync dispatcher;
    private final PropertyMetaData propertyMetaData;
    private final RevealStrategy revealStrategy;

    @ProxyCodeSplit
    @NameToken(NameTokens.EJB3Presenter)
    public interface MyProxy extends Proxy<EJB3Presenter>, Place {
    }

    public interface MyView extends View, FrameworkView {
        void loadPools();
        void loadTimerService();
        void setPresenter(EJB3Presenter ejb3Presenter);
    }

    @Inject
    public EJB3Presenter(EventBus eventBus, MyView view, MyProxy proxy,
        DispatchAsync dispatcher, PropertyMetaData propertyMetaData, RevealStrategy revealStrategy) {
        super(eventBus, view, proxy);

        this.dispatcher = dispatcher;
        this.propertyMetaData = propertyMetaData;
        this.revealStrategy = revealStrategy;
        this.ejb3MetaData = propertyMetaData.getBeanMetaData(EJB3Subsystem.class);
    }

    protected void onBind() {
        super.onBind();
        getView().setPresenter(this);
    }

    @Override
    protected void onReset() {
        super.onReset();
        getView().initialLoad();
        // TODO maybe async?
        getView().loadPools();
        getView().loadTimerService();
    }

    @Override
    protected void revealInParent() {
        revealStrategy.revealInParent(this);
    }

    public String[] getPoolNames() {
        return new String [] {"mdb-strict-max-pool", "slsb-strict-max-pool"};
    }

    /*
    private void loadSubsystem() {
        AddressBinding address = ejb3MetaData.getAddress();
        ModelNode operation = address.asResource(Baseadress.get());
        operation.get(ModelDescriptionConstants.OP).set(ModelDescriptionConstants.READ_RESOURCE_OPERATION);

        dispatcher.execute(new DMRAction(operation), new SimpleCallback<DMRResponse>() {
            @Override
            public void onSuccess(DMRResponse result) {
                ModelNode response = ModelNode.fromBase64(result.getResponseText());
                EntityAdapter<EJB3Subsystem> adapter = new EntityAdapter<EJB3Subsystem>(EJB3Subsystem.class, propertyMetaData);
                / *
                .with(new KeyAssignment() {
                    @Override
                    public Object valueForKey(String key) {
                        return name;
                    }
                }); * /
                EJB3Subsystem subsystem = adapter.fromDMR(response.get(ModelDescriptionConstants.RESULT));
                System.out.println("***  " + subsystem);
                getView().updateSubsystem(subsystem);
            }
        });
    } */
}
