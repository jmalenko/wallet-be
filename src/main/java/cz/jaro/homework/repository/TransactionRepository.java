package cz.jaro.homework.repository;

import cz.jaro.homework.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.transaction.annotation.Transactional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

//    List<Transaction> findAllByType(String type);
//
//    @Query("SELECT * " +
//            "FROM transaction t " +
//            "LEFT JOIN keyvalue kv ON kv.transaction_id = t.id " +
//            "WHERE :from < timestamp AND timestamp <= :to")
//    List<Transaction> findAllByTimestampRange(Long from, Long to);
//    List<Transaction> findAllByTimestampBetween(Long from, Long to);

    @Modifying
    @Transactional
    @NativeQuery(value = "DELETE FROM transaction WHERE id = ?1")
    void deleteById_Query(Long id);

}
