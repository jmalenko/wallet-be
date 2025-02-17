package cz.jaro.homework.model;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.data.relational.core.mapping.Column;


public record KeyValue(
        @ManyToOne
        @JoinColumn(name = "transaction_id")
        Transaction transaction,
        @Column(value = "key_")
        String key,
        String value
) {
}
