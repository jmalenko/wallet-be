package cz.jaro.wallet.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

import java.io.Serializable;
import java.util.Date;


@Entity
@DiscriminatorValue("EXTERNAL")

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class TransactionExternal extends Transaction implements Serializable {

    private String counterpartyAccount;

    public TransactionExternal(Long id, Account account, Amount amount, String reference, Date created, String counterpartyAccount) {
        super(id, account, amount, reference, created);
        this.counterpartyAccount = counterpartyAccount;
    }

}
