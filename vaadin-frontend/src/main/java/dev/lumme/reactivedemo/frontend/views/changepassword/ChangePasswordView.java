package dev.lumme.reactivedemo.frontend.views.changepassword;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import dev.lumme.reactivedemo.common.client.ChangePasswordClient;
import dev.lumme.reactivedemo.frontend.views.main.MainView;
import reactor.util.context.Context;

import java.util.Objects;

@PageTitle("Change password")
@Route(value = "change-password", layout = MainView.class)
public class ChangePasswordView extends VerticalLayout {

    private final ChangePasswordClient changePasswordClient;

    public ChangePasswordView(ChangePasswordClient changePasswordClient) {
        this.changePasswordClient = changePasswordClient;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if (attachEvent.isInitialAttach()) {
            PasswordField newField = new PasswordField("New password");
            PasswordField confirmField = new PasswordField("Confirm");

            Button changePassword = new Button("Change password with context");
            changePassword.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

            Button changePasswordWrong = new Button("Change password without context");
            changePasswordWrong.addThemeVariants(ButtonVariant.LUMO_ERROR);

            Div errorDiv = new Div();
            errorDiv.getStyle().set("color", "var(--lumo-error-text-color)");
            errorDiv.getStyle().set("font-size", "var(--lumo-font-size-xs)");
            errorDiv.getStyle().set("white-space", "pre-wrap");

            changePassword.addClickListener(e -> changePassword(newField, confirmField, errorDiv, true));
            changePasswordWrong.addClickListener(e -> changePassword(newField, confirmField, errorDiv, false));

            add(newField, confirmField, changePassword, changePasswordWrong, errorDiv);
        }
    }

    private void changePassword(PasswordField newField, PasswordField confirmField, Div errorDiv, boolean withContext) {
        String error = validatePassword(newField.getValue(), confirmField.getValue());

        if (error == null) {
            int statusCode = sendPasswordChangeRequest(newField.getValue(), withContext);
            Notification.show("Status code: " + statusCode, 3000, Notification.Position.MIDDLE);
        }
        errorDiv.setText(error);
    }

    private Integer sendPasswordChangeRequest(String newPassword, boolean withContext) {
        Context context = withContext ?
                Context.of("user", VaadinSession.getCurrent().getAttribute("user")) :
                Context.empty();

        return changePasswordClient.changePassword(newPassword)
                .subscriberContext(context)
                .block();
    }

    private String validatePassword(String newPassword, String confirmPassword) {
        if (!Objects.equals(newPassword, confirmPassword) || !newPassword.equals("Vaadin123")) {
            return String.join("\n",
                    "Passwords must match",
                    "Password must not be blank",
                    "Password length must be greater than 8 characters and less than 10 characters",
                    "Password must start with the letter 'V'",
                    "Password must be the name of a Java framework",
                    "Password must not be an insult",
                    "Password must not contains the letter 'x'",
                    "Password must be old enough to drive",
                    "Password must not drink before the age of 21",
                    "Password must comply with GDPR",
                    "Password must contain the letters 'aadin'",
                    "Password must end with '123'",
                    "Password must be 'Vaadin123'",
                    "Password must be able to run Crysis at 120 FPS",
                    "Password must be scalable and thread safe");
        }
        return null;
    }
}
