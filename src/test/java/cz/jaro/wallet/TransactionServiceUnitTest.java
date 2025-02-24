package cz.jaro.wallet;

import cz.jaro.wallet.model.KeyValue;
import cz.jaro.wallet.model.Transaction;
import cz.jaro.wallet.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TransactionServiceUnitTest {

    @Autowired
    TransactionService transactionService;

    @Test
    void increaseCounter() {
        long id = new Random().nextInt(10000);
        Transaction transaction = new Transaction(id, 101L, "counter", "actor1", new ArrayList<>());

        transactionService.increaseCounter(transaction);

        assertThat(transaction.getData().size()).isEqualTo(2);

        KeyValue keyValueCounter = transactionService.getTransactionDataKey(transaction, "counter");
        assertThat(keyValueCounter.getValue()).isEqualTo("1");

        KeyValue keyValueModified = transactionService.getTransactionDataKey(transaction, "modified");
        assertThat(keyValueModified).isNotNull();

        transactionService.increaseCounter(transaction);

        KeyValue keyValueCounter2 = transactionService.getTransactionDataKey(transaction, "counter");
        assertThat(keyValueCounter2.getValue()).isEqualTo("2");

        KeyValue keyValueModified2 = transactionService.getTransactionDataKey(transaction, "modified");
        assertThat(keyValueModified2).isNotNull();
    }

    @Test
    void increaseCounterNotANumber() {
        long id = new Random().nextInt(10000);
        Transaction transaction = new Transaction(id, 101L, "counter", "actor1", new ArrayList<>());
        transaction.addData(new KeyValue(null, null, "counter", "X"));

        transactionService.increaseCounter(transaction);

        assertThat(transaction.getData().size()).isEqualTo(2);

        KeyValue keyValueCounter = transactionService.getTransactionDataKey(transaction, "counter");
        assertThat(keyValueCounter.getValue()).isEqualTo("1");

        KeyValue keyValueModified = transactionService.getTransactionDataKey(transaction, "modified");
        assertThat(keyValueModified).isNotNull();
    }

}
