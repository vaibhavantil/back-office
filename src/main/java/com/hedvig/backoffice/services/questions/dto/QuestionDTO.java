package com.hedvig.backoffice.services.questions.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.hedvig.backoffice.domain.Question;
import com.hedvig.backoffice.services.messages.BotMessageException;
import com.hedvig.backoffice.services.messages.dto.BotMessage;
import com.hedvig.backoffice.web.dto.PersonnelDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
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
    private LocalDateTime date;

    public QuestionDTO(String hid, JsonNode message) {
        this.hid = hid;
        this.message = message;
    }

    public static QuestionDTO fromDomain(Question question) {
        BotMessage message = null;
        try {
            message = question.getMessage() != null
                    ? new BotMessage(question.getMessage())
                    : null;
        } catch (BotMessageException e) {
            logger.error("Conversion failed", e);
        }

        BotMessage answer = null;
        try {
            answer = question.getAnswer() != null
                    ? new BotMessage(question.getAnswer())
                    : null;
        } catch (BotMessageException e) {
            logger.error("Conversion failed", e);
        }

        return new QuestionDTO(
                question.getId(),
                Optional.ofNullable(message).map(BotMessage::getMessage).orElse(null),
                Optional.ofNullable(answer).map(BotMessage::getMessage).orElse(null),
                Optional.ofNullable(question.getPersonnel()).map(PersonnelDTO::fromDomain).orElse(null),
                question.getHid(),
                question.getTimestamp());
    }

    public static Question toDomain(QuestionDTO dto) {
        return new Question(dto.getHid(), dto.getMessage().toString());
    }

}
