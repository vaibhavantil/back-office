package com.hedvig.backoffice.services.itemizer

import com.hedvig.backoffice.config.feign.FeignConfig
import com.hedvig.backoffice.services.itemizer.dto.*
import com.hedvig.backoffice.services.itemizer.dto.request.*
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.*
import java.util.*

@FeignClient(
  name = "itemizerClient",
  url = "\${itemizer.baseUrl:itemizer}",
  configuration = [FeignConfig::class]
)
interface ItemizerClient {
  @GetMapping("/_/item/families")
  fun getFamilies(): List<ItemFamily>

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
}