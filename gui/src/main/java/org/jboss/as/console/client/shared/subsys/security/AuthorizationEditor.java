package org.jboss.as.console.client.shared.subsys.security;

import java.util.List;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

import org.jboss.as.console.client.Console;
import org.jboss.as.console.client.shared.subsys.security.model.AuthorizationPolicyModule;
import org.jboss.as.console.client.shared.subsys.security.wizard.NewAuthorizationPolicyModuleWizard;
import org.jboss.as.console.client.widgets.tables.ButtonCell;
import org.jboss.ballroom.client.widgets.tables.DefaultCellTable;
import org.jboss.ballroom.client.widgets.tables.DefaultEditTextCell;
import org.jboss.ballroom.client.widgets.tables.DefaultPager;
import org.jboss.ballroom.client.widgets.tools.ToolButton;
import org.jboss.ballroom.client.widgets.tools.ToolStrip;
import org.jboss.ballroom.client.widgets.window.DefaultWindow;

public class AuthorizationEditor {
    private final SecurityPresenter presenter;
    private DefaultCellTable<AuthorizationPolicyModule> attributesTable;
    private ListDataProvider<AuthorizationPolicyModule> attributesProvider;
    protected DefaultWindow window;

    AuthorizationEditor(SecurityPresenter presenter) {
        this.presenter = presenter;
    }

    Widget asWidget() {
        VerticalPanel vpanel = new VerticalPanel();
        vpanel.setStyleName("fill-layout-width");

        attributesTable = new DefaultCellTable<AuthorizationPolicyModule>(4);
        attributesTable.getElement().setAttribute("style", "margin-top:5px;");
        attributesProvider = new ListDataProvider<AuthorizationPolicyModule>();
        attributesProvider.addDataDisplay(attributesTable);

        ToolStrip tableTools = new ToolStrip();
        ToolButton addModule = new ToolButton(Console.CONSTANTS.common_label_add());
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

        return vpanel;
    }

    void setData(List<AuthorizationPolicyModule> newList) {
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
}
