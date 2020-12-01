package dev.lumme.reactivedemo.frontend.views.socket;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import dev.lumme.reactivedemo.frontend.socket.SocketClient;
import dev.lumme.reactivedemo.frontend.views.main.MainView;

@PageTitle("Socket")
@Route(layout = MainView.class)
public class SocketView extends VerticalLayout {

    private final SocketClient socketClient;

    public SocketView(SocketClient socketClient) {
        this.socketClient = socketClient;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if (attachEvent.isInitialAttach()) {
            Button button = new Button("Read messages", e -> socketClient.readMessages());
            add(button);
        }
    }
}
