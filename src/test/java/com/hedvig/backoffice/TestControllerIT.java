package com.hedvig.backoffice;

import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = IntegrationTestApplication.class)
public class TestControllerIT {

  private DockerService postgres;
  private DockerService productPricing;

  @Value("src/test/resources/integrationTests/product-pricing")
  private File productPricingPathToConfig;

  @Value("src/test/resources/integrationTests/product-pricing")
  private File productPricingPathToResource;

  @Value("src/test/resources/integrationTests/bot-service")
  private File botServicePathToConfig;


  private static final String PRODUCT_PRICING_TARGET_RESOURCE_NAME = "/tariff-data";
  private static final String POSTGRES_NAME = "postgresOne";
  private static final String PRODUCT_PRICING_NAME = "productPricingOne";

  @Autowired
  private MockMvc mvc;

  @BeforeEach
  public void prepareContainers() throws Throwable {

    val ports = new HashMap<Integer, Integer>() {
      {
        put(8888, 5432);
      }
    };

    postgres = DockerService.builder().name(POSTGRES_NAME).image("postgres:latest").ports(ports)
      .env(new String[]{"POSTGRES_USER=postgres", "POSTGRES_PASSWORD=hedvig",
        "POSTGRES_DB=product_pricing"})
      .build();

    postgres.startContainer();

    val productPorts = new HashMap<Integer, Integer>() {
      {
        put(7085, 80);
      }
    };

    if (postgres.getDockerHost().length() > 0) {
      productPricing = DockerService.builder().image(
        "201427539538.dkr.ecr.eu-central-1.amazonaws.com/product-pricing:8f44b3204155773766d02ebaad9aec4a92c7ce37")
        .ports(productPorts).link(POSTGRES_NAME).env(new String[]{"AWS_REGION=eu-central-1"})
        .config(productPricingPathToConfig.getAbsolutePath())
        .resource(productPricingPathToResource.getAbsolutePath(),
          PRODUCT_PRICING_TARGET_RESOURCE_NAME)
        .name(PRODUCT_PRICING_NAME)
        .build();
    }

    productPricing.startContainer();

    TimeUnit.MINUTES.sleep(2);

    URL url = new URL("http://localhost:7085/createProduct");

    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setDoOutput(true);
    conn.setRequestMethod("POST");
    conn.setRequestProperty("Content-Type", "application/json");
    String input = "{\n"
      + "  \"address\": {\n"
      + "    \"city\": \"string\",\n"
      + "    \"floor\": 0,\n"
      + "    \"street\": \"string\",\n"
      + "    \"zipCode\": \"string\"\n"
      + "  },\n"
      + "  \"birthDate\": \"2018-09-11\",\n"
      + "  \"currentInsurer\": \"string\",\n"
      + "  \"firstName\": \"string\",\n"
      + "  \"houseType\": \"BRF\",\n"
      + "  \"lastName\": \"string\",\n"
      + "  \"livingSpace\": 0,\n"
      + "  \"memberId\": \"12345\",\n"
      + "  \"personsInHouseHold\": 0,\n"
      + "  \"safetyIncreasers\": [\n"
      + "    \"SMOKE_ALARM\"\n"
      + "  ],\n"
      + "  \"ssn\": \"string\",\n"
      + "  \"student\": true\n"
      + "}";

    OutputStream os = conn.getOutputStream();
    os.write(input.getBytes());
    os.flush();

    if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
      throw new RuntimeException("Failed : HTTP error code : "
        + conn.getResponseCode());
    }

    BufferedReader br = new BufferedReader(new InputStreamReader(
      (conn.getInputStream())));

    String output;
    System.out.println("Output from Server .... \n");
    while ((output = br.readLine()) != null) {
      System.out.println(output);
    }

    conn.disconnect();
  }

  @AfterEach
  public void dispose() {
    postgres.disposeContainer();
    productPricing.disposeContainer();
  }

  @WithMockUser(value = "spring")
  @Test
  public void test() throws Exception {
    this.mvc.perform(get("/test/12345")).andExpect(status().isOk()).andReturn();
  }
}
