package com.hedvig.backoffice.services.product_pricing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.hedvig.backoffice.config.feign.ExternalServiceNotFoundException;
import com.hedvig.backoffice.services.members.MemberServiceStub;
import com.hedvig.backoffice.services.product_pricing.dto.InsuranceActivateDTO;
import com.hedvig.backoffice.services.product_pricing.dto.InsuredAtOtherCompanyDTO;
import com.hedvig.backoffice.services.product_pricing.dto.MonthlySubscriptionDTO;
import com.hedvig.backoffice.web.dto.InsuranceModificationDTO;
import com.hedvig.backoffice.web.dto.InsuranceStatusDTO;
import com.hedvig.backoffice.web.dto.ModifyInsuranceRequestDTO;
import com.hedvig.backoffice.web.dto.SafetyIncreaserType;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.money.Monetary;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class ProductPricingServiceStub implements ProductPricingService {

  private List<InsuranceStatusDTO> insurances;

  @Autowired
  public ProductPricingServiceStub(ObjectMapper mapper) {
    long minSignedOnDay = LocalDate.of(2011, 1, 3).toEpochDay();
    long maxSignedOnDay = LocalDate.of(2018, 12, 31).toEpochDay();
    String[] states = {"QUOTE", "SIGNED", "TERMINATED"};
    List<SafetyIncreaserType> safetyIncreasers =
        Arrays.asList(SafetyIncreaserType.SAFETY_DOOR, SafetyIncreaserType.BURGLAR_ALARM);
    insurances =
        IntStream.range(0, MemberServiceStub.testMemberIds.length)
            .mapToObj(
                i -> {
                  String memberId = Long.toString(MemberServiceStub.testMemberIds[i]);
                  String insuranceState = states[RandomUtils.nextInt(0, states.length)];

                  InsuranceStatusDTO insurance =
                      new InsuranceStatusDTO(
                          UUID.randomUUID().toString(),
                          memberId,
                          ("Firstname" + memberId),
                          ("Lastname" + memberId),
                          "Street",
                          "City",
                          "ZipCode",
                          0,
                          (float) 50,
                          safetyIncreasers,
                          "PENDING",
                          insuranceState,
                          RandomUtils.nextInt(0, 9),
                          new BigDecimal(Math.random()),
                          null,
                          true,
                          "BRF",
                          null,
                          null,
                          false,
                          null,
                          false,
                          null);

                  if (insurance.getInsuranceState().equals(states[1])) {
                    long randomSignedOnDate =
                        ThreadLocalRandom.current().nextLong(minSignedOnDay, maxSignedOnDay);
                    LocalDate randomSignedOnLocalDate = LocalDate.ofEpochDay(randomSignedOnDate);
                    LocalTime randomSignedOnLocalTime =
                        LocalTime.ofNanoOfDay(randomSignedOnDate * RandomUtils.nextInt(0, 1000000));
                    insurance.setSignedOn(
                        Instant.from(
                            ZonedDateTime.of(
                                LocalDateTime.of(randomSignedOnLocalDate, randomSignedOnLocalTime),
                                ZoneId.of("Europe/Stockholm"))));

                    insurance.setInsuranceActiveFrom(
                        LocalDateTime.of(randomSignedOnLocalDate, randomSignedOnLocalTime));

                    insurance.setCertificateUploaded(true);
                    insurance.setCertificateUrl("http://hedvigeleonora.se/");
                  }

                  return insurance;
                })
            .collect(Collectors.toList());
  }

  @Override
  public byte[] insuranceContract(String memberId, String token) {
    return new byte[0];
  }

  @Override
  public InsuranceStatusDTO insurance(String memberId, String token) {
    Optional<InsuranceStatusDTO> insurance =
        insurances.stream().filter(i -> i.getMemberId().equals(memberId)).findAny();
    if (!insurance.isPresent()) {
      throw new ExternalServiceNotFoundException("insurance not found", "");
    }
    return insurance.get();
  }

  @Override
  public void activate(String memberId, InsuranceActivateDTO dto, String token) {
    this.insurance(memberId, token)
        .setInsuranceActiveFrom(
            dto.getActivationDate().atTime(RandomUtils.nextInt(0, 9), RandomUtils.nextInt(0, 9)));
  }

  @Override
  public List<InsuranceStatusDTO> search(String state, String query, String token) {
    if (StringUtils.isBlank(state) && StringUtils.isBlank(query)) {
      return insurances;
    }
    return insurances
        .stream()
        .filter(
            u ->
                (StringUtils.isNotBlank(query) && u.getMemberFirstName().contains(query))
                    || (StringUtils.isNotBlank(state) && u.getInsuranceState().contains(state)))
        .collect(Collectors.toList());
  }

  @Override
  public void sendCancellationEmail(String memberId, String token) {
    this.insurance(memberId, token).setCancellationEmailSent(true);
  }

  @Override
  public void uploadCertificate(
      String memberId, String fileName, String contentType, byte[] data, String token) {
    log.info("certificate uploaded: memberId = " + memberId + ", name = " + fileName);
    this.insurance(memberId, token).setCertificateUploaded(true);
  }

  @Override
  public void setInsuredAtOtherCompany(String memberId, InsuredAtOtherCompanyDTO dto) {
    this.insurance(memberId, "").setInsuredAtOtherCompany(dto.isInsuredAtOtherCompany());
  }

  @Override
  public List<InsuranceStatusDTO> getInsurancesByMember(String memberId, String token) {
    return insurances
        .stream()
        .filter(x -> x.getMemberId().equals(memberId))
        .collect(Collectors.toList());
  }

  @Override
  public InsuranceStatusDTO createmodifiedProduct(
      String memberId, InsuranceModificationDTO changeRequest, String token) {
    Optional<InsuranceStatusDTO> current =
        insurances
            .stream()
            .filter(x -> x.getProductId().equals(changeRequest.idToBeReplaced.toString()))
            .findFirst();

    if (current.isPresent()) {

      InsuranceStatusDTO c = current.get();

      InsuranceStatusDTO updated =
          new InsuranceStatusDTO(
              UUID.randomUUID().toString(),
              changeRequest.memberId,
              c.getMemberFirstName(),
              c.getMemberLastName(),
              changeRequest.street,
              changeRequest.city,
              changeRequest.zipCode,
              changeRequest.floor,
              changeRequest.livingSpace,
              changeRequest.safetyIncreasers,
              c.getInsuranceStatus(),
              "QUOTE",
              changeRequest.personsInHouseHold,
              c.getCurrentTotalPrice(),
              c.getNewTotalPrice(),
              c.getInsuredAtOtherCompany(),
              changeRequest.getHouseType(),
              null,
              null,
              false,
              c.getCertificateUrl(),
              c.isCancellationEmailSent(),
              c.getSignedOn());

      insurances.add(updated);

      return updated;

    } else
      log.error("createmodifiedProduct, no product foudn with id {}", changeRequest.idToBeReplaced);
    return null;
  }

  @Override
  public void modifyProduct(String memberId, ModifyInsuranceRequestDTO request, String token) {

    Optional<InsuranceStatusDTO> current =
        insurances
            .stream()
            .filter(x -> x.getProductId().equals(request.insuranceIdToBeReplaced.toString()))
            .findFirst();

    Optional<InsuranceStatusDTO> updated =
        insurances
            .stream()
            .filter(x -> x.getProductId().equals(request.insuranceIdToReplace.toString()))
            .findFirst();

    if (current.isPresent() && updated.isPresent()) {

      InsuranceStatusDTO c = current.get();
      InsuranceStatusDTO u = updated.get();
      c.setInsuranceActiveTo(request.terminationDate.atStartOfDay());

      u.setInsuranceState("SIGNED");
      u.setInsuranceActiveFrom(request.activationDate.atStartOfDay());
    }
  }

  @Override
  public List<MonthlySubscriptionDTO> getMonthlyPayments(YearMonth month) {
    return Lists.newArrayList(
        new MonthlySubscriptionDTO("123456", Money.of(100, Monetary.getCurrency("SEK"))),
        new MonthlySubscriptionDTO("3267661", Money.of(200, Monetary.getCurrency("SEK"))));
  }

  @Override
  public MonthlySubscriptionDTO getMonthlyPaymentsByMember(YearMonth month, String memberId) {
    return new MonthlySubscriptionDTO(
        memberId, Money.of(RandomUtils.nextLong(), Monetary.getCurrency("SEK")));
  }
}
