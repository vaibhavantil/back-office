package com.hedvig.backoffice.services.questions.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedvig.backoffice.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

@Data
@AllArgsConstructor
public class QuestionDTO {

    private static Logger logger = LoggerFactory.getLogger(QuestionDTO.class);

    private Long id;
    private JsonNode message;

    public static QuestionDTO fromDomain(Question question) {
        JsonNode message = Optional.ofNullable(question.getMessage())
                .map(QuestionDTO::parseMessage).orElse(null);

        return new QuestionDTO(
                question.getId(),
                message);
    }

    public static Question toDomain(QuestionDTO dto) {
        return new Question(dto.getId(), dto.getMessage().toString());
    }

    public static JsonNode parseMessage(String message) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(message, JsonNode.class);
        } catch (IOException e) {
            logger.error("conversion failed");
        }
        return null;
    }

}
