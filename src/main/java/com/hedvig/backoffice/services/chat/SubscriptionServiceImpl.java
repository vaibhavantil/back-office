package com.hedvig.backoffice.services.chat;

import com.hedvig.backoffice.domain.Subscription;
import com.hedvig.backoffice.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public Subscription getOrCreateSubscription(String hid) {
        return subscriptionRepository.findByHid(hid).orElseGet(() -> {
            Subscription newSub = new Subscription(hid);
            subscriptionRepository.save(newSub);
            return newSub;
        });
    }
}
