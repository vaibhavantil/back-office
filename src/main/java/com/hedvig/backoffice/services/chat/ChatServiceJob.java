package com.hedvig.backoffice.services.chat;

import com.hedvig.backoffice.domain.ChatContext;
import com.hedvig.backoffice.repository.ChatContextRepository;
import com.hedvig.backoffice.services.messages.BotService;
import com.hedvig.backoffice.services.messages.BotServiceException;
import com.hedvig.backoffice.services.chat.data.ErrorChatMessage;
import com.hedvig.backoffice.services.chat.data.ChatMessage;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

public class ChatServiceJob extends QuartzJobBean {

    private ChatService chatService;
    private ChatContextRepository chatContextRepository;
    private BotService botService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        Iterable<ChatContext> chats = chatContextRepository.findAll();
        for (ChatContext chat : chats) {
            List<ChatMessage> messages;
            try {
                messages = botService.updates(chat.getHid(), chat.getTimestamp());
            } catch (BotServiceException e) {
                chatService.retranslate(chat.getHid(), new ErrorChatMessage(500, e.getMessage()));
                throw new JobExecutionException(e);
            }

            if (messages.size() > 0) {
                chat.setTimestamp(messages.get(messages.size() - 1).getTimestamp());
                chatContextRepository.save(chat);
                chatService.retranslate(chat.getHid(), messages);
            }
        }
    }

    public void setChatService(ChatService chatService) {
        this.chatService = chatService;
    }

    public void setChatContextRepository(ChatContextRepository chatContextRepository) {
        this.chatContextRepository = chatContextRepository;
    }

    public void setBotService(BotService botService) {
        this.botService = botService;
    }
}
