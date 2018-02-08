package com.hedvig.backoffice.web.dto.claims;

import com.hedvig.backoffice.services.claims.ClaimStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
public class ClaimDTO {

    private String id;
    private String userId;
    private ClaimStatus status;
    private String type;
    private ClaimDetailsDTO details;
    private String url;
    private BigDecimal resume;
    private BigDecimal total;
    private Instant timestamp;

}
