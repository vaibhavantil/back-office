package com.hedvig.backoffice.services.members;

import com.hedvig.backoffice.services.members.dto.InsuranceCancellationDTO;
import com.hedvig.backoffice.web.dto.MemberDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MemberServiceClientFallback implements MemberServiceClient {

  @Override
  public Page<MemberDTO> search(
      String status,
      String query,
      int pageNumber,
      int pageSize,
      String direction,
      String orderBy,
      String token) {
    log.error("request to member-service failed");
    return null;
  }

  @Override
  public Page<MemberDTO> listAllMembers(
      int pageNumber, int pageSize, String direction, String orderBy, String token) {
    log.error("request to member-service failed");
    return null;
  }

  @Override
  public MemberDTO member(String memberId, String token) {
    log.error("request to member-service failed");
    return null;
  }

  @Override
  public void editMember(String memberId, MemberDTO memberDTO, String token) {
    log.error("request to member-service failed");
  }

  @Override
  public void cancelInsurance(String id, InsuranceCancellationDTO dto, String token) {
    log.error("request to member-service failed");
  }
}
