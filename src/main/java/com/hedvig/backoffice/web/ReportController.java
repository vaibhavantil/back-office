package com.hedvig.backoffice.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.hedvig.backoffice.services.product_pricing.ProductPricingService;
import com.hedvig.backoffice.services.product_pricing.dto.MonthlyBordereauDTO;
import com.hedvig.backoffice.services.product_pricing.dto.ProductType;
import com.hedvig.backoffice.web.dto.BordereauCSVDTO;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = {"/api/reports"})
public class ReportController {

  private Logger log = LoggerFactory.getLogger(ReportController.class);
  private final ProductPricingService productPricingService;

  public ReportController(
    ProductPricingService productPricingService) {
    this.productPricingService = productPricingService;
  }

  @GetMapping(value = "/bdx")
  public ResponseEntity<?> getBDX(HttpServletResponse response, @RequestParam("year") int year,
    @RequestParam("month") int month,
    @RequestParam("isStudent") boolean isStudent) {

    response.setContentType("text/csv");
    String csvFileName = String
      .format("attachment;filename=bdx_%s_%s%s.csv", isStudent ? "student" : "regular", month,
        year);
    response.setHeader("Content-Disposition", csvFileName);

    YearMonth period;

    CsvMapper mapper = new CsvMapper();

    try {
      period = YearMonth.of(year, month);
      List<BordereauCSVDTO> bdx = getBDX(period, isStudent).stream().map(BordereauCSVDTO::new)
        .collect(
          Collectors.toList());

      CsvSchema schema = mapper.schemaFor(BordereauCSVDTO.class).withHeader();
      String csv = mapper.writer(schema).writeValueAsString(bdx);

      response.getOutputStream().print(csv);

      return ResponseEntity.ok().build();

    } catch (DateTimeException exception) {
      log.error(
        "Failed to parse year: {} and month: {} to YearMonth type with the following error: {}",
        year,
        month,
        exception.getMessage());
      return ResponseEntity.badRequest().build();
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    } catch (IOException e) {
      log.error("Failed to stream the csv file {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

  }

  private List<MonthlyBordereauDTO> getBDX(YearMonth period, boolean isStudent) {
    List<MonthlyBordereauDTO> bdx = new ArrayList<>();

    if (isStudent) {
      bdx.addAll(
        productPricingService.getMonthlyBordereauByProductType(period, ProductType.STUDENT_BRF));
      bdx.addAll(
        productPricingService.getMonthlyBordereauByProductType(period, ProductType.STUDENT_RENT));
    } else {
      for (ProductType type : ProductType.values()) {
        if (type != ProductType.STUDENT_BRF && type != ProductType.STUDENT_RENT) {
          bdx.addAll(productPricingService.getMonthlyBordereauByProductType(period, type));
        }
      }
    }
    return bdx;
  }

}
