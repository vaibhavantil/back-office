package com.hedvig.backoffice.services.itemizer

import com.hedvig.backoffice.config.feign.FeignConfig
import com.hedvig.backoffice.services.itemizer.dto.CanValuateClaimItem
import com.hedvig.backoffice.services.itemizer.dto.ClaimItem
import com.hedvig.backoffice.services.itemizer.dto.ClaimItemValuation
import com.hedvig.backoffice.services.itemizer.dto.ItemBrand
import com.hedvig.backoffice.services.itemizer.dto.ItemCompany
import com.hedvig.backoffice.services.itemizer.dto.ItemFamily
import com.hedvig.backoffice.services.itemizer.dto.ItemModel
import com.hedvig.backoffice.services.itemizer.dto.ItemType
import com.hedvig.backoffice.services.itemizer.dto.request.GetValuationRequest
import com.hedvig.backoffice.services.itemizer.dto.request.InsertItemCategoriesRequest
import com.hedvig.backoffice.services.itemizer.dto.request.InsertValuationRulesRequest
import com.hedvig.backoffice.services.itemizer.dto.request.UpsertClaimItemRequest
import com.hedvig.backoffice.services.itemizer.dto.request.UpsertItemBrandRequest
import com.hedvig.backoffice.services.itemizer.dto.request.UpsertItemCompanyRequest
import com.hedvig.backoffice.services.itemizer.dto.request.UpsertItemModelRequest
import com.hedvig.backoffice.services.itemizer.dto.request.UpsertItemTypeRequest
import com.hedvig.backoffice.services.itemizer.dto.request.UpsertValuationRuleRequest
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import java.util.UUID

@FeignClient(
    name = "itemizerClient",
    url = "\${itemizer.baseUrl:itemizer}",
    configuration = [FeignConfig::class]
)
interface ItemizerClient {
    @PostMapping("/_/item/add")
    fun insertItemCategories(
        @RequestBody request: InsertItemCategoriesRequest,
        @RequestParam insertedBy: String
    ): List<Boolean>

    @PostMapping("/_/valuate/add")
    fun insertValuationRules(
        @RequestBody request: InsertValuationRulesRequest,
        @RequestParam insertedBy: String
    ): List<Boolean>

    @GetMapping("/_/item/families")
    fun getFamilies(): List<ItemFamily>

    @GetMapping("/_/item/companies")
    fun getCompanies(): List<ItemCompany>

    @GetMapping("/_/item/family/{itemFamilyId}/types")
    fun getTypesByFamily(
        @PathVariable itemFamilyId: String
    ): List<ItemType>

    @GetMapping("/_/item/type/{itemTypeId}/brands")
    fun getBrandsByType(
        @PathVariable itemTypeId: UUID
    ): List<ItemBrand>

    @GetMapping("/_/item/brand/{itemBrandId}/models")
    fun getModelsByBrand(
        @PathVariable itemBrandId: UUID
    ): List<ItemModel>

    @PostMapping("/_/item/company/upsert")
    fun upsertItemCompany(
        @RequestBody request: UpsertItemCompanyRequest,
        @RequestParam updatedBy: String
    ): UUID

    @PostMapping("/_/item/type/upsert")
    fun upsertItemType(
        @RequestBody request: UpsertItemTypeRequest,
        @RequestParam updatedBy: String
    ): UUID

    @PostMapping("/_/item/brand/upsert")
    fun upsertItemBrand(
        @RequestBody request: UpsertItemBrandRequest,
        @RequestParam updatedBy: String
    ): UUID

    @PostMapping("/_/item/model/upsert")
    fun upsertItemModel(
        @RequestBody request: UpsertItemModelRequest,
        @RequestParam updatedBy: String
    ): UUID

    @PostMapping("/_/claim/item/upsert")
    fun upsertClaimItem(
        @RequestBody request: UpsertClaimItemRequest,
        @RequestParam updatedBy: String
    ): UUID

    @GetMapping("/_/claim/item/{claimId}/items")
    fun getClaimItemsByClaimId(
        @PathVariable claimId: UUID
    ): List<ClaimItem>

    @DeleteMapping("/_/claim/item/{claimItemId}")
    fun deleteClaimItem(
        @PathVariable claimItemId: UUID,
        @RequestParam removedBy: String
    ): UUID

    @PostMapping("/_/valuate/item")
    fun getValuation(
        @RequestBody request: GetValuationRequest
    ): ClaimItemValuation

    @GetMapping("/_/valuate/rule/exists")
    fun canValuateClaimItem(
        @RequestParam typeOfContract: String,
        @RequestParam itemFamilyId: String,
        @RequestParam itemTypeId: UUID?
    ): CanValuateClaimItem

    @PostMapping("/_/valuate/rule/upsert")
    fun upsertValuationRule(
        @RequestBody request: UpsertValuationRuleRequest,
        @RequestParam updatedBy: String
    ): UUID
}
