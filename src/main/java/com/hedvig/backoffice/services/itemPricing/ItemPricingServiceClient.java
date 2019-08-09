package com.hedvig.backoffice.services.itemPricing;

import com.hedvig.backoffice.services.itemPricing.dto.CategoryDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ItemSearchDTO;
import com.hedvig.backoffice.services.itemPricing.dto.PayloadDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ItemBodyDTO;
import com.hedvig.backoffice.services.itemPricing.dto.PricepointDTO;
import com.hedvig.backoffice.services.itemPricing.dto.InventoryItemDTO;
import com.hedvig.backoffice.services.itemPricing.dto.FilterPayloadDTO;
import com.hedvig.backoffice.services.itemPricing.dto.FilterSuggestionDTO;
import com.hedvig.backoffice.services.itemPricing.dto.FilterDTO;
import java.util.List;
import java.util.ArrayList;
import com.hedvig.backoffice.config.feign.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
  name = "itemPricing-service",
  url = "${itemPricingService.baseUrl:itemPricing}",
  configuration = FeignConfig.class)

public interface ItemPricingServiceClient {
  @GetMapping("/api/v1/categories")
  List<CategoryDTO> getCategories();

  @GetMapping("/api/v1/inventory")
  List<InventoryItemDTO> getInventory(@RequestParam String claimId);

  @GetMapping("/api/v1/prices")
  List<PricepointDTO> getPrices(
    @RequestParam String date,
    @RequestParam List<String> ids
  );

  @PostMapping("/api/v1/items/search")
  ItemSearchDTO getItems(@RequestBody PayloadDTO payload);

  @PostMapping("/api/v1/inventory/add")
  boolean addInventoryItem(@RequestBody InventoryItemDTO item);

  @PostMapping("/api/v1/inventory/remove")
  boolean removeInventoryItem(@RequestParam String inventoryItemId);

  @PostMapping("/api/v1/inventory/filters/remove")
  boolean removeInventoryFilters(@RequestParam String inventoryItemId);

  @GetMapping("/api/v1/filters")
  List<FilterSuggestionDTO> getAllFilters(@RequestParam String categoryId);

  @GetMapping("/api/v1/inventory/filters")
  List<FilterDTO> getInventoryItemFilters(@RequestParam String inventoryItemId);

}
