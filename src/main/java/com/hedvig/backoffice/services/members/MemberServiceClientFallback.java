package com.hedvig.backoffice.services.members;

import com.hedvig.backoffice.web.dto.MemberDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class MemberServiceClientFallback implements MemberServiceClient {

    @Override
    public List<MemberDTO> search(String status, String query, String token) {
        log.error("request to member-service failed");
        return new ArrayList<>();
    }

    @Override
    public MemberDTO member(String memberId, String token) {
        log.error("request to member-service failed");
        return null;
    }

    @Override
    public void editMember(String memberId, MemberDTO memberDTO, String token){
        log.error("request to member-service failed");
    }
}
