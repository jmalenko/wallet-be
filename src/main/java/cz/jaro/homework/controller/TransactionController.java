package cz.jaro.homework.controller;

import cz.jaro.homework.model.Transaction;
import cz.jaro.homework.repository.TransactionRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionRepository repository;

    public TransactionController(TransactionRepository transactionRepository) {
        this.repository = transactionRepository;
    }

    @GetMapping
    public List<Transaction> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Transaction> findById(@PathVariable Long id) {
        return Optional.ofNullable(repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found.")));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody Transaction transaction) {
        repository.save(transaction);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Transaction transaction, @PathVariable Long id) {
        Transaction existingTransaction = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found."));
        repository.save(transaction);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @GetMapping("/filter/timestamp/{from}/{to}")
    public List<Transaction> filterByTimestampRange(@PathVariable String from, @PathVariable String to) {
        try {
            return repository.findAllByTimestampRange(Long.decode(from), Long.decode(to));
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Range limit is not a number.");
        }
    }

    @GetMapping("/filter/type/{type}")
    public List<Transaction> filterByType(@PathVariable String type) {
        return repository.findAllByType(type);
    }

    @GetMapping("/filter/data/{key}")
    public List<Transaction> filterByKey(@PathVariable String key) {
        return repository.findAllByDataKey(key);
    }


}
