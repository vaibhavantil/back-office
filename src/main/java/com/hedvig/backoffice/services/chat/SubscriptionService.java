package com.hedvig.backoffice.services.chat;

import com.hedvig.backoffice.domain.Subscription;

public interface SubscriptionService {

  Subscription getOrCreateSubscription(String memberId);
}
