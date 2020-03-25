package com.hedvig.backoffice.services.itemPricing;

import com.hedvig.backoffice.services.itemPricing.dto.ItemCategoryDTO;
import com.hedvig.backoffice.services.itemPricing.dto.SearchItemDTO;
import com.hedvig.backoffice.services.itemPricing.dto.InventoryItemDTO;
import java.util.List;

import com.hedvig.backoffice.config.feign.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
  name = "itemPricing-service",
  url = "${itemPricingService.baseUrl:item-pricing-service}",
  configuration = FeignConfig.class)

public interface ItemPricingServiceClient {
  @GetMapping("/api/v1/items/categories")
  List<ItemCategoryDTO> getCategories();

  @GetMapping("/api/v1/items/suggestions")
  List<SearchItemDTO> getSuggestions(@RequestParam String query);

  @GetMapping("/api/v1/items/details")
  SearchItemDTO getItemDetails(@RequestParam List<String> ids);

  @GetMapping("/api/v1/inventory")
  List<InventoryItemDTO> getInventory(@RequestParam String claimId);

  @PostMapping("/api/v1/inventory/add")
  boolean addInventoryItem(@RequestBody InventoryItemDTO item);

  @PostMapping("/api/v1/inventory/remove")
  boolean removeInventoryItem(@RequestParam String inventoryItemId);


}
