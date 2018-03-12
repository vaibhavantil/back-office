package com.hedvig.backoffice.services.questions.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.hedvig.backoffice.domain.QuestionGroup;
import com.hedvig.backoffice.web.dto.PersonnelDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class QuestionGroupDTO {

    private Long id;
    private String hid;
    private Long date;
    private Long answerDate;
    private JsonNode answer;
    private PersonnelDTO personnel;
    private List<QuestionDTO> questions;

    public static QuestionGroupDTO fromDomain(QuestionGroup group) {
        JsonNode answer = Optional.ofNullable(group.getAnswer())
                .map(QuestionDTO::parseMessage).orElse(null);

        return new QuestionGroupDTO(
                group.getId(),
                group.getSubscription().getHid(),
                Optional.ofNullable(group.getDate()).map(Instant::toEpochMilli).orElse(null),
                Optional.ofNullable(group.getAnswerDate()).map(Instant::toEpochMilli).orElse(null),
                answer,
                Optional.ofNullable(group.getPersonnel()).map(PersonnelDTO::fromDomain).orElse(null),
                group.getQuestions().stream().map(QuestionDTO::fromDomain).collect(Collectors.toList()));
    }

}
