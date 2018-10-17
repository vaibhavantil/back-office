package com.hedvig.backoffice.web.dto;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.data.domain.Sort;

@Value
@AllArgsConstructor
public class QuestionSortOrder {
  QuestionSortFields field;
  Sort.Direction direction;
}
