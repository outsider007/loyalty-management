package ru.kuznetsov.loyaltymanagement.services.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.loyaltymanagement.dao.domain.Balance;
import ru.kuznetsov.loyaltymanagement.dao.domain.Customer;
import ru.kuznetsov.loyaltymanagement.dao.repositories.BalanceRepository;
import ru.kuznetsov.loyaltymanagement.dao.repositories.CustomerRepository;
import ru.kuznetsov.loyaltymanagement.utils.ResponseTransfer;

import java.math.BigInteger;
import java.time.LocalDate;

@RestController
@RequestMapping("/service/customers")
public class CustomerController {
    private final CustomerRepository customerRepository;
    private final BalanceRepository balanceRepository;

    @Autowired
    public CustomerController(CustomerRepository customerRepository, BalanceRepository balanceRepository) {
        this.customerRepository = customerRepository;
        this.balanceRepository = balanceRepository;
    }

    @GetMapping("/{id}")
    public ResponseTransfer getCustomerByCustomerId(@PathVariable Integer id) {
        return new ResponseTransfer(customerRepository.findById(id));
    }

    @PostMapping("/add")
    public ResponseTransfer addNewCustomer(@RequestBody Customer customer) {
        if (customer.getFirstName() != null && customer.getBirthday() != null && customer.getPhoneNumber() != null) {
            customer.setRegisteredDate(LocalDate.now());
            customerRepository.save(customer);
            balanceRepository.save(new Balance(null, customer.getId(), new BigInteger("0")));
            return new ResponseTransfer(customer);
        } else {
            return new ResponseTransfer("Invalid incoming parameters! Fields firstName and birthday must not be null");
        }
    }

}
