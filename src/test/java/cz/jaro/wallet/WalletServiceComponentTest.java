package cz.jaro.wallet;

import cz.jaro.wallet.repository.UserRepository;
import cz.jaro.wallet.service.WalletService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class WalletServiceComponentTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    WalletService transactionService;

//    @Test
//    public void increaseCounters() {
//        long id = new Random().nextInt(10000);
//        User user = new User(id, System.currentTimeMillis(), "counter", "actor1", new ArrayList<>());
//        List<User> users = new ArrayList<>();
//        users.add(user);
//
//        when(userRepository.findAllByType(any(String.class))).thenReturn(users);
//
//        int count = transactionService.increaseCounters();
//
//        verify(userRepository, times(count)).findAllByType(any(String.class));
//
//        assertThat(count).isEqualTo(1);
//    }

}
