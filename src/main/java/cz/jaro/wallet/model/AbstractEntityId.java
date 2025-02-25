package cz.jaro.wallet.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@MappedSuperclass
@SequenceGenerator(name = "sequenceGenerator")

@NoArgsConstructor
@Getter
@Setter
public class AbstractEntityId implements Serializable {

    @Id
    @GeneratedValue(generator = "sequenceGenerator")
    protected Long id;

}