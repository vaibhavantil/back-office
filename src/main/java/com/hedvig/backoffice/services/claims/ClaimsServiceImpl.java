package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.services.claims.dto.*;
import feign.FeignException;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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
  public ClaimPaymentResponse addPayment(String memberId, ClaimPayment dto, String token) {
    switch (dto.getType()) {
      case Manual: {
        try {
          client.addPayment(dto, token);
          return ClaimPaymentResponse.SUCCESSFUL;
        } catch (FeignException ex) {
          if (ex.status() == HttpStatus.FORBIDDEN.value()) {
            return ClaimPaymentResponse.FORBIDDEN;
          }
          return ClaimPaymentResponse.FAILED;
        }
      }

      case Automatic: {
        try {
          client.addAutomaticPayment(memberId,
            ClaimPaymentRequest.fromClaimPayment(dto));
          return ClaimPaymentResponse.SUCCESSFUL;
        } catch (FeignException ex) {
          if (ex.status() == HttpStatus.FORBIDDEN.value()) {
            return ClaimPaymentResponse.FORBIDDEN;
          }
          return ClaimPaymentResponse.FAILED;
        }
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

  @Override
  public void markEmployeeClaim(EmployeeClaimRequestDTO dto, String token) {
    client.markEmployeeClaim(dto, token);
  }
}
