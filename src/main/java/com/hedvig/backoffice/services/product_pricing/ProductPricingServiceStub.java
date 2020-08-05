package com.hedvig.backoffice.services.product_pricing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.hedvig.backoffice.config.feign.ExternalServiceNotFoundException;
import com.hedvig.backoffice.graphql.types.AssignVoucherFreeMonths;
import com.hedvig.backoffice.services.members.MemberServiceStub;
import com.hedvig.backoffice.services.product_pricing.dto.*;
import com.hedvig.backoffice.services.product_pricing.dto.contract.*;
import com.hedvig.backoffice.web.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import javax.money.Monetary;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Comparator.nullsFirst;
import static java.util.Comparator.nullsLast;

@Slf4j
public class ProductPricingServiceStub implements ProductPricingService {

  private List<InsuranceStatusDTO> insurances;

  @Autowired
  public ProductPricingServiceStub(ObjectMapper mapper) {
    long minSignedOnDay = LocalDate.of(2011, 1, 3).toEpochDay();
    long maxSignedOnDay = LocalDate.of(2018, 12, 31).toEpochDay();
    List<SafetyIncreaserType> safetyIncreasers =
      Arrays.asList(SafetyIncreaserType.SAFETY_DOOR, SafetyIncreaserType.BURGLAR_ALARM);
    insurances =
      IntStream.range(0, MemberServiceStub.testMemberIds.length)
        .mapToObj(
          i -> {
            String memberId = Long.toString(MemberServiceStub.testMemberIds[i]);
            ProductState insuranceState = ProductState.values()[RandomUtils.nextInt(0, ProductState.values().length)];

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
                null,
                "BRF",
                null,
                null,
                false,
                null,
                false,
                null,
                new ArrayList<>(),
                null,
                null,
                null,
                null,
                null
              );

            if (insurance.getInsuranceState() == ProductState.SIGNED) {
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
  public void cancel(String memberId, InsuranceCancellationDateDTO dto, String token) {
    this.insurance(memberId, token)
      .setInsuranceActiveTo(dto.getCancellationDate().atZone(ZoneId.of("Europe/Stockholm")).toLocalDateTime());
  }

  public List<InsuranceStatusDTO> search(ProductState state, String query, String token) {
    if (state == null && StringUtils.isBlank(query)) {
      return insurances;
    }
    return insurances
      .stream()
      .filter(
        u ->
          (StringUtils.isNotBlank(query) && u.getMemberFirstName().contains(query))
            || (state != null && u.getInsuranceState() == state))
      .collect(Collectors.toList());
  }

  @Override
  public InsuranceSearchResultDTO searchPaged(ProductState state, String query, Integer page, Integer pageSize, ProductSortColumns sortBy, Sort.Direction sortDirection, String token) {
    List<InsuranceStatusDTO> filtered = search(state, query, token);

    if (sortBy != null) {
      filtered.sort((sortDirection == Sort.Direction.DESC ? COMPARATORS_DESC : COMPARATORS_ASC).get(sortBy));
    }

    if (page != null && pageSize != null) {
      int offset = page * pageSize;
      int totalPages = filtered.size() / pageSize;
      if (filtered.size() % pageSize != 0) {
        totalPages++;
      }

      filtered = filtered.subList(offset, Math.min(filtered.size(), offset + pageSize));
      return new InsuranceSearchResultDTO(filtered, page, totalPages);
    } else {
      return new InsuranceSearchResultDTO(filtered, null, null);
    }
  }

  private static EnumMap<ProductSortColumns, Comparator<InsuranceStatusDTO>> COMPARATORS_ASC = new EnumMap<ProductSortColumns, Comparator<InsuranceStatusDTO>>(ProductSortColumns.class) {{
    put(ProductSortColumns.ACTIVE_FROM_DATE, Comparator.comparing((InsuranceStatusDTO ins) -> ins.getInsuranceActiveFrom(), nullsLast(LocalDateTime::compareTo)));
    put(ProductSortColumns.ACTIVE_TO_DATE, Comparator.comparing((InsuranceStatusDTO ins) -> ins.getInsuranceActiveTo(), nullsLast(LocalDateTime::compareTo)));
    put(ProductSortColumns.CANCELLATION_EMAIL_SENT_DATE, Comparator.comparing((InsuranceStatusDTO ins) -> ins.isCancellationEmailSent(), nullsLast(Boolean::compareTo)));
    put(ProductSortColumns.CERTIFICATE_UPLOADED, Comparator.comparing((InsuranceStatusDTO ins) -> ins.isCertificateUploaded(), nullsLast(Boolean::compareTo)));
    put(ProductSortColumns.CONTRACT_SIGNED_DATE, Comparator.comparing((InsuranceStatusDTO ins) -> ins.getSignedOn(), nullsLast(Instant::compareTo)));
    put(ProductSortColumns.HOUSEHOLD_SIZE, Comparator.comparing((InsuranceStatusDTO ins) -> ins.getPersonsInHouseHold(), nullsLast(Integer::compareTo)));
    put(ProductSortColumns.MEMBER_FULL_NAME, Comparator.comparing((InsuranceStatusDTO ins) -> ins.getMemberFirstName() + " " + ins.getMemberLastName(), nullsLast(String::compareTo)));
    put(ProductSortColumns.STATUS, Comparator.comparing((InsuranceStatusDTO ins) -> ins.getInsuranceStatus(), nullsLast(String::compareTo)));
    put(ProductSortColumns.TYPE, Comparator.comparing((InsuranceStatusDTO ins) -> ins.getInsuranceType(), nullsLast(String::compareTo)));
  }};

  private static EnumMap<ProductSortColumns, Comparator<InsuranceStatusDTO>> COMPARATORS_DESC = new EnumMap<ProductSortColumns, Comparator<InsuranceStatusDTO>>(ProductSortColumns.class) {{
    put(ProductSortColumns.ACTIVE_FROM_DATE, Comparator.comparing((InsuranceStatusDTO ins) -> ins.getInsuranceActiveFrom(), nullsFirst(LocalDateTime::compareTo)).reversed());
    put(ProductSortColumns.ACTIVE_TO_DATE, Comparator.comparing((InsuranceStatusDTO ins) -> ins.getInsuranceActiveTo(), nullsFirst(LocalDateTime::compareTo)).reversed());
    put(ProductSortColumns.CANCELLATION_EMAIL_SENT_DATE, Comparator.comparing((InsuranceStatusDTO ins) -> ins.isCancellationEmailSent(), nullsFirst(Boolean::compareTo)).reversed());
    put(ProductSortColumns.CERTIFICATE_UPLOADED, Comparator.comparing((InsuranceStatusDTO ins) -> ins.isCertificateUploaded(), nullsFirst(Boolean::compareTo)).reversed());
    put(ProductSortColumns.CONTRACT_SIGNED_DATE, Comparator.comparing((InsuranceStatusDTO ins) -> ins.getSignedOn(), nullsFirst(Instant::compareTo)).reversed());
    put(ProductSortColumns.HOUSEHOLD_SIZE, Comparator.comparing((InsuranceStatusDTO ins) -> ins.getPersonsInHouseHold(), nullsFirst(Integer::compareTo)).reversed());
    put(ProductSortColumns.MEMBER_FULL_NAME, Comparator.comparing((InsuranceStatusDTO ins) -> ins.getMemberFirstName() + " " + ins.getMemberLastName(), nullsFirst(String::compareTo)).reversed());
    put(ProductSortColumns.STATUS, Comparator.comparing((InsuranceStatusDTO ins) -> ins.getInsuranceStatus(), nullsFirst(String::compareTo)).reversed());
    put(ProductSortColumns.TYPE, Comparator.comparing((InsuranceStatusDTO ins) -> ins.getInsuranceType(), nullsFirst(String::compareTo)).reversed());
  }};

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
  public void setInsuredAtOtherCompany(String memberId, InsuredAtOtherCompanyDTO dto, String token) {
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
          ProductState.QUOTE,
          changeRequest.personsInHouseHold,
          c.getCurrentTotalPrice(),
          c.getNewTotalPrice(),
          c.getInsuredAtOtherCompany(),
          c.getCurrentInsurer(),
          changeRequest.getHouseType(),
          null,
          null,
          false,
          c.getCertificateUrl(),
          c.isCancellationEmailSent(),
          c.getSignedOn(),
          new ArrayList<>(),
          null,
          null,
          null,
          null,
          null
        );

      insurances.add(updated);

      return updated;

    } else {
      log.error("createmodifiedProduct, no product foudn with id {}", changeRequest.idToBeReplaced);
    }
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

      u.setInsuranceState(ProductState.SIGNED);
      u.setInsuranceActiveFrom(request.activationDate.atStartOfDay());
    }
  }

