package cz.jaro.wallet.model;


import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AccountInfo {

    private String accountId;

    private String currency;

    private Amount balance;

}
