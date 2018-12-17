package com.hedvig.backoffice.services.claims;

import com.hedvig.backoffice.services.claims.dto.ClaimPaymentResponse;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.hedvig.backoffice.services.claims.dto.Claim;
import com.hedvig.backoffice.services.claims.dto.ClaimData;
import com.hedvig.backoffice.services.claims.dto.ClaimNote;
import com.hedvig.backoffice.services.claims.dto.ClaimPayment;
import com.hedvig.backoffice.services.claims.dto.ClaimReserveUpdate;
import com.hedvig.backoffice.services.claims.dto.ClaimSearchResultDTO;
import com.hedvig.backoffice.services.claims.dto.ClaimSortColumn;
import com.hedvig.backoffice.services.claims.dto.ClaimStateUpdate;
import com.hedvig.backoffice.services.claims.dto.ClaimType;
import com.hedvig.backoffice.services.claims.dto.ClaimTypeUpdate;
import com.hedvig.backoffice.services.claims.dto.CreateBackofficeClaimDTO;
import org.springframework.data.domain.Sort;

public interface ClaimsService {

  List<Claim> list(String token);

  List<Claim> listByUserId(String userId, String token);

  Claim find(String id, String token);

  List<ClaimType> types(String token);

  ClaimSearchResultDTO search(Integer page, Integer pageSize, ClaimSortColumn sortBy,
      Sort.Direction sortDirection, String token);

  ClaimPaymentResponse addPayment(String memberId, ClaimPayment dto, String token);

  void addNote(ClaimNote dto, String token);

  void addData(ClaimData data, String token);

  void changeState(ClaimStateUpdate state, String token);

  void changeReserve(ClaimReserveUpdate reserve, String token);

  void changeType(ClaimTypeUpdate type, String token);

  Map<String, Long> statistics(String token);

  long totalClaims(String token);

  List<Claim> getClaimsByIds(List<UUID> ids);

  UUID createClaim(CreateBackofficeClaimDTO claimData, String token);
}
