package cz.jaro.wallet.repository;

import cz.jaro.wallet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<User, Long> {

}
