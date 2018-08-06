package com.hedvig.backoffice.services.questions.dto;

import com.hedvig.backoffice.domain.QuestionGroup;
import com.hedvig.backoffice.web.dto.PersonnelDTO;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionGroupDTO {

  private Long id;
  private String memberId;
  private Long date;
  private Long answerDate;
  private String answer;
  private PersonnelDTO personnel;
  private List<QuestionDTO> questions;

  public static QuestionGroupDTO fromDomain(QuestionGroup group) {
    return new QuestionGroupDTO(
        group.getId(),
        group.getSubscription().getMemberId(),
        Optional.ofNullable(group.getDate()).map(Instant::toEpochMilli).orElse(null),
        Optional.ofNullable(group.getAnswerDate()).map(Instant::toEpochMilli).orElse(null),
        group.getAnswer(),
        Optional.ofNullable(group.getPersonnel()).map(PersonnelDTO::fromDomain).orElse(null),
        group.getQuestions().stream().map(QuestionDTO::fromDomain).collect(Collectors.toList()));
  }
}
