package org.jboss.as.console.client.shared.subsys.security.wizard;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import org.jboss.as.console.client.Console;
import org.jboss.as.console.client.shared.BeanFactory;
import org.jboss.as.console.client.shared.properties.PropertyEditor;
import org.jboss.as.console.client.shared.properties.PropertyManagement;
import org.jboss.as.console.client.shared.properties.PropertyRecord;
import org.jboss.as.console.client.shared.subsys.security.AuthorizationEditor;
import org.jboss.as.console.client.shared.subsys.security.model.AuthorizationPolicyModule;
import org.jboss.ballroom.client.widgets.forms.Form;
import org.jboss.ballroom.client.widgets.forms.FormValidation;
import org.jboss.ballroom.client.widgets.forms.TextBoxItem;
import org.jboss.ballroom.client.widgets.window.DialogueOptions;
import org.jboss.ballroom.client.widgets.window.WindowContentBuilder;

public class NewAuthorizationPolicyModuleWizard implements PropertyManagement {
    private final AuthorizationEditor editor;
    private final BeanFactory factory = GWT.create(BeanFactory.class);
    private final List<PropertyRecord> properties = new ArrayList<PropertyRecord>();
    private PropertyEditor propEditor;

    public NewAuthorizationPolicyModuleWizard(AuthorizationEditor editor) {
        this.editor = editor;
    }

    public Widget asWidget() {
        VerticalPanel layout = new VerticalPanel();
        layout.setStyleName("window-content");
        final Form<AuthorizationPolicyModule> form = new Form<AuthorizationPolicyModule>(AuthorizationPolicyModule.class);

        TextBoxItem code = new TextBoxItem("code", "Code");
        TextBoxItem flag = new TextBoxItem("flag", "Flag");
        form.setFields(code, flag);

        layout.add(form.asWidget());
        propEditor = new PropertyEditor(this, true);
        layout.add(propEditor.asWidget());

        DialogueOptions options = new DialogueOptions(
            new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    FormValidation validation = form.validate();
                    if (!validation.hasErrors()) {
                        AuthorizationPolicyModule data = form.getUpdatedEntity();
                        data.setProperties(properties);

                        editor.closeWizard();
                        editor.addPolicy(data);
                    }
                }
            }, new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    editor.closeWizard();
                }
            });

        return new WindowContentBuilder(layout, options).build();
    }

    // PropertyManagement methods

    @Override
    public void onCreateProperty(String reference, PropertyRecord prop) {
        // No need to implement
    }

    @Override
    public void onDeleteProperty(String reference, PropertyRecord prop) {
        properties.remove(prop);
        propEditor.setProperties("", properties);
    }

    @Override
    public void onChangeProperty(String reference, PropertyRecord prop) {
        // No need to implement
    }

    @Override
    public void launchNewPropertyDialoge(String reference) {
        PropertyRecord proto = factory.property().as();
        proto.setKey(Console.CONSTANTS.common_label_name().toLowerCase());
        proto.setValue(Console.CONSTANTS.common_label_value().toLowerCase());

        properties.add(proto);
        propEditor.setProperties("", properties);
    }

    @Override
    public void closePropertyDialoge() {
    }
}
