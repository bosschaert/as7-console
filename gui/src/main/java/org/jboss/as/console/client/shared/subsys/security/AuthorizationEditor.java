package org.jboss.as.console.client.shared.subsys.security;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import org.jboss.as.console.client.Console;
import org.jboss.as.console.client.shared.properties.PropertyEditor;
import org.jboss.as.console.client.shared.properties.PropertyManagement;
import org.jboss.as.console.client.shared.properties.PropertyRecord;
import org.jboss.as.console.client.shared.subsys.security.model.AuthorizationPolicyModule;
import org.jboss.as.console.client.shared.subsys.security.wizard.NewAuthorizationPolicyModuleWizard;
import org.jboss.as.console.client.widgets.tables.ButtonCell;
import org.jboss.ballroom.client.widgets.ContentGroupLabel;
import org.jboss.ballroom.client.widgets.tables.DefaultCellTable;
import org.jboss.ballroom.client.widgets.tables.DefaultEditTextCell;
import org.jboss.ballroom.client.widgets.tables.DefaultPager;
import org.jboss.ballroom.client.widgets.tools.ToolButton;
import org.jboss.ballroom.client.widgets.tools.ToolStrip;
import org.jboss.ballroom.client.widgets.window.DefaultWindow;

public class AuthorizationEditor implements PropertyManagement {
    private final SecurityPresenter presenter;

    private DefaultCellTable<AuthorizationPolicyModule> attributesTable;
    private ListDataProvider<AuthorizationPolicyModule> attributesProvider;
    private String domainName;
    private ToolButton addModule;
    private List<AuthorizationPolicyModule> backup;
    private DefaultWindow window;

    AuthorizationEditor(SecurityPresenter presenter) {
        this.presenter = presenter;
    }

    Widget asWidget() {
        VerticalPanel vpanel = new VerticalPanel();
        vpanel.setStyleName("fill-layout-width");

        attributesTable = new DefaultCellTable<AuthorizationPolicyModule>(4);
        attributesTable.getElement().setAttribute("style", "margin-top:5px;");
        attributesTable.setEnabled(false);
        attributesProvider = new ListDataProvider<AuthorizationPolicyModule>();
        attributesProvider.addDataDisplay(attributesTable);

        ToolStrip tableTools = new ToolStrip();
        addModule = new ToolButton(Console.CONSTANTS.common_label_add());
        addModule.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                openWizard();
            }
        });
        tableTools.addToolButtonRight(addModule);
        vpanel.add(tableTools);

        Column<AuthorizationPolicyModule, String> codeColumn = new Column<AuthorizationPolicyModule, String>(new DefaultEditTextCell()) {
            @Override
            public String getValue(AuthorizationPolicyModule record) {
                return record.getCode();
            }
        };
        attributesTable.addColumn(codeColumn, "Code");

        Column<AuthorizationPolicyModule, String> flagColumn = new Column<AuthorizationPolicyModule, String>(new DefaultEditTextCell()) {
            @Override
            public String getValue(AuthorizationPolicyModule record) {
                return record.getFlag();
            }
        };
        attributesTable.addColumn(flagColumn, "Flag");

        Column<AuthorizationPolicyModule, AuthorizationPolicyModule> removeColumn = new Column<AuthorizationPolicyModule, AuthorizationPolicyModule>(
                new ButtonCell<AuthorizationPolicyModule>(Console.CONSTANTS.common_label_delete(), new ActionCell.Delegate<AuthorizationPolicyModule>() {
                    @Override
                    public void execute(AuthorizationPolicyModule object) {
                        attributesProvider.getList().remove(object);
                    }
                })
            ) {
                @Override
                public AuthorizationPolicyModule getValue(AuthorizationPolicyModule record) {
                    return record;
                }
            };
        attributesTable.addColumn(removeColumn, "");
        vpanel.add(attributesTable);

        DefaultPager pager = new DefaultPager();
        pager.setDisplay(attributesTable);
        vpanel.add(pager);

        final PropertyEditor propertyEditor = new PropertyEditor(this, true, 5);

        // TODO check that there is no selection model yet
        final SingleSelectionModel<AuthorizationPolicyModule> ssm = new SingleSelectionModel<AuthorizationPolicyModule>();
        ssm.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                System.out.println("+++ selection changed");
                AuthorizationPolicyModule policy = ssm.getSelectedObject();
                List<PropertyRecord> props = policy.getProperties();
                if (props == null)
                    props = new ArrayList<PropertyRecord>();
                propertyEditor.setProperties("", props);
            }
        });
        attributesTable.setSelectionModel(ssm);

        vpanel.add(new ContentGroupLabel("Properties"));
        vpanel.add(propertyEditor.asWidget());
        propertyEditor.setAllowEditProps(false);

        setEditingEnabled(false);

        return vpanel;
    }

    void setData(String domainName, List<AuthorizationPolicyModule> newList) {
        this.domainName = domainName;

        List<AuthorizationPolicyModule> list = attributesProvider.getList();
        list.clear();
        list.addAll(newList);
    }

    private void openWizard() {
        window = new DefaultWindow("New Authorization Policy");
        window.setWidth(480);
        window.setHeight(360);
        window.setWidget(new NewAuthorizationPolicyModuleWizard(AuthorizationEditor.this).asWidget());
        window.setGlassEnabled(true);
        window.center();
    }

    public void closeWizard() {
        if (window != null)
            window.hide();
    }

    public void addPolicy(AuthorizationPolicyModule policy) {
        attributesProvider.getList().add(policy);
    }

    // These are for the property editor
    @Override
    public void onCreateProperty(String reference, PropertyRecord prop) {
    }

    @Override
    public void onDeleteProperty(String reference, PropertyRecord prop) {
    }

    @Override
    public void onChangeProperty(String reference, PropertyRecord prop) {
    }

    @Override
    public void launchNewPropertyDialoge(String reference) {
    }

    @Override
    public void closePropertyDialoge() {
    }

    public void setEditingEnabled(boolean isEnabled) {
        attributesTable.setEnabled(false);
        addModule.setEnabled(isEnabled);
    }

    public void onCancel() {
        setEditingEnabled(false);

        List<AuthorizationPolicyModule> list = attributesProvider.getList();
        list.clear();
        list.addAll(backup);
        backup = null;
    }

    public void onEdit() {
        backup = new ArrayList<AuthorizationPolicyModule>(attributesProvider.getList());
        setEditingEnabled(true);
    }

    public void onSave() {
        setEditingEnabled(false);

        presenter.saveAuthorization(domainName, attributesProvider.getList());
    }
}
