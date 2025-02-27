package cz.jaro.wallet.model;


import lombok.*;

import java.util.Date;


/**
 * At the end of _date_, the balance was _balance_.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DateBalance {

    private Date date;

    private Amount amount;

}
