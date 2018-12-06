package com.hedvig.backoffice.services.members;

import com.hedvig.backoffice.services.members.dto.InsuranceCancellationDTO;
import com.hedvig.backoffice.services.members.dto.MembersSortColumn;
import com.hedvig.backoffice.services.members.dto.MemberDTO;
import com.hedvig.backoffice.web.dto.MemberFraudulentStatusDTO;
import com.hedvig.backoffice.web.dto.MemberStatus;
import com.hedvig.backoffice.services.members.dto.MembersSearchResultDTO;
import java.util.List;
import org.springframework.data.domain.Sort;

public interface MemberService {

  List<MemberDTO> search(MemberStatus status, String query, String token);

  MembersSearchResultDTO searchPaged(MemberStatus status, String query, Integer page,
    Integer pageSize, MembersSortColumn sortBy, Sort.Direction sortDirection, String token);

  MemberDTO findByMemberId(String memberId, String token);

  void editMember(String memberId, MemberDTO memberDTO, String token);

  void cancelInsurance(String hid, InsuranceCancellationDTO dto, String token);

  List<MemberDTO> getMembersByIds(List<String> ids);

  void setFraudulentStatus(String memberId, MemberFraudulentStatusDTO dto, String token);
}
