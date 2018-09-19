package com.hedvig.backoffice.services.members;

import com.hedvig.backoffice.services.members.dto.InsuranceCancellationDTO;
import com.hedvig.backoffice.services.members.dto.MemberSanctionStatus;
import com.hedvig.backoffice.web.dto.MemberDTO;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;

public class MemberServiceImpl implements MemberService {

  private static Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);

  private final MemberServiceClient client;

  public MemberServiceImpl(MemberServiceClient client) {
    this.client = client;

    logger.info("MEMBER SERVICE:");
    logger.info("class: " + MemberServiceImpl.class.getName());
  }

  @Override
  public List<MemberDTO> search(String status, String query, String token) {
    return client.search(status, query, token);
  }

  @Override
  public MemberDTO findByMemberId(String memberId, String token) {
    return client.member(memberId, token);
  }

  @Override
  public void editMember(String memberId, MemberDTO memberDTO, String token) {
    if (!client.member(memberId, token).equals(memberDTO)) {
      client.editMember(memberId, memberDTO, token);
    }
  }

  @Override
  public void cancelInsurance(String hid, InsuranceCancellationDTO dto, String token) {
    client.cancelInsurance(hid, dto, token);
  }

  @Override
  public List<MemberDTO> getMembersByIds(List<String> ids) {
    return client.getMembers(ids);
  }

  @Override
  public MemberSanctionStatus getMemberSanctionStatus(String memberId) {
    try {
      ResponseEntity<MemberSanctionStatus> response = client.getMemberSanctionStatus(memberId);
      return response.getBody();
    } catch (RestClientResponseException ex) {
      if (ex.getRawStatusCode() == 404) {
        return MemberSanctionStatus.Undetermined;
      }
      logger.error("Could not check sanction list for member {} , {}", memberId, ex);
      throw ex;
    }
  }
}
