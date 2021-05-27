package ru.kuznetsov.loyaltymanagement.rest.balancecontrol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.loyaltymanagement.rest.dao.domain.Balance;
import ru.kuznetsov.loyaltymanagement.rest.dao.domain.BalanceChange;
import ru.kuznetsov.loyaltymanagement.rest.dao.repositories.BalanceChangeRepository;
import ru.kuznetsov.loyaltymanagement.rest.dao.repositories.BalanceRepository;
import ru.kuznetsov.loyaltymanagement.rest.dao.repositories.CustomerRepository;
import ru.kuznetsov.loyaltymanagement.rest.utils.BalanceChanger;
import ru.kuznetsov.loyaltymanagement.rest.utils.OperationType;
import ru.kuznetsov.loyaltymanagement.rest.utils.ResponseTransfer;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping(value = "/service")
public class ServicesController {
    @Autowired
    private BalanceRepository balanceRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private BalanceChangeRepository balanceChangeRepository;

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
    public ResponseTransfer updateBalanceByCustomerId(@PathVariable Integer customerId, @RequestBody BalanceChanger balanceChanger) {
        if (balanceChanger == null || customerId == null || balanceChanger.getOperationType() == null || balanceChanger.getSumChange() == null) {
            return new ResponseTransfer(new ResponseTransfer("Invalid incoming parameters!"));
        }

        List<Balance> balanceList = balanceRepository.findByCustomerId(customerId);
        if (balanceList == null || balanceList.isEmpty()) {
            return new ResponseTransfer(new ResponseTransfer(String.format("Balance for %d customerId! not found!", customerId)));
        }

        Balance balance = balanceList.iterator().next();
        Integer operationType = balanceChanger.getOperationType();

        if (OperationType.SUM.intValue() == operationType.intValue()) {
            balance.setBalance(balance.getBalance().add(balanceChanger.getSumChange()));
        } else if (OperationType.DIFFERENCE.intValue() == operationType.intValue()) {
            balance.setBalance(balance.getBalance().subtract(balanceChanger.getSumChange()).intValue() >= 0 ?
                    balance.getBalance().subtract(balanceChanger.getSumChange()) : new BigInteger("0"));
        } else {
            return new ResponseTransfer(new ResponseTransfer("Invalid operation type!"));
        }
        balanceRepository.save(balance);

        BalanceChange balanceChange = new BalanceChange(null, balance.getId(), operationType, balanceChanger.getSumChange(),
                balance.getBalance(), new Date());
        balanceChangeRepository.save(balanceChange);


        return new ResponseTransfer(balance);
    }


}
