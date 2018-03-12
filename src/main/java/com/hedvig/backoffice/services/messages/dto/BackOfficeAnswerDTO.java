package com.hedvig.backoffice.services.messages.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class BackOfficeAnswerDTO {

    public String userId;

    @NotNull
    public String msg;
}
