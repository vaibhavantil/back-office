package com.hedvig.backoffice.services.members;

import com.hedvig.backoffice.web.dto.MemberDTO;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    List<MemberDTO> search(String status, String query);
    MemberDTO findByHid(String hid);

}
