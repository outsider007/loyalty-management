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
import ru.kuznetsov.loyaltymanagement.web.repositories.BalanceChangeRepository;
import ru.kuznetsov.loyaltymanagement.web.repositories.BalanceRepository;
import ru.kuznetsov.loyaltymanagement.web.repositories.CustomerRepository;

import java.util.List;
import java.util.NoSuchElementException;

@SpringComponent
@UIScope
public class CustomerView extends VerticalLayout {

    private CustomerRepository customerRepository;
    private BalanceRepository balanceRepository;
    private CustomerEditView customerEditView;
    private BalanceChangeView balanceChangeView;
    private BalanceChangeRepository balanceChangeRepository;
    private final Button addNew;
    private final Button editCustomer;
    private final Checkbox checkBalance;
    private final Button deleteCustomer;
    private final Button showBalanceChange;
    private final Grid<Customer> customerGrid;
    private final Grid<Balance> balanceGrid;
    private final TextField filter;
    private final HorizontalLayout customersList;
    private final H4 customerListLabel;
    private final H4 balanceLabel;

    @Autowired
    public CustomerView(CustomerRepository customerRepository, CustomerEditView customerEditView, BalanceRepository balanceRepository,
                        BalanceChangeView balanceChangeView, BalanceChangeRepository balanceChangeRepository) {
        this.balanceChangeRepository = balanceChangeRepository;
        this.customerRepository = customerRepository;
        this.balanceChangeView = balanceChangeView;
        this.customerEditView = customerEditView;
        this.balanceRepository = balanceRepository;
        this.customerGrid = new Grid<>(Customer.class);
        this.balanceGrid = new Grid<>(Balance.class);
        this.filter = new TextField();
        this.addNew = new Button("Новый покупатель", VaadinIcon.PLUS.create());
        this.editCustomer = new Button("Редактировать", VaadinIcon.EDIT.create());
        this.checkBalance = new Checkbox("Показывать информацию о балансе");
        this.deleteCustomer = new Button("Удалить", VaadinIcon.TRASH.create());
        this.showBalanceChange = new Button("История изменения баланса", VaadinIcon.CASH.create());
        this.customersList = new HorizontalLayout(filter, addNew, editCustomer, deleteCustomer, showBalanceChange);
        this.customerListLabel = new H4("Список покупателей");
        this.balanceLabel = new H4("Баланс покупателя");
        customerEditView.setCustomerGrid(customerGrid);

        initElements();
        listCustomers(null);
        add(customersList, checkBalance, customerListLabel, customerGrid, balanceLabel, balanceGrid, customerEditView, balanceChangeView);
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
        checkBalance.setValue(true);
    }

    private void initFilter() {
        filter.setWidth("300px");
        filter.setPlaceholder("Фильтрация по фамилии");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
    }

    private void initListeners() {
        try {
            filter.addValueChangeListener(e -> listCustomers(e.getValue()));
            addNew.addClickListener(e -> customerEditView.editCustomer(new Customer()));
            editCustomer.addClickListener(e -> editCustomer(customerGrid.getSelectedItems().stream().iterator().next()));
            deleteCustomer.addClickListener(e -> deleteCustomer(customerGrid.getSelectedItems().stream().iterator().next()));
            customerGrid.asSingleSelect().addValueChangeListener(e -> {
                if (e.getValue() != null) {
                    showCustomerBalance(e.getValue().getId());
                }
            });
            checkBalance.addValueChangeListener(e -> setVisibleBalanceGrid(e.getValue()));
            showBalanceChange.addClickListener(e -> {
                if (!customerGrid.getSelectedItems().isEmpty() && customerGrid.getSelectedItems().stream().iterator().next() != null) {
                    Integer customerId = customerGrid.getSelectedItems().stream().iterator().next().getId();
                    try {
                        List<Balance> balanceByCustomerId = balanceRepository.findByCustomerId(customerId);
                        balanceChangeView.setBalanceId(balanceByCustomerId.stream().iterator().next().getId());
                        balanceChangeView.open();
                        balanceChangeView.setVisible(true);
                    } catch (NoSuchElementException ex) {
                        Notification.show("Не найден баланс покупателя с id " + customerId, 3000, Notification.Position.TOP_STRETCH);
                        balanceChangeView.close();
                        balanceChangeView.setVisible(false);
                    }
                }
            });
        } catch (NoSuchElementException exception) {
            // FIXME Игнорируем ошибки, которые возникают при инициализации листнеров в тот момент, когда списки пусты
        }
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
        customerEditView.editCustomer(customer);
    }

    private void deleteCustomer(Customer customer) {
        if (!isSelectCustomer(customer)) {
            return;
        }
        customerRepository.delete(customer);
        balanceRepository.deleteByCustomerId(customer.getId());

        List<Balance> balances = balanceRepository.findByCustomerId(customer.getId());
        if (!balances.isEmpty() && balances.get(0) != null) {
            balanceChangeRepository.deleteByBalanceId(balances.get(0).getId());
        }

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
