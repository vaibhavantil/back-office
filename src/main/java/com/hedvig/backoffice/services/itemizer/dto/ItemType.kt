package com.hedvig.backoffice.services.itemizer.dto

import com.hedvig.backoffice.graphql.UnionType
import com.hedvig.backoffice.graphql.types.itemizer.ItemCategory
import com.hedvig.backoffice.graphql.types.itemizer.ItemCategoryKind
import java.util.*

@UnionType
data class ItemType(
  override val id: UUID,
  override val displayName: String,
  override val searchTerms: String
) : ItemCategory<UUID> {
  override val nextKind: ItemCategoryKind = ItemCategoryKind.BRAND
}
