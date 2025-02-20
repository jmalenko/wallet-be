package cz.jaro.transaction.service;

import cz.jaro.transaction.model.KeyValue;
import cz.jaro.transaction.model.Transaction;
import cz.jaro.transaction.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class TransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    private static final String TYPE = "counter";

    private static final String KEY_COUNTER = "counter";
    private static final String KEY_MODIFIED = "modified";

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public int increaseCounters() {
        List<Transaction> transactions = transactionRepository.findAllByType(TYPE);

        // Business logic
        log.info("Processing " + transactions.size() + " transactions");
        transactions.forEach(this::increaseCounter);

        return transactions.size();
    }

    public void increaseCounter(Transaction transaction) {
        // Increase counter
        KeyValue keyValueCounter = getTransactionDataKey(transaction, KEY_COUNTER);
        if (keyValueCounter == null) {
            keyValueCounter = new KeyValue(null, null, KEY_COUNTER, String.valueOf(1));
            transaction.addData(keyValueCounter);
        } else {
            try {
                long value = Long.decode(keyValueCounter.getValue());
                value++;
                keyValueCounter.setValue(String.valueOf(value));
            } catch (Exception e) {
                log.debug("Cannot update counter for transaction id " + transaction.getId());
                keyValueCounter.setValue(String.valueOf(1));
            }
        }

        // Set modified
        KeyValue keyValueModified = getTransactionDataKey(transaction, KEY_MODIFIED);
        if (keyValueModified == null) {
            keyValueModified = new KeyValue(null, null, KEY_MODIFIED, null);
            transaction.addData(keyValueModified);
        }
        keyValueModified.setValue(new Date().toString());
    }

    public KeyValue getTransactionDataKey(Transaction transaction, String key) {
        for (KeyValue keyValue : transaction.getData()) {
            if (keyValue.getKey().equals(key))
                return keyValue;
        }
        return null;
    }

}
