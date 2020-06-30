package com.hedvig.backoffice.services.itemizer

import com.hedvig.backoffice.graphql.types.itemizer.ItemCategory
import com.hedvig.backoffice.graphql.types.itemizer.ItemCategoryKind
import com.hedvig.backoffice.services.itemizer.dto.CanValuateClaimItem
import com.hedvig.backoffice.services.itemizer.dto.ClaimItem
import com.hedvig.backoffice.services.itemizer.dto.ClaimItemValuation
import com.hedvig.backoffice.services.itemizer.dto.request.*
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

class ItemizerServiceStub : ItemizerService {
  override fun getCategories(kind: ItemCategoryKind, parentId: String?): List<ItemCategory> = listOf()

  override fun upsertItemCompany(request: UpsertItemCompanyRequest, email: String): UUID = UUID.randomUUID()

  override fun upsertItemType(request: UpsertItemTypeRequest, email: String): UUID = UUID.randomUUID()

  override fun upsertItemBrand(request: UpsertItemBrandRequest, email: String): UUID = UUID.randomUUID()

  override fun upsertItemModel(request: UpsertItemModelRequest, email: String): UUID = UUID.randomUUID()

  override fun upsertClaimItem(request: UpsertClaimItemRequest, email: String): UUID = UUID.randomUUID()

  override fun insertItemCategories(request: InsertItemCategoriesRequest, email: String): List<Boolean> = listOf()

  override fun getClaimItems(claimId: UUID): List<ClaimItem> = listOf()

  override fun deleteClaimItem(claimItemId: UUID, email: String): UUID = UUID.randomUUID()

  override fun upsertValuationRule(request: UpsertValuationRuleRequest, email: String): UUID = UUID.randomUUID()

  override fun canValuateClaimItem(typeOfContract: String, itemFamilyId: String, itemTypeId: UUID?) = CanValuateClaimItem(false, null, null)

  override fun getValuation(
    purchasePrice: BigDecimal,
    itemFamilyId: String,
    itemTypeId: UUID?,
    typeOfContract: String,
    purchaseDate: LocalDate,
    baseDate: LocalDate?
  ) = ClaimItemValuation(BigDecimal(1000), null)
}
