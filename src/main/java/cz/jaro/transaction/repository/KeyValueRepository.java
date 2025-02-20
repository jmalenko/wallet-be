package cz.jaro.transaction.repository;

import cz.jaro.transaction.model.KeyValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeyValueRepository extends JpaRepository<KeyValue, Long> {

}
