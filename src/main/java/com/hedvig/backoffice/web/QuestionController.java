package com.hedvig.backoffice.web;

import com.hedvig.backoffice.services.questions.QuestionService;
import com.hedvig.backoffice.services.questions.dto.QuestionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<QuestionDTO> list() {
        return questionService.list();
    }

    @GetMapping("/answered")
    public List<QuestionDTO> answered() {
        return questionService.answered();
    }

    @GetMapping("/not-answered")
    public List<QuestionDTO> notAnswered() {
        return questionService.notAnswered();
    }

}
