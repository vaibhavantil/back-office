package com.hedvig.backoffice.web;

import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.repository.PersonnelRepository;
import com.hedvig.backoffice.security.AuthorizationException;
import com.hedvig.backoffice.security.GatekeeperUser;
import com.hedvig.backoffice.services.messages.dto.BackOfficeResponseDTO;
import com.hedvig.backoffice.services.questions.QuestionNotFoundException;
import com.hedvig.backoffice.services.questions.QuestionService;
import com.hedvig.backoffice.services.questions.dto.QuestionGroupDTO;
import com.hedvig.backoffice.web.dto.QuestionGroupWebDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
    @AuthenticationPrincipal GatekeeperUser principal
  ) throws AuthorizationException, QuestionNotFoundException {
    Personnel personnel = personnelRepository.findByEmail(principal.getUsername()).orElseThrow(AuthorizationException::new);
    QuestionGroupDTO answer = questionService.answer(memberId, message.getMsg(), personnel);
    return new QuestionGroupWebDTO(answer);
  }

  @PostMapping("/done/{memberId}")
  public QuestionGroupDTO done(
    @PathVariable String memberId, @AuthenticationPrincipal GatekeeperUser principal
  ) throws QuestionNotFoundException, AuthorizationException {
    Personnel personnel = personnelRepository.findByEmail(principal.getUsername()).orElseThrow(AuthorizationException::new);
    return questionService.done(memberId, personnel);
  }
}
