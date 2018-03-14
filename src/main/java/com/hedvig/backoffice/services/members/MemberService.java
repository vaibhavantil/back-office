package com.hedvig.backoffice.services.members;

import com.hedvig.backoffice.web.dto.MemberDTO;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    Optional<List<MemberDTO>> search(String status, String query);
    Optional<MemberDTO> findByHid(String hid) throws MemberNotFoundException;
    void sendNotificationMail(String hid);
}