  @Override
  public List<MonthlySubscriptionDTO> getMonthlyPayments(YearMonth month) {
    return Lists.newArrayList(
      new MonthlySubscriptionDTO("123456", Money.of(100, Monetary.getCurrency("SEK"))),
      new MonthlySubscriptionDTO("2820671", Money.of(RandomUtils.nextInt(99, 999), Monetary.getCurrency("SEK"))),
      new MonthlySubscriptionDTO("6865256", Money.of(RandomUtils.nextInt(99, 999), Monetary.getCurrency("SEK"))),
      new MonthlySubscriptionDTO("9417985", Money.of(RandomUtils.nextInt(99, 999), Monetary.getCurrency("SEK"))),
      new MonthlySubscriptionDTO("3267661", Money.of(200, Monetary.getCurrency("SEK"))));
  }

  @Override
  public MonthlySubscriptionDTO getMonthlyPaymentsByMember(YearMonth month, String memberId) {
    return new MonthlySubscriptionDTO(
      memberId, Money.of(RandomUtils.nextLong(), Monetary.getCurrency("SEK")));
  }

  @Override
  public List<MonthlyBordereauDTO> getMonthlyBordereauByProductType(YearMonth month,
                                                                    ProductType productType) {
    return Lists.newArrayList(
      new MonthlyBordereauDTO("12345", Money.of(100, Monetary.getCurrency("SEK")),
        Money.of(50, Monetary.getCurrency("SEK")), productType.toString()),
      new MonthlyBordereauDTO("56789", Money.of(500, Monetary.getCurrency("SEK")),
        Money.of(500, Monetary.getCurrency("SEK")), productType.toString()),
      new MonthlyBordereauDTO("56789", Money.of(0, Monetary.getCurrency("SEK")),
        Money.of(5, Monetary.getCurrency("SEK")), productType.toString()));
  }

