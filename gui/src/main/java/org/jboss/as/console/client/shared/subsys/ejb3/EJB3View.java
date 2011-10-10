package org.jboss.as.console.client.shared.subsys.ejb3;

import java.util.EnumSet;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import org.jboss.as.console.client.shared.dispatch.DispatchAsync;
import org.jboss.as.console.client.shared.subsys.ejb3.model.EJB3Subsystem;
import org.jboss.as.console.client.shared.viewframework.AbstractEntityView;
import org.jboss.as.console.client.shared.viewframework.EntityDetails;
import org.jboss.as.console.client.shared.viewframework.EntityEditor;
import org.jboss.as.console.client.shared.viewframework.EntityToDmrBridge;
import org.jboss.as.console.client.shared.viewframework.FrameworkButton;
import org.jboss.as.console.client.widgets.forms.FormMetaData;
import org.jboss.as.console.client.widgets.forms.PropertyMetaData;
import org.jboss.ballroom.client.widgets.forms.Form;
import org.jboss.ballroom.client.widgets.forms.FormAdapter;
import org.jboss.ballroom.client.widgets.tables.DefaultCellTable;

public class EJB3View extends AbstractEntityView<EJB3Subsystem> implements EJB3Presenter.MyView {
    private final EntityToDmrBridge<EJB3Subsystem> bridge;
    private final FormMetaData formMetaData;
    private final PoolsSection poolsSection;
    private final TimerServiceView timerServiceView;

    @Inject
    public EJB3View(PropertyMetaData propertyMetaData, DispatchAsync dispatcher) {
        super(EJB3Subsystem.class, EnumSet.of(FrameworkButton.ADD));
        formMetaData = propertyMetaData.getBeanMetaData(EJB3Subsystem.class).getFormMetaData();
        bridge = new SingleEntityToDmrBridgeImpl<EJB3Subsystem>(propertyMetaData, EJB3Subsystem.class, this, dispatcher);

        poolsSection = new PoolsSection(propertyMetaData, dispatcher);

        timerServiceView = new TimerServiceView(propertyMetaData, dispatcher);
    }

//    @Override
//    public void itemAction(Action action, ObservableFormItem item) {
//        if (item.getPropertyBinding().getJavaName())
//    }

    @Override
    public Widget createWidget() {
        LayoutPanel layout = new LayoutPanel();
        ScrollPanel scroll = new ScrollPanel();
        VerticalPanel vpanel = new VerticalPanel();
        vpanel.setStyleName("rhs-content-pane");
        scroll.add(vpanel);

        layout.add(scroll);
        layout.setWidgetTopHeight(scroll, 0, Style.Unit.PX, 100, Style.Unit.PCT);

        entityEditor = makeEntityEditor();
        entityEditor.addWidgetToPanel(vpanel);

        TabPanel bottomPanel = new TabPanel();
        bottomPanel.setStyleName("default-tabpanel");
        bottomPanel.add(poolsSection.asWidget(), poolsSection.getPluralEntityName());
        bottomPanel.selectTab(0);
        vpanel.add(bottomPanel);

        TabLayoutPanel tabLayoutPanel = new TabLayoutPanel(25, Style.Unit.PX);
        tabLayoutPanel.addStyleName("default-tabpanel");

        tabLayoutPanel.add(layout, "Pools");
        tabLayoutPanel.add(timerServiceView.asWidget(), "Services");
        tabLayoutPanel.selectTab(0);

        return tabLayoutPanel;
    }

    @Override
    protected EntityEditor<EJB3Subsystem> makeEntityEditor() {
        EntityDetails<EJB3Subsystem> details = new EntityDetails<EJB3Subsystem>(getPluralEntityName(), makeEditEntityDetailsForm(), getEntityBridge(), hideButtons);
        return new EntityEditor<EJB3Subsystem>(getPluralEntityName(), null, makeEntityTable(), details, hideButtons);
    }

    @Override
    protected FormMetaData getFormMetaData() {
        return formMetaData;
    }

    @Override
    protected EntityToDmrBridge<EJB3Subsystem> getEntityBridge() {
        return bridge;
    }

    @Override
    protected DefaultCellTable<EJB3Subsystem> makeEntityTable() {
        DefaultCellTable<EJB3Subsystem> table = new DefaultCellTable<EJB3Subsystem>(5);
        table.setVisible(false);
        return table;
    }

    @Override
    protected FormAdapter<EJB3Subsystem> makeAddEntityForm() {
        // TODO delete this!
        Form<EJB3Subsystem> form = new Form<EJB3Subsystem>(EJB3Subsystem.class);
        form.setNumColumns(1);
        return form;
    }

    @Override
    protected String getPluralEntityName() {
        return "EJB3 Subsystem"; // TODO i18n // is this one used at all?
    }

    @Override
    public void loadPools() {
        poolsSection.initialLoad();
    }

    @Override
    public void loadTimerService() {
        timerServiceView.initialLoad();
    }
}
