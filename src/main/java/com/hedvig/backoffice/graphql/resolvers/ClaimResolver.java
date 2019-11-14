package com.hedvig.backoffice.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.hedvig.backoffice.graphql.Util;
import com.hedvig.backoffice.graphql.dataloaders.MemberLoader;
import com.hedvig.backoffice.graphql.types.AccidentalDamageClaim;
import com.hedvig.backoffice.graphql.types.ApplianceClaim;
import com.hedvig.backoffice.graphql.types.AssaultClaim;
import com.hedvig.backoffice.graphql.types.Claim;
import com.hedvig.backoffice.graphql.types.ClaimFileUpload;
import com.hedvig.backoffice.graphql.types.ConfirmedFraudClaim;
import com.hedvig.backoffice.graphql.types.FileUpload;
import com.hedvig.backoffice.graphql.types.FireDamageClaim;
import com.hedvig.backoffice.graphql.types.LiabilityClaim;
import com.hedvig.backoffice.graphql.types.LuggageDelayClaim;
import com.hedvig.backoffice.graphql.types.Member;
import com.hedvig.backoffice.graphql.types.NotCoveredClaim;
import com.hedvig.backoffice.graphql.types.TestClaim;
import com.hedvig.backoffice.graphql.types.TheftClaim;
import com.hedvig.backoffice.graphql.types.TravelAccidentClaim;
import com.hedvig.backoffice.graphql.types.WaterDamageClaim;
import com.hedvig.backoffice.services.MessagesFrontendPostprocessor;
import com.hedvig.backoffice.services.claims.ClaimsService;
import com.hedvig.backoffice.services.claims.dto.ClaimFileDTO;
import com.hedvig.backoffice.services.claims.dto.ClaimsFilesUploadDTO;
import com.hedvig.backoffice.services.messages.dto.FileUploadDTO;
import graphql.schema.DataFetchingEnvironment;
import java.util.ArrayList;
import java.util.List;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class ClaimResolver implements GraphQLResolver<Claim> {

  private final MemberLoader memberLoader;
  private final MessagesFrontendPostprocessor messagesFrontendPostprocessor;
  private final ClaimsService claimsService;

  public ClaimResolver(MemberLoader memberLoader,
                       MessagesFrontendPostprocessor messagesFrontendPostprocessor,
                       ClaimsService claimsService) {
    this.memberLoader = memberLoader;
    this.messagesFrontendPostprocessor = messagesFrontendPostprocessor;
    this.claimsService = claimsService;
  }

  public CompletableFuture<Member> getMember(Claim claim) {
    return memberLoader.load(claim.getMemberId());
  }

  public List<ClaimFileUpload> getClaimFiles(Claim claim) {
    String claimId = claim.getId().toString();
    val claimFiles = claimsService.allClaimsFiles(claimId);

    List<ClaimFileDTO> claimFileDTOS = claimFiles.getBody().getClaimsFiles();

    List<ClaimFileUpload> claimFileUploads = new ArrayList<>();

    if(claimFileDTOS.isEmpty()) return claimFileUploads;

    claimFileDTOS.forEach(claimFile -> {
      ClaimFileUpload claimUpload = new ClaimFileUpload(
        claimFile.getClaimFileId(),
        messagesFrontendPostprocessor.processFileUrl(claimFile.getKey(), claimFile.getBucket()),
        claimFile.getClaimId()
      );
      claimFileUploads.add(claimUpload);
      }
    );
    return claimFileUploads;
  }

  public Object getType(Claim claim, DataFetchingEnvironment env) {
    if (claim.get_type() == null) {
      return null;
    }
    switch (Util.claimType(claim.get_type())) {
      case TheftClaim: {
        return TheftClaim.fromClaimData(claim.get_claimData());
      }
      case AssaultClaim: {
        return AssaultClaim.fromClaimData(claim.get_claimData());
      }
      case AccidentalDamageClaim: {
        return AccidentalDamageClaim.fromClaimData(claim.get_claimData());
      }
      case WaterDamageClaim: {
        return WaterDamageClaim.fromClaimData(claim.get_claimData());
      }
      case TravelAccidentClaim: {
        return TravelAccidentClaim.fromClaimData(claim.get_claimData());
      }
      case LuggageDelayClaim: {
        return LuggageDelayClaim.fromClaimData(claim.get_claimData());
      }
      case NotCoveredClaim: {
        return NotCoveredClaim.fromClaimData(claim.get_claimData());
      }
      case FireDamageClaim: {
        return FireDamageClaim.fromClaimData(claim.get_claimData());
      }
      case ConfirmedFraudClaim: {
        return ConfirmedFraudClaim.fromClaimData(claim.get_claimData());
      }
      case TestClaim: {
        return TestClaim.fromClaimData(claim.get_claimData());
      }
      case LiabilityClaim: {
        return LiabilityClaim.fromClaimData(claim.get_claimData());
      }
      case ApplianceClaim: {
        return ApplianceClaim.fromClaimData(claim.get_claimData());
      }
    }
    throw new RuntimeException(String.format("Unsupported claim type: %s", claim.get_type()));
  }
}
