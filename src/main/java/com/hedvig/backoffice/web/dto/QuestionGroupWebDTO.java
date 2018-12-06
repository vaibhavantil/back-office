package com.hedvig.backoffice.web.dto;

import com.hedvig.backoffice.services.questions.dto.QuestionGroupDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.hedvig.backoffice.util.TzHelper.SWEDEN_TZ;
import static com.hedvig.backoffice.util.TzHelper.toLocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionGroupWebDTO {
  Long id;
  String memberId;
  Long date;
  Long answerDate;
  String answer;
  PersonnelDTO personnel;
  List<QuestionWebDTO> questions;

  LocalDateTime localDate;
  LocalDateTime localAnswerDate;

  public QuestionGroupWebDTO(QuestionGroupDTO group) {
    this.id = group.getId();
    this.memberId = group.getMemberId();
    this.date = group.getDate();
    this.localDate = toLocalDateTime(group.getDate(), SWEDEN_TZ);
    this.answerDate = group.getAnswerDate();
    this.localAnswerDate = toLocalDateTime(group.getAnswerDate(), SWEDEN_TZ);
    this.answer = group.getAnswer();
    this.personnel = group.getPersonnel();
    this.questions = group.getQuestions().stream()
      .map(QuestionWebDTO::new)
      .collect(Collectors.toList());
  }
}
