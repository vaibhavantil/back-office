package com.hedvig.backoffice.web;

import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.repository.PersonnelRepository;
import com.hedvig.backoffice.security.AuthorizationException;
import com.hedvig.backoffice.services.messages.dto.BackOfficeResponseDTO;
import com.hedvig.backoffice.services.questions.QuestionNotFoundException;
import com.hedvig.backoffice.services.questions.QuestionService;
import com.hedvig.backoffice.services.questions.dto.QuestionGroupDTO;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.validation.Valid;

import com.hedvig.backoffice.web.dto.QuestionSortFields;
import com.hedvig.backoffice.web.dto.QuestionSortOrder;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
  public List<QuestionGroupDTO> list() {
    return questionService.list();
  }

  @GetMapping("/answered")
  public List<QuestionGroupDTO> answered(
    @RequestParam(name = "pageId", required = false) Integer page,
    @RequestParam(name = "pageSize", required = false) Integer pageSize,
    @RequestParam(name = "sortField", required = false) QuestionSortFields reqSortFields[],
    @RequestParam(name = "sortDirection", required = false) Sort.Direction reqSortDirections[]
  ) {
    page = (page != null) ? page : 0; //This will be removed once frontend is ready
    pageSize = (pageSize != null) ? pageSize : 20;
    val sortFields = (reqSortFields != null) ? reqSortFields : new QuestionSortFields[0];
    val sortDirections = (reqSortDirections != null) ? reqSortDirections : new Sort.Direction[0];

    if (sortFields.length != sortDirections.length) {
      throw new RuntimeException("Amount of sortField params should be equal to amount of sortDirection");
    }

    List<QuestionSortOrder> sortOrders = IntStream.range(0, sortFields.length).
      mapToObj(i -> new QuestionSortOrder(sortFields[i], sortDirections[i]))
      .collect(Collectors.toList());

    return questionService.answered(page, pageSize, sortOrders);
  }

  @GetMapping("/not-answered")
  public List<QuestionGroupDTO> notAnswered(
    @RequestParam(name = "pageId", required = false) Integer page,
    @RequestParam(name = "pageSize", required = false) Integer pageSize,
    @RequestParam(name = "sortField", required = false) QuestionSortFields reqSortFields[],
    @RequestParam(name = "sortDirection", required = false) Sort.Direction reqSortDirections[]
  ) {
    page = (page != null) ? page : 0; //This will be removed once frontend is ready
    pageSize = (pageSize != null) ? pageSize : 20;
    val sortFields = (reqSortFields != null) ? reqSortFields : new QuestionSortFields[0];
    val sortDirections = (reqSortDirections != null) ? reqSortDirections : new Sort.Direction[0];

    if (sortFields.length != sortDirections.length) {
      throw new RuntimeException("Amount of sortField params should be equal to amount of sortDirection");
    }

    List<QuestionSortOrder> sortOrders = IntStream.range(0, sortFields.length).
      mapToObj(i -> new QuestionSortOrder(sortFields[i], sortDirections[i]))
      .collect(Collectors.toList());

    return questionService.notAnswered(page, pageSize, sortOrders);
  }

  @PostMapping("/answer/{memberId}")
  public QuestionGroupDTO answer(
      @PathVariable String memberId,
      @Valid @RequestBody BackOfficeResponseDTO message,
      @AuthenticationPrincipal String principal)
      throws AuthorizationException, QuestionNotFoundException {

    Personnel personnel =
        personnelRepository.findById(principal).orElseThrow(AuthorizationException::new);
    return questionService.answer(memberId, message.getMsg(), personnel);
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
