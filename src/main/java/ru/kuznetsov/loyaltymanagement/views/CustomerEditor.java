package ru.kuznetsov.loyaltymanagement.views;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import ru.kuznetsov.loyaltymanagement.domain.Balance;
import ru.kuznetsov.loyaltymanagement.domain.Customer;
import ru.kuznetsov.loyaltymanagement.repositories.BalanceChangeRepository;
import ru.kuznetsov.loyaltymanagement.repositories.BalanceRepository;
import ru.kuznetsov.loyaltymanagement.repositories.CustomerRepository;

import java.math.BigInteger;
import java.time.LocalDate;

@SpringComponent
@UIScope
public class CustomerEditor extends VerticalLayout implements KeyNotifier {

    private final CustomerRepository customerRepository;
    private final BalanceRepository balanceRepository;

    private Customer customer;
    private ChangeHandler changeHandler;

    private TextField firstName = new TextField("Фамилия");
    private TextField middleName = new TextField("Имя");
    private TextField lastName = new TextField("Отчество");
    private RadioButtonGroup<String> gender = new RadioButtonGroup<>();
    private DatePicker birthday = new DatePicker();

    private Button save = new Button("Сохранить", VaadinIcon.CHECK.create());
    private Button cancel = new Button("Отмена");
    private Button delete = new Button("Удалить", VaadinIcon.TRASH.create());

    private HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    private Binder<Customer> binder = new Binder<>(Customer.class);

    @Autowired
    public CustomerEditor(CustomerRepository customerRepository, BalanceRepository balanceRepository) {
        this.customerRepository = customerRepository;
        this.balanceRepository = balanceRepository;

        gender.setLabel("Пол");
        gender.setItems("Мужской", "Женский");
        gender.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);

        birthday.setLabel("Дата рождения");

        binder.bindInstanceFields(this);
        add(firstName, middleName, lastName, gender, birthday, actions);

        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");
        addKeyPressListener(Key.ENTER, e -> save());
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editCustomer(customer));

        setVisible(false);
    }

    void delete() {
        customerRepository.delete(customer);
        changeHandler.onChange();
    }

    void save() {
        customer.setRegisteredDate(LocalDate.now());
        customerRepository.save(customer);
        balanceRepository.save(new Balance(null, customer.getId(), new BigInteger("0")));
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editCustomer(Customer editedCustomer) {
        if (editedCustomer == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = editedCustomer.getId() != null;

        if (persisted) {
            customer = customerRepository.findById(editedCustomer.getId()).get();
        } else {
            customer = editedCustomer;
        }

        cancel.setVisible(persisted);
        binder.setBean(customer);

        setVisible(true);

        firstName.focus();
    }

    public void setChangeHandler(ChangeHandler handler) {
        changeHandler = handler;
    }

}
