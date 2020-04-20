package com.hedvig.backoffice.services.itemizer

import com.hedvig.backoffice.graphql.types.itemizer.ItemCategory
import com.hedvig.backoffice.graphql.types.itemizer.ItemCategoryKind

class ItemizerServiceStub: ItemizerService {
  override fun getCategories(kind: ItemCategoryKind, parentId: String?): List<ItemCategory> = listOf()
}
