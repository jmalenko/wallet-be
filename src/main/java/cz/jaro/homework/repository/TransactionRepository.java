package cz.jaro.homework.repository;

import cz.jaro.homework.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

//    List<Transaction> findAllByType(String type);
//
//    @Query("SELECT * " +
//            "FROM transaction t " +
//            "LEFT JOIN keyvalue kv ON kv.transaction_id = t.id " +
//            "WHERE :from < timestamp AND timestamp <= :to")
//    List<Transaction> findAllByTimestampRange(Long from, Long to);
//
//    @Query("SELECT * " +
//            "FROM transaction t " +
//            "LEFT JOIN keyvalue kv ON kv.transaction_id = t.id " +
//            "WHERE kv.key_ = :key")
//    List<Transaction> findAllByDataKey(String key);

}
