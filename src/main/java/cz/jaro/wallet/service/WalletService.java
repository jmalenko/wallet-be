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

import java.util.*;

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
        long randomName = 10 + new Random().nextInt(90);
        ArrayList<Account> accounts = new ArrayList<Account>();

        User user = new User(null, "Name" + randomName, accounts);

        Account accountCZK = new Account(null, "CZK", user, new ArrayList<>());
        Account accountEUR = new Account(null, "EUR", user, new ArrayList<>());

        user.addAccount(accountCZK);
        user.addAccount(accountEUR);

        return userRepository.save(user);
    }

    @Transactional
    public Transaction receiveExternal(Account account) {
        String counterpartyAccountId = "123456";
        String counterpartyReference = "External deposit";
        Amount counterpartyAmount = new Amount(Long.valueOf(100 + new Random().nextInt(900)), 0L); // same currency

        TransactionExternal transaction = new TransactionExternal(null, account, counterpartyAmount, counterpartyReference, new Date(), counterpartyAccountId);
        account.addTransaction(transaction);

        return transactionRepository.save(transaction);
    }

    // For initial sample data load
    @Transactional
    public Transaction receiveExternal(Account account, Amount amount, String reference, Date created, String counterpartyAccountId) {
        TransactionExternal transaction = new TransactionExternal(null, account, amount, reference, created, counterpartyAccountId);
        account.addTransaction(transaction);

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction sendExternal(Account account, Amount amount, String reference, String counterpartyAccountId) throws Exception {
        // FUTURE Check that the account belongs to the (authorized) user
        // Assumption: counterparty account has the same currency

        if (!amountIsPositive(amount))
            throw new Exception("Amount is not positive.");

        if (!amountIsLessThanOrEqual(amount, getAccountBalance(account)))
            throw new Exception("Account balance is lower than the transaction amount.");

        Amount amountWithSign = new Amount(-amount.getWhole(), amount.getDecimal());

        TransactionExternal transaction = new TransactionExternal(null, account, amountWithSign, reference, new Date(), counterpartyAccountId);
        account.addTransaction(transaction);

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction sendInternal(Account account, Amount amount, String reference, Account counterpartyAccount) throws Exception {
        if (!amountIsPositive(amount))
            throw new Exception("Amount is not positive.");

        if (!amountIsLessThanOrEqual(amount, getAccountBalance(account)))
            throw new Exception("Account balance is lower than the transaction amount.");

        if (!account.getCurrency().equals(counterpartyAccount.getCurrency()))
            throw new Exception("Account and counterparty account have different currencies.");

        if (account.getId().equals(counterpartyAccount.getId()))
            throw new Exception("Account and counterparty are the same.");

        Amount amountNegative = amountNegate(amount);
        Date created = new Date();

        TransactionInternal transactionOutgoing = new TransactionInternal(null, account, amountNegative, reference, created, counterpartyAccount);
        account.addTransaction(transactionOutgoing);
        TransactionInternal transactionOutgoingSaved = transactionRepository.save(transactionOutgoing);

        TransactionInternal transactionIncoming = new TransactionInternal(null, counterpartyAccount, amount, reference, created, account);
        counterpartyAccount.addTransaction(transactionIncoming);
        TransactionInternal transactionIncomingSaved = transactionRepository.save(transactionIncoming);

        return transactionOutgoingSaved;
    }

    public List<DateBalance> getDailyBalances(Account account) {
        List<DateBalance> dailyBalances = new ArrayList<DateBalance>();

        List<Transaction> transactions = account.getTransactions();
        transactions.sort(Comparators.CREATED);

        Date date = null;
        Amount amount = null;

        for (Transaction transaction : transactions) {
            if (date == null) {
                date = transaction.getCreated();
                amount = transaction.getAmount();
            } else {
                if (isDateSame(date, transaction.getCreated())) {
                    amount = amountAdd(amount, transaction.getAmount());
                } else {
                    DateBalance dateBalance = new DateBalance(new Date(date.getYear(), date.getMonth(), date.getDay(), 0, 0, 0), new Amount(amount.getWhole(), amount.getDecimal()));
                    dailyBalances.add(dateBalance);

                    date = transaction.getCreated();
                    amount = transaction.getAmount();
                }
            }
        }

        if (date != null) {
            DateBalance dateBalance = new DateBalance(new Date(date.getYear(), date.getMonth(), date.getDay(), 0, 0, 0), new Amount(amount.getWhole(), amount.getDecimal()));
            dailyBalances.add(dateBalance);
        }

        return dailyBalances;
    }

    public Amount getAccountBalance(Account account) {
        List<DateBalance> dailyBalances = getDailyBalances(account);

        if (dailyBalances.isEmpty()) {
            return new Amount(0L, 0L);
        } else {
            DateBalance last = dailyBalances.get(dailyBalances.size() - 1);
            return last.getAmount();
        }
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

    // Amount representation rules:
    // - decimal is always positive
    // - whole is signed

    public double amountToReal(Amount amount) {
        return amount.getWhole() + amount.getDecimal() / 100.0 * (amountIsPositive(amount) ? 1 : -1);
    }

    public Amount realToAmount(Double real) {
        long whole = real.longValue();
        long decimal = (long) (real - whole) * 100;
        return new Amount(whole, decimal);
    }

    public Amount pairToAmount(Long whole, Long decimal) {
        return new Amount(whole, Math.abs(decimal));
    }

    public long amountToLong(Amount amount) {
        return 100 * amount.getWhole() + amount.getDecimal();
    }

    public Amount longToAmount(Long number) {
        long decimal = number % 100;
        long whole = (number - decimal) / 100;
        return new Amount(whole, decimal);
    }

    public boolean amountIsPositive(Amount amount) {
        return 0 < amount.getWhole();
    }

    public boolean amountIsLessThanOrEqual(Amount amount1, Amount amount2) {
        return amount1.getWhole() < amount2.getWhole() ||
                (amount1.getWhole() == amount2.getWhole() && amount1.getDecimal() <= amount2.getDecimal());
    }

    public Amount amountAdd(Amount amount1, Amount amount2) {
        long l1 = amountToLong(amount1);
        long l2 = amountToLong(amount2);

        long res = l1 + l2;

        return longToAmount(res);
    }

    public Amount amountNegate(Amount amount) {
        return new Amount(-amount.getWhole(), amount.getDecimal());
    }

    private boolean isDateSame(Date d1, Date d2) {
        return d1.getYear() == d2.getYear() &&
                d1.getMonth() == d2.getMonth() &&
                d1.getDay() == d2.getDay();
    }

    public static class Comparators {
        public static final Comparator<Transaction> CREATED = (Transaction t1, Transaction t2) -> t1.compareTo(t2);
    }

}
