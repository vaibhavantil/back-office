package com.hedvig.backoffice.services.members;

import com.hedvig.backoffice.web.dto.MemberDTO;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MemberServiceStub implements MemberService {

    private static Logger logger = LoggerFactory.getLogger(MemberServiceStub.class);

    private List<MemberDTO> users;

    public MemberServiceStub() {
        users = IntStream.range(0, 10).mapToObj(i -> {
            MemberDTO user = new MemberDTO(RandomUtils.nextInt());
            user.setFirstName("Test user " + i);
            return user;
        }).collect(Collectors.toList());

        logger.info("MEMBER SERVICE:");
        logger.info("class: " + MemberServiceStub.class.getName());
    }

    @Override
    public List<MemberDTO> list() throws MemberServiceException {
        return users;
    }

    @Override
    public List<MemberDTO> find(String query) throws MemberServiceException {
        List<MemberDTO> result = users.stream()
                .filter(u -> u.getHid().contains(query) || u.getFirstName().contains(query))
                .collect(Collectors.toList());

        if (result.size() == 0) {
            MemberDTO dto = new MemberDTO(RandomUtils.nextLong());
            dto.setFirstName(query);
            result.add(dto);
        }

        return result;
    }

    @Override
    public MemberDTO findByHid(String hid) throws MemberNotFoundException, MemberServiceException {
        return users.stream()
                .filter(u -> u.getHid().equals(hid))
                .findAny()
                .orElse(new MemberDTO(Long.parseLong(hid)));
    }

}
