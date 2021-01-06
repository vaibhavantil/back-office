package com.hedvig.backoffice.graphql.resolvers

import com.coxautodev.graphql.tools.GraphQLResolver
import com.hedvig.backoffice.graphql.Util
import com.hedvig.backoffice.graphql.dataloaders.MemberLoader
import com.hedvig.backoffice.graphql.types.Claim
import com.hedvig.backoffice.graphql.types.ClaimFileUpload
import com.hedvig.backoffice.graphql.types.ClaimTypes
import com.hedvig.backoffice.graphql.types.Member
import com.hedvig.backoffice.graphql.types.claims.*
import com.hedvig.backoffice.services.UploadedFilePostprocessor
import com.hedvig.backoffice.services.product_pricing.ProductPricingService
import com.hedvig.backoffice.services.product_pricing.dto.contract.Contract
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.stream.Collectors

@Component
class ClaimResolver(
    private val memberLoader: MemberLoader,
    private val uploadedFilePostprocessor: UploadedFilePostprocessor,
    private val productPricingService: ProductPricingService
) : GraphQLResolver<Claim?> {
    fun getMember(claim: Claim): CompletableFuture<Member> {
        return try {
            memberLoader.load(claim.memberId)
        } catch (exception: Exception) {
            CompletableFuture()
        }
    }

    fun getClaimFiles(claim: Claim): List<ClaimFileUpload> {
        val claimFileUploads: MutableList<ClaimFileUpload> = ArrayList()
        val claimFiles = claim.claimFiles
        if (claimFiles.isEmpty()) {
            return claimFileUploads
        }
        val claimFileUploadDtos = claimFiles.stream().map { (claimFileId, bucket, key, claimId, contentType, UploadedAt, _, category) ->
            val claimUpload = ClaimFileUpload(
                claimFileId,
                uploadedFilePostprocessor.processFileUrl(key, bucket),
                UploadedAt,
                claimId,
                category,
                contentType
            )
            claimUpload
        }.collect(Collectors.toList())
        claimFileUploads.addAll(claimFileUploadDtos)
        return claimFileUploads
    }

    fun getType(claim: Claim, env: DataFetchingEnvironment?): Any? {
        return if (claim._type == null) {
            null
        } else when (Util.claimType(claim._type)) {
            ClaimTypes.TheftClaim -> {
                TheftClaim.fromClaimData(claim._claimData)
            }
            ClaimTypes.AssaultClaim -> {
                AssaultClaim.fromClaimData(claim._claimData)
            }
            ClaimTypes.AccidentalDamageClaim -> {
                AccidentalDamageClaim.fromClaimData(claim._claimData)
            }
            ClaimTypes.WaterDamageClaim -> {
                WaterDamageClaim.fromClaimData(claim._claimData)
            }
            ClaimTypes.TravelAccidentClaim -> {
                TravelAccidentClaim.fromClaimData(claim._claimData)
            }
            ClaimTypes.LuggageDelayClaim -> {
                LuggageDelayClaim.fromClaimData(claim._claimData)
            }
            ClaimTypes.NotCoveredClaim -> {
                NotCoveredClaim.fromClaimData(claim._claimData)
            }
            ClaimTypes.FireDamageClaim -> {
                FireDamageClaim.fromClaimData(claim._claimData)
            }
            ClaimTypes.ConfirmedFraudClaim -> {
                ConfirmedFraudClaim.fromClaimData(claim._claimData)
            }
            ClaimTypes.LiabilityClaim -> {
                LiabilityClaim.fromClaimData(claim._claimData)
            }
            ClaimTypes.ApplianceClaim -> {
                ApplianceClaim.fromClaimData(claim._claimData)
            }
            ClaimTypes.LegalProtectionClaim -> {
                LegalProtectionClaim.fromClaimData(claim._claimData)
            }
            ClaimTypes.WaterDamageBathroomClaim -> {
                WaterDamageBathroomClaim.fromClaimData(claim._claimData)
            }
            ClaimTypes.WaterDamageKitchenClaim -> {
                WaterDamageKitchenClaim.fromClaimData(claim._claimData)
            }
            ClaimTypes.BurglaryClaim -> {
                BurglaryClaim.fromClaimData(claim._claimData)
            }
            ClaimTypes.FloodingClaim -> {
                FloodingClaim.fromClaimData(claim._claimData)
            }
            ClaimTypes.EarthquakeClaim -> {
                EarthquakeClaim.fromClaimData(claim._claimData)
            }
            ClaimTypes.InstallationsClaim -> {
                InstallationsClaim.fromClaimData(claim._claimData)
            }
            ClaimTypes.SnowPressureClaim -> {
                SnowPressureClaim.fromClaimData(claim._claimData)
            }
            ClaimTypes.StormDamageClaim -> {
                StormDamageClaim.fromClaimData(claim._claimData)
            }
            ClaimTypes.VerminAndPestsClaim -> {
                VerminAndPestsClaim.fromClaimData(claim._claimData)
            }
            ClaimTypes.TestClaim -> {
                TestClaim.fromClaimData(claim._claimData)
            }
        }
    }

    fun getContract(claim: Claim): Contract? {
        return if (claim.contractId != null) {
            productPricingService.getContractById(claim.contractId)
        } else null
    }

}
