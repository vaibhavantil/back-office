package com.hedvig.backoffice.services.itemizer

import com.hedvig.backoffice.graphql.types.itemizer.ItemCategory
import com.hedvig.backoffice.graphql.types.itemizer.ItemCategoryKind
import com.hedvig.backoffice.services.itemizer.dto.request.AddItemCompanyRequest
import java.util.*

class ItemizerServiceImpl(
  private val itemizerClient: ItemizerClient
) : ItemizerService {
  @Suppress("TYPE_MISMATCH")
  override fun getCategories(kind: ItemCategoryKind, parentId: String?): List<ItemCategory<Any>> = when (kind) {
    ItemCategoryKind.FAMILY -> itemizerClient.getFamilies()
    ItemCategoryKind.TYPE -> itemizerClient.getTypesByFamily(parentId!!)
    ItemCategoryKind.BRAND -> itemizerClient.getBrandsByType(UUID.fromString(parentId!!))
    ItemCategoryKind.MODEL -> itemizerClient.getModelsByBrand(UUID.fromString(parentId!!))
  }

  override fun addItemCompany(request: AddItemCompanyRequest, token: String) {
    itemizerClient.addItemCompany(request, token)
  }
}
