package com.hedvig.backoffice.web;

import com.hedvig.backoffice.domain.Personnel;
import com.hedvig.backoffice.repository.PersonnelRepository;
import com.hedvig.backoffice.security.AuthorizationException;
import com.hedvig.backoffice.services.messages.dto.BackOfficeResponseDTO;
import com.hedvig.backoffice.services.questions.QuestionNotFoundException;
import com.hedvig.backoffice.services.questions.QuestionService;
import com.hedvig.backoffice.services.questions.dto.QuestionGroupDTO;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/answer/{memberId}")
    public QuestionGroupDTO answer(@PathVariable String memberId,
                                    @Valid @RequestBody BackOfficeResponseDTO message,
                                    @AuthenticationPrincipal String principal)
            throws AuthorizationException, QuestionNotFoundException {

        Personnel personnel = personnelRepository.findById(principal).orElseThrow(AuthorizationException::new);
        return questionService.answer(memberId, message.getMsg(), personnel);
    }

    @PostMapping("/done/{memberId}")
    public QuestionGroupDTO done(@PathVariable String memberId, @AuthenticationPrincipal String principal)
            throws QuestionNotFoundException, AuthorizationException {
        Personnel personnel = personnelRepository.findById(principal).orElseThrow(AuthorizationException::new);
        return questionService.done(memberId, personnel);
    }

}
