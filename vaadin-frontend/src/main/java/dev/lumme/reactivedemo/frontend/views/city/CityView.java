package dev.lumme.reactivedemo.frontend.views.city;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import dev.lumme.reactivedemo.common.client.CityClient;
import dev.lumme.reactivedemo.common.dto.CityDTO;
import dev.lumme.reactivedemo.frontend.views.main.MainView;

import java.util.Collections;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@PageTitle("Cities: Skylines")
@Route(value = "hello", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
public class CityView extends VerticalLayout {

    private final CityClient cityClient;

    private Grid<CityDTO> grid;

    public CityView(CityClient cityClient) {
        this.cityClient = cityClient;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        if (attachEvent.isInitialAttach()) {
            grid = new Grid<>(CityDTO.class);

            Button setItemsSync = new Button("Fetch items sync", e -> setGridValuesSync());
            Button setItemsAsync = new Button("Fetch items async", e -> setGridValuesAsync());
            Button setItemsReactive = new Button("Fetch items reactive", e -> setGridValuesReactive());

            Button clear = new Button("Clear", e -> grid.setItems(Collections.emptyList()));
            clear.addThemeVariants(ButtonVariant.LUMO_ERROR);

            Button responsivenessButton = new Button("Test responsiveness", e -> Notification.show("Reponse"));
            responsivenessButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

            add(grid, new HorizontalLayout(setItemsSync, setItemsAsync, setItemsReactive, clear, responsivenessButton));
        }
    }

    private void setGridValuesSync() {
        grid.setItems(cityClient.findCitiesSync());
    }

    private void setGridValuesAsync() {
        UI ui = getUI().get();
        cityClient.findCitiesAsync(
                cities -> ui.access(() -> grid.setItems(cities)));
    }

    private void setGridValuesReactive() {
        UI ui = getUI().get();
        cityClient.findCitiesReactive().subscribe(
                cities -> ui.access(() -> grid.setItems(cities)));
    }
}
