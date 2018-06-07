package com.hedvig.backoffice.services.members;

import com.hedvig.backoffice.services.members.dto.InsuranceCancellationDTO;
import com.hedvig.backoffice.web.dto.MemberDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import org.springframework.data.domain.Page;

public class MemberServiceImpl implements MemberService {

  private static Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);

  private final MemberServiceClient client;

  public MemberServiceImpl(MemberServiceClient client) {
    this.client = client;

    logger.info("MEMBER SERVICE:");
    logger.info("class: " + MemberServiceImpl.class.getName());
  }

  @Override
  public Page<MemberDTO> search(
      String status,
      String query,
      int pageNumber,
      int pageSize,
      String direction,
      String orderBy,
      String token) {
    return client.search(status, query, pageNumber, pageSize, direction, orderBy, token);
  }

  @Override
  public Page<MemberDTO> listAllMembers(
      int pageNumber, int pageSize, String direction, String orderBy, String token) {
    return client.listAllMembers(pageNumber, pageSize, direction, orderBy, token);
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
}
