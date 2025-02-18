package cz.jaro.homework.controller;

import cz.jaro.homework.model.KeyValue;
import cz.jaro.homework.model.Transaction;
import cz.jaro.homework.repository.TransactionRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionRepository repository;

    @GetMapping("/all")
    public List<Transaction> findAll() {
        return repository.findAll();
    }

    @GetMapping("/count")
    public Long count() {
        return repository.count();
    }

    @GetMapping("/{id}")
    public Optional<Transaction> findById(@PathVariable Long id) {
        return Optional.ofNullable(repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found.")));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction create(@Valid @RequestBody Transaction transaction) {
        try {
            // Synchronize bidirectional relations
            for (KeyValue keyValue : transaction.getData()) {
                keyValue.setTransaction(transaction);
            }

            return repository.save(transaction);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A transaction with this id already exists.");
        } catch (HttpMessageNotReadableException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Cannot deserialize transaction.", e);
        }
    }

    @PutMapping("")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Transaction update(@Valid @RequestBody Transaction transaction) {
        Transaction existingTransaction = repository.findById(transaction.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found in the repository."));

        existingTransaction.disconnectBidirectionalRelationships();

        existingTransaction.setTimestamp(transaction.getTimestamp());
        existingTransaction.setType(transaction.getType());
        existingTransaction.setActor(transaction.getActor());
        for (KeyValue keyValue : transaction.getData()) {
            existingTransaction.addData(keyValue);
        }

        return repository.save(existingTransaction);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        Transaction transaction = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found in the repository."));

        transaction.disconnectBidirectionalRelationships();

        repository.deleteById_Query(id);
    }

//    @GetMapping("/search/timestamp/{from}/{to}")
//    public List<Transaction> filterByTimestampRange(@PathVariable String from, @PathVariable String to) {
//        try {
//            return repository.findAllByTimestampRange(Long.decode(from), Long.decode(to));
//        } catch (NumberFormatException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Range limit is not a number.");
//        }
//    }
//
//    @GetMapping("/search/type/{type}")
//    public List<Transaction> filterByType(@PathVariable String type) {
//        return repository.findAllByType(type);
//    }
//
@GetMapping("/search/data/{key}")
public List<Transaction> searchByKey(@PathVariable String key) {
    return repository.findAllByDataKey(key);
}


}
