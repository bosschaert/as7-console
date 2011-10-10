package org.jboss.as.console.client.shared.subsys.ejb3;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Command;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.Proxy;

import org.jboss.as.console.client.Console;
import org.jboss.as.console.client.core.NameTokens;
import org.jboss.as.console.client.domain.model.SimpleCallback;
import org.jboss.as.console.client.shared.dispatch.DispatchAsync;
import org.jboss.as.console.client.shared.dispatch.impl.DMRAction;
import org.jboss.as.console.client.shared.dispatch.impl.DMRResponse;
import org.jboss.as.console.client.shared.subsys.Baseadress;
import org.jboss.as.console.client.shared.subsys.RevealStrategy;
import org.jboss.as.console.client.shared.subsys.ejb3.model.StrictMaxBeanPool;
import org.jboss.as.console.client.shared.viewframework.FrameworkView;
import org.jboss.as.console.client.widgets.forms.AddressBinding;
import org.jboss.as.console.client.widgets.forms.BeanMetaData;
import org.jboss.as.console.client.widgets.forms.PropertyMetaData;
import org.jboss.dmr.client.ModelDescriptionConstants;
import org.jboss.dmr.client.ModelNode;

public class EJB3Presenter extends Presenter<EJB3Presenter.MyView, EJB3Presenter.MyProxy>{
    private final DispatchAsync dispatcher;
    private final PropertyMetaData propertyMetaData;
    private final RevealStrategy revealStrategy;
    private final BeanMetaData slsbMetaData;

    @ProxyCodeSplit
    @NameToken(NameTokens.EJB3Presenter)
    public interface MyProxy extends Proxy<EJB3Presenter>, Place {
    }

    public interface MyView extends View, FrameworkView {
        void loadPools();
        void loadTimerService();
        void setPoolNames(List<String> poolNames);
    }

    @Inject
    public EJB3Presenter(EventBus eventBus, MyView view, MyProxy proxy,
        DispatchAsync dispatcher, PropertyMetaData propertyMetaData, RevealStrategy revealStrategy) {
        super(eventBus, view, proxy);

        this.dispatcher = dispatcher;
        this.propertyMetaData = propertyMetaData;
        this.revealStrategy = revealStrategy;
        this.slsbMetaData = propertyMetaData.getBeanMetaData(StrictMaxBeanPool.class);
    }

    @Override
    protected void onReset() {
        super.onReset();
        loadPoolNames();
//        // TODO maybe async?
//        getView().initialLoad();
//        getView().loadPools();
//        getView().loadTimerService();
    }

    @Override
    protected void revealInParent() {
        revealStrategy.revealInParent(this);
    }

    private void loadPoolNames() {
        AddressBinding address = slsbMetaData.getAddress();
        ModelNode operation = address.asSubresource(Baseadress.get());
        operation.get(ModelDescriptionConstants.OP).set(ModelDescriptionConstants.READ_CHILDREN_NAMES_OPERATION);

        dispatcher.execute(new DMRAction(operation), new SimpleCallback<DMRResponse>() {
            @Override
            public void onSuccess(DMRResponse result) {
                ModelNode response = ModelNode.fromBase64(result.getResponseText());
                ModelNode res = response.get(ModelDescriptionConstants.RESULT);
                List<String> poolNames = new ArrayList<String>();
                for (ModelNode n : res.asList()) {
                    poolNames.add(n.asString());
                }
                getView().setPoolNames(poolNames);
                getView().initialLoad();

                // Load these async to speed things up
                Console.schedule(new Command() {
                    @Override
                    public void execute() {
                        getView().loadPools();

                        Console.schedule(new Command() {
                            @Override
                            public void execute() {
                                getView().loadTimerService();
                            }
                        });
                    }
                });
            }
        });
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
