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
package org.jboss.as.console.client.shared.subsys.jacorb.model;

import org.jboss.as.console.client.shared.viewframework.NamedEntity;
import org.jboss.as.console.client.widgets.forms.Address;
import org.jboss.as.console.client.widgets.forms.Binding;
import org.jboss.as.console.client.widgets.forms.FormItem;

/**
 * @author David Bosschaert
 */
@Address("/subsystem=jacorb")
public interface JacOrbSubsystem extends NamedEntity {
    @Override
    @Binding(detypedName="name", key=true)
    @FormItem(defaultValue="",
              localLabel="common_label_name",
              required=true,
              formItemTypeForEdit="TEXT",
              formItemTypeForAdd="TEXT_BOX")
    public String getName();
    @Override
    public void setName(String name);

    @Binding(detypedName="print-version")
    @FormItem(defaultValue="off",
              label="Print Version")
    public String getPrintVersion();
    public void setPrintVersion(String value);

    @Binding(detypedName="use-imr")
    @FormItem(defaultValue="off",
              label="Use IMR")
    public String getUseIMR();
    public void setUseIMR(String value);

    @Binding(detypedName="use-bom")
    @FormItem(defaultValue="off",
              label="Use GIOP 1.2 BOMs")
    public String getUseBOM();
    public void setUseBOM(String value);

    @Binding(detypedName="cache-typecodes")
    @FormItem(defaultValue="off",
              label="Cache Typecodes")
    public String getCacheTypecodes();
    public void setCacheTypecodes(String value);

    @Binding(detypedName="cache-poa-names")
    @FormItem(defaultValue="off",
              label="Cache POA Names")
    public String getCachePOANames();
    public void setCachePOANames(String value);

    @Binding(detypedName="giop-minor-version")
    @FormItem(defaultValue="2",
              label="GIOP Minor Version",
              formItemTypeForAdd="NUMBER_BOX",
              formItemTypeForEdit="NUMBER_BOX")
    public int getGiopMinorVersion();
    public void setGiopMinorVersion(int value);

    @Binding(detypedName="retries")
    @FormItem(defaultValue="5",
              label="Retries",
              formItemTypeForAdd="NUMBER_BOX",
              formItemTypeForEdit="NUMBER_BOX")
    public int getRetries();
    public void setRetries(int value);

    @Binding(detypedName="retry-interval")
    @FormItem(defaultValue="500",
              label="Retry Interval",
              formItemTypeForAdd="NUMBER_BOX",
              formItemTypeForEdit="NUMBER_BOX")
    public int getRetryInterval();
    public void setRetryInterval(int value);

    @Binding(detypedName="client-timeout")
    @FormItem(defaultValue="0",
              label="Client Timeout",
              formItemTypeForAdd="NUMBER_BOX",
              formItemTypeForEdit="NUMBER_BOX")
    public int getClientTimeout();
    public void setClientTimeout(int value);

    @Binding(detypedName="server-timeout")
    @FormItem(defaultValue="0",
              label="Server Timeout",
              formItemTypeForAdd="NUMBER_BOX",
              formItemTypeForEdit="NUMBER_BOX")
    public int getServerTimeout();
    public void setServerTimeout(int value);

    @Binding(detypedName="max-server-connections")
    @FormItem(defaultValue="2147483647",
              label="Maximum Server Connections",
              formItemTypeForAdd="NUMBER_BOX",
              formItemTypeForEdit="NUMBER_BOX")
    public int getMaxServerConnections();
    public void setMaxServerConnections(int value);

    @Binding(detypedName="max-managed-buf-size")
    @FormItem(defaultValue="24",
              label="Maximum Buffer Size",
              formItemTypeForAdd="NUMBER_BOX",
              formItemTypeForEdit="NUMBER_BOX")
    public int getMaxManagedBufSize();
    public void setMaxManagedBufSize(int value);

    @Binding(detypedName="outbuf-size")
    @FormItem(defaultValue="2048",
              label="Outgoing Buffer Size",
              formItemTypeForAdd="NUMBER_BOX",
              formItemTypeForEdit="NUMBER_BOX")
    public int getOutbufSize();
    public void setOutbufSize(int value);
}
