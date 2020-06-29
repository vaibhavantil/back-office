package com.hedvig.backoffice.services.itemizer

import com.hedvig.backoffice.config.feign.FeignConfig
import com.hedvig.backoffice.services.itemizer.dto.*
import com.hedvig.backoffice.services.itemizer.dto.request.*
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

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

  @GetMapping("/_/evaluate/item")
  fun getEvaluation(
    @RequestParam purchasePrice: BigDecimal,
    @RequestParam itemFamilyId: String,
    @RequestParam itemTypeId: UUID?,
    @RequestParam typeOfContract: String,
    @RequestParam purchaseDate: LocalDate,
    @RequestParam baseDate: LocalDate?
  ): Evaluation

  @GetMapping("/_/evaluate/rule/exists")
  fun canEvaluate(
    @RequestParam typeOfContract: String,
    @RequestParam itemFamilyId: String,
    @RequestParam itemTypeId: UUID?
  ): CanEvaluate

  @PostMapping("/_/evaluate/rule/upsert")
  fun upsertEvaluationRule(
    @RequestBody request: UpsertEvaluationRuleRequest,
    @RequestParam updatedBy: String
  ): UUID

}
