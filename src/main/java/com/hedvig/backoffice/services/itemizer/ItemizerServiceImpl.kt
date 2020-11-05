package com.hedvig.backoffice.services.itemizer

import com.hedvig.backoffice.graphql.types.itemizer.ItemCategory
import com.hedvig.backoffice.graphql.types.itemizer.ItemCategoryKind
import com.hedvig.backoffice.services.itemizer.dto.ClaimItem
import com.hedvig.backoffice.services.itemizer.dto.request.*
import com.hedvig.backoffice.services.product_pricing.dto.contract.TypeOfContract
import java.util.*

class ItemizerServiceImpl(
    private val itemizerClient: ItemizerClient
) : ItemizerService {
    override fun getCategories(kind: ItemCategoryKind, parentId: String?): List<ItemCategory> = when (kind) {
        ItemCategoryKind.FAMILY -> itemizerClient.getFamilies()
        ItemCategoryKind.TYPE -> itemizerClient.getTypesByFamily(parentId!!)
        ItemCategoryKind.BRAND -> itemizerClient.getBrandsByType(UUID.fromString(parentId!!))
        ItemCategoryKind.MODEL -> itemizerClient.getModelsByBrand(UUID.fromString(parentId!!))
        ItemCategoryKind.COMPANY -> itemizerClient.getCompanies()
    }

    override fun upsertItemCompany(request: UpsertItemCompanyRequest, email: String) = itemizerClient.upsertItemCompany(request, email)

    override fun upsertItemType(request: UpsertItemTypeRequest, email: String) = itemizerClient.upsertItemType(request, email)

    override fun upsertItemBrand(request: UpsertItemBrandRequest, email: String) = itemizerClient.upsertItemBrand(request, email)

    override fun upsertItemModel(request: UpsertItemModelRequest, email: String) = itemizerClient.upsertItemModel(request, email)

    override fun upsertClaimItem(request: UpsertClaimItemRequest, email: String) = itemizerClient.upsertClaimItem(request, email)

    override fun insertItemCategories(request: InsertItemCategoriesRequest, email: String) =
        itemizerClient.insertItemCategories(request, email)

    override fun insertValuationRules(request: InsertValuationRulesRequest, email: String): List<Boolean> =
        itemizerClient.insertValuationRules(request, email)

    override fun getClaimItems(claimId: UUID): List<ClaimItem> = itemizerClient.getClaimItemsByClaimId(claimId)

    override fun deleteClaimItem(claimItemId: UUID, email: String) = itemizerClient.deleteClaimItem(claimItemId, email)

    override fun upsertValuationRule(request: UpsertValuationRuleRequest, email: String) =
        itemizerClient.upsertValuationRule(request, email)

    override fun canValuateClaimItem(typeOfContract: TypeOfContract, itemFamilyId: String, itemTypeId: UUID?) =
        itemizerClient.canValuateClaimItem(typeOfContract, itemFamilyId, itemTypeId)

    override fun getClaimItemValuation(request: GetClaimItemValuationRequest) = itemizerClient.getClaimItemValuation(request)

    override fun getClaimValuation(claimId: UUID) = itemizerClient.getClaimValuation(claimId)
}

