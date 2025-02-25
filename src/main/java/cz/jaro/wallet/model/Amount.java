package cz.jaro.wallet.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Amount {

    private Long whole;

    private Long decimal;

}

