package com.hedvig.backoffice.web.graphql.types;

import javax.money.MonetaryAmount;

import lombok.Data;

@Data
public class MonthlySubscription {
    String memberId;
    Boolean directDebitStatus;
    MonetaryAmount amount;
    Member member;

    public MonthlySubscription(String memberId, Boolean directDebitStatus, MonetaryAmount amount) {
        this.memberId = memberId;
        this.directDebitStatus = directDebitStatus;
        this.amount = amount;
    }
}