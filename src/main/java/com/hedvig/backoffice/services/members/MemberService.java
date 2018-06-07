package com.hedvig.backoffice.services.members;

import com.hedvig.backoffice.services.members.dto.InsuranceCancellationDTO;
import com.hedvig.backoffice.web.dto.MemberDTO;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface MemberService {

  Page<MemberDTO> search(
      String status,
      String query,
      int pageNumber,
      int pageSize,
      String direction,
      String orderBy,
      String token);

  Page<MemberDTO> listAllMembers(
      int pageNumber, int pageSize, String direction, String orderBy, String token);

  MemberDTO findByMemberId(String memberId, String token);

  void editMember(String memberId, MemberDTO memberDTO, String token);

  void cancelInsurance(String hid, InsuranceCancellationDTO dto, String token);
}
