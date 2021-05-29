package ru.kuznetsov.loyaltymanagement.services.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.loyaltymanagement.dao.domain.Balance;
import ru.kuznetsov.loyaltymanagement.dao.domain.BalanceChange;
import ru.kuznetsov.loyaltymanagement.dao.repositories.BalanceChangeRepository;
import ru.kuznetsov.loyaltymanagement.dao.repositories.BalanceRepository;
import ru.kuznetsov.loyaltymanagement.dao.repositories.CustomerRepository;
import ru.kuznetsov.loyaltymanagement.utils.BalanceChanger;
import ru.kuznetsov.loyaltymanagement.utils.OperationType;
import ru.kuznetsov.loyaltymanagement.utils.ResponseTransfer;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping(value = "/service")
public class BalanceController {
    private final BalanceRepository balanceRepository;
    private final CustomerRepository customerRepository;
    private final BalanceChangeRepository balanceChangeRepository;

    @Autowired
    public BalanceController(BalanceRepository balanceRepository, CustomerRepository customerRepository, BalanceChangeRepository balanceChangeRepository) {
        this.balanceRepository = balanceRepository;
        this.customerRepository = customerRepository;
        this.balanceChangeRepository = balanceChangeRepository;
    }

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

    @PostMapping("/balance/{customerId}")
    public ResponseTransfer updateBalanceByCustomerId(@PathVariable Integer customerId, @RequestBody BalanceChanger balanceChanger) {
        if (balanceChanger == null || customerId == null || balanceChanger.getOperation() == null || balanceChanger.getSum() == null) {
            return new ResponseTransfer(new ResponseTransfer("Invalid incoming parameters!"));
        }

        List<Balance> balanceList = balanceRepository.findByCustomerId(customerId);
        if (balanceList == null || balanceList.isEmpty()) {
            return new ResponseTransfer(new ResponseTransfer(String.format("Balance for %d customerId! not found!", customerId)));
        }

        Balance balance = balanceList.iterator().next();
        Integer operationType = balanceChanger.getOperation();
        BigInteger oldBalanceValue = balance.getBalance();
        BigInteger changeSum = new BigInteger("0");

        if (OperationType.SUM.intValue() == operationType.intValue()) {
            balance.setBalance(balance.getBalance().add(balanceChanger.getSum()));
            changeSum = changeSum.add(balanceChanger.getSum());
        } else if (OperationType.DIFFERENCE.intValue() == operationType.intValue()) {
            balance.setBalance(balance.getBalance().subtract(balanceChanger.getSum()).intValue() >= 0 ?
                    balance.getBalance().subtract(balanceChanger.getSum()) : new BigInteger("0"));
            changeSum = oldBalanceValue.subtract(balance.getBalance());
        } else {
            return new ResponseTransfer(new ResponseTransfer("Invalid operation type!"));
        }
        balanceRepository.save(balance);

        BalanceChange balanceChange = new BalanceChange(null, balance.getId(), operationType, changeSum,
                balance.getBalance(), new Date());
        balanceChangeRepository.save(balanceChange);


        return new ResponseTransfer(balance);
    }


}
