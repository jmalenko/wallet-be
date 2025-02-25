package cz.jaro.wallet.repository;

import cz.jaro.wallet.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Result is ordered by created time
    @NativeQuery(
            "FROM transaction t " +
                    "WHERE t.account_id = ?1 " +
                    "ORDER by t.created")
    List<Transaction> findAllByAccountId(Long accountId);
}
