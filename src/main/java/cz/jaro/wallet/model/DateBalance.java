package cz.jaro.wallet.model;


import lombok.*;

import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DateBalance {

    private Date date;

    private Amount amount;

}
