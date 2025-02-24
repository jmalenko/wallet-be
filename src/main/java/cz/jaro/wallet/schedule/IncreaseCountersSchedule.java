package cz.jaro.wallet.schedule;

import cz.jaro.wallet.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class IncreaseCountersSchedule {
    private static final Logger log = LoggerFactory.getLogger(IncreaseCountersSchedule.class);

    @Autowired
    private TransactionService transactionService;

    @Scheduled(fixedRate = 10000)
    public void increaseCounters() {
        log.info("Processing of new applications starts at " + new Date());
        transactionService.increaseCounters();
    }
}
