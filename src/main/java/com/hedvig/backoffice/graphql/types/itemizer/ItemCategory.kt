package com.hedvig.backoffice.graphql.types.itemizer

import com.hedvig.backoffice.graphql.UnionType

@UnionType
interface ItemCategory {
  val id: Any
  val nextKind: ItemCategoryKind?
  val displayName: String
  val searchTerms: String
}
