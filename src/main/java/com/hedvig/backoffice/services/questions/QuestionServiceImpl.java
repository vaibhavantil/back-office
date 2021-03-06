package com.hedvig.backoffice.services.questions;

import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.domain.Question;
import com.hedvig.backoffice.domain.QuestionGroup;
import com.hedvig.backoffice.domain.Subscription;
import com.hedvig.backoffice.repository.QuestionGroupRepository;
import com.hedvig.backoffice.repository.QuestionRepository;
import com.hedvig.backoffice.services.UploadedFilePostprocessor;
import com.hedvig.backoffice.services.chat.SubscriptionService;
import com.hedvig.backoffice.services.messages.BotService;
import com.hedvig.backoffice.services.messages.dto.BotMessageDTO;
import com.hedvig.backoffice.services.notificationService.NotificationService;
import com.hedvig.backoffice.services.personnel.PersonnelService;
import com.hedvig.backoffice.services.questions.dto.QuestionGroupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

  private final QuestionRepository questionRepository;
  private final QuestionGroupRepository questionGroupRepository;
  private final SubscriptionService subscriptionService;
  private final BotService botService;
  private final PersonnelService personnelService;
  private final NotificationService notificationService;
  private final UploadedFilePostprocessor messagesPostprocessor;

  @Autowired
  public QuestionServiceImpl(
    QuestionRepository questionRepository,
    QuestionGroupRepository questionGroupRepository,
    SubscriptionService subscriptionService,
    BotService botService,
    PersonnelService personnelService,
    NotificationService notificationService,
    UploadedFilePostprocessor messagesPostprocessor
  ) {
    this.questionRepository = questionRepository;
    this.questionGroupRepository = questionGroupRepository;
    this.subscriptionService = subscriptionService;
    this.botService = botService;
    this.personnelService = personnelService;
    this.notificationService = notificationService;
    this.messagesPostprocessor = messagesPostprocessor;
  }

  private QuestionGroupDTO postProcessQuestionGroup(QuestionGroupDTO group) {
    group.getQuestions().forEach(q -> messagesPostprocessor.processMessage(q.getMessage()));
    return group;
  }

  @Override
  public List<QuestionGroupDTO> list() {
    return questionGroupRepository
      .findAll()
      .stream()
      .map(QuestionGroupDTO.Companion::fromDomain)
      .map(this::postProcessQuestionGroup)
      .collect(Collectors.toList());
  }

  @Override
  public List<QuestionGroupDTO> notAnswered() {
    return questionGroupRepository
      .notAnswered()
      .stream()
      .map(QuestionGroupDTO.Companion::fromDomain)
      .map(this::postProcessQuestionGroup)
      .collect(Collectors.toList());
  }

  @Transactional
  @Override
  public QuestionGroupDTO answer(String memberId, String message, Personnel personnel) throws QuestionNotFoundException {
    QuestionGroup group =
      questionGroupRepository
        .findUnasweredByMemberId(memberId)
        .orElseThrow(() -> new QuestionNotFoundException(memberId));
    group.setAnswerDate(Instant.now());
    group.setAnswer(message);
    group.setPersonnel(personnel);

    botService.answerQuestion(memberId, message, personnelService.getIdToken(personnel.getEmail()));
    sendNotification(memberId, personnelService.getIdToken(personnel.getEmail()), message);
    questionGroupRepository.save(group);

    return QuestionGroupDTO.Companion.fromDomain(group);
  }

  @Override
  public QuestionGroupDTO done(String memberId, Personnel personnel) throws QuestionNotFoundException {
    QuestionGroup group = questionGroupRepository
      .findUnasweredByMemberId(memberId)
      .orElseThrow(() -> new QuestionNotFoundException(memberId));
    group.setAnswerDate(Instant.now());
    group.setAnswer("");
    group.setPersonnel(personnel);
    questionGroupRepository.save(group);

    return QuestionGroupDTO.Companion.fromDomain(group);
  }

  @Transactional
  @Override
  public void addNewQuestions(List<BotMessageDTO> questions) {
    if (questions.size() == 0) return;

    for (BotMessageDTO message : questions) {
      Optional<Question> question = questionRepository.findById(message.getGlobalId());
      if (question.isPresent()) continue;

      Subscription sub = subscriptionService.getOrCreateSubscription("" + message.getHeader().getFromId());
      QuestionGroup group =
        questionGroupRepository.findUnasweredBySub(sub).orElseGet(() -> new QuestionGroup(sub));

      group.addQuestion(
        question.orElseGet(
          () ->
            new Question(
              message.getGlobalId(),
              message.toJson(),
              message.getTimestamp())));
      group.correctDate(message.getTimestamp());
      questionGroupRepository.save(group);
    }
  }

  private void sendNotification(String memberId, String personnelToken, String message) {
    if (notificationService.getFirebaseToken(memberId).isPresent()) {
      notificationService.sendPushNotification(memberId, message);
      return;
    }
  }
}