  @Override
  public List<SwitchableSwitcherEmailDTO> getSwitchableSwitcherEmails() {
    return Collections.emptyList();
  }

  @Override
  public void markSwitchableSwitcherEmailAsReminded(UUID emailId) {
    // noop
  }

  @Override
  public List<Contract> getContractsByMemberId(String memberId) {
    return null;
  }

  @Override
  public void activatePendingAgreement(UUID contractId, ActivatePendingAgreementRequest request, String token) {
    // noop
  }

  @Override
  public void terminateContract(UUID contractId, TerminateContractRequest request, String token) {
    // noop
  }

  @Override
  public void changeTerminationDate(UUID contractId, ChangeTerminationDateRequest request, String token) {
    // noop
  }

  @Override
  public Contract getContractById(UUID contractId) {
    return null;
  }

  @Override
  public void revertTermination(UUID contractId, String token) {
    // noop
  }

  @Override
  public void changeFromDate(UUID agreementId, ChangeFromDateOnAgreementRequest request, String token) {
    // noop
  }

  @Override
  public void changeToDate(UUID agreementId, ChangeToDateOnAgreementRequest request, String token) {
    // noop
  }

  @Override
  public ContractMarketInfo getContractMarketInfoByMemberId(String memberId) {
    return null;
  }

  @Override
  public void regenerateCertificate(UUID agreementId, String token) {
    // noop
  }

  @Override
  public List<PartnerCampaignSearchResponse> searchPartnerCampaigns(String code, String partnerId, LocalDate activeFrom, LocalDate activeTo) {
    return null;
  }

  @Override
  public void assignCampaignToPartnerPercentageDiscount(AssignVoucherPercentageDiscountRequest request) {
    //  noop
  }

  @Override
  public void assignCampaignToPartnerFreeMonths(AssignVoucherFreeMonthsRequest request) {
    //  noop
  }

  @Override
  public List<PartnerResponseDto> getPartnerCampaignOwners() {
    return null;
  }

  @Override
  public ReferralInformationDto getReferralInformation(String memberId) {
    return null;
  }

  @Override
  public EligibleForReferralDto getEligibleForReferral(String memberId) {
    return new EligibleForReferralDto(false);
  }

  @Override
  public Boolean manualRedeemCampaign(String memberId, ManualRedeemCampaignRequest request) {
    return false;
  }

  @Override
  public Boolean manualUnRedeemCampaign(String memberId, ManualUnRedeemCampaignRequest request) {
    return false;
  }

  @Override
  public ManualRedeemEnableReferralsCampaignResponse manualRedeemEnableReferralsCampaign(Market market, ManualRedeemEnableReferralsCampaignRequest request) {
    return new ManualRedeemEnableReferralsCampaignResponse(new ArrayList<>());
  }

  @Override
  public List<RedeemedCampaignDto> redeemedCampaigns(String memberId) {
    return null;
  }

  @Override
  public List<MemberSearchResultDTOExtended> extendMemberSearchResult(List<Long> memberIds) {
    return memberIds.stream()
      .map(memberId -> new MemberSearchResultDTOExtended(
        memberId,
        null,
        null,
        null,
        null,
        null
      ))
      .collect(Collectors.toList());
  }
}
