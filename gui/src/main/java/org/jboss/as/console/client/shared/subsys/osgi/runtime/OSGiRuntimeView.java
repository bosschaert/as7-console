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

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Widget;

import org.jboss.as.console.client.core.SuspendableViewImpl;

/**
 * @author David Bosschaert
 */
public class OSGiRuntimeView extends SuspendableViewImpl implements OSGiRuntimePresenter.MyView {
    private Frame frame;

    @Override
    public Widget createWidget()
    {
        frame = new Frame();
        frame.setWidth("100%");
        frame.setHeight("100%");
        return frame;
    }

    @Override
    public void initFrame(int port, String rootContext) {
        String url = "http://" + Window.Location.getHostName() + ":" + port + "/" + rootContext;
        frame.setUrl(url);
    }

    @Override
    public void notConfigured() {
        // show not configured
    }
}
