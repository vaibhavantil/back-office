package com.hedvig.backoffice.services.itemizer

import com.hedvig.backoffice.graphql.types.itemizer.ItemCategory
import com.hedvig.backoffice.graphql.types.itemizer.ItemCategoryKind
import com.hedvig.backoffice.services.itemizer.dto.ClaimItem
import com.hedvig.backoffice.services.itemizer.dto.request.*
import java.util.*

class ItemizerServiceStub : ItemizerService {
  override fun getCategories(kind: ItemCategoryKind, parentId: String?): List<ItemCategory> = listOf()

  override fun upsertItemCompany(request: UpsertItemCompanyRequest, email: String): UUID = UUID.randomUUID()

  override fun upsertItemType(request: UpsertItemTypeRequest, email: String): UUID = UUID.randomUUID()

  override fun upsertItemBrand(request: UpsertItemBrandRequest, email: String): UUID = UUID.randomUUID()

  override fun upsertItemModel(request: UpsertItemModelRequest, email: String): UUID = UUID.randomUUID()

  override fun upsertClaimItem(request: UpsertClaimItemRequest, email: String): UUID = UUID.randomUUID()

  override fun getClaimItems(claimId: UUID): List<ClaimItem> = listOf()

  override fun deleteClaimItemById(claimItemId: UUID, email: String): UUID = UUID.randomUUID()
}
