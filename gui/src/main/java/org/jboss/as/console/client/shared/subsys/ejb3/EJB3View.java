package org.jboss.as.console.client.shared.subsys.ejb3;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import org.jboss.as.console.client.shared.dispatch.DispatchAsync;
import org.jboss.as.console.client.shared.subsys.ejb3.model.EJB3Subsystem;
import org.jboss.as.console.client.shared.subsys.ejb3.model.StrictMaxBeanPool;
import org.jboss.as.console.client.shared.viewframework.AbstractEntityView;
import org.jboss.as.console.client.shared.viewframework.Columns;
import org.jboss.as.console.client.shared.viewframework.EntityToDmrBridge;
import org.jboss.as.console.client.shared.viewframework.EntityToDmrBridgeImpl;
import org.jboss.as.console.client.widgets.forms.FormMetaData;
import org.jboss.as.console.client.widgets.forms.PropertyMetaData;
import org.jboss.ballroom.client.widgets.forms.Form;
import org.jboss.ballroom.client.widgets.forms.FormAdapter;
import org.jboss.ballroom.client.widgets.tables.DefaultCellTable;
import org.jboss.ballroom.client.widgets.tools.ToolStrip;

public class EJB3View extends AbstractEntityView<StrictMaxBeanPool> implements EJB3Presenter.MyView {
    private final EntityToDmrBridge<StrictMaxBeanPool> bridge;
    private final FormMetaData formMetaData;
    private EJB3Presenter presenter;
    private EJB3SubsystemEditor subsystemEditor;
    private TimerServiceView timerServiceView;

    @Inject
    public EJB3View(PropertyMetaData propertyMetaData, DispatchAsync dispatcher) {
        super(StrictMaxBeanPool.class);
        formMetaData = propertyMetaData.getBeanMetaData(StrictMaxBeanPool.class).getFormMetaData();
        bridge = new EntityToDmrBridgeImpl<StrictMaxBeanPool>(propertyMetaData, StrictMaxBeanPool.class, this, dispatcher);

        timerServiceView = new TimerServiceView(propertyMetaData, dispatcher);
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

        subsystemEditor = new EJB3SubsystemEditor(presenter);
        vpanel.add(subsystemEditor.asWidget());

        entityEditor = makeEntityEditor();
        entityEditor.addWidgetToPanel(vpanel);

        TabLayoutPanel tabLayoutPanel = new TabLayoutPanel(25, Style.Unit.PX);
        tabLayoutPanel.addStyleName("default-tabpanel");

        tabLayoutPanel.add(layout, "Pools");
        tabLayoutPanel.add(timerServiceView.asWidget(), "Services");
        tabLayoutPanel.selectTab(0);

        return tabLayoutPanel;
    }

//    @Override
//    public void itemAction(Action action, ObservableFormItem item) {
//        if (item.getPropertyBinding().getJavaName())
//    }

    @Override
    public void setPresenter(EJB3Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected FormMetaData getFormMetaData() {
        return formMetaData;
    }

    @Override
    protected FormAdapter<StrictMaxBeanPool> makeAddEntityForm() {
        Form<StrictMaxBeanPool> form = new Form<StrictMaxBeanPool>(StrictMaxBeanPool.class);
        form.setNumColumns(1);
        form.setFields(getFormMetaData().findAttribute("name").getFormItemForAdd());
        return form;
    }

    @Override
    protected EntityToDmrBridge<StrictMaxBeanPool> getEntityBridge() {
        return bridge;
    }

    @Override
    protected DefaultCellTable<StrictMaxBeanPool> makeEntityTable() {
        DefaultCellTable<StrictMaxBeanPool> table = new DefaultCellTable<StrictMaxBeanPool>(10);

        table.addColumn(new Columns.NameColumn(), Columns.NameColumn.LABEL);

        return table;
    }

    @Override
    protected String getPluralEntityName() {
        return "EJB Pools"; // TODO i18n // is this one used at all?
    }

    @Override
    public void loadTimerService() {
        timerServiceView.initialLoad();
    }

    @Override
    public void updateSubsystem(EJB3Subsystem subsystem) {
        subsystemEditor.setProviderDetails(subsystem);
    }
}
