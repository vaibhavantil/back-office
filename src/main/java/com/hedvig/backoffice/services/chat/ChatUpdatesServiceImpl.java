package com.hedvig.backoffice.services.chat;

import com.hedvig.backoffice.domain.MessageInfo;
import com.hedvig.backoffice.domain.Subscription;
import com.hedvig.backoffice.repository.SubscriptionRepository;
import com.hedvig.backoffice.services.chat.data.Message;
import com.hedvig.backoffice.services.messages.BotService;
import com.hedvig.backoffice.services.messages.BotServiceException;
import com.hedvig.backoffice.services.messages.data.BotServiceMessage;
import com.hedvig.backoffice.services.updates.UpdateType;
import com.hedvig.backoffice.services.updates.UpdatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatUpdatesServiceImpl implements ChatUpdatesService {

    private final ChatService chatService;
    private final SubscriptionRepository subscriptionRepository;
    private final BotService botService;
    private final UpdatesService updatesService;

    @Autowired
    public ChatUpdatesServiceImpl(ChatService chatService,
                                  SubscriptionRepository subscriptionRepository,
                                  BotService botService,
                                  UpdatesService updatesService) {
        this.chatService = chatService;
        this.subscriptionRepository = subscriptionRepository;
        this.botService = botService;
        this.updatesService = updatesService;
    }

    @Override
    public void update() throws ChatUpdateException {
        List<Subscription> subscriptions = subscriptionRepository.findActiveSubscriptions();
        int updatesCount = 0;

        for (Subscription s : subscriptions) {
            List<BotServiceMessage> messages;
            try {
                messages = botService.messages(s.getHid());
            } catch (BotServiceException e) {
                chatService.send(s.getHid(), Message.error(e.getCode(), e.getMessage()));
                throw new ChatUpdateException(e);
            }

            List<BotServiceMessage> updates = messages
                    .stream()
                    .filter(m -> !s.getMessages().containsKey(m.getGlobalId())
                            || s.getMessages().get(m.getGlobalId()).getTimestamp().isBefore(m.getTimestamp()))
                    .collect(Collectors.toList());

            if (updates.size() > 0) {
                updates.forEach(m -> s.getMessages().put(m.getGlobalId(), new MessageInfo(m.getGlobalId(), m.getTimestamp())));
                subscriptionRepository.save(s);

                updatesCount += updates.size();

                chatService.send(s.getHid(), Message.chat(updates));
            }
        }

        updatesService.append(updatesCount, UpdateType.CHATS);
    }
}
