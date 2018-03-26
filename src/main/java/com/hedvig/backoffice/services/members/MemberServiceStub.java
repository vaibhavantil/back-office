package com.hedvig.backoffice.services.members;

import com.hedvig.backoffice.web.dto.MemberDTO;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MemberServiceStub implements MemberService {

    private static Logger logger = LoggerFactory.getLogger(MemberServiceStub.class);
    private static long[] testMemberIds = { 123456L };

    private List<MemberDTO> users;

    public MemberServiceStub() {
        String[] statuses = { "INITIATED", "ONBOARDING", "SIGNED", "INACTIVATED" };

        users = IntStream.range(0, 200).mapToObj(i -> {
            long id = i < testMemberIds.length ? testMemberIds[i] : RandomUtils.nextInt();
            MemberDTO user = new MemberDTO(id);
            user.setFirstName("Test user " + id);
            user.setStatus(statuses[RandomUtils.nextInt(0, 4)]);
            return user;
        }).collect(Collectors.toList());

        logger.info("MEMBER SERVICE:");
        logger.info("class: " + MemberServiceStub.class.getName());
    }

    @Override
    public Optional<List<MemberDTO>> search(String status, String query) throws MemberServiceException {
        if (StringUtils.isBlank(status) && StringUtils.isBlank(query)) {
            return Optional.of(users);
        }

        List<MemberDTO> result = users.stream()
                .filter(u -> (StringUtils.isNotBlank(query) && u.getFirstName().contains(query))
                        || (StringUtils.isNotBlank(status) && u.getStatus().contains(status)))
                .collect(Collectors.toList());

        return Optional.of(result);
    }

    @Override
    public Optional<MemberDTO> findByHid(String hid) throws MemberNotFoundException, MemberServiceException {
        return Optional.of(users.stream()
                .filter(u -> u.getHid().equals(hid))
                .findAny()
                .orElse(new MemberDTO(Long.parseLong(hid))));
    }

}
