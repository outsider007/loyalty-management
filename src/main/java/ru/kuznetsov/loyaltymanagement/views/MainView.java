package ru.kuznetsov.loyaltymanagement.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route
public class MainView extends AppLayout {

    private final VerticalLayout mainLayout;
    private CustomerView customerView;

    @Autowired
    public MainView(CustomerView customerView) {
        this.mainLayout = new VerticalLayout();
        this.customerView = customerView;

        customerView.setVisible(true);
        mainLayout.add(customerView);
        setContent(customerView);
    }

}