package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.services.claims.dto.Claim;
import com.hedvig.backoffice.services.claims.dto.ClaimData;
import com.hedvig.backoffice.services.claims.dto.ClaimNote;
import com.hedvig.backoffice.services.claims.dto.ClaimPayment;
import com.hedvig.backoffice.services.claims.dto.ClaimReserveUpdate;
import com.hedvig.backoffice.services.claims.dto.ClaimStateUpdate;
import com.hedvig.backoffice.services.claims.dto.ClaimType;
import com.hedvig.backoffice.services.claims.dto.ClaimTypeUpdate;
import java.util.List;
import java.util.Map;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public class ClaimsServiceImpl implements ClaimsService {

  private static Logger logger = LoggerFactory.getLogger(ClaimsServiceImpl.class);

  private final ClaimsServiceClient client;

  @Autowired
  public ClaimsServiceImpl(@Value("${claims.baseUrl}") String baseUrl, ClaimsServiceClient client) {
    this.client = client;

    logger.info("CLAIMS SERVICE:");
    logger.info("class: " + ClaimsServiceImpl.class.getName());
    logger.info("base url: " + baseUrl);
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
  public void addPayment(ClaimPayment dto, String token) {
    client.addPayment(dto, token);
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
}
