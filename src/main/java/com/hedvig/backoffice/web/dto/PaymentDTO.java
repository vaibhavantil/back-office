package com.hedvig.backoffice.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PaymentDTO {

    private LocalDateTime date;
    private BigDecimal amount;
    private String noteId;
    private boolean exg;

}
