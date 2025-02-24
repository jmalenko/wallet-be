package cz.jaro.wallet.repository;

import cz.jaro.wallet.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByType(String type);

    List<Transaction> findAllByTimestampBetween(Long from, Long to);

    @Modifying
    @Transactional
    @NativeQuery(value = "DELETE FROM transaction WHERE id = ?1")
    void deleteById_Query(Long id);

    @NativeQuery(
            "SELECT t.id, t.timestamp, t.type, t.actor " +
                    "FROM transaction t " +
                    "LEFT JOIN key_value kv ON kv.transaction_id = t.id " +
                    "WHERE kv.key_ = ?1")
    List<Transaction> findAllByDataKey(String key);

}
