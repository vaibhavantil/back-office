package com.hedvig.backoffice.services.members;

import com.hedvig.backoffice.web.dto.MemberDTO;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MemberServiceStub implements MemberService {

    private static Logger logger = LoggerFactory.getLogger(MemberServiceStub.class);
    public static long[] testMemberIds = { 123456L, 3267661L, 2820671L, 6865256L, 9417985L,
            9403769L, 6871398L, 5418127L, 2134653L, 2503961L, 5867700L, 4254211, 9908657L, 1074023L
    };

    private List<MemberDTO> users;

    public MemberServiceStub() {
        String[] statuses = { "INITIATED", "ONBOARDING", "SIGNED", "INACTIVATED" };

        long minDay = LocalDate.of(1970, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(2018, 12, 31).toEpochDay();

        users = IntStream.range(0, testMemberIds.length + 100).mapToObj(i -> {
            long id = i < testMemberIds.length ? testMemberIds[i] : RandomUtils.nextInt();
            MemberDTO user = new MemberDTO(id);
            user.setFirstName("Test user " + id);
            user.setStatus(statuses[RandomUtils.nextInt(0, 4)]);

            long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
            LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
            user.setBirthDate(randomDate);

            return user;
        }).collect(Collectors.toList());

        logger.info("MEMBER SERVICE:");
        logger.info("class: " + MemberServiceStub.class.getName());
    }

    @Override
    public List<MemberDTO> search(String status, String query, String token) {
        if (StringUtils.isBlank(status) && StringUtils.isBlank(query)) {
            return users;
        }

        List<MemberDTO> result = users.stream()
                .filter(u -> (StringUtils.isNotBlank(query) && u.getFirstName().contains(query))
                        || (StringUtils.isNotBlank(status) && u.getStatus().contains(status)))
                .collect(Collectors.toList());

        return result;
    }

    @Override
    public MemberDTO findByMemberId(String memberId, String token) {
        return users.stream()
                .filter(u -> u.getMemberId().toString().equals(memberId))
                .findAny()
                .orElse(new MemberDTO(Long.parseLong(memberId)));
    }

    @Override
    public void editMember(String memberId, MemberDTO memberDTO, String token){
        users = users.stream()
                    .map(o -> o.getMemberId().toString().equals(memberId) ? memberDTO : o)
                    .collect(Collectors.toList());
    }
}
