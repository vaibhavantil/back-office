package com.hedvig.backoffice.services.members;

import com.hedvig.backoffice.graphql.types.WhitelistMember;
import com.hedvig.backoffice.graphql.types.Whitelisted;
import com.hedvig.backoffice.services.members.dto.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.hedvig.backoffice.web.dto.MemberFraudulentStatusDTO;
import com.hedvig.backoffice.web.dto.MemberStatus;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import static com.hedvig.backoffice.services.members.dto.Flag.RED;

public class MemberServiceStub implements MemberService {

  private static Logger logger = LoggerFactory.getLogger(MemberServiceStub.class);
  public static long[] testMemberIds = {123456L, 3267661L, 2820671L, 6865256L, 9417985L, 9403769L,
      6871398L, 5418127L, 2134653L, 2503961L, 5867700L, 4254211, 9908657L, 1074023L};

  private List<MemberDTO> users;

  public MemberServiceStub() {

    long minBirthDay = LocalDate.of(1970, 1, 1).toEpochDay();
    long maxBirthDay = LocalDate.of(2010, 12, 31).toEpochDay();
    long minSignedOnDay = LocalDate.of(2011, 1, 3).toEpochDay();
    long maxSignedOnDay = LocalDate.of(2018, 12, 31).toEpochDay();

    MemberStatus[] memberStatuses = MemberStatus.values();

    users = IntStream.range(0, testMemberIds.length + 100).mapToObj(i -> {
      long id = i < testMemberIds.length ? testMemberIds[i] : RandomUtils.nextInt();
      MemberDTO user = new MemberDTO(id);
      user.setFirstName("Test user " + id);

      user.setStatus(memberStatuses[RandomUtils.nextInt(0, memberStatuses.length)]);

                  user.setFraudulentStatus(FraudulentStatus.values()[RandomUtils.nextInt(0, FraudulentStatus.values().length)].toString());

      long randomDay = ThreadLocalRandom.current().nextLong(minBirthDay, maxBirthDay);
      LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
      user.setBirthDate(randomDate);

      long randomSignedOnDate =
          ThreadLocalRandom.current().nextLong(minSignedOnDay, maxSignedOnDay);
      LocalDate randomSignedOnLocalDate = LocalDate.ofEpochDay(randomSignedOnDate);
      LocalTime randomSignedOnLocalTime =
          LocalTime.ofNanoOfDay(randomSignedOnDate * RandomUtils.nextInt(0, 1000000));
      user.setCreatedOn(Instant
          .from(ZonedDateTime.of(LocalDateTime.of(randomSignedOnLocalDate, randomSignedOnLocalTime),
              ZoneId.of("Europe/Stockholm"))));

      if (user.getStatus() == MemberStatus.SIGNED) {
        user.setSignedOn(Instant.from(
            ZonedDateTime.of(LocalDateTime.of(randomSignedOnLocalDate, randomSignedOnLocalTime),
                ZoneId.of("Europe/Stockholm"))));
      }

      return user;
    }).collect(Collectors.toList());

    logger.info("MEMBER SERVICE:");
    logger.info("class: " + MemberServiceStub.class.getName());
  }

  @Override
  public List<MemberDTO> search(MemberStatus status, String query, String token) {
    if (status == null && StringUtils.isBlank(query)) {
      return users;
    }

    List<MemberDTO> result = users.stream()
        .filter(u -> (StringUtils.isNotBlank(query) && u.getFirstName().contains(query))
            || (status != null && u.getStatus() == status))
        .collect(Collectors.toList());

    return result;
  }

  @Override
  public MembersSearchResultDTO searchPaged(MemberStatus status, String query, Integer page,
      Integer pageSize, MembersSortColumn sortBy, Sort.Direction sortDirection, String token) {
    List<MemberDTO> members = search(status, query, token);
    if (sortBy != null) {
      members.sort(
          (sortDirection == Sort.Direction.DESC ? MEMBER_COMPARATORS_DESC : MEMBER_COMPARATORS_ASC)
              .get(sortBy));
    }

    if (page != null && pageSize != null) {
      int totalPages = members.size() / pageSize;
      if (members.size() % pageSize != 0) {
        totalPages++;
      }

      int sublistStart = page * pageSize;
      int sublistEnd = sublistStart + pageSize;
      members = members.subList(sublistStart, Math.min(members.size(), sublistEnd));
      return new MembersSearchResultDTO(members, page, totalPages);
    }

    return new MembersSearchResultDTO(members, null, null);
  }

