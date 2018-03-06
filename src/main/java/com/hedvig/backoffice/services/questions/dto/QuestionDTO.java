package com.hedvig.backoffice.services.questions.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedvig.backoffice.domain.Question;
import com.hedvig.backoffice.web.dto.PersonnelDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

@Data
@AllArgsConstructor
public class QuestionDTO {

    private static Logger logger = LoggerFactory.getLogger(QuestionDTO.class);

    private Long id;
    private JsonNode message;
    private JsonNode answer;
    private PersonnelDTO personnel;
    private String hid;
    private Long date;
    private Long answerDate;

    public QuestionDTO(String hid, JsonNode message, long date) {
        this.hid = hid;
        this.message = message;
        this.date = date;
    }

    public static QuestionDTO fromDomain(Question question) {
        JsonNode message = Optional.ofNullable(question.getMessage())
                .map(QuestionDTO::parseMessage).orElse(null);

        JsonNode answer = Optional.ofNullable(question.getAnswer())
                .map(QuestionDTO::parseMessage).orElse(null);

        return new QuestionDTO(
                question.getId(),
                message,
                answer,
                Optional.ofNullable(question.getPersonnel()).map(PersonnelDTO::fromDomain).orElse(null),
                question.getSubscription().getHid(),
                Optional.ofNullable(question.getDate()).map(Instant::toEpochMilli).orElse(null),
                Optional.ofNullable(question.getAnswerDate()).map(Instant::toEpochMilli).orElse(null));
    }

    public static Question toDomain(QuestionDTO dto) {
        return new Question(dto.getMessage().toString(), Instant.ofEpochMilli(dto.getDate()));
    }

    private static JsonNode parseMessage(String message) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(message, JsonNode.class);
        } catch (IOException e) {
            logger.error("conversion failed");
        }
        return null;
    }

}
