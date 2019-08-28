package com.hedvig.backoffice.services.members;

import java.util.List;
import java.util.Optional;

import com.hedvig.backoffice.graphql.types.WhitelistMember;
import com.hedvig.backoffice.services.members.dto.*;
import com.hedvig.backoffice.web.dto.MemberFraudulentStatusDTO;
import com.hedvig.backoffice.web.dto.MemberStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

public class MemberServiceImpl implements MemberService {

  private static Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);

  private final MemberServiceClient client;

  public MemberServiceImpl(MemberServiceClient client) {
    this.client = client;

    logger.info("MEMBER SERVICE:");
    logger.info("class: " + MemberServiceImpl.class.getName());
  }

  @Override
  public List<MemberDTO> search(MemberStatus status, String query, String token) {
    return client.search(status, query, token);
  }

  @Override
  public MembersSearchResultDTO searchPaged(MemberStatus status, String query, Integer page,
      Integer pageSize, MembersSortColumn sortBy, Sort.Direction sortDirection, String token) {
    return client.searchPaged(status, query, page, pageSize, sortBy, sortDirection, token);
  }

  @Override
  public MemberDTO findByMemberId(String memberId, String token) {
    return client.member(memberId, token);
  }

  @Override
  public void editMember(String memberId, MemberDTO memberDTO, String token) {
    if (!client.member(memberId, token).equals(memberDTO))
      client.editMember(memberId, memberDTO, token);
  }

  @Override
  public void cancelInsurance(String hid, InsuranceCancellationDTO dto, String token) {
    client.cancelInsurance(hid, dto, token);
  }

  @Override
  public List<MemberDTO> getMembersByIds(List<String> ids) {
    return client.getMembers(new ChargeMembersDTO(ids));
  }

  @Override
  public void setFraudulentStatus(String memberId, MemberFraudulentStatusDTO dto, String token) {
    client.setFraudulentStatus(memberId, dto, token);
    logger.info("Change member status for "+memberId+": "+dto.getFraudulentStatus()+", "+dto.getFraudulentStatusDescription());
  }

  @Override
  public Optional<PersonDTO> getPerson(String memberId) {
    return client.getPerson(memberId);
  }

  @Override
  public void whitelistMember(String memberId, String whitelistedBy) {
    client.whitelistMember(memberId, whitelistedBy);
  }
}
