package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.services.claims.dto.Claim;
import com.hedvig.backoffice.services.claims.dto.ClaimData;
import com.hedvig.backoffice.services.claims.dto.ClaimDeductibleUpdate;
import com.hedvig.backoffice.services.claims.dto.ClaimNote;
import com.hedvig.backoffice.services.claims.dto.ClaimPayment;
import com.hedvig.backoffice.services.claims.dto.ClaimReserveUpdate;
import com.hedvig.backoffice.services.claims.dto.ClaimSearchResultDTO;
import com.hedvig.backoffice.services.claims.dto.ClaimSortColumn;
import com.hedvig.backoffice.services.claims.dto.ClaimStateUpdate;
import com.hedvig.backoffice.services.claims.dto.ClaimType;
import com.hedvig.backoffice.services.claims.dto.ClaimTypeUpdate;
import com.hedvig.backoffice.services.claims.dto.ClaimsByIdsDto;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.hedvig.backoffice.services.claims.dto.CreateBackofficeClaimDTO;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;

public class ClaimsServiceImpl implements ClaimsService {
  private final ClaimsServiceClient client;

  @Autowired
  public ClaimsServiceImpl(@Value("${claims.baseUrl}") String baseUrl, ClaimsServiceClient client) {
    this.client = client;
  }

  @Override
  public List<Claim> list(String token) {
    return client.list(token);
  }

  @Override
  public List<Claim> listByUserId(String userId, String token) {
    return client.listByUserId(userId, token);
  }

  @Override
  public Claim find(String id, String token) {
    return client.find(id, token);
  }

  @Override
  public List<ClaimType> types(String token) {
    return client.types(token);
  }

  @Override
  public ClaimSearchResultDTO search(Integer page, Integer pageSize, ClaimSortColumn sortBy,
      Sort.Direction sortDirection, String token) {
    return client.search(page, pageSize, sortBy, sortDirection, token);
  }

  @Override
  public void addPayment(String memberId, ClaimPayment dto, String token) {
    switch (dto.getType()) {
      case Manual: {
        client.addPayment(dto, token);
        break;
      }

      case Automatic: {
        client.addAutomaticPayment(memberId, dto, token);
        break;
      }

      default:
        throw new RuntimeException(
            String.format("Unhandled Claim Payment Type: %s", dto.getType()));
    }
  }

  @Override
  public void addNote(ClaimNote dto, String token) {
    client.addNote(dto, token);
  }

  @Override
  public void addData(ClaimData data, String token) {
    client.addDataItem(data, token);
  }

  @Override
  public void changeState(ClaimStateUpdate state, String token) {
    client.updateState(state, token);
  }

  @Override
  public void changeReserve(ClaimReserveUpdate reserve, String token) {
    client.updateReserve(reserve, token);
  }

  @Override
  public void changeDeductible(ClaimDeductibleUpdate deductible, String token) {
    client.updateDeductible(deductible, token);
  }

  @Override
  public void changeType(ClaimTypeUpdate type, String token) {
    client.updateType(type, token);
  }

  @Override
  public Map<String, Long> statistics(String token) {
    return client.statistics(token);
  }

  @Override
  public long totalClaims(String token) {
    val stat = statistics(token);

    return stat.getOrDefault(ClaimState.OPEN.name(), 0L)
        + stat.getOrDefault(ClaimState.REOPENED.name(), 0L);
  }

  @Override
  public List<Claim> getClaimsByIds(List<UUID> ids) {
    return client.getClaimsByIds(new ClaimsByIdsDto(ids));
  }

  @Override
  public UUID createClaim(CreateBackofficeClaimDTO claimData, String token) {
    return client.createClaim(claimData, token).getClaimId();
  }
}
