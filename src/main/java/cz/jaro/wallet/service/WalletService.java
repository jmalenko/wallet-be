package cz.jaro.wallet.service;

import cz.jaro.wallet.model.*;
import cz.jaro.wallet.repository.AccountRepository;
import cz.jaro.wallet.repository.TransactionRepository;
import cz.jaro.wallet.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WalletService {

    private static final Logger log = LoggerFactory.getLogger(WalletService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public User createUser() {
        User user = new User();

        // TODO

        User userSaved = userRepository.save(user);

        return user;
    }

    @Transactional
    public Transaction receiveExternal(Account account) {
        return null;
    }

    @Transactional
    public Transaction sendExternal(Account account, String iban, Amount amount, String reference) {
        return null;
    }

    @Transactional
    public Transaction sendInternal(Account accountFrom, Account accountTo, Amount amount, String reference) {
        return null;
    }

    public List<Account> getAccounts(User user) {
        return null;
    }

    public List<Account> getTransactions(Account account) {
        return null;
    }

    public List<DateBalance> getDailyBalances(Account account) {
        return null;
    }

    public Amount getAccountBalance(Account account) {
        List<DateBalance> accountBalances = getDailyBalances(account);
//        DateBalance lastDateAmount = accountBalances.getLast();
//        return lastDateAmount.getAmount();
        return null;
        // TODO balance
    }

    public Amount convert(Amount amountFrom, String currencyFrom, String currencyTo) {
        final double CZK_EUR = 25;

        if (currencyFrom.equals(currencyTo))
            return amountFrom;

        double real = amountToReal(amountFrom);
        Amount amountTo;

        if (currencyFrom.equals("CZK") && currencyTo.equals("EUR")) {
            amountTo = realToAmount(real / CZK_EUR);
        } else if (currencyFrom.equals("EUR") && currencyTo.equals("CZK")) {
            amountTo = realToAmount(real * CZK_EUR);
        } else {
            throw new UnsupportedOperationException("Conversion from " + currencyFrom + " to " + currencyTo + " is not supported");
        }

        return amountTo;
    }

    public double amountToReal(Amount amount) {
        return amount.getWhole() + amount.getDecimal() / 100;
    }

    public Amount realToAmount(Double real) {
        Amount amount = new Amount();

        // TODO use builder

        return amount;
    }

//    // TODO delete
//
//    @Transactional
//    public int increaseCounters() {
//        List<User> users = userRepository.findAllByType(TYPE);
//
//        // Business logic
//        log.info("Processing " + users.size() + " transactions");
//        users.forEach(this::increaseCounter);
//
//        return users.size();
//    }
//
//    public void increaseCounter(User user) {
//        // Increase counter
//        Account accountCounter = getTransactionDataKey(user, KEY_COUNTER);
//        if (accountCounter == null) {
//            accountCounter = new Account(null, null, KEY_COUNTER, String.valueOf(1));
//            user.addAccount(accountCounter);
//        } else {
//            try {
//                long value = Long.decode(accountCounter.getCurency());
//                value++;
//                accountCounter.setCurency(String.valueOf(value));
//            } catch (Exception e) {
//                log.debug("Cannot update counter for transaction id " + user.getId());
//                accountCounter.setCurency(String.valueOf(1));
//            }
//        }
//
//        // Set modified
//        Account accountModified = getTransactionDataKey(user, KEY_MODIFIED);
//        if (accountModified == null) {
//            accountModified = new Account(null, null, KEY_MODIFIED, null);
//            user.addAccount(accountModified);
//        }
//        accountModified.setCurency(new Date().toString());
//    }
//
//    public Account getTransactionDataKey(User user, String key) {
//        for (Account account : user.getAccounts()) {
//            if (account.getKey().equals(key))
//                return account;
//        }
//        return null;
//    }

}
