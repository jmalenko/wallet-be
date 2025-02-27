package cz.jaro.wallet.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.jaro.wallet.model.Account;
import cz.jaro.wallet.model.Amount;
import cz.jaro.wallet.model.User;
import cz.jaro.wallet.repository.UserRepository;
import cz.jaro.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Loads initial data when the database is empty).
 */
@Component
@Slf4j

@RequiredArgsConstructor
public class InitialDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    private final WalletService walletService;

    @Override
    public void run(String... args) throws Exception {
//        userRepository.deleteAll();

        if (userRepository.count() == 0) {
            log.info("Initializing database");

            // JSON from a file
//            try (InputStream inputStream = TypeReference.class.getResourceAsStream("content.json")) {
//                userRepository.saveAll(objectMapper.readValue(inputStream, new TypeReference<List<User>>() {}));
//            }

            // Build some entities

            // User with internal transactions
            User user1 = walletService.createUser();
            for (Account account : user1.getAccounts()) {
                walletService.receiveExternal(account);
            }
            userRepository.save(user1);

            // User with no transactions
            User user2 = walletService.createUser();
            userRepository.save(user2);

            // Internal transaction between users
            Account account1 = null;
            for (Account account : user1.getAccounts()) {
                if (account.getCurrency().equals("CZK"))
                    account1 = account;
            }

            Account account2 = null;
            for (Account account : user2.getAccounts()) {
                if (account.getCurrency().equals("CZK"))
                    account2 = account;
            }

            walletService.sendInternal(account1, new Amount(12L, 4L), "First internal transfer", account2);

            // External transaction
            walletService.sendExternal(account1, new Amount(1L, 0L), "External withdrawal", "CZ1234");

            // Daily transactions
            Account account3 = null;
            for (Account account : user2.getAccounts()) {
                if (account.getCurrency().equals("EUR"))
                    account3 = account;
            }

            for (int day = 1; day <= 20; day++) {
                for (int count = 1; count <= day % 3; count++) {
                    LocalDateTime localDateTime = LocalDateTime.of(2020, 1, day, 5 + count, 0, 0);
                    Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                    walletService.receiveExternal(account3, new Amount(Long.valueOf(day), Long.valueOf(count)), "Deposit", date, "DE123");
                }
            }
        }
    }

}

