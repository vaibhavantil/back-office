package com.hedvig.backoffice.services.claims;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.hedvig.backoffice.config.feign.ExternalServiceNotFoundException;
import com.hedvig.backoffice.services.claims.dto.Claim;
import com.hedvig.backoffice.services.claims.dto.ClaimData;
import com.hedvig.backoffice.services.claims.dto.ClaimEvent;
import com.hedvig.backoffice.services.claims.dto.ClaimNote;
import com.hedvig.backoffice.services.claims.dto.ClaimPayment;
import com.hedvig.backoffice.services.claims.dto.ClaimPaymentType;
import com.hedvig.backoffice.services.claims.dto.ClaimReserveUpdate;
import com.hedvig.backoffice.services.claims.dto.ClaimSearchResultDTO;
import com.hedvig.backoffice.services.claims.dto.ClaimSortColumn;
import com.hedvig.backoffice.services.claims.dto.ClaimSource;
import com.hedvig.backoffice.services.claims.dto.ClaimStateUpdate;
import com.hedvig.backoffice.services.claims.dto.ClaimType;
import com.hedvig.backoffice.services.claims.dto.ClaimTypeUpdate;
import com.hedvig.backoffice.services.claims.dto.CreateBackofficeClaimDTO;
import com.hedvig.backoffice.services.members.MemberService;
import com.hedvig.backoffice.services.settings.SystemSettingsService;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.val;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;

import static com.hedvig.backoffice.util.TzHelper.SWEDEN_TZ;

public class ClaimsServiceStub implements ClaimsService {

  private static Logger logger = LoggerFactory.getLogger(ClaimsServiceStub.class);

  private List<Claim> claims;
  private List<ClaimType> types;

