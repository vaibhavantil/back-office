package com.hedvig.backoffice.services.itemizer

import com.hedvig.backoffice.graphql.types.itemizer.ItemCategory
import com.hedvig.backoffice.graphql.types.itemizer.ItemCategoryKind
import com.hedvig.backoffice.services.itemizer.dto.CanValuateClaimItem
import com.hedvig.backoffice.services.itemizer.dto.ClaimItem
import com.hedvig.backoffice.services.itemizer.dto.ClaimItemValuation
import com.hedvig.backoffice.services.itemizer.dto.request.GetValuationRequest
import com.hedvig.backoffice.services.itemizer.dto.request.InsertItemCategoriesRequest
import com.hedvig.backoffice.services.itemizer.dto.request.InsertValuationRulesRequest
import com.hedvig.backoffice.services.itemizer.dto.request.UpsertClaimItemRequest
import com.hedvig.backoffice.services.itemizer.dto.request.UpsertItemBrandRequest
import com.hedvig.backoffice.services.itemizer.dto.request.UpsertItemCompanyRequest
import com.hedvig.backoffice.services.itemizer.dto.request.UpsertItemModelRequest
import com.hedvig.backoffice.services.itemizer.dto.request.UpsertItemTypeRequest
import com.hedvig.backoffice.services.itemizer.dto.request.UpsertValuationRuleRequest
import java.util.UUID

interface ItemizerService {
    fun getCategories(kind: ItemCategoryKind, parentId: String?): List<ItemCategory>
    fun upsertItemCompany(request: UpsertItemCompanyRequest, email: String): UUID
    fun upsertItemType(request: UpsertItemTypeRequest, email: String): UUID
    fun upsertItemBrand(request: UpsertItemBrandRequest, email: String): UUID
    fun upsertItemModel(request: UpsertItemModelRequest, email: String): UUID
    fun upsertClaimItem(request: UpsertClaimItemRequest, email: String): UUID
    fun insertItemCategories(request: InsertItemCategoriesRequest, email: String): List<Boolean>
    fun insertValuationRules(request: InsertValuationRulesRequest, email: String): List<Boolean>
    fun getClaimItems(claimId: UUID): List<ClaimItem>
    fun deleteClaimItem(claimItemId: UUID, email: String): UUID
    fun upsertValuationRule(request: UpsertValuationRuleRequest, email: String): UUID
    fun canValuateClaimItem(typeOfContract: String, itemFamilyId: String, itemTypeId: UUID?): CanValuateClaimItem
    fun getValuation(request: GetValuationRequest): ClaimItemValuation
}
