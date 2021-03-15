package com.hedvig.backoffice.graphql.resolvers

import com.coxautodev.graphql.tools.GraphQLResolver
import com.hedvig.backoffice.graphql.Util
import com.hedvig.backoffice.graphql.dataloaders.MemberLoader
import com.hedvig.backoffice.graphql.types.Claim
import com.hedvig.backoffice.graphql.types.ClaimFileUpload
import com.hedvig.backoffice.graphql.types.ClaimTypes
import com.hedvig.backoffice.graphql.types.Member
import com.hedvig.backoffice.graphql.types.claims.AccidentalDamageClaim
import com.hedvig.backoffice.graphql.types.claims.ApplianceClaim
import com.hedvig.backoffice.graphql.types.claims.AssaultClaim
import com.hedvig.backoffice.graphql.types.claims.BurglaryClaim
import com.hedvig.backoffice.graphql.types.claims.ClaimItemSet
import com.hedvig.backoffice.graphql.types.claims.ConfirmedFraudClaim
import com.hedvig.backoffice.graphql.types.claims.EarthquakeClaim
import com.hedvig.backoffice.graphql.types.claims.FireDamageClaim
import com.hedvig.backoffice.graphql.types.claims.FloodingClaim
import com.hedvig.backoffice.graphql.types.claims.InstallationsClaim
import com.hedvig.backoffice.graphql.types.claims.LegalProtectionClaim
import com.hedvig.backoffice.graphql.types.claims.LiabilityClaim
import com.hedvig.backoffice.graphql.types.claims.LuggageDelayClaim
import com.hedvig.backoffice.graphql.types.claims.NotCoveredClaim
import com.hedvig.backoffice.graphql.types.claims.SnowPressureClaim
import com.hedvig.backoffice.graphql.types.claims.StormDamageClaim
import com.hedvig.backoffice.graphql.types.claims.TestClaim
import com.hedvig.backoffice.graphql.types.claims.TheftClaim
import com.hedvig.backoffice.graphql.types.claims.TravelAccidentClaim
import com.hedvig.backoffice.graphql.types.claims.VerminAndPestsClaim
import com.hedvig.backoffice.graphql.types.claims.WaterDamageBathroomClaim
import com.hedvig.backoffice.graphql.types.claims.WaterDamageClaim
import com.hedvig.backoffice.graphql.types.claims.WaterDamageKitchenClaim
import com.hedvig.backoffice.services.UploadedFilePostprocessor
import com.hedvig.backoffice.services.chat.data.Message.logger
import com.hedvig.backoffice.services.claims.dto.ClaimData
import com.hedvig.backoffice.services.itemizer.ItemizerService
import com.hedvig.backoffice.services.product_pricing.ProductPricingService
import com.hedvig.backoffice.services.product_pricing.dto.contract.Contract
import com.hedvig.backoffice.services.product_pricing.dto.contract.GenericAgreement
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture
import java.util.stream.Collectors

@Component
class ClaimResolver(
    private val memberLoader: MemberLoader,
    private val uploadedFilePostprocessor: UploadedFilePostprocessor,
    private val productPricingService: ProductPricingService,
    private val itemizerService: ItemizerService
) : GraphQLResolver<Claim> {
    fun getMember(claim: Claim): CompletableFuture<Member> {
        return try {
            memberLoader.load(claim.memberId)
        } catch (exception: Exception) {
            logger.error(exception.message)
            CompletableFuture()
        }
    }

    fun getClaimFiles(claim: Claim): List<ClaimFileUpload> {
        val claimFiles = claim.claimFiles

        if (claimFiles.isEmpty()) {
            return emptyList()
        }

        return claimFiles.stream().map { claimFile ->
            ClaimFileUpload(
                claimFile.claimFileId,
                uploadedFilePostprocessor.processFileUrl(claimFile.key, claimFile.bucket),
                claimFile.UploadedAt,
                claimFile.claimId,
                claimFile.category,
                claimFile.contentType
            )
        }.collect(Collectors.toList())
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
            else -> null
        }
    }

    fun getContract(claim: Claim): Contract? = claim.contractId?.let { id -> productPricingService.getContractById(id) }

    fun getItemSet(claim: Claim): ClaimItemSet = ClaimItemSet(
        items = itemizerService.getClaimItems(claim.id)
    )

    fun getAgreement(claim: Claim): GenericAgreement? {
        val contractId = claim.contractId ?: return null
        val dateClaimData = ClaimData.withoutDuplicates(claim._claimData).find { it.name == "DATE" } ?: return null
        val dateOfLoss = LocalDateTime.parse(dateClaimData.value).toLocalDate()
        return productPricingService.getAgreementForContractActiveOnDate(contractId, dateOfLoss)
    }
}
