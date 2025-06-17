package com.JEE.demo.service;

import com.JEE.demo.entity.Loan;
import com.JEE.demo.service.observer.LoanObserver;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService implements LoanObserver {

    private static NotificationService INSTANCE;
    private static final Logger LOG = LoggerFactory.getLogger(NotificationService.class);

    @PostConstruct
    private void init() { INSTANCE = this; }

    public static NotificationService getInstance() { return INSTANCE; }

    @Override
    public void onCreated(Loan loan) {
        LOG.info("Notification : {} a emprunté «{}»",
                loan.getUser().getEmail(), loan.getBook().getTitle());
    }
}
