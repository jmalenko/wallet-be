package cz.jaro.homework.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import org.springframework.data.annotation.Id;

import java.util.Set;


public record Transaction(
        @Id
        Long id,
        Long timestamp,
        String type,
        String actor,
        @OneToMany(mappedBy = "transaction_id", cascade = CascadeType.ALL, orphanRemoval = true)
        Set<KeyValue> data
) {
}

