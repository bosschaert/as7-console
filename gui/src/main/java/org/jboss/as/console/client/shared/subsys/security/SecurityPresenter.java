/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.jboss.as.console.client.shared.subsys.security;

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

import org.jboss.as.console.client.core.NameTokens;
import org.jboss.as.console.client.domain.model.SimpleCallback;
import org.jboss.as.console.client.shared.BeanFactory;
import org.jboss.as.console.client.shared.dispatch.DispatchAsync;
import org.jboss.as.console.client.shared.dispatch.impl.DMRAction;
import org.jboss.as.console.client.shared.dispatch.impl.DMRResponse;
import org.jboss.as.console.client.shared.dispatch.impl.SimpleDMRResponseHandler;
import org.jboss.as.console.client.shared.subsys.Baseadress;
import org.jboss.as.console.client.shared.subsys.RevealStrategy;
import org.jboss.as.console.client.shared.subsys.security.model.AbstractAuthData;
import org.jboss.as.console.client.shared.subsys.security.model.AuthenticationLoginModule;
import org.jboss.as.console.client.shared.subsys.security.model.AuthorizationPolicyProvider;
import org.jboss.as.console.client.shared.subsys.security.model.SecurityDomain;
import org.jboss.as.console.client.shared.viewframework.FrameworkView;
import org.jboss.dmr.client.ModelDescriptionConstants;
import org.jboss.dmr.client.ModelNode;

/**
 * @author David Bosschaert
 */
public class SecurityPresenter extends Presenter<SecurityPresenter.MyView, SecurityPresenter.MyProxy> {
    public static final String SECURITY_SUBSYSTEM = "security";

    private final DispatchAsync dispatcher;
    private final BeanFactory factory;
    private final RevealStrategy revealStrategy;

    @ProxyCodeSplit
    @NameToken(NameTokens.SecurityPresenter)
    public interface MyProxy extends Proxy<SecurityPresenter>, Place {
    }

    public interface MyView extends View, FrameworkView {
        void setPresenter(SecurityPresenter presenter);
        void setAuthenticationLoginModules(String domainName, List<AuthenticationLoginModule> modules, boolean resourceExists);
        void setAuthorizationPolicyProviders(String domainName, List<AuthorizationPolicyProvider> providers, boolean resourceExists);

        void loadSecurityDomain(String domainName);
    }

    @Inject
    public SecurityPresenter(EventBus eventBus, MyView view, MyProxy proxy,
        DispatchAsync dispatcher, BeanFactory factory, RevealStrategy revealStrategy) {
        super(eventBus, view, proxy);

        this.dispatcher = dispatcher;
        this.factory = factory;
        this.revealStrategy = revealStrategy;
    }

    @Override
    protected void onBind() {
        super.onBind();
        getView().setPresenter(this);
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

    public void updateDomainSelection(final SecurityDomain domain) {
        // load sub-elements which are not automatically loaded by the framework

        loadAuth(domain, "authorization", "policy-modules", AuthorizationPolicyProvider.class);
        loadAuth(domain, "authentication", "login-modules", AuthenticationLoginModule.class);
    }

    private <T extends AbstractAuthData> void loadAuth(final SecurityDomain domain, String type, final String attrName, final Class<T> cls) {
        ModelNode operation = createOperation(ModelDescriptionConstants.READ_RESOURCE_OPERATION);
        operation.get(ModelDescriptionConstants.ADDRESS).add("security-domain", domain.getName());
        operation.get(ModelDescriptionConstants.ADDRESS).add(type, "classic");

        dispatcher.execute(new DMRAction(operation), new SimpleCallback<DMRResponse>() {
            @Override
            public void onSuccess(DMRResponse result) {
                ModelNode response = ModelNode.fromBase64(result.getResponseText());
                ModelNode model = response.get(ModelDescriptionConstants.RESULT);

                List<T> modules = new ArrayList<T>();
                boolean resourceExists = model.hasDefined(attrName);
                if (resourceExists) {
                    for (ModelNode node : model.get(attrName).asList()) {
                        T pm = factory.create(cls).as();

                        pm.setCode(node.get("code").asString());
                        pm.setFlag(node.get("flag").asString());

                        modules.add(pm);
                    }
                }

                if (AuthorizationPolicyProvider.class.equals(cls)) {
                    getView().setAuthorizationPolicyProviders(domain.getName(), (List<AuthorizationPolicyProvider>) modules, resourceExists);
                } else if (AuthenticationLoginModule.class.equals(cls)) {
                    getView().setAuthenticationLoginModules(domain.getName(), (List<AuthenticationLoginModule>) modules, resourceExists);
                }
            }
        });
    }

    public void saveAuthorization(String domainName, List<AuthorizationPolicyProvider> list, boolean resourceExists) {
        saveAuth(domainName, list, "authorization", "policy-modules", resourceExists);
    }

    public void saveAuthentication(String domainName, List<AuthenticationLoginModule> list, boolean resourceExists) {
        saveAuth(domainName, list, "authentication", "login-modules", resourceExists);
    }

    public <T extends AbstractAuthData> void saveAuth(final String domainName, List<T> list, String type, String attrName, boolean resourceExists) {
        if (list.size() == 0)
            return;

        ModelNode operation = null;

        ModelNode valueList = new ModelNode();
        valueList.setEmptyList();
        for (T pm : list) {
            ModelNode n = new ModelNode();
            n.get("code").set(pm.getCode());
            n.get("flag").set(pm.getFlag());
            valueList.add(n);
        }

        if (resourceExists) {
            operation = createOperation(ModelDescriptionConstants.WRITE_ATTRIBUTE_OPERATION);
            operation.get(ModelDescriptionConstants.ADDRESS).add("security-domain", domainName);
            operation.get(ModelDescriptionConstants.ADDRESS).add(type, "classic");
            operation.get(ModelDescriptionConstants.NAME).set(attrName);

            operation.get("value").set(valueList);
        } else {
            operation = createOperation(ModelDescriptionConstants.ADD);
            operation.get(ModelDescriptionConstants.ADDRESS).add("security-domain", domainName);
            operation.get(ModelDescriptionConstants.ADDRESS).add(type, "classic");
            operation.get(attrName).set(valueList);
        }

        dispatcher.execute(new DMRAction(operation), new SimpleDMRResponseHandler(ModelDescriptionConstants.WRITE_ATTRIBUTE_OPERATION,
            attrName, domainName, new Command() {
                @Override
                public void execute() {
                    getView().loadSecurityDomain(domainName);
                }
            }));
    }

    private ModelNode createOperation(String operator) {
        ModelNode operation = new ModelNode();
        operation.get(ModelDescriptionConstants.OP).set(operator);
        operation.get(ModelDescriptionConstants.ADDRESS).set(Baseadress.get());
        operation.get(ModelDescriptionConstants.ADDRESS).add(ModelDescriptionConstants.SUBSYSTEM, SECURITY_SUBSYSTEM);
        return operation;
    }
}
