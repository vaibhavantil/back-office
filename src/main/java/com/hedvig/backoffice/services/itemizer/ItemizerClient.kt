package com.hedvig.backoffice.services.itemizer

import com.hedvig.backoffice.config.feign.FeignConfig
import com.hedvig.backoffice.services.itemizer.dto.ItemBrand
import com.hedvig.backoffice.services.itemizer.dto.ItemFamily
import com.hedvig.backoffice.services.itemizer.dto.ItemModel
import com.hedvig.backoffice.services.itemizer.dto.ItemType
import com.hedvig.backoffice.services.itemizer.dto.request.AddItemCompanyRequest
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

  @PostMapping("/company/add")
  fun addItemCompany(
    @RequestBody request: AddItemCompanyRequest,
    @RequestHeader("Authorization") token: String
  )
}