  public ClaimsServiceStub(MemberService memberService, SystemSettingsService settingsService) {
    long minSignedOnDay = LocalDate.of(2011, 1, 3).toEpochDay();
    long maxSignedOnDay = LocalDate.of(2018, 12, 31).toEpochDay();
    try {
      val resource = new ClassPathResource("claim_types.json").getInputStream();
      val mapper = new ObjectMapper();
      types = mapper.readValue(resource,
          mapper.getTypeFactory().constructCollectionType(List.class, ClaimType.class));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    List<String> memberIds =
        memberService.search(null, "", settingsService.getInternalAccessToken()).stream()
            .map(o -> o.getMemberId().toString()).collect(Collectors.toList());

    claims = IntStream.range(0, 10).mapToObj(i -> {
      String id = UUID.randomUUID().toString();
      String memberId = memberIds.size() > i ? memberIds.get(i)
          : memberIds.size() > 0 ? memberIds.get(0) : UUID.randomUUID().toString();

      val note = new ClaimNote();
      note.setText("Testnote 123");
      val notes = Lists.newArrayList(note);

      val payment = new ClaimPayment();
      payment.setAmount(BigDecimal.valueOf(100));
      payment.setType(ClaimPaymentType.Manual);
      payment.setHandlerReference(UUID.randomUUID());
      payment.setDate(LocalDateTime.now());
      payment.setExGratia(false);
      val payments = Lists.newArrayList(payment);

      Claim claim = new Claim();
      claim.setId(id);
      claim.setUserId(memberId);
      claim.setState(ClaimState.OPEN);
      claim.setAudioURL("http://techslides.com/demos/samples/sample.aac");
      claim.setClaimSource(ClaimSource.APP);
      claim.setPayments(payments);
      claim.setNotes(notes);
      claim.setEvents(new ArrayList<>());
      claim.setData(new ArrayList<>());
      claim.setAssets(new ArrayList<>());
      claim.setReserve(BigDecimal.valueOf(100));

      long randomSignedOnDate =
          ThreadLocalRandom.current().nextLong(minSignedOnDay, maxSignedOnDay);
      LocalDate randomSignedOnLocalDate = LocalDate.ofEpochDay(randomSignedOnDate);
      LocalTime randomSignedOnLocalTime =
          LocalTime.ofNanoOfDay(randomSignedOnDate * RandomUtils.nextInt(0, 1000000));

      claim.setDate(LocalDateTime.of(randomSignedOnLocalDate, randomSignedOnLocalTime));

      return claim;
    }).collect(Collectors.toList());

    logger.info("CLAIMS SERVICE:");
    logger.info("class: " + ClaimsServiceStub.class.getName());
  }

  @Override
  public List<Claim> list(String token) {
    return claims;
  }

  @Override
  public List<Claim> listByUserId(String userId, String token) {
    return claims.stream().filter(c -> c.getUserId().equals(userId)).collect(Collectors.toList());
  }

  @Override
  public Claim find(String id, String token) {
    return claims.stream().filter(c -> c.getId().equals(id)).findAny()
        .orElseThrow(() -> new ExternalServiceNotFoundException("claim not found", "mock"));
  }

  @Override
  public List<ClaimType> types(String token) {
    return types;
  }

  @Override
  public ClaimSearchResultDTO search(Integer page, Integer pageSize, ClaimSortColumn sortBy,
      Sort.Direction sortDirection, String token) {
    List<Claim> claims = list(token);

    if (sortBy != null) {
      claims.sort(
          (sortDirection == Sort.Direction.DESC ? CLAIM_COMPARATORS_DESC : CLAIM_COMPARATORS_ASC)
              .get(sortBy));
    }

    if (page != null && pageSize != null) {
      int totalPages = claims.size() / pageSize;
      if (claims.size() % pageSize != 0) {
        totalPages++;
      }

      claims = claims.subList(page * pageSize, Math.min(claims.size(), pageSize * (page + 1)));
      return new ClaimSearchResultDTO(claims, page, totalPages);
    }

    return new ClaimSearchResultDTO(claims, null, null);
  }

  EnumMap<ClaimSortColumn, Comparator<Claim>> CLAIM_COMPARATORS_ASC =
      new EnumMap<ClaimSortColumn, Comparator<Claim>>(ClaimSortColumn.class) {
        private static final long serialVersionUID = 1L;

        {
          put(ClaimSortColumn.DATE, Comparator.comparing((Claim c) -> c.getDate(),
              Comparator.nullsLast(LocalDateTime::compareTo)));
          put(ClaimSortColumn.RESERVES, Comparator.comparing((Claim c) -> c.getReserve(),
              Comparator.nullsLast(BigDecimal::compareTo)));
          put(ClaimSortColumn.TYPE, Comparator.comparing((Claim c) -> c.getType(),
              Comparator.nullsLast(String::compareTo)));
          put(ClaimSortColumn.STATE, Comparator.comparing((Claim c) -> c.getState(),
              Comparator.nullsLast(ClaimState::compareTo)));
        }
      };

  EnumMap<ClaimSortColumn, Comparator<Claim>> CLAIM_COMPARATORS_DESC =
      new EnumMap<ClaimSortColumn, Comparator<Claim>>(ClaimSortColumn.class) {
        private static final long serialVersionUID = 1L;

        {
          put(ClaimSortColumn.DATE, Comparator
              .comparing((Claim c) -> c.getDate(), Comparator.nullsFirst(LocalDateTime::compareTo))
              .reversed());
          put(ClaimSortColumn.RESERVES, Comparator
              .comparing((Claim c) -> c.getReserve(), Comparator.nullsFirst(BigDecimal::compareTo))
              .reversed());
          put(ClaimSortColumn.TYPE,
              Comparator
                  .comparing((Claim c) -> c.getType(), Comparator.nullsFirst(String::compareTo))
                  .reversed());
          put(ClaimSortColumn.STATE, Comparator
              .comparing((Claim c) -> c.getState(), Comparator.nullsFirst(ClaimState::compareTo))
              .reversed());
        }
      };

  @Override
  public void addPayment(String memberId, ClaimPayment dto, String token) {
    Claim claim = find(dto.getClaimID(), token);
    dto.setDate(LocalDateTime.now());
    switch (dto.getType()) {
      case Manual: {
        dto.setHandlerReference(null);
      }
      case Automatic: {
        dto.setHandlerReference(UUID.randomUUID());
      }
    }
    claim.getPayments().add(dto);
    addEvent(claim, "[test] payment added");
  }

  @Override
  public void addNote(ClaimNote dto, String token) {
    Claim claim = find(dto.getClaimID(), token);
    claim.getNotes().add(dto);
    addEvent(claim, "[test] note added");
  }

  @Override
  public void addData(ClaimData data, String token) {
    Claim claim = find(data.getClaimID(), token);
    claim.getData().add(data);
    addEvent(claim, "[test] data added");
  }

  @Override
  public void changeState(ClaimStateUpdate state, String token) {
    Claim claim = find(state.getClaimID(), token);
    claim.setState(state.getState());
    addEvent(claim, "[test] state changed");
  }

  @Override
  public void changeReserve(ClaimReserveUpdate reserve, String token) {
    Claim claim = find(reserve.getClaimID(), token);
    claim.setReserve(reserve.getAmount());
    addEvent(claim, "[test] reserve changed");
  }

  @Override
  public void changeType(ClaimTypeUpdate type, String token) {
    Claim claim = find(type.getClaimID(), token);
    claim.setType(type.getType());
    addEvent(claim, "[test] type changed");
  }

  @Override
  public Map<String, Long> statistics(String token) {
    Map<String, Long> stat = new HashMap<>();
    for (ClaimState state : ClaimState.values()) {
      stat.put(state.name(), 10L);
    }

    return stat;
  }

  @Override
  public long totalClaims(String token) {
    val stat = statistics(token);

    return stat.getOrDefault(ClaimState.OPEN.name(), 0L)
        + stat.getOrDefault(ClaimState.REOPENED.name(), 0L);
  }

  @Override
  public UUID createClaim(CreateBackofficeClaimDTO claimData, String token) {
    UUID id = UUID.randomUUID();
    Claim claim = new Claim();
    claim.setId("" + id);
    claim.setDate(claimData.getRegistrationDate().atZone(SWEDEN_TZ).toLocalDateTime());
    claim.setState(ClaimState.OPEN);
    claim.setUserId(claimData.getMemberId());
    claim.setClaimSource(claimData.getClaimSource());

    claim.setPayments(new ArrayList<>());
    claim.setNotes(new ArrayList<>());
    claim.setEvents(new ArrayList<>());
    claim.setData(new ArrayList<>());
    claim.setAssets(new ArrayList<>());

    claims.add(claim);

    return id;
  }

  private void addEvent(Claim claim, String message) {
    ClaimEvent event = new ClaimEvent();
    event.setText(message);
    event.setDate(LocalDateTime.now());

    claim.getEvents().add(event);
  }

  @Override
  public List<Claim> getClaimsByIds(List<UUID> ids) {
    return claims.stream().filter(c -> ids.contains(UUID.fromString(c.getId())))
        .collect(Collectors.toList());
  }
}
