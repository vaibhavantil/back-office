package com.hedvig.backoffice.services.questions.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.hedvig.backoffice.web.dto.MemberDTO;
import com.hedvig.backoffice.web.dto.PersonnelDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Question {

    private String id;
    private JsonNode message;
    private JsonNode answer;
    private PersonnelDTO personnel;
    private MemberDTO member;
    private LocalDateTime date;

}
