package ru.kuznetsov.loyaltymanagement.rest.balancecontrol;

import elemental.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.loyaltymanagement.rest.dao.domain.Customer;
import ru.kuznetsov.loyaltymanagement.rest.dao.repositories.BalanceRepository;
import ru.kuznetsov.loyaltymanagement.rest.dao.repositories.CustomerRepository;
import ru.kuznetsov.loyaltymanagement.rest.utils.ResponseTransfer;


@RestController
@RequestMapping(value = "/service")
public class ServicesController {
    @Autowired
    private BalanceRepository balanceRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/customers")
    public ResponseTransfer users() {
        return new ResponseTransfer(customerRepository.findAll());
    }

    @GetMapping("/balance")
    public ResponseTransfer balance() {
        return new ResponseTransfer(balanceRepository.findAll());
    }

    @GetMapping("/balance/{id}")
    public ResponseTransfer getBalanceById(@PathVariable Integer id) {
        return new ResponseTransfer(balanceRepository.findById(id));
    }

    @GetMapping("/balance/{customerId}")
    public ResponseTransfer getBalanceByCustomerId(@PathVariable Integer customerId) {
        return new ResponseTransfer(balanceRepository.findByCustomerId(customerId));
    }

    @GetMapping("/customers/{id}")
    public ResponseTransfer getCustomerByCustomerId(@PathVariable Integer id) {
        return new ResponseTransfer(customerRepository.findById(id));
    }

    @RequestMapping(value = "/balance/{customerId}", method = RequestMethod.POST)
    public ResponseTransfer updateBalanceByCustomerId(@PathVariable Integer customerId, @RequestBody Customer customer) {
        String result = "Successful";
        customer.toString();

        return new ResponseTransfer(result);
    }


}
