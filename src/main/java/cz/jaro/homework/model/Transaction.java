package cz.jaro.homework.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import java.util.ArrayList;
import java.util.List;

/*
Implementation notes:
- The id is always defined. This breaks the default implementation of repository.save() that decides whether to use
  INSERT or UPDATE SQL command. In isNew() we force the INSERT and handle duplicate entry by catching the exception.
- Although data is a map in the requirement, we are persisting it as list. This is because JPA doesn't support map.
 */

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Transaction implements Persistable<Long> {
    @Id
    @NotNull
    private Long id;

    private Long timestamp;

    private String type;

    private String actor;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<KeyValue> data = new ArrayList<>();

    public void addData(KeyValue keyValue) {
        data.add(keyValue);
        keyValue.setTransaction(this);
    }

    public void removeData(KeyValue keyValue) {
        data.remove(keyValue);
        keyValue.setTransaction(null);
    }

    public void disconnectBidirectionalRelationships() {
        data.forEach(keyValue -> keyValue.setTransaction(null));
        data.clear();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return true;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", type='" + type + '\'' +
                ", actor='" + actor + '\'' +
                '}';
    }

}

