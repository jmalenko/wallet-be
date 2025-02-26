package cz.jaro.wallet.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "transaction_type", discriminatorType = DiscriminatorType.STRING)
@JsonIgnoreProperties("account")

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Transaction implements Comparable<Transaction> {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnoreProperties("account_id")
    private Account account;

    @Embedded
    private Amount amount;

    private String reference;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Override
    public int compareTo(Transaction t2) {
        return created.compareTo(t2.getCreated());
    }
}
