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
import ru.kuznetsov.loyaltymanagement.dao.domain.Balance;
import ru.kuznetsov.loyaltymanagement.dao.domain.Customer;
import ru.kuznetsov.loyaltymanagement.dao.repositories.BalanceChangeRepository;
import ru.kuznetsov.loyaltymanagement.dao.repositories.BalanceRepository;
import ru.kuznetsov.loyaltymanagement.dao.repositories.CustomerRepository;

import java.util.List;
import java.util.NoSuchElementException;

@SpringComponent
@UIScope
public class CustomerView extends VerticalLayout {

    private final CustomerRepository customerRepository;
    private final BalanceRepository balanceRepository;
    private final CustomerEditView customerEditView;
    private final BalanceChangeView balanceChangeView;
    private final BalanceChangeRepository balanceChangeRepository;
    private final Button addNew;
    private final Button editCustomer;
    private final Checkbox checkBalance;
    private final Button deleteCustomer;
    private final Button showBalanceChange;
    private final Grid<Customer> customerGrid;
    private final Grid<Balance> balanceGrid;
    private final TextField filter;
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
        this.addNew = new Button("?????????? ????????????????????", VaadinIcon.PLUS.create());
        this.editCustomer = new Button("??????????????????????????", VaadinIcon.EDIT.create());
        this.checkBalance = new Checkbox("???????????????????? ???????????????????? ?? ??????????????");
        this.deleteCustomer = new Button("??????????????", VaadinIcon.TRASH.create());
        this.showBalanceChange = new Button("?????????????? ?????????????????? ??????????????", VaadinIcon.CASH.create());
        HorizontalLayout customersList = new HorizontalLayout(filter, addNew, editCustomer, deleteCustomer, showBalanceChange);
        H4 customerListLabel = new H4("???????????? ??????????????????????");
        this.balanceLabel = new H4("???????????? ????????????????????");
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
        customerGrid.setColumns("id", "firstName", "middleName", "lastName", "gender", "birthday", "phoneNumber", "registeredDate");
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
        filter.setWidth("400px");
        filter.setPlaceholder("?????????? ???? ???????????? ?????????????? ?????? ???????????? ????????????????");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
    }

    private void initListeners() {
        try {
            // FIXME ?????????????? ???????????????? ???? ?????????????????????? ???????????????? ?? ?????????????????? ??????????
            filter.addValueChangeListener(e -> listCustomers(e.getValue()));
            addNew.addClickListener(e -> customerEditView.editCustomer(new Customer()));
            editCustomer.addClickListener(e -> {
                if (!customerGrid.getSelectedItems().isEmpty()) {
                    editCustomer(customerGrid.getSelectedItems().stream().iterator().next());
                } else {
                    Notification.show("???? ???????? ?????????????? ???? ????????????!",
                            3000, Notification.Position.TOP_STRETCH);
                }
            });
            deleteCustomer.addClickListener(e -> {
                if (!customerGrid.getSelectedItems().isEmpty()) {
                    deleteCustomer(customerGrid.getSelectedItems().stream().iterator().next());
                } else {
                    Notification.show("???? ???????? ?????????????? ???? ????????????!",
                            3000, Notification.Position.TOP_STRETCH);
                }
            });
            customerGrid.asSingleSelect().addValueChangeListener(e -> {
                if (e.getValue() != null) {
                    showCustomerBalance(e.getValue().getId());
                }
            });
            checkBalance.addValueChangeListener(e -> setVisibleBalanceElements(e.getValue()));
            showBalanceChange.addClickListener(e -> {
                if (!customerGrid.getSelectedItems().isEmpty() && customerGrid.getSelectedItems().stream().iterator().next() != null) {
                    Integer customerId = customerGrid.getSelectedItems().stream().iterator().next().getId();
                    try {
                        List<Balance> balanceByCustomerId = balanceRepository.findByCustomerId(customerId);
                        balanceChangeView.setCustomerBalanceId(balanceByCustomerId.stream().iterator().next().getId());
                        balanceChangeView.open();
                        balanceChangeView.setVisible(true);
                    } catch (NoSuchElementException ex) {
                        Notification.show("???? ???????????? ???????????? ???????????????????? ?? id " + customerId, 3000, Notification.Position.TOP_STRETCH);
                        balanceChangeView.close();
                        balanceChangeView.setVisible(false);
                    }
                } else {
                    Notification.show("???? ???????? ?????????????? ???? ????????????!",
                            3000, Notification.Position.TOP_STRETCH);
                }
            });
        } catch (NoSuchElementException exception) {
            // FIXME ???????????????????? ????????????, ?????????????? ?????????????????? ?????? ?????????????????????????? ?????????????????? ?? ?????? ????????????, ?????????? ???????????? ??????????
        }
    }

    private void setVisibleBalanceElements(boolean isVisible) {
        balanceLabel.setVisible(isVisible);
        balanceGrid.setVisible(isVisible);
        showBalanceChange.setVisible(isVisible);
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
            customerGrid.setItems(customerRepository.findByPhoneNumberStartingWithOrFirstNameStartingWith(filterText, filterText));
        }
    }

    private boolean isSelectCustomer(Customer customer) {
        if (customer == null) {
            Notification.show("???? ???????? ?????????????? ???? ????????????!",
                    3000, Notification.Position.TOP_STRETCH);
            return false;
        } else {
            return true;
        }
    }

}
