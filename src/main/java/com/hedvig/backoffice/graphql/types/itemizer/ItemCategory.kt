package com.hedvig.backoffice.graphql.types.itemizer

interface ItemCategory<T> {
  val id: T
  val nextKind: ItemCategoryKind?
  val displayName: String
  val searchTerms: String
}
