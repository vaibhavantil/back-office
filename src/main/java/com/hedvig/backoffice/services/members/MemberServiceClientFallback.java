package com.hedvig.backoffice.services.members;

import com.hedvig.backoffice.services.members.dto.InsuranceCancellationDTO;
import com.hedvig.backoffice.web.dto.MemberDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MemberServiceClientFallback implements MemberServiceClient {

  @Override
  public List<MemberDTO> search(String status, String query, String token) {
    log.error("request to member-service failed");
    return new ArrayList<>();
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

  @Override
  public List<MemberDTO> getMembers(List<String> memberIds) {
    log.error("request to member-service failed");
    return null;
  }
}
