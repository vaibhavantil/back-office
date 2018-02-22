package com.hedvig.backoffice.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.hedvig.backoffice.services.messages.BotMessageException;
import com.hedvig.backoffice.services.messages.BotServiceException;
import com.hedvig.backoffice.services.messages.dto.BotMessage;
import com.hedvig.backoffice.services.questions.QuestionService;
import com.hedvig.backoffice.services.questions.dto.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    public List<Question> list() {
        return questionService.list();
    }

    @PutMapping("/answer/{id}")
    public ResponseEntity<?> answer(@PathVariable String id, @RequestBody JsonNode node)
            throws BotMessageException, BotServiceException {
        questionService.answer(id, new BotMessage(node,true));

        return ResponseEntity.noContent().build();
    }

}
