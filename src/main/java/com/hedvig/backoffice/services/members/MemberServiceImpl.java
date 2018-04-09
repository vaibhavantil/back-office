package com.hedvig.backoffice.services.members;

import com.hedvig.backoffice.web.dto.MemberDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class MemberServiceImpl implements MemberService {

    private static Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);

    private final MemberServiceClient client;

    public MemberServiceImpl(MemberServiceClient client) {
        this.client = client;

        logger.info("MEMBER SERVICE:");
        logger.info("class: " + MemberServiceImpl.class.getName());
    }

    @Override
    public List<MemberDTO> search(String status, String query, String token) {
        return client.search(status, query, token);
    }

    @Override
    public MemberDTO findByHid(String hid, String token) {
        return client.member(hid, token);
    }

}
