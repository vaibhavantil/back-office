package com.hedvig.backoffice.services.questions;

import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.domain.Question;
import com.hedvig.backoffice.repository.QuestionRepository;
import com.hedvig.backoffice.services.chat.ChatService;
import com.hedvig.backoffice.services.messages.dto.BotMessage;
import com.hedvig.backoffice.services.questions.dto.QuestionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    private static Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);

    private final QuestionRepository questionRepository;
    private final ChatService chatService;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository, ChatService chatService) {
        this.questionRepository = questionRepository;
        this.chatService = chatService;
    }

    @Override
    public List<QuestionDTO> list() {
        return questionRepository
                .findAll()
                .stream()
                .map(QuestionDTO::fromDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<QuestionDTO> answered() {
        return questionRepository
                .answered()
                .stream()
                .map(QuestionDTO::fromDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<QuestionDTO> notAnswered() {
        return questionRepository
                .notAnswered()
                .stream()
                .map(QuestionDTO::fromDomain)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public boolean answer(Long id, BotMessage message, Personnel personnel) throws QuestionNotFoundException {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException("question with id " + id + " not found"));

        if (!chatService.append(question.getHid(), message)) {
            return false;
        }

        question.setAnswer(message.getMessage().toString());
        question.setPersonnel(personnel);
        question.setTimestamp(new Date().toInstant());
        questionRepository.save(question);

        return true;
    }

    @Transactional
    @Override
    public void save(List<QuestionDTO> questions) {
        questionRepository.save(questions.stream()
                .map(QuestionDTO::toDomain)
                .collect(Collectors.toList()));
    }

}
