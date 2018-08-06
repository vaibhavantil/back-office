package com.hedvig.backoffice.services.members;

import com.hedvig.backoffice.services.members.dto.InsuranceCancellationDTO;
import com.hedvig.backoffice.web.dto.MemberDTO;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemberServiceStub implements MemberService {

  private static Logger logger = LoggerFactory.getLogger(MemberServiceStub.class);
  public static long[] testMemberIds = {
    123456L, 3267661L, 2820671L, 6865256L, 9417985L, 9403769L, 6871398L, 5418127L, 2134653L,
    2503961L, 5867700L, 4254211, 9908657L, 1074023L
  };

  private List<MemberDTO> users;

  public MemberServiceStub() {
    String[] statuses = {"INITIATED", "ONBOARDING", "SIGNED", "INACTIVATED"};

    long minBirthDay = LocalDate.of(1970, 1, 1).toEpochDay();
    long maxBirthDay = LocalDate.of(2010, 12, 31).toEpochDay();
    long minSignedOnDay = LocalDate.of(2011, 1, 3).toEpochDay();
    long maxSignedOnDay = LocalDate.of(2018, 12, 31).toEpochDay();

    users =
        IntStream.range(0, testMemberIds.length + 100)
            .mapToObj(
                i -> {
                  long id = i < testMemberIds.length ? testMemberIds[i] : RandomUtils.nextInt();
                  MemberDTO user = new MemberDTO(id);
                  user.setFirstName("Test user " + id);
                  user.setStatus(statuses[RandomUtils.nextInt(0, 4)]);

                  long randomDay = ThreadLocalRandom.current().nextLong(minBirthDay, maxBirthDay);
                  LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
                  user.setBirthDate(randomDate);

                  long randomSignedOnDate =
                      ThreadLocalRandom.current().nextLong(minSignedOnDay, maxSignedOnDay);
                  LocalDate randomSignedOnLocalDate = LocalDate.ofEpochDay(randomSignedOnDate);
                  LocalTime randomSignedOnLocalTime =
                      LocalTime.ofNanoOfDay(randomSignedOnDate * RandomUtils.nextInt(0, 1000000));
                  user.setCreatedOn(
                      Instant.from(
                          ZonedDateTime.of(
                              LocalDateTime.of(randomSignedOnLocalDate, randomSignedOnLocalTime),
                              ZoneId.of("Europe/Stockholm"))));

                  if (user.getStatus().equals(statuses[2])) {
                    user.setSignedOn(
                        Instant.from(
                            ZonedDateTime.of(
                                LocalDateTime.of(randomSignedOnLocalDate, randomSignedOnLocalTime),
                                ZoneId.of("Europe/Stockholm"))));
                  }

                  return user;
                })
            .collect(Collectors.toList());

    logger.info("MEMBER SERVICE:");
    logger.info("class: " + MemberServiceStub.class.getName());
  }

  @Override
  public List<MemberDTO> search(String status, String query, String token) {
    if (StringUtils.isBlank(status) && StringUtils.isBlank(query)) {
      return users;
    }

    List<MemberDTO> result =
        users
            .stream()
            .filter(
                u ->
                    (StringUtils.isNotBlank(query) && u.getFirstName().contains(query))
                        || (StringUtils.isNotBlank(status) && u.getStatus().contains(status)))
            .collect(Collectors.toList());

    return result;
  }

  @Override
  public MemberDTO findByMemberId(String memberId, String token) {
    return users
        .stream()
        .filter(u -> u.getMemberId().toString().equals(memberId))
        .findAny()
        .orElse(new MemberDTO(Long.parseLong(memberId)));
  }

  @Override
  public void cancelInsurance(String hid, InsuranceCancellationDTO dto, String token) {
    logger.info("Insurance " + hid + " Cancelled at date " + dto.getCancellationDate().toString());
  }

  @Override
  public void editMember(String memberId, MemberDTO memberDTO, String token) {
    users =
        users
            .stream()
            .map(o -> o.getMemberId().toString().equals(memberId) ? memberDTO : o)
            .collect(Collectors.toList());
  }

  @Override
  public List<MemberDTO> getMembersByIds(List<String> ids) {
    return users
        .stream()
        .filter(u -> ids.contains(u.getMemberId().toString()))
        .collect(Collectors.toList());
  }
}
