package com.hedvig.backoffice.services.chat;

import com.hedvig.backoffice.domain.ChatContext;
import com.hedvig.backoffice.repository.ChatContextRepository;
import com.hedvig.backoffice.services.chat.data.Message;
import com.hedvig.backoffice.services.messages.BotService;
import com.hedvig.backoffice.services.messages.BotServiceException;
import com.hedvig.backoffice.services.messages.BotServiceMessage;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.List;

public class ChatServiceJob extends QuartzJobBean {

    private static Logger logger = LoggerFactory.getLogger(ChatServiceJob.class);

    private ChatService chatService;
    private ChatContextRepository chatContextRepository;
    private BotService botService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        Iterable<ChatContext> chats = chatContextRepository.findAll();
        for (ChatContext chat : chats) {
            List<BotServiceMessage> messages;
            try {
                messages = botService.updates(chat.getHid(), chat.getTimestamp());
            } catch (BotServiceException e) {
                chatService.send(chat.getHid(), Message.error(500, e.getMessage()));
                throw new JobExecutionException(e);
            }

            if (messages.size() > 0) {
                try {
                    chat.setTimestamp(messages.get(messages.size() - 1).getTimestamp());
                } catch (BotServiceException e) {
                    logger.error("bot-service error:", e);
                    chat.setTimestamp(new Date().toInstant());
                }
                chatContextRepository.save(chat);
                chatService.send(chat.getHid(), Message.chat(messages));
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
