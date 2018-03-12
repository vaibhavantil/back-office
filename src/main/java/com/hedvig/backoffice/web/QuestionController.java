package com.hedvig.backoffice.web;

import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.repository.PersonnelRepository;
import com.hedvig.backoffice.security.AuthorizationException;
import com.hedvig.backoffice.services.messages.dto.BackOfficeAnswerDTO;
import com.hedvig.backoffice.services.questions.QuestionNotFoundException;
import com.hedvig.backoffice.services.questions.QuestionService;
import com.hedvig.backoffice.services.questions.dto.QuestionGroupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PostMapping("/answer/{hid}")
    public ResponseEntity<?> answer(@PathVariable String hid,
                                    @Valid @RequestBody BackOfficeAnswerDTO message,
                                    @AuthenticationPrincipal String principal)
            throws AuthorizationException, QuestionNotFoundException {
        Personnel personnel = personnelRepository.findByEmail(principal).orElseThrow(AuthorizationException::new);
        questionService.answer(hid, message.getMsg(), personnel);

        return ResponseEntity.noContent().build();
    }

}
