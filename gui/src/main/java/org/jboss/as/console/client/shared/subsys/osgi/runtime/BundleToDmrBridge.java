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
package org.jboss.as.console.client.shared.subsys.osgi.runtime;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jboss.as.console.client.shared.dispatch.DispatchAsync;
import org.jboss.as.console.client.shared.dispatch.impl.DMRAction;
import org.jboss.as.console.client.shared.subsys.Baseadress;
import org.jboss.as.console.client.shared.subsys.osgi.runtime.model.OSGiBundle;
import org.jboss.as.console.client.shared.viewframework.DmrCallback;
import org.jboss.as.console.client.shared.viewframework.EntityToDmrBridgeImpl;
import org.jboss.as.console.client.shared.viewframework.FrameworkView;
import org.jboss.as.console.client.widgets.forms.PropertyMetaData;
import org.jboss.dmr.client.ModelDescriptionConstants;
import org.jboss.dmr.client.ModelNode;

/**
 * @author David Bosschaert
 */
class BundleToDmrBridge extends EntityToDmrBridgeImpl<OSGiBundle> {
    BundleToDmrBridge(PropertyMetaData propertyMetadata, Class<? extends OSGiBundle> type, FrameworkView view,
            DispatchAsync dispatcher) {
        super(propertyMetadata, type, view, dispatcher);
    }

    @Override
    public void loadEntities(String name) {
        nameOfLastEdited = name;

        ModelNode operation = address.asSubresource(Baseadress.get());
        operation.get(ModelDescriptionConstants.OP).set(ModelDescriptionConstants.READ_CHILDREN_RESOURCES_OPERATION);
        operation.get(ModelDescriptionConstants.INCLUDE_RUNTIME).set(true);

        dispatcher.execute(new DMRAction(operation), new DmrCallback() {
            @Override
            public void onDmrSuccess(ModelNode response) {
                List<OSGiBundle> entities = entityAdapter.fromDMRList(response.get(ModelDescriptionConstants.RESULT).asList());
                Collections.sort(entities, new Comparator<OSGiBundle>() {
                    @Override
                    public int compare(OSGiBundle o1, OSGiBundle o2) {
                        return new Long(o1.getName()).compareTo(new Long(o2.getName()));
                    }
                });
                entityList = entities;
                view.refresh();
            }
        });
    }
}
