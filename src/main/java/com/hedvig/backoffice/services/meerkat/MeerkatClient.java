package com.hedvig.backoffice.services.meerkat;


import com.hedvig.backoffice.config.feign.FeignConfig;
import com.hedvig.backoffice.services.meerkat.dto.MeerkatResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(
  name = "meerkatClient",
  url = "${meerkat.baseUrl:meerkat}",
  configuration = FeignConfig.class
)
public interface MeerkatClient {

  @RequestMapping(value = "/api/check?query={query}", method = RequestMethod.GET)
  ResponseEntity<MeerkatResponse> getSanctionListStatus(@PathVariable("query") String query);
}
