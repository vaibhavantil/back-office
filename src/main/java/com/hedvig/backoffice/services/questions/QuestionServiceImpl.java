package com.hedvig.backoffice.services.questions;

import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.domain.Question;
import com.hedvig.backoffice.domain.QuestionGroup;
import com.hedvig.backoffice.domain.Subscription;
import com.hedvig.backoffice.repository.QuestionGroupRepository;
import com.hedvig.backoffice.repository.QuestionRepository;
import com.hedvig.backoffice.services.chat.SubscriptionService;
import com.hedvig.backoffice.services.messages.BotService;
import com.hedvig.backoffice.services.messages.dto.BotMessage;
import com.hedvig.backoffice.services.questions.dto.QuestionGroupDTO;
import com.hedvig.backoffice.services.updates.UpdateType;
import com.hedvig.backoffice.services.updates.UpdatesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    private final QuestionGroupRepository questionGroupRepository;

    private final SubscriptionService subscriptionService;

    private final UpdatesService updatesService;

    private final BotService botService;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository,
                               QuestionGroupRepository questionGroupRepository,
                               SubscriptionService subscriptionService,
                               UpdatesService updatesService,
                               BotService botService) {

        this.questionRepository = questionRepository;
        this.questionGroupRepository = questionGroupRepository;
        this.subscriptionService = subscriptionService;
        this.updatesService = updatesService;
        this.botService = botService;
    }

    @Override
    public List<QuestionGroupDTO> list() {
        return questionGroupRepository
                .findAll()
                .stream()
                .map(QuestionGroupDTO::fromDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<QuestionGroupDTO> answered() {
        return questionGroupRepository
                .answered()
                .stream()
                .map(QuestionGroupDTO::fromDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<QuestionGroupDTO> notAnswered() {
        return questionGroupRepository
                .notAnswered()
                .stream()
                .map(QuestionGroupDTO::fromDomain)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void answer(String hid, String message, Personnel personnel) throws QuestionNotFoundException {
        QuestionGroup group = questionGroupRepository.findUnasweredByHid(hid).orElseThrow(() -> new QuestionNotFoundException(hid));
        group.setAnswerDate(Instant.now());
        group.setAnswer(message);
        group.setPersonnel(personnel);

        botService.answerQuestion(hid, message);
        questionGroupRepository.save(group);
        updatesService.changeOn(-1, UpdateType.QUESTIONS);
    }

    @Transactional
    @Override
    public void addNewQuestions(List<BotMessage> questions) {
        if (questions.size() == 0) return;

        for (BotMessage message : questions) {
            Optional<Question> question = questionRepository.findById(message.getGlobalId());
            if (question.isPresent()) continue;

            Subscription sub = subscriptionService.getOrCreateSubscription(message.getHid());
            QuestionGroup group = questionGroupRepository.findUnasweredBySub(sub).orElseGet(() -> new QuestionGroup(sub));

            group.addQuestion(question
                    .orElseGet(() -> new Question(message.getGlobalId(), message.getMessage().toString(), message.getTimestamp())));
            group.correctDate(message.getTimestamp());
            questionGroupRepository.save(group);
        }

        long count = Optional.ofNullable(questionGroupRepository.notAnsweredCount()).orElse(0L);
        updatesService.set(count, UpdateType.QUESTIONS);
    }

}
