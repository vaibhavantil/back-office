package com.hedvig.backoffice.web.dto;

import com.hedvig.backoffice.services.product_pricing.dto.InsuranceStatusDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.hedvig.backoffice.util.TzHelper.SWEDEN_TZ;
import static com.hedvig.backoffice.util.TzHelper.toLocalDateTime;

@Data
@NoArgsConstructor
public class InsuranceStatusWebDTO {
  String productId;
  String memberId;
  String memberFirstName;
  String memberLastName;
  String street;
  String city;
  String zipCode;
  Integer floor;
  Float livingSpace;
  List<SafetyIncreaserType> safetyIncreasers;
  String insuranceStatus;
  ProductState insuranceState;
  Integer personsInHouseHold;
  BigDecimal currentTotalPrice;
  BigDecimal newTotalPrice;
  Boolean insuredAtOtherCompany;
  String currentInsurer;
  String insuranceType;
  LocalDateTime insuranceActiveFrom;
  LocalDateTime insuranceActiveTo;
  boolean certificateUploaded;
  String certificateUrl;
  boolean cancellationEmailSent;
  LocalDateTime signedOn;

  public InsuranceStatusWebDTO(InsuranceStatusDTO ins) {
    this.productId = ins.getProductId();
    this.memberId = ins.getMemberId();
    this.memberFirstName = ins.getMemberFirstName();
    this.memberLastName = ins.getMemberLastName();
    this.street = ins.getStreet();
    this.city = ins.getCity();
    this.zipCode = ins.getZipCode();
    this.floor = ins.getFloor();
    this.livingSpace = ins.getLivingSpace();
    this.safetyIncreasers = new ArrayList<>(ins.getSafetyIncreasers());
    this.insuranceStatus = ins.getInsuranceStatus();
    this.insuranceState = ins.getInsuranceState();
    this.personsInHouseHold = ins.getPersonsInHouseHold();
    this.currentTotalPrice = ins.getCurrentTotalPrice();
    this.newTotalPrice = ins.getNewTotalPrice();
    this.insuredAtOtherCompany = ins.getInsuredAtOtherCompany();
    this.currentInsurer = ins.getCurrentInsurer();
    this.insuranceType = ins.getInsuranceType();
    this.insuranceActiveFrom = ins.getInsuranceActiveFrom();
    this.insuranceActiveTo = ins.getInsuranceActiveTo();
    this.certificateUploaded = ins.isCertificateUploaded();
    this.certificateUrl = ins.getCertificateUrl();
    this.cancellationEmailSent = ins.isCancellationEmailSent();
    this.signedOn = toLocalDateTime(ins.getSignedOn(), SWEDEN_TZ);
  }
}
