package com.hedvig.backoffice.services.members;

import com.hedvig.backoffice.services.members.dto.InsuranceCancellationDTO;
import com.hedvig.backoffice.web.dto.MemberDTO;
import java.util.List;

public interface MemberService {

  List<MemberDTO> search(String status, String query, String token);

  MemberDTO findByMemberId(String memberId, String token);

  void editMember(String memberId, MemberDTO memberDTO, String token);

  void cancelInsurance(String hid, InsuranceCancellationDTO dto, String token);

  List<MemberDTO> getMembersByIds(List<String> ids);
}
