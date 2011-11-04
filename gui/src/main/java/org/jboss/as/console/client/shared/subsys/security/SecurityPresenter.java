package org.jboss.as.console.client.shared.subsys.security;

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
import org.jboss.as.console.client.shared.viewframework.FrameworkView;

public class SecurityPresenter extends Presenter<SecurityPresenter.MyView, SecurityPresenter.MyProxy> {
    private final RevealStrategy revealStrategy;

    @ProxyCodeSplit
    @NameToken(NameTokens.SecurityPresenter)
    public interface MyProxy extends Proxy<SecurityPresenter>, Place {
    }

    public interface MyView extends View, FrameworkView {
    }

    @Inject
    public SecurityPresenter(EventBus eventBus, MyView view, MyProxy proxy,
        DispatchAsync dispatcher, RevealStrategy revealStrategy) {
        super(eventBus, view, proxy);

        this.revealStrategy = revealStrategy;
    }

    @Override
    protected void revealInParent() {
        revealStrategy.revealInParent(this);
    }
}
