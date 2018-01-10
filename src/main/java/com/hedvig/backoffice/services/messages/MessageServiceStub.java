package com.hedvig.backoffice.services.messages;

import com.hedvig.backoffice.services.messages.data.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MessageServiceStub implements MessageService {

    private ConcurrentHashMap<String, List<Message>> messages;

    @Autowired
    public MessageServiceStub() {
        this.messages = new ConcurrentHashMap<>();
    }

    @Override
    public List<Message> messages(String hid) {
        return messages.computeIfAbsent(hid, k -> new ArrayList<>());
    }

    @Override
    public List<Message> messages(String hid, int count) {
        List<Message> all = messages.computeIfAbsent(hid, k -> new ArrayList<>());
        if (all.size() <= count) {
            return all;
        }

        return all.subList(all.size() - count, all.size());
    }

    @Override
    public void response(String hid, Message message) {
        List<Message> userMessages = messages.computeIfAbsent(hid, k -> new ArrayList<>());
        userMessages.add(message);
    }
}
