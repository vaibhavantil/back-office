package com.hedvig.backoffice.services.questions;

import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.domain.Question;
import com.hedvig.backoffice.domain.Subscription;
import com.hedvig.backoffice.repository.QuestionRepository;
import com.hedvig.backoffice.services.chat.SubscriptionService;
import com.hedvig.backoffice.services.messages.dto.BotMessage;
import com.hedvig.backoffice.services.questions.dto.QuestionDTO;
import com.hedvig.backoffice.services.updates.UpdateType;
import com.hedvig.backoffice.services.updates.UpdatesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    private static Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);

    private final QuestionRepository questionRepository;
    private final SubscriptionService subscriptionService;
    private final UpdatesService updatesService;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository, SubscriptionService subscriptionService,
                               UpdatesService updatesService) {
        this.questionRepository = questionRepository;
        this.subscriptionService = subscriptionService;
        this.updatesService = updatesService;
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
    public void answer(Long id, BotMessage message, Personnel personnel) throws QuestionNotFoundException {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException("question with id " + id + " not found"));

        question.setAnswer(message.getMessage().toString());
        question.setPersonnel(personnel);
        question.setAnswerDate(message.getTimestamp());

        questionRepository.save(question);
    }

    @Transactional
    @Override
    public void answer(String hid, BotMessage message, Personnel personnel) {
        List<Question> questions = questionRepository.findUnasweredByHid(hid);
        if (questions.size() > 0) {
            questions.forEach(q -> {
                q.setAnswerDate(message.getTimestamp());
                q.setAnswer(message.getMessage().toString());
                q.setPersonnel(personnel);
            });
            questionRepository.save(questions);
            updatesService.change(-questions.size(), UpdateType.QUESTIONS);
        }
    }

    @Transactional
    @Override
    public void addNewQuestions(List<QuestionDTO> questions) {
        if (questions.size() == 0) return;

        for (QuestionDTO dto : questions) {
            Subscription sub = subscriptionService.getOrCreateSubscription(dto.getHid());
            Question question = QuestionDTO.toDomain(dto);
            question.setSubscription(sub);
            questionRepository.save(question);
        }

        long count = Optional.ofNullable(questionRepository.notAnsweredCount()).orElse(0L);
        updatesService.set(count, UpdateType.QUESTIONS);
    }

}
