package com.hedvig.backoffice.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.hedvig.backoffice.services.claims.ClaimStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ClaimDTO {

    private String id;
    private String userId;
    private ClaimStatus status;
    private ClaimTypeDTO type;
    private String url;

    @JsonDeserialize(using= LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate registrationDate;

}
