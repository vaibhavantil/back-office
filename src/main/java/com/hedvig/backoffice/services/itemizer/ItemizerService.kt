package com.hedvig.backoffice.services.itemizer

import com.hedvig.backoffice.graphql.types.itemizer.ItemCategory
import com.hedvig.backoffice.graphql.types.itemizer.ItemCategoryKind
import com.hedvig.backoffice.services.itemizer.dto.request.AddItemCompanyRequest

interface ItemizerService {
  fun getCategories(kind: ItemCategoryKind, parentId: String?): List<ItemCategory<Any>>
  fun addItemCompany(request: AddItemCompanyRequest, token: String)
}
