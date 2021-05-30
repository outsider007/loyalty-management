package ru.kuznetsov.loyaltymanagement.web.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

@Route
@SpringComponent
@UIScope
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