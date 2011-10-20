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
package org.jboss.as.console.client.shared.subsys.ejb3;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import org.jboss.as.console.client.core.SuspendableViewImpl;
import org.jboss.as.console.client.shared.dispatch.DispatchAsync;
import org.jboss.as.console.client.widgets.forms.PropertyMetaData;
import org.jboss.ballroom.client.widgets.ContentGroupLabel;

/**
 * @author David Bosschaert
 */
public class ServicesView extends SuspendableViewImpl {
    private final TimerServiceView timerServiceView;

    public ServicesView(PropertyMetaData propertyMetaData, DispatchAsync dispatcher) {
        timerServiceView = new TimerServiceView(propertyMetaData, dispatcher);
    }

    @Override
    public Widget createWidget() {
        VerticalPanel vpanel = new VerticalPanel();
        vpanel.setStyleName("rhs-content-panel");
        vpanel.add(new ContentGroupLabel("EJB Services"));

        TabPanel bottomPanel = new TabPanel();
        bottomPanel.setStyleName("default-tabpanel");

        VerticalPanel p1 = new VerticalPanel();
        p1.add(timerServiceView.asWidget());
        VerticalPanel p2 = new VerticalPanel();
        p2.add(new HTML("Hello there<p/>hi<p/>ho"));

        bottomPanel.add(p1, timerServiceView.getEntityDisplayName());
        bottomPanel.add(p2, "Two");
        bottomPanel.selectTab(0);

        vpanel.add(bottomPanel);

        return vpanel;
    }

    public void initialLoad() {
        timerServiceView.initialLoad();
    }
}
