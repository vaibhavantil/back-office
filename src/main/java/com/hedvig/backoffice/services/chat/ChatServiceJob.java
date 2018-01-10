package com.hedvig.backoffice.services.chat;

import com.hedvig.backoffice.domain.ChatContext;
import com.hedvig.backoffice.repository.ChatContextRepository;
import com.hedvig.backoffice.services.messages.BotService;
import com.hedvig.backoffice.services.messages.data.Message;
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
            List<Message> messages = botService.updates(chat.getHid());
            for (Message m : messages) {
                chatService.retranslate(chat.getHid(), m);
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
