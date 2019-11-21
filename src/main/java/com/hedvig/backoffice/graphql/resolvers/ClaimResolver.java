package com.hedvig.backoffice.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.hedvig.backoffice.graphql.Util;
import com.hedvig.backoffice.graphql.dataloaders.MemberLoader;
import com.hedvig.backoffice.graphql.types.ClaimFile;
import com.hedvig.backoffice.graphql.types.claims.*;
import com.hedvig.backoffice.graphql.types.Claim;
import com.hedvig.backoffice.graphql.types.ClaimFileUpload;
import com.hedvig.backoffice.graphql.types.Member;
import com.hedvig.backoffice.services.UploadedFilePostprocessor;
import com.hedvig.backoffice.services.claims.ClaimsService;
import graphql.schema.DataFetchingEnvironment;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class ClaimResolver implements GraphQLResolver<Claim> {

  private final MemberLoader memberLoader;
  private final UploadedFilePostprocessor uploadedFilePostprocessor;
  private final ClaimsService claimsService;

  public ClaimResolver(MemberLoader memberLoader,
                       UploadedFilePostprocessor uploadedFilePostprocessor,
                       ClaimsService claimsService) {
    this.memberLoader = memberLoader;
    this.uploadedFilePostprocessor = uploadedFilePostprocessor;
    this.claimsService = claimsService;
  }

  public CompletableFuture<Member> getMember(Claim claim) {
    return memberLoader.load(claim.getMemberId());
  }

  public List<ClaimFileUpload> getClaimFiles(Claim claim) {

    List<ClaimFileUpload> claimFileUploads = new ArrayList<>();

    List<ClaimFile> claimFiles = claim.claimFiles;

    if (claimFiles.isEmpty()) {
      return claimFileUploads;
    }

    claimFiles.forEach(claimFile -> {
      ClaimFileUpload claimUpload = new ClaimFileUpload(
        claimFile.getClaimFileId(),
        uploadedFilePostprocessor.processFileUrl(claimFile.getKey(), claimFile.getBucket()),
        claimFile.getUploadedAt(),
        claimFile.getClaimId(),
        claimFile.getMarkedAsDeleted(),
        claimFile.getCategory()
      );
        claimFileUploads.add(claimUpload);
    });
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
      case LiabilityClaim: {
        return LiabilityClaim.fromClaimData(claim.get_claimData());
      }
      case ApplianceClaim: {
        return ApplianceClaim.fromClaimData(claim.get_claimData());
      }
      case LegalProtectionClaim: {
        return LegalProtectionClaim.fromClaimData(claim.get_claimData());
      }
      case WaterDamageBathroomClaim: {
        return WaterDamageBathroomClaim.fromClaimData(claim.get_claimData());
      }
      case WaterDamageKitchenClaim: {
        return WaterDamageKitchenClaim.fromClaimData(claim.get_claimData());
      }
      case BurglaryClaim: {
        return BurglaryClaim.fromClaimData(claim.get_claimData());
      }
      case FloodingClaim: {
        return FloodingClaim.fromClaimData(claim.get_claimData());
      }
      case EarthquakeClaim: {
        return EarthquakeClaim.fromClaimData(claim.get_claimData());
      }
      case InstallationsClaim: {
        return InstallationsClaim.fromClaimData(claim.get_claimData());
      }
      case SnowPressureClaim: {
        return SnowPressureClaim.fromClaimData(claim.get_claimData());
      }
      case StormDamageClaim: {
        return StormDamageClaim.fromClaimData(claim.get_claimData());
      }
      case VerminAndPestsClaim: {
        return VerminAndPestsClaim.fromClaimData(claim.get_claimData());
      }
      case TestClaim: {
        return TestClaim.fromClaimData(claim.get_claimData());
      }
    }
    throw new RuntimeException(String.format("Unsupported claim type: %s", claim.get_type()));
  }
}
