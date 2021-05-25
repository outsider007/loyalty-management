package ru.kuznetsov.loyaltymanagement.web.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import ru.kuznetsov.loyaltymanagement.web.domain.Balance;
import ru.kuznetsov.loyaltymanagement.web.domain.Customer;
import ru.kuznetsov.loyaltymanagement.web.repositories.BalanceRepository;
import ru.kuznetsov.loyaltymanagement.web.repositories.CustomerRepository;

import java.util.NoSuchElementException;

@SpringComponent
@UIScope
public class CustomerView extends VerticalLayout {

    private CustomerRepository customerRepository;
    private BalanceRepository balanceRepository;
    private CustomerEditView editor;
    private final Button addNew;
    private final Button editCustomer;
    private final Checkbox checkBalance;
    private final Button deleteCustomer;
    private final Grid<Customer> customerGrid;
    private final Grid<Balance> balanceGrid;
    private final TextField filter;
    private final HorizontalLayout customersList;
    private final H4 customerListLabel;
    private final H4 balanceLabel;

    @Autowired
    public CustomerView(CustomerRepository customerRepository, CustomerEditView editor, BalanceRepository balanceRepository) {
        this.customerRepository = customerRepository;
        this.editor = editor;
        this.balanceRepository = balanceRepository;
        this.customerGrid = new Grid<>(Customer.class);
        this.balanceGrid = new Grid<>(Balance.class);
        this.filter = new TextField();
        this.addNew = new Button("Новый покупатель", VaadinIcon.PLUS.create());
        this.editCustomer = new Button("Редактировать", VaadinIcon.EDIT.create());
        this.checkBalance = new Checkbox("Показывать информацию о балансе");
        this.deleteCustomer = new Button("Удалить", VaadinIcon.TRASH.create());
        this.customersList = new HorizontalLayout(filter, addNew, editCustomer, deleteCustomer);
        this.customerListLabel = new H4("Список покупателей");
        this.balanceLabel = new H4("Баланс покупателя");
        editor.setCustomerGrid(customerGrid);

        initElements();
        listCustomers(null);
        add(customersList, checkBalance, customerListLabel, customerGrid, balanceLabel, balanceGrid, editor);
    }

    private void initElements() {
        initCustomerGrid();
        initBalanceComponents();
        initFilter();
        initListeners();
    }

    private void initCustomerGrid() {
        customerGrid.setHeight("550px");
        customerGrid.setColumns("id", "firstName", "middleName", "lastName", "gender", "birthday", "registeredDate");
    }

    private void initBalanceComponents() {
        balanceGrid.setHeight("100px");
        balanceGrid.setMinWidth("250px");
        balanceGrid.setWidth("600px");
        balanceGrid.setColumns("id", "balance");
        balanceGrid.getColumnByKey("balance").setAutoWidth(true);
        balanceGrid.setVisible(false);
        balanceLabel.setVisible(false);
    }

    private void initFilter() {
        filter.setWidth("300px");
        filter.setPlaceholder("Фильтрация по фамилии");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
    }

    private void initListeners() {
        filter.addValueChangeListener(e -> listCustomers(e.getValue()));
        addNew.addClickListener(e -> editor.editCustomer(new Customer()));
        editCustomer.addClickListener(e -> editCustomer(customerGrid.getSelectedItems().stream().iterator().next()));
        deleteCustomer.addClickListener(e -> deleteCustomer(customerGrid.getSelectedItems().stream().iterator().next()));
        customerGrid.asSingleSelect().addValueChangeListener(e -> {
            if (e.getValue() != null) {
                showCustomerBalance(e.getValue().getId());
            }
        });
        checkBalance.addValueChangeListener(e -> setVisibleBalanceGrid(e.getValue()));
    }

    private void setVisibleBalanceGrid(boolean isVisible) {
        balanceLabel.setVisible(isVisible);
        balanceGrid.setVisible(isVisible);
    }

    private void showCustomerBalance(Integer customerId) {
        Balance balance;
        try {
            balance = balanceRepository.findByCustomerId(customerId).stream().iterator().next();
        } catch (NoSuchElementException exception) {
            balanceGrid.setItems();
            return;
        }


        balanceGrid.setItems(balance);
        balanceGrid.getDataProvider().refreshAll();
        balanceGrid.recalculateColumnWidths();
    }

    private void editCustomer(Customer customer) {
        if (!isSelectCustomer(customer)) {
            return;
        }
        editor.editCustomer(customer);
    }

    private void deleteCustomer(Customer customer) {
        if (!isSelectCustomer(customer)) {
            return;
        }
        customerRepository.delete(customer);
        balanceRepository.deleteByCustomerId(customer.getId());
        customerGrid.getDataProvider().refreshAll();
        customerGrid.setItems(customerRepository.findAll());
    }

    private void listCustomers(String filterText) {
        if (StringUtils.isBlank(filterText)) {
            customerGrid.setItems(customerRepository.findAll());
        } else {
            customerGrid.setItems(customerRepository.findByFirstNameStartingWith(filterText));
        }
    }

    private boolean isSelectCustomer(Customer customer) {
        if (customer == null) {
            Notification.show("Ни один элемент не выбран!",
                    3000, Notification.Position.TOP_STRETCH);
            return false;
        } else {
            return true;
        }
    }

}
