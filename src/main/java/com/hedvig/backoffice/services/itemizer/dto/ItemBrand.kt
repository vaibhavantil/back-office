package com.hedvig.backoffice.services.itemizer.dto

import com.hedvig.backoffice.graphql.UnionType
import com.hedvig.backoffice.graphql.types.itemizer.ItemCategory
import com.hedvig.backoffice.graphql.types.itemizer.ItemCategoryKind
import java.util.*

@UnionType
data class ItemBrand(
    override val id: UUID,
    override val displayName: String,
    override val searchTerms: String,
    val companyName: String
): ItemCategory {
  override val nextKind: ItemCategoryKind = ItemCategoryKind.MODEL
}
