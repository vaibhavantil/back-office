package com.hedvig.backoffice.web.dto;

import com.hedvig.backoffice.services.questions.dto.QuestionDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.hedvig.backoffice.util.TzHelper.SWEDEN_TZ;
import static com.hedvig.backoffice.util.TzHelper.toLocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionWebDTO {
  Long id;
  BotMessageWebDTO message;
  Long date;
  LocalDateTime localDate;

  public QuestionWebDTO(QuestionDTO q) {
    this.id = q.getId();
    this.message = new BotMessageWebDTO(q.getMessage());
    this.date = q.getDate();
    this.localDate = toLocalDateTime(q.getDate(), SWEDEN_TZ);
  }
}
