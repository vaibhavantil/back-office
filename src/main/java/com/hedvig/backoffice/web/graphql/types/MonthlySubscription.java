package com.hedvig.backoffice.web.graphql.types;

import lombok.Data;

@Data
public class MonthlySubscription {
    String memberId;
    Boolean directDebitStatus;
    Integer price;
    Member member;

    public MonthlySubscription(String memberId, Boolean directDebitStatus, Integer price) {
        this.memberId = memberId;
        this.directDebitStatus = directDebitStatus;
        this.price = price;
    }
}