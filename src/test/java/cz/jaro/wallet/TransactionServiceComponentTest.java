package cz.jaro.wallet;

import cz.jaro.wallet.model.Transaction;
import cz.jaro.wallet.repository.TransactionRepository;
import cz.jaro.wallet.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceComponentTest {

    @Mock
    TransactionRepository transactionRepository;

    @InjectMocks
    TransactionService transactionService;

    @Test
    public void increaseCounters() {
        long id = new Random().nextInt(10000);
        Transaction transaction = new Transaction(id, System.currentTimeMillis(), "counter", "actor1", new ArrayList<>());
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        when(transactionRepository.findAllByType(any(String.class))).thenReturn(transactions);

        int count = transactionService.increaseCounters();

        verify(transactionRepository, times(count)).findAllByType(any(String.class));

        assertThat(count).isEqualTo(1);
    }

}
