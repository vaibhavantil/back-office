package com.hedvig.backoffice.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.repository.PersonnelRepository;
import com.hedvig.backoffice.security.AuthorizationException;
import com.hedvig.backoffice.services.messages.BotMessageException;
import com.hedvig.backoffice.services.messages.BotServiceException;
import com.hedvig.backoffice.services.messages.dto.BotMessage;
import com.hedvig.backoffice.services.questions.QuestionNotFoundException;
import com.hedvig.backoffice.services.questions.QuestionService;
import com.hedvig.backoffice.services.questions.dto.QuestionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;
    private final PersonnelRepository personnelRepository;

    @Autowired
    public QuestionController(QuestionService questionService, PersonnelRepository personnelRepository) {
        this.questionService = questionService;
        this.personnelRepository = personnelRepository;
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

    @PutMapping("/answer/{id}")
    public ResponseEntity<?> answer(@PathVariable Long id, @AuthenticationPrincipal String principal, @RequestBody JsonNode node)
            throws BotMessageException, QuestionNotFoundException, BotServiceException, AuthorizationException {

        Personnel personnel = personnelRepository.findByEmail(principal).orElseThrow(AuthorizationException::new);

        if (!questionService.answer(id, new BotMessage(node,true), personnel)) {
            throw new BotServiceException("response failed");
        }

        return ResponseEntity.noContent().build();
    }

}
