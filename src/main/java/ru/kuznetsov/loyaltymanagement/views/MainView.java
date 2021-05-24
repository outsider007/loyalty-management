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
import org.springframework.beans.factory.annotation.Autowired;
import ru.kuznetsov.loyaltymanagement.domain.Customer;
import ru.kuznetsov.loyaltymanagement.repositories.CustomerRepository;

@Route
public class MainView extends VerticalLayout {

    private CustomerRepository customerRepository;
    private CustomerEditor editor;
    private final Button addNew;
    private final Button editCustomer;
    private final Button checkBalance;
    private final Button deleteCustomer;

    private final Grid<Customer> grid;
    private final TextField filter;

    @Autowired
    public MainView(CustomerRepository customerRepository, CustomerEditor editor) {
        this.customerRepository = customerRepository;
        this.editor = editor;
        this.grid = new Grid<>(Customer.class);
        this.filter = new TextField();
        this.addNew = new Button("Новый покупатель", VaadinIcon.PLUS.create());
        this.editCustomer = new Button("Редактировать", VaadinIcon.EDIT.create());
        this.checkBalance = new Button("Просмотр баланса", VaadinIcon.CASH.create());
        this.deleteCustomer = new Button("Удалить", VaadinIcon.TRASH.create());

        HorizontalLayout actions = new HorizontalLayout(filter, addNew, editCustomer, deleteCustomer, checkBalance);
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
        grid.setColumns("id", "firstName", "middleName", "lastName", "gender", "birthday", "registeredDate");
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
    }

    private void initFilter() {
        filter.setWidth("300px");
        filter.setPlaceholder("Фильтрация по фамилии");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
    }

    private void initListeners() {
        filter.addValueChangeListener(e -> listCustomers(e.getValue()));
        addNew.addClickListener(e -> editor.editCustomer(new Customer()));
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listCustomers(filter.getValue());
        });
        editCustomer.addClickListener(e -> editor.editCustomer(grid.getSelectedItems().stream().iterator().next()));
    }

    private void listCustomers(String filterText) {
        if (StringUtils.isBlank(filterText)) {
            grid.setItems(customerRepository.findAll());
        } else {
            grid.setItems(customerRepository.findByFirstNameStartsWithIgnoreCase(filterText));
        }
    }

}