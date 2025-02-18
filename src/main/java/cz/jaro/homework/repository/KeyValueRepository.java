package cz.jaro.homework.repository;

import cz.jaro.homework.model.KeyValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeyValueRepository extends JpaRepository<KeyValue, Long> {

}
