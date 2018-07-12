package com.hedvig.backoffice.web.dto;

import lombok.Value;

@Value
public class MonthlySubscription {
    String memberId;
    Boolean directDebitStatus;
    Integer price;
}