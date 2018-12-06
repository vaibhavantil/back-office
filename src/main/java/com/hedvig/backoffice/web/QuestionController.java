package com.hedvig.backoffice.web;

import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.repository.PersonnelRepository;
import com.hedvig.backoffice.security.AuthorizationException;
import com.hedvig.backoffice.services.messages.dto.BackOfficeResponseDTO;
import com.hedvig.backoffice.services.questions.QuestionNotFoundException;
import com.hedvig.backoffice.services.questions.QuestionService;
import com.hedvig.backoffice.services.questions.dto.QuestionGroupDTO;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

import com.hedvig.backoffice.web.dto.QuestionGroupWebDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

  private final QuestionService questionService;
  private final PersonnelRepository personnelRepository;

  @Autowired
  public QuestionController(
      QuestionService questionService, PersonnelRepository personnelRepository) {
    this.questionService = questionService;
    this.personnelRepository = personnelRepository;
  }

  @GetMapping
  public List<QuestionGroupWebDTO> list() {
    return questionService.list().stream()
      .map(QuestionGroupWebDTO::new)
      .collect(Collectors.toList());
  }

  @GetMapping("/answered")
  public List<QuestionGroupWebDTO> answered() {
    return questionService.answered().stream()
      .map(QuestionGroupWebDTO::new)
      .collect(Collectors.toList());
  }

  @GetMapping("/not-answered")
  public List<QuestionGroupWebDTO> notAnswered() {
    return questionService.notAnswered().stream()
      .map(QuestionGroupWebDTO::new)
      .collect(Collectors.toList());
  }

  @PostMapping("/answer/{memberId}")
  public QuestionGroupWebDTO answer(
      @PathVariable String memberId,
      @Valid @RequestBody BackOfficeResponseDTO message,
      @AuthenticationPrincipal String principal)
      throws AuthorizationException, QuestionNotFoundException {

    Personnel personnel =
        personnelRepository.findById(principal).orElseThrow(AuthorizationException::new);
    QuestionGroupDTO answer = questionService.answer(memberId, message.getMsg(), personnel);
    return new QuestionGroupWebDTO(answer);
  }

  @PostMapping("/done/{memberId}")
  public QuestionGroupDTO done(
      @PathVariable String memberId, @AuthenticationPrincipal String principal)
      throws QuestionNotFoundException, AuthorizationException {
    Personnel personnel =
        personnelRepository.findById(principal).orElseThrow(AuthorizationException::new);
    return questionService.done(memberId, personnel);
  }
}
