package com.hedvig.backoffice.services.itemizer

import com.hedvig.backoffice.graphql.types.itemizer.ItemCategory
import com.hedvig.backoffice.graphql.types.itemizer.ItemCategoryKind
import com.hedvig.backoffice.services.itemizer.dto.request.*
import java.math.BigDecimal
import java.time.LocalDate
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

  override fun insertItemCategories(request: InsertItemCategoriesRequest, email: String) = itemizerClient.insertItemCategories(request, email)

  override fun getClaimItems(claimId: UUID) = itemizerClient.getClaimItemsByClaimId(claimId)

  override fun deleteClaimItem(claimItemId: UUID, email: String) = itemizerClient.deleteClaimItem(claimItemId, email)

  override fun upsertEvaluationRule(request: UpsertEvaluationRuleRequest, email: String) =
    itemizerClient.upsertEvaluationRule(request, email)

  override fun canEvaluate(typeOfContract: String, itemFamilyId: String, itemTypeId: UUID?) =
    itemizerClient.canEvaluate(typeOfContract, itemFamilyId, itemTypeId)

  override fun getEvaluation(
    purchasePrice: BigDecimal,
    itemFamilyId: String,
    itemTypeId: UUID?,
    typeOfContract: String,
    purchaseDate: LocalDate,
    baseDate: LocalDate?) = itemizerClient.getEvaluation(
    purchasePrice,
    itemFamilyId,
    itemTypeId,
    typeOfContract,
    purchaseDate,
    baseDate
  )
}