  private static EnumMap<MembersSortColumn, Comparator<MemberDTO>> MEMBER_COMPARATORS_ASC =
      new EnumMap<MembersSortColumn, Comparator<MemberDTO>>(MembersSortColumn.class) {
        private static final long serialVersionUID = 1L;

        {
          put(MembersSortColumn.NAME,
              Comparator.comparing((MemberDTO m) -> "" + m.getLastName() + m.getFirstName(),
                  Comparator.nullsLast(String::compareTo)));
          put(MembersSortColumn.SIGN_UP, Comparator.comparing((MemberDTO m) -> m.getSignedOn(),
              Comparator.nullsLast(Instant::compareTo)));
          put(MembersSortColumn.CREATED, Comparator.comparing((MemberDTO m) -> m.getCreatedOn(),
              Comparator.nullsLast(Instant::compareTo)));
        }
      };

  private static EnumMap<MembersSortColumn, Comparator<MemberDTO>> MEMBER_COMPARATORS_DESC =
      new EnumMap<MembersSortColumn, Comparator<MemberDTO>>(MembersSortColumn.class) {
        private static final long serialVersionUID = 1L;

        {
          put(MembersSortColumn.NAME,
              Comparator.comparing((MemberDTO m) -> "" + m.getLastName() + m.getFirstName(),
                  Comparator.nullsFirst(String::compareTo)).reversed());
          put(MembersSortColumn.SIGN_UP, Comparator.comparing((MemberDTO m) -> m.getSignedOn(),
              Comparator.nullsFirst(Instant::compareTo)).reversed());
          put(MembersSortColumn.CREATED, Comparator.comparing((MemberDTO m) -> m.getCreatedOn(),
              Comparator.nullsFirst(Instant::compareTo)).reversed());
        }
      };


  @Override
  public MemberDTO findByMemberId(String memberId, String token) {
    return users.stream().filter(u -> u.getMemberId().toString().equals(memberId)).findAny()
        .orElse(new MemberDTO(Long.parseLong(memberId)));
  }

  @Override
  public void cancelInsurance(String hid, InsuranceCancellationDTO dto, String token) {
    logger.info("Insurance " + hid + " Cancelled at date " + dto.getCancellationDate().toString());
  }

  @Override
  public void editMember(String memberId, MemberDTO memberDTO, String token) {
    users = users.stream().map(o -> o.getMemberId().toString().equals(memberId) ? memberDTO : o)
        .collect(Collectors.toList());
  }

  @Override
  public List<MemberDTO> getMembersByIds(List<String> ids) {
    return users.stream().filter(u -> ids.contains(u.getMemberId().toString()))
        .collect(Collectors.toList());
  }

  @Override
  public void setFraudulentStatus(String memberId, MemberFraudulentStatusDTO dto, String token) {

  }

  @Override
  public void whitelistMember(String memberId, String whitelistedBy) {

  }

  @Override
  public Optional<PersonDTO> getPerson(String memberId) {

    List<Flag> personFlags = new ArrayList<>();
    personFlags.add(RED);
    personFlags.add(RED);

    List <PaymentDefaultDTO> paymentDefaults = new ArrayList<>();
    paymentDefaults.add(new PaymentDefaultDTO(
      2017,
      12,
      "AV",
      "AV",
      Money.of(200, "SEK"),
      "6",
      "AB"
    ));
    paymentDefaults.add(new PaymentDefaultDTO(
      2019,
      8,
      "AI",
      "AI name",
      Money.of(125, "SEK"),
      "5",
      "AB"
    ));

    DebtDTO debtDTO = new DebtDTO(
      paymentDefaults,
      LocalDate.parse("2019-01-01"),
      Money.of(10000, "SEK"),
      10,
      Money.of(20, "SEK"),
      2,
      Money.of(34, "SEK"),
      Instant.now(),
      LocalDateTime.now()
    );

    Whitelisted whitelisted = new Whitelisted(
      Instant.now(),
      "blah@hedvig.com"
    );

    PersonStatusDTO personStatusDTO = new PersonStatusDTO(
      RED,
      true
    );

    PersonDTO personDTO = new PersonDTO(
      personFlags,
      debtDTO,
      Optional.of(whitelisted),
      personStatusDTO
    );

    return Optional.of(personDTO);
  }

  private enum FraudulentStatus {
    NOT_FRAUD,
    SUSPECTED_FRAUD,
    CONFIRMED_FRAUD
  }

}
