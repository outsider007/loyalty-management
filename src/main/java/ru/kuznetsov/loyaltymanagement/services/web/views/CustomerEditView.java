package ru.kuznetsov.loyaltymanagement.services.web.views;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import ru.kuznetsov.loyaltymanagement.dao.domain.Balance;
import ru.kuznetsov.loyaltymanagement.dao.domain.Customer;
import ru.kuznetsov.loyaltymanagement.dao.repositories.BalanceRepository;
import ru.kuznetsov.loyaltymanagement.dao.repositories.CustomerRepository;

import java.math.BigInteger;
import java.time.LocalDate;

@SpringComponent
@UIScope
public class CustomerEditView extends Dialog implements KeyNotifier {

    private CustomerRepository customerRepository;
    private BalanceRepository balanceRepository;
    private Customer customer;
    private TextField firstName;
    private TextField middleName;
    private TextField lastName;
    private RadioButtonGroup<String> gender;
    private DatePicker birthday;
    private TextField phoneNumber;
    private Button save;
    private Button cancel;
    private Button delete;
    private HorizontalLayout actions;
    private Binder<Customer> binder;
    private VerticalLayout mainLayout;
    @Setter
    private Grid<Customer> customerGrid;

    @Autowired
    public CustomerEditView(CustomerRepository customerRepository, BalanceRepository balanceRepository) {
        this.customerRepository = customerRepository;
        this.balanceRepository = balanceRepository;
        this.firstName = new TextField("Фамилия");
        this.middleName = new TextField("Имя");
        this.lastName = new TextField("Отчество");
        this.gender = new RadioButtonGroup<>();
        this.birthday = new DatePicker("Дата рождения");
        this.phoneNumber = new TextField("Телефон");
        this.save = new Button("Сохранить", VaadinIcon.CHECK.create());
        this.cancel = new Button("Отмена");
        this.delete = new Button("Удалить", VaadinIcon.TRASH.create());
        this.actions = new HorizontalLayout(save, cancel);
        this.binder = new Binder<>(Customer.class);
        this.mainLayout = new VerticalLayout(firstName, middleName, lastName, gender, phoneNumber, birthday, actions);

        add(mainLayout);
        setVisible(false);
        initElements();
        setCloseOnOutsideClick(false);
    }

    public final void editCustomer(Customer editedCustomer) {
        if (editedCustomer == null) {
            return;
        }

        final boolean persisted = editedCustomer.getId() != null;
        if (persisted) {
            customer = customerRepository.findById(editedCustomer.getId()).get();
        } else {
            customer = editedCustomer;
        }

        binder.setBean(customer);
        firstName.focus();
        setVisible(true);
        open();
    }

    private void initElements() {
        configureElements();
        initListeners();
    }

    private void configureElements() {
        firstName.setRequired(true);
        gender.setLabel("Пол");
        gender.setItems("Мужской", "Женский");
        gender.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        birthday.setRequired(true);
        phoneNumber.setRequired(true);
        binder.bindInstanceFields(this);
    }

    private void initListeners() {
            save.getElement().getThemeList().add("primary");
            delete.getElement().getThemeList().add("error");
            addKeyPressListener(Key.ENTER, e -> save());
            save.addClickListener(e -> save());
            cancel.addClickListener(e -> close());
            // FIXME При обновлении грида после изменений баланс у модели может нулевой(при создании)
            close();
    }

    private boolean validateFields() {
        return !firstName.isEmpty() && !birthday.isEmpty() && !phoneNumber.isEmpty();
    }

     private void save() {
         if (!validateFields()) {
            Notification.show("Некорректно заполнены обязательные поля Фамилия, Дата рождения, Номер телефона!",
                    3000, Notification.Position.TOP_STRETCH);
            return;
         }

        if (customer.getRegisteredDate() == null) {
            customer.setRegisteredDate(LocalDate.now());
        }

        customerRepository.save(customer);

        if (customer.getBalance() == null) {
            balanceRepository.save(new Balance(null, customer.getId(), new BigInteger("0")));
        }

        close();
        customerGrid.setItems(customerRepository.findAll());
    }
}
