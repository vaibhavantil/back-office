package com.hedvig.backoffice.config;

import com.hedvig.backoffice.services.account.AccountService;
import com.hedvig.backoffice.services.account.AccountServiceImpl;
import com.hedvig.backoffice.services.account.AccountServiceStub;
import com.hedvig.backoffice.services.apigateway.ApiGatewayService;
import com.hedvig.backoffice.services.apigateway.ApiGatewayServiceImpl;
import com.hedvig.backoffice.services.apigateway.ApiGatewayServiceStub;
import com.hedvig.backoffice.services.claims.ClaimsService;
import com.hedvig.backoffice.services.claims.ClaimsServiceImpl;
import com.hedvig.backoffice.services.claims.ClaimsServiceStub;
import com.hedvig.backoffice.services.expo.ExpoNotificationService;
import com.hedvig.backoffice.services.expo.ExpoNotificationServiceImpl;
import com.hedvig.backoffice.services.expo.ExpoNotificationServiceStub;
import com.hedvig.backoffice.services.itemizer.ItemizerService;
import com.hedvig.backoffice.services.itemizer.ItemizerServiceImpl;
import com.hedvig.backoffice.services.itemizer.ItemizerServiceStub;
import com.hedvig.backoffice.services.meerkat.Meerkat;
import com.hedvig.backoffice.services.meerkat.MeerkatImpl;
import com.hedvig.backoffice.services.meerkat.MeerkatStub;
import com.hedvig.backoffice.services.members.MemberService;
import com.hedvig.backoffice.services.members.MemberServiceImpl;
import com.hedvig.backoffice.services.members.MemberServiceStub;
import com.hedvig.backoffice.services.messages.BotService;
import com.hedvig.backoffice.services.messages.BotServiceImpl;
import com.hedvig.backoffice.services.messages.BotServiceStub;
import com.hedvig.backoffice.services.payments.PaymentService;
import com.hedvig.backoffice.services.payments.PaymentServiceImpl;
import com.hedvig.backoffice.services.payments.PaymentServiceStub;
import com.hedvig.backoffice.services.priceEngine.PriceEngineService;
import com.hedvig.backoffice.services.priceEngine.PriceEngineServiceImpl;
import com.hedvig.backoffice.services.priceEngine.PriceEngineServiceStub;
import com.hedvig.backoffice.services.product_pricing.ProductPricingService;
import com.hedvig.backoffice.services.product_pricing.ProductPricingServiceImpl;
import com.hedvig.backoffice.services.product_pricing.ProductPricingServiceStub;
import com.hedvig.backoffice.services.underwriter.UnderwriterService;
import com.hedvig.backoffice.services.underwriter.UnderwriterServiceImpl;
import com.hedvig.backoffice.services.underwriter.UnderwriterServiceStub;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Configuration
@Slf4j
public class ExternalServicesConfig {

    private final ApplicationContext context;

    private final RestTemplate rest = new RestTemplateBuilder()
        // use short connect timeout to make sure we bail asap if it seems to be down,
        // saving us precious boot time
        .setConnectTimeout(30)
        .setReadTimeout(1000)
        .build();

    @Autowired
    public ExternalServicesConfig(ApplicationContext context) {
        this.context = context;
    }

    @Bean
    public BotService botService() {
        return create("botservice", BotServiceStub.class, BotServiceImpl.class);
    }

    @Bean
    public MemberService memberService() {
        return create("memberservice", MemberServiceStub.class, MemberServiceImpl.class);
    }

    @Bean
    public ClaimsService claimsService() {
        return create("claims", ClaimsServiceStub.class, ClaimsServiceImpl.class);
    }

    @Bean
    public ExpoNotificationService expoNotificationService() {
        return create("expo", ExpoNotificationServiceStub.class, ExpoNotificationServiceImpl.class);
    }

    @Bean
    public ProductPricingService productPricingService() {
        return create("productPricing", ProductPricingServiceStub.class, ProductPricingServiceImpl.class);
    }

    @Bean
    public UnderwriterService underwriterService() {
        return create("underwriter", UnderwriterServiceStub.class, UnderwriterServiceImpl.class);
    }

    @Bean
    public PaymentService paymentService() {
        return create("paymentService", PaymentServiceStub.class, PaymentServiceImpl.class);
    }

    @Bean
    public Meerkat meerkat() {
        return create("meerkat", MeerkatStub.class, MeerkatImpl.class);
    }

    @Bean
    public AccountService accountService() {
        return create("accountService", AccountServiceStub.class, AccountServiceImpl.class);
    }

    @Bean
    public PriceEngineService priceEngineService() {
        return create("priceEngine", PriceEngineServiceStub.class, PriceEngineServiceImpl.class);
    }

    @Bean
    public ItemizerService itemizerService() {
        return create("itemizer", ItemizerServiceStub.class, ItemizerServiceImpl.class);
    }

    @Bean
    public ApiGatewayService apiGatewayService() {
        return create("apiGateway", ApiGatewayServiceStub.class, ApiGatewayServiceImpl.class);
    }

    private <S> S create(String prefix, Class<? extends S> stubClass, Class<? extends S> liveClass) {
        AutowireCapableBeanFactory factory = context.getAutowireCapableBeanFactory();
        String mode = context.getEnvironment().getProperty(prefix + ".mode", "live");

        switch (mode) {
            case "live":
                return factory.createBean(liveClass);
            case "stub":
                return factory.createBean(stubClass);
            case "auto-discover":
                log.info("Auto discovering upstream service [{}]", prefix);
                String baseUrl = context.getEnvironment().getProperty(prefix + ".baseUrl");
                boolean discovered = isHealthy(baseUrl);
                if (discovered) {
                    log.info("Discovered service [{}] - will use live one", prefix);
                } else {
                    log.info("Did not discover service [{}] - will use stub", prefix);
                }
                return discovered ? factory.createBean(liveClass) : factory.createBean(stubClass);
            default:
                log.error("Unrecognized mode: {}", mode);
                return factory.createBean(liveClass);
        }
    }

    private boolean isHealthy(String baseUrl) {
        try {
            ResponseEntity<?> response = rest.getForEntity(baseUrl + "/actuator/health", Map.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }
}
