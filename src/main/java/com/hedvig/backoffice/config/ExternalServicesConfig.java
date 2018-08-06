package com.hedvig.backoffice.config;

import com.hedvig.backoffice.services.assettracker.AssetTrackerClient;
import com.hedvig.backoffice.services.assettracker.AssetTrackerClientImpl;
import com.hedvig.backoffice.services.assettracker.AssetTrackerClientStub;
import com.hedvig.backoffice.services.claims.ClaimsService;
import com.hedvig.backoffice.services.claims.ClaimsServiceImpl;
import com.hedvig.backoffice.services.claims.ClaimsServiceStub;
import com.hedvig.backoffice.services.expo.ExpoNotificationService;
import com.hedvig.backoffice.services.expo.ExpoNotificationServiceImpl;
import com.hedvig.backoffice.services.expo.ExpoNotificationServiceStub;
import com.hedvig.backoffice.services.members.MemberService;
import com.hedvig.backoffice.services.members.MemberServiceImpl;
import com.hedvig.backoffice.services.members.MemberServiceStub;
import com.hedvig.backoffice.services.messages.BotService;
import com.hedvig.backoffice.services.messages.BotServiceImpl;
import com.hedvig.backoffice.services.messages.BotServiceStub;
import com.hedvig.backoffice.services.payments.PaymentService;
import com.hedvig.backoffice.services.payments.PaymentServiceImpl;
import com.hedvig.backoffice.services.payments.PaymentServiceStub;
import com.hedvig.backoffice.services.product_pricing.ProductPricingService;
import com.hedvig.backoffice.services.product_pricing.ProductPricingServiceImpl;
import com.hedvig.backoffice.services.product_pricing.ProductPricingServiceStub;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExternalServicesConfig {

  private final ApplicationContext context;

  @Autowired
  public ExternalServicesConfig(ApplicationContext context) {
    this.context = context;
  }

  @Bean
  public AssetTrackerClient assetTracker(@Value("${tracker.stub:false}") boolean stub) {
    val factory = context.getAutowireCapableBeanFactory();

    return stub
        ? factory.createBean(AssetTrackerClientStub.class)
        : factory.createBean(AssetTrackerClientImpl.class);
  }

  @Bean
  public BotService botService(@Value("${botservice.stub:false}") boolean stub) {
    val factory = context.getAutowireCapableBeanFactory();

    return stub
        ? factory.createBean(BotServiceStub.class)
        : factory.createBean(BotServiceImpl.class);
  }

  @Bean
  public MemberService memberService(@Value("${memberservice.stub:false}") boolean stub) {
    val factory = context.getAutowireCapableBeanFactory();

    return stub
        ? factory.createBean(MemberServiceStub.class)
        : factory.createBean(MemberServiceImpl.class);
  }

  @Bean
  public ClaimsService claimsService(@Value("${claims.stub:false}") boolean stub) {
    val factory = context.getAutowireCapableBeanFactory();

    return stub
        ? factory.createBean(ClaimsServiceStub.class)
        : factory.createBean(ClaimsServiceImpl.class);
  }

  @Bean
  public ExpoNotificationService expoNotificationService(
      @Value("${expo.stub:false}") boolean stub) {
    val factory = context.getAutowireCapableBeanFactory();
    return stub
        ? factory.createBean(ExpoNotificationServiceStub.class)
        : factory.createBean(ExpoNotificationServiceImpl.class);
  }

  @Bean
  public ProductPricingService productPricingService(
      @Value("${productPricing.stub:false}") boolean stub) {
    val factory = context.getAutowireCapableBeanFactory();
    return stub
        ? factory.createBean(ProductPricingServiceStub.class)
        : factory.createBean(ProductPricingServiceImpl.class);
  }

  @Bean
  public PaymentService paymentService(@Value("${paymentService.stub:false}") boolean stub) {
    val factory = context.getAutowireCapableBeanFactory();
    return stub
        ? factory.createBean(PaymentServiceStub.class)
        : factory.createBean(PaymentServiceImpl.class);
  }
}
