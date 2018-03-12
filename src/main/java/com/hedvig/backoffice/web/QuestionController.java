package com.hedvig.backoffice.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.hedvig.backoffice.services.questions.QuestionService;
import com.hedvig.backoffice.services.questions.dto.QuestionGroupDTO;
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
    public List<QuestionGroupDTO> list() {
        return questionService.list();
    }

    @GetMapping("/answered")
    public List<QuestionGroupDTO> answered() {
        return questionService.answered();
    }

    @GetMapping("/not-answered")
    public List<QuestionGroupDTO> notAnswered() {
        return questionService.notAnswered();
    }

    @PostMapping("/answer/{id}")
    public ResponseEntity<?> answer(@PathVariable Long id, @RequestBody JsonNode message) {


        return ResponseEntity.noContent().build();
    }

}
