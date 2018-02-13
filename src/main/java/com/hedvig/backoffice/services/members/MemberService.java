package com.hedvig.backoffice.services.members;

import com.hedvig.backoffice.web.dto.MemberDTO;

import java.util.List;

public interface MemberService {

    List<MemberDTO> list() throws MemberServiceException;
    List<MemberDTO> find(String query) throws MemberServiceException;
    MemberDTO findByHid(String hid) throws MemberNotFoundException, MemberServiceException;

}
