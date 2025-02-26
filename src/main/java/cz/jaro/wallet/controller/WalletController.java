package cz.jaro.wallet.controller;

import cz.jaro.wallet.model.*;
import cz.jaro.wallet.repository.AccountRepository;
import cz.jaro.wallet.repository.TransactionRepository;
import cz.jaro.wallet.repository.UserRepository;
import cz.jaro.wallet.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin
@RequestMapping("/wallet")
public class WalletController {

    private static final Logger log = LoggerFactory.getLogger(WalletController.class);

    @Autowired
    private WalletService service;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/createUser")
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser() {
        return service.createUser();
    }

    private static Amount pairToAmount(String amountWhole, String amountDecimal) {
        return new Amount(Long.valueOf(amountWhole), Long.valueOf(amountDecimal));
    }

    @GetMapping("/receiveExternal/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public Transaction receiveExternal(@PathVariable Long accountId) {
        try {
            Account account = accountRepository.findById(accountId).get();
            return service.receiveExternal(account);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account does not exist.", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Cannot process transaction.", e);
        }
    }

    @GetMapping("/sendExternal/{accountId}/{counterpartyAccountId}/{amountWhole}/{amountDecimal}/{reference}")
    @ResponseStatus(HttpStatus.OK)
    public Transaction sendExternal(@PathVariable Long accountId, @PathVariable String counterpartyAccountId, @PathVariable String amountWhole, @PathVariable String amountDecimal, @PathVariable String reference) {
        try {
            Account account = accountRepository.findById(accountId).get();
            Amount amount = pairToAmount(amountWhole, amountDecimal);
            return service.sendExternal(account, amount, reference, counterpartyAccountId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account does not exist.", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage(), e);
        }
    }

    @GetMapping("/sendInternal/{accountId}/{counterpartyAccountId}/{amountWhole}/{amountDecimal}/{reference}")
    @ResponseStatus(HttpStatus.OK)
    public Transaction sendInternal(@PathVariable Long accountId, @PathVariable Long counterpartyAccountId, @PathVariable String amountWhole, @PathVariable String amountDecimal, @PathVariable String reference) {
        try {
            Account account = accountRepository.findById(accountId).get();
            Account counterpartyAccount = accountRepository.findById(counterpartyAccountId).get();
            Amount amount = pairToAmount(amountWhole, amountDecimal);
            return service.sendInternal(account, amount, reference, counterpartyAccount);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account does not exist.", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage(), e);
        }
    }

    @GetMapping("/getAccounts/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Account> getAccounts(@PathVariable Long userId) {
        try {
            // FUTURE Check that the userId corresponds to the (authorized) user

            User user = userRepository.findById(userId).get();
            return user.getAccounts();
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist.", e);
        }
    }

    @GetMapping("/getTransactions/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Transaction> getTransactions(@PathVariable Long accountId) {
        try {
            // FUTURE Check that the account belongs to the (authorized) user

            Account account = accountRepository.findById(accountId).get();
            List<Transaction> transactions = account.getTransactions();
            transactions.sort(WalletService.Comparators.CREATED);
            return transactions;
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account does not exist.", e);
        }
    }

    @GetMapping("/getAccountInfo/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public AccountInfo getAccountInfo(@PathVariable Long accountId) {
        try {
            Account account = accountRepository.findById(accountId).get();
            Amount balance = service.getAccountBalance(account);
            return new AccountInfo(account.getId().toString(), account.getCurrency(), balance);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account does not exist.", e);
        }
    }

    @GetMapping("/getDailyBalances/{accountId}")
    @ResponseStatus(HttpStatus.OK)
    public List<DateBalance> getDailyBalances(@PathVariable Long accountId) {
        try {
            Account account = accountRepository.findById(accountId).get();
            return service.getDailyBalances(account);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account does not exist.", e);
        }
    }

    // Debug

    @GetMapping("/getUsers")
    @ResponseStatus(HttpStatus.CREATED)
    public List<User> allUsers() {
        return userRepository.findAll();
    }

}
