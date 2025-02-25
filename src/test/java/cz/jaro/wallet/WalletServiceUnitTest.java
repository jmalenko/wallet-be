package cz.jaro.wallet;

import cz.jaro.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WalletServiceUnitTest {

    @Autowired
    WalletService walletService;

//    @Test
//    void increaseCounter() {
//        long id = new Random().nextInt(10000);
//        User user = new User(id, 101L, "counter", "actor1", new ArrayList<>());
//
//        walletService.increaseCounter(user);
//
//        assertThat(user.getAccounts().size()).isEqualTo(2);
//
//        Account accountCounter = walletService.getTransactionDataKey(user, "counter");
//        assertThat(accountCounter.getCurency()).isEqualTo("1");
//
//        Account accountModified = walletService.getTransactionDataKey(user, "modified");
//        assertThat(accountModified).isNotNull();
//
//        walletService.increaseCounter(user);
//
//        Account accountCounter2 = walletService.getTransactionDataKey(user, "counter");
//        assertThat(accountCounter2.getCurency()).isEqualTo("2");
//
//        Account accountModified2 = walletService.getTransactionDataKey(user, "modified");
//        assertThat(accountModified2).isNotNull();
//    }
//
//    @Test
//    void increaseCounterNotANumber() {
//        long id = new Random().nextInt(10000);
//        User user = new User(id, 101L, "counter", "actor1", new ArrayList<>());
//        user.addAccount(new Account(null, null, "counter", "X"));
//
//        walletService.increaseCounter(user);
//
//        assertThat(user.getAccounts().size()).isEqualTo(2);
//
//        Account accountCounter = walletService.getTransactionDataKey(user, "counter");
//        assertThat(accountCounter.getCurency()).isEqualTo("1");
//
//        Account accountModified = walletService.getTransactionDataKey(user, "modified");
//        assertThat(accountModified).isNotNull();
//    }

}
