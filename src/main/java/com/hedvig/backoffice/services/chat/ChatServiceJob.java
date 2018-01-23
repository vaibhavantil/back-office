package com.hedvig.backoffice.services.chat;

import com.hedvig.backoffice.domain.MessageInfo;
import com.hedvig.backoffice.domain.Subscription;
import com.hedvig.backoffice.repository.SubscriptionRepository;
import com.hedvig.backoffice.services.chat.data.Message;
import com.hedvig.backoffice.services.messages.BotService;
import com.hedvig.backoffice.services.messages.BotServiceException;
import com.hedvig.backoffice.services.messages.data.BotServiceMessage;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;
import java.util.stream.Collectors;

public class ChatServiceJob extends QuartzJobBean {

    private ChatService chatService;
    private SubscriptionRepository subscriptionRepository;
    private BotService botService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        List<Subscription> subscriptions = subscriptionRepository.findActiveSubscriptions();

        for (Subscription s : subscriptions) {
            List<BotServiceMessage> messages;
            try {
                messages = botService.messages(s.getHid());
            } catch (BotServiceException e) {
                chatService.send(s.getHid(), Message.error(e.getCode(), e.getMessage()));
                throw new JobExecutionException(e);
            }

            List<BotServiceMessage> updates = messages
                    .stream()
                    .filter(m -> !s.getMessages().containsKey(m.getGlobalId())
                            || s.getMessages().get(m.getGlobalId()).getTimestamp().isBefore(m.getTimestamp()))
                    .collect(Collectors.toList());

            if (updates.size() > 0) {
                updates.forEach(m -> s.getMessages().put(m.getGlobalId(), new MessageInfo(m.getGlobalId(), m.getTimestamp())));
                subscriptionRepository.save(s);

                chatService.send(s.getHid(), Message.chat(updates));
            }
        }
    }

    public void setChatService(ChatService chatService) {
        this.chatService = chatService;
    }

    public void setSubscriptionRepository(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public void setBotService(BotService botService) {
        this.botService = botService;
    }
}
