package cz.jaro.wallet.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

import java.io.Serializable;
import java.util.Date;


@Entity
@DiscriminatorValue("INTERNAL")

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class TransactionInternal extends Transaction implements Serializable {

    @JsonIgnoreProperties({"transactions", "currency"})
    private Account counterpartyAccount;

    public TransactionInternal(Long id, Account account, Amount amount, String reference, Date created, Account counterpartyAccount) {
        super(id, account, amount, reference, created);
        this.counterpartyAccount = counterpartyAccount;
    }


}
