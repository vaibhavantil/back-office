package com.hedvig.backoffice.services.itemPricing;

import com.hedvig.backoffice.services.itemPricing.dto.CategoryDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ItemSearchDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ItemSearchQueryDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ItemPricepointDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ClaimInventoryItemDTO;
import com.hedvig.backoffice.services.itemPricing.dto.FilterSuggestionDTO;
import com.hedvig.backoffice.services.itemPricing.dto.FilterDTO;
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
  @GetMapping("/api/v1/categories")
  List<CategoryDTO> getCategories();

  @GetMapping("/api/v1/inventory")
  List<ClaimInventoryItemDTO> getInventory(@RequestParam String claimId);

  @GetMapping("/api/v1/prices")
  List<ItemPricepointDTO> getPrices(
    @RequestParam String date,
    @RequestParam List<String> ids
  );

  @PostMapping("/api/v1/items/search")
  ItemSearchDTO getItems(@RequestBody ItemSearchQueryDTO payload);

  @PostMapping("/api/v1/inventory/add")
  boolean addInventoryItem(@RequestBody ClaimInventoryItemDTO item);

  @PostMapping("/api/v1/inventory/remove")
  boolean removeInventoryItem(@RequestParam String inventoryItemId);

  @PostMapping("/api/v1/inventory/filters/remove")
  boolean removeInventoryFilters(@RequestParam String inventoryItemId);

  @GetMapping("/api/v1/filters")
  List<FilterSuggestionDTO> getAllFilters(@RequestParam String categoryId);

  @GetMapping("/api/v1/inventory/filters")
  List<FilterDTO> getInventoryItemFilters(@RequestParam String inventoryItemId);

}
