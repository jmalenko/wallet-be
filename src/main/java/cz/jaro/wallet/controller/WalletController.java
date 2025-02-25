package cz.jaro.wallet.controller;

import cz.jaro.wallet.model.*;
import cz.jaro.wallet.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class WalletController {

    private static final Logger log = LoggerFactory.getLogger(WalletController.class);

    @Autowired
    private WalletService service;

    @GetMapping("/createUser")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser() {
        return service.createUser();
    }

    @GetMapping("/receiveExternal")
    @ResponseStatus(HttpStatus.OK)
    public Transaction receiveExternal(Account account) {
        return service.receiveExternal(account);
    }

    @GetMapping("/sendExternal")
    @ResponseStatus(HttpStatus.OK)
    public Transaction sendExternal(Account account, String iban, Amount amount, String reference) {
        return service.sendExternal(account, iban, amount, reference);
    }

    @GetMapping("/sendInternal")
    @ResponseStatus(HttpStatus.OK)
    public Transaction sendInternal(Account accountFrom, Account accountTo, Amount amount, String reference) {
        return service.sendInternal(accountFrom, accountTo, amount, reference);
    }

    @GetMapping("/getAccounts")
    @ResponseStatus(HttpStatus.OK)
    public List<Account> getAccounts(User user) {
        return service.getAccounts(user);
    }

    @GetMapping("/getTransactions")
    @ResponseStatus(HttpStatus.OK)
    public List<Account> getTransactions(Account account) {
        return null;
    }

    @GetMapping("/getDailyBalances")
    @ResponseStatus(HttpStatus.OK)
    public List<DateBalance> getDailyBalances(Account account) {
        return service.getDailyBalances(account);
    }
}
