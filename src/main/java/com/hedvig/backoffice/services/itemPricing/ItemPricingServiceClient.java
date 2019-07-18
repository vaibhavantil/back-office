package com.hedvig.backoffice.services.itemPricing;

import com.hedvig.backoffice.services.itemPricing.dto.CategoryDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ItemSearchDTO;
import com.hedvig.backoffice.services.itemPricing.dto.PayloadDTO;
import com.hedvig.backoffice.services.itemPricing.dto.ItemBodyDTO;
import com.hedvig.backoffice.services.itemPricing.dto.PricepointDTO;
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
  url = "${itemPricingService.baseUrl}",
  configuration = FeignConfig.class)

public interface ItemPricingServiceClient {
  @GetMapping("/api/v1/categories")
  List<CategoryDTO> getCategories();

  @GetMapping("/api/v1/prices")
  List<PricepointDTO> getPrices(@RequestParam String date,
                                @RequestParam List<String> ids);

  @PostMapping("/api/v1/items/search")
  ItemSearchDTO getItems(@RequestBody PayloadDTO payload);

}
