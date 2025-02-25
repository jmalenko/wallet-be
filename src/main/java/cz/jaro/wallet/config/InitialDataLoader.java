package cz.jaro.wallet.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.jaro.wallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * Loads initial data when the database is empty).
 */
@Component
@RequiredArgsConstructor
public class InitialDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            try (InputStream inputStream = TypeReference.class.getResourceAsStream("content.json")) {
//                userRepository.saveAll(objectMapper.readValue(inputStream,new TypeReference<List<User>>(){}));
            }
        }
    }

}

