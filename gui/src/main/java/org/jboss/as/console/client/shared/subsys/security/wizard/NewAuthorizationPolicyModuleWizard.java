package org.jboss.as.console.client.shared.subsys.security.wizard;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import org.jboss.as.console.client.shared.subsys.security.AuthorizationEditor;
import org.jboss.as.console.client.shared.subsys.security.model.AuthorizationPolicyModule;
import org.jboss.ballroom.client.widgets.forms.Form;
import org.jboss.ballroom.client.widgets.forms.FormValidation;
import org.jboss.ballroom.client.widgets.forms.TextBoxItem;
import org.jboss.ballroom.client.widgets.window.DialogueOptions;
import org.jboss.ballroom.client.widgets.window.WindowContentBuilder;

public class NewAuthorizationPolicyModuleWizard {
    private final AuthorizationEditor editor;

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

        DialogueOptions options = new DialogueOptions(
            new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    FormValidation validation = form.validate();
                    if (!validation.hasErrors()) {
                        AuthorizationPolicyModule data = form.getUpdatedEntity();
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
}
