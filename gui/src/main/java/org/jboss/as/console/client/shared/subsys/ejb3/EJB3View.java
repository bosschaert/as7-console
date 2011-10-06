package org.jboss.as.console.client.shared.subsys.ejb3;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import org.jboss.as.console.client.shared.dispatch.DispatchAsync;
import org.jboss.as.console.client.shared.subsys.ejb3.model.Pool;
import org.jboss.as.console.client.shared.viewframework.AbstractEntityView;
import org.jboss.as.console.client.shared.viewframework.Columns;
import org.jboss.as.console.client.shared.viewframework.EntityToDmrBridge;
import org.jboss.as.console.client.shared.viewframework.EntityToDmrBridgeImpl;
import org.jboss.as.console.client.widgets.forms.FormMetaData;
import org.jboss.as.console.client.widgets.forms.PropertyMetaData;
import org.jboss.ballroom.client.widgets.ContentGroupLabel;
import org.jboss.ballroom.client.widgets.ContentHeaderLabel;
import org.jboss.ballroom.client.widgets.forms.Form;
import org.jboss.ballroom.client.widgets.forms.FormAdapter;
import org.jboss.ballroom.client.widgets.tables.DefaultCellTable;
import org.jboss.ballroom.client.widgets.tools.ToolStrip;

public class EJB3View extends AbstractEntityView<Pool> implements EJB3Presenter.MyView {
    private final FormMetaData formMetaData;
    private final EntityToDmrBridge<Pool> bridge;

    @Inject
    public EJB3View(PropertyMetaData propertyMetaData, DispatchAsync dispatcher) {
        super(Pool.class);
        formMetaData = propertyMetaData.getBeanMetaData(Pool.class).getFormMetaData();
        bridge = new EntityToDmrBridgeImpl<Pool>(propertyMetaData, Pool.class, this, dispatcher);
    }



    @Override
    public Widget createWidget() {
        LayoutPanel layout = new LayoutPanel();
        ScrollPanel scroll = new ScrollPanel();
        VerticalPanel vpanel = new VerticalPanel();
        vpanel.setStyleName("rhs-content-panel");
        scroll.add(vpanel);
        // Add an empty toolstrip to make this panel look similar to others
        ToolStrip toolStrip = new ToolStrip();
        layout.add(toolStrip);

        layout.add(scroll);
        layout.setWidgetTopHeight(toolStrip, 0, Style.Unit.PX, 26, Style.Unit.PX);
        layout.setWidgetTopHeight(scroll, 26, Style.Unit.PX, 100, Style.Unit.PCT);

        vpanel.add(new ContentHeaderLabel("Default Pools"));
        vpanel.add(new ContentGroupLabel("Stateless Session Beans"));
        vpanel.add(new ContentGroupLabel("Message Driven Beans"));

        entityEditor = makeEntityEditor();
        entityEditor.asWidget(vpanel);

        TabLayoutPanel tabLayoutPanel = new TabLayoutPanel(25, Style.Unit.PX);
        tabLayoutPanel.addStyleName("default-tabpanel");

        tabLayoutPanel.add(layout, "Pools");
        tabLayoutPanel.add(new VerticalPanel(), "Services");

        tabLayoutPanel.selectTab(0);

        return tabLayoutPanel;
    }

    @Override
    protected FormMetaData getFormMetaData() {
        return formMetaData;
    }

    @Override
    protected FormAdapter<Pool> makeAddEntityForm() {
        Form<Pool> form = new Form<Pool>(Pool.class);
        form.setNumColumns(1);
        form.setFields(getFormMetaData().findAttribute("name").getFormItemForAdd());
        return form;
    }

    @Override
    protected EntityToDmrBridge<Pool> getEntityBridge() {
        return bridge;
    }

    @Override
    protected DefaultCellTable<Pool> makeEntityTable() {
        DefaultCellTable<Pool> table = new DefaultCellTable<Pool>(10);

        table.addColumn(new Columns.NameColumn(), Columns.NameColumn.LABEL);

        return table;
    }

    @Override
    protected String getPluralEntityName() {
        return "EJB Pools"; // TODO i18n // is this one used at all?
    }
}
