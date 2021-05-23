package ru.kuznetsov.loyaltymanagement.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import ru.kuznetsov.loyaltymanagement.domain.Customer;
import ru.kuznetsov.loyaltymanagement.repositories.CustomerRepository;

@Route
public class MainView extends VerticalLayout {

    private final CustomerRepository repo;
    private final CustomerEditor editor;
    private final Button addNewBtn;

    final Grid<Customer> grid;
    final TextField filter;

    public MainView(CustomerRepository repo, CustomerEditor editor) {
        this.repo = repo;
        this.editor = editor;
        this.grid = new Grid<>(Customer.class);
        this.filter = new TextField();
        this.addNewBtn = new Button("Новый покупатель", VaadinIcon.PLUS.create());

        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, grid, editor);

        initElements();
        listCustomers(null);
    }

    private void initElements() {
        initGrid();
        initFilter();
        initListeners();
    }

    private void initGrid() {
        grid.setHeight("300px");
        grid.setColumns("id", "firstName", "middleName", "lastName", "gender", "birthday", "registeredDate", "balance.balance");
        grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);
    }

    private void initFilter() {
        filter.setPlaceholder("Фильтрация по имени");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
    }

    private void initListeners() {
        filter.addValueChangeListener(e -> listCustomers(e.getValue()));
        grid.asSingleSelect().addValueChangeListener(e -> editor.editCustomer(e.getValue()));
        addNewBtn.addClickListener(e -> editor.editCustomer(new Customer()));
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listCustomers(filter.getValue());
        });
    }

    private void listCustomers(String filterText) {
        if (StringUtils.isBlank(filterText)) {
            grid.setItems(repo.findAll());
        } else {
            grid.setItems(repo.findByFirstNameStartsWithIgnoreCase(filterText));
        }
    }

}