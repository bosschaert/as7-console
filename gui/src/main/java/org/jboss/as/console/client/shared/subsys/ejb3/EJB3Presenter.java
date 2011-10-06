package org.jboss.as.console.client.shared.subsys.ejb3;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.Proxy;

import org.jboss.as.console.client.core.NameTokens;
import org.jboss.as.console.client.shared.subsys.RevealStrategy;
import org.jboss.as.console.client.shared.viewframework.FrameworkView;

public class EJB3Presenter extends Presenter<EJB3Presenter.MyView, EJB3Presenter.MyProxy>{
    private RevealStrategy revealStrategy;

    @ProxyCodeSplit
    @NameToken(NameTokens.EJB3Presenter)
    public interface MyProxy extends Proxy<EJB3Presenter>, Place {
    }

    public interface MyView extends FrameworkView {
    }

    @Inject
    public EJB3Presenter(EventBus eventBus, MyView view, MyProxy proxy, RevealStrategy revealStrategy) {
        super(eventBus, view, proxy);

        this.revealStrategy = revealStrategy;
    }

    @Override
    protected void onReset() {
        super.onReset();
        getView().initialLoad();
    }

    @Override
    protected void revealInParent() {
        revealStrategy.revealInParent(this);
    }
}
