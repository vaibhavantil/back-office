package com.hedvig.backoffice.services.itemizer.dto

import com.hedvig.backoffice.graphql.UnionType
import com.hedvig.backoffice.graphql.types.itemizer.ItemCategory
import com.hedvig.backoffice.graphql.types.itemizer.ItemCategoryKind

@UnionType
data class ItemFamily(
    override val id: String,
    override val displayName: String,
    override val searchTerms: String
): ItemCategory<String> {
  override val nextKind: ItemCategoryKind = ItemCategoryKind.TYPE
}

