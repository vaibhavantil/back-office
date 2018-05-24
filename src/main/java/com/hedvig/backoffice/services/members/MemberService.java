package com.hedvig.backoffice.services.members;

import com.hedvig.backoffice.web.dto.MemberDTO;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    List<MemberDTO> search(String status, String query, String token);
    MemberDTO findByMemberId(String memberId, String token);
    void editMember(String memberId, MemberDTO memberDTO, String token);
}
