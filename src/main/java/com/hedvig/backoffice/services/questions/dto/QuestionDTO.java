package com.hedvig.backoffice.services.questions.dto;

import com.hedvig.backoffice.domain.Question;
import com.hedvig.backoffice.services.messages.dto.BotMessageDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.Optional;

@Data
@AllArgsConstructor
public class QuestionDTO {
  private Long id;
  private BotMessageDTO message;
  private Long date;

  public static QuestionDTO fromDomain(Question question) {
    return new QuestionDTO(
        question.getId(),
        BotMessageDTO.fromJson(question.getMessage()),
        Optional.ofNullable(question.getDate()).map(Instant::toEpochMilli).orElse(null)
    );
  }

}
