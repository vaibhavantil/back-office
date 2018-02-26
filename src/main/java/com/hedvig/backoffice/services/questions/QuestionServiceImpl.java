package com.hedvig.backoffice.services.questions;

import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.domain.Question;
import com.hedvig.backoffice.repository.QuestionRepository;
import com.hedvig.backoffice.services.messages.BotService;
import com.hedvig.backoffice.services.messages.BotServiceException;
import com.hedvig.backoffice.services.messages.dto.BotMessage;
import com.hedvig.backoffice.services.questions.dto.QuestionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    private static Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);

    private final QuestionRepository questionRepository;
    private final BotService botService;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository, BotService botService) {
        this.questionRepository = questionRepository;
        this.botService = botService;
    }

    @Override
    public List<QuestionDTO> list() {
        return questionRepository
                .findAll()
                .stream()
                .map(QuestionDTO::fromDomain)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public boolean answer(Long id, BotMessage message, Personnel personnel) throws QuestionNotFoundException {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException("question with id " + id + " not found"));

        try {
            botService.response(question.getHid(), message);
        } catch (BotServiceException e) {
            logger.error("response failed", e);
            return false;
        }

        question.setAnswer(message.getMessage().toString());
        question.setPersonnel(personnel);
        question.setTimestamp(LocalDateTime.now());

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
