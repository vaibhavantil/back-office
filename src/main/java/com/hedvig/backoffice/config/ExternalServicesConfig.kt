package com.hedvig.backoffice.config

import com.hedvig.backoffice.services.messages.BotServiceStub
import com.hedvig.backoffice.services.members.MemberServiceStub
import com.hedvig.backoffice.services.claims.ClaimsService
import com.hedvig.backoffice.services.claims.ClaimsServiceStub
import com.hedvig.backoffice.services.claims.ClaimsServiceImpl
import com.hedvig.backoffice.services.expo.ExpoNotificationService
import com.hedvig.backoffice.services.expo.ExpoNotificationServiceStub
import com.hedvig.backoffice.services.expo.ExpoNotificationServiceImpl
import com.hedvig.backoffice.services.product_pricing.ProductPricingServiceStub
import com.hedvig.backoffice.services.underwriter.UnderwriterServiceStub
import com.hedvig.backoffice.services.payments.PaymentServiceStub
import com.hedvig.backoffice.services.meerkat.MeerkatStub
import com.hedvig.backoffice.services.account.AccountServiceStub
import com.hedvig.backoffice.services.priceEngine.PriceEngineServiceStub
import com.hedvig.backoffice.services.itemizer.ItemizerService
import com.hedvig.backoffice.services.itemizer.ItemizerServiceStub
import com.hedvig.backoffice.services.itemizer.ItemizerServiceImpl
import com.hedvig.backoffice.services.apigateway.ApiGatewayServiceStub
import com.hedvig.backoffice.services.apigateway.ApiGatewayServiceImpl
import com.hedvig.backoffice.services.account.AccountService
import com.hedvig.backoffice.services.account.AccountServiceImpl
import com.hedvig.backoffice.services.apigateway.ApiGatewayService
import com.hedvig.backoffice.services.meerkat.Meerkat
import com.hedvig.backoffice.services.meerkat.MeerkatImpl
import com.hedvig.backoffice.services.members.MemberService
import com.hedvig.backoffice.services.members.MemberServiceImpl
import com.hedvig.backoffice.services.messages.BotService
import com.hedvig.backoffice.services.messages.BotServiceImpl
import com.hedvig.backoffice.services.payments.PaymentService
import com.hedvig.backoffice.services.payments.PaymentServiceImpl
import com.hedvig.backoffice.services.priceEngine.PriceEngineService
import com.hedvig.backoffice.services.priceEngine.PriceEngineServiceImpl
import com.hedvig.backoffice.services.product_pricing.ProductPricingService
import com.hedvig.backoffice.services.product_pricing.ProductPricingServiceImpl
import com.hedvig.backoffice.services.underwriter.UnderwriterService
import com.hedvig.backoffice.services.underwriter.UnderwriterServiceImpl
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.ResponseEntity
import org.springframework.web.client.getForEntity
import java.lang.Exception
import kotlin.reflect.KClass

@Configuration
class ExternalServicesConfig @Autowired constructor(private val context: ApplicationContext) {

    companion object {
        private val log = LoggerFactory.getLogger(ExternalServicesConfig::class.java)
    }

    private val rest = RestTemplateBuilder()
        // use short connect timeout to make sure we bail asap if it seems to be down,
        // saving us precious boot time
        .setConnectTimeout(30)
        .setReadTimeout(1000)
        .build()

    @Bean
    fun botService(): BotService {
        return create(
            prefix = "botservice",
            stubClass = BotServiceStub::class,
            liveClass = BotServiceImpl::class
        )
    }

    @Bean
    fun memberService(): MemberService {
        return create(
            prefix = "memberservice",
            stubClass = MemberServiceStub::class,
            liveClass = MemberServiceImpl::class
        )
    }

    @Bean
    fun claimsService(): ClaimsService {
        return create(
            prefix = "claims",
            stubClass = ClaimsServiceStub::class,
            liveClass = ClaimsServiceImpl::class
        )
    }

    @Bean
    fun expoNotificationService(): ExpoNotificationService {
        return create(
            prefix = "expo",
            stubClass = ExpoNotificationServiceStub::class,
            liveClass = ExpoNotificationServiceImpl::class
        )
    }

    @Bean
    fun productPricingService(): ProductPricingService {
        return create(
            prefix = "productPricing",
            stubClass = ProductPricingServiceStub::class,
            liveClass = ProductPricingServiceImpl::class
        )
    }

    @Bean
    fun underwriterService(): UnderwriterService {
        return create(
            prefix = "underwriter",
            stubClass = UnderwriterServiceStub::class,
            liveClass = UnderwriterServiceImpl::class
        )
    }

    @Bean
    fun paymentService(): PaymentService {
        return create(
            prefix = "paymentService",
            stubClass = PaymentServiceStub::class,
            liveClass = PaymentServiceImpl::class
        )
    }

    @Bean
    fun meerkat(): Meerkat {
        return create(
            prefix = "meerkat",
            stubClass = MeerkatStub::class,
            liveClass = MeerkatImpl::class
        )
    }

    @Bean
    fun accountService(): AccountService {
        return create(
            prefix = "accountService",
            stubClass = AccountServiceStub::class,
            liveClass = AccountServiceImpl::class
        )
    }

    @Bean
    fun priceEngineService(): PriceEngineService {
        return create(
            prefix = "priceEngine",
            stubClass = PriceEngineServiceStub::class,
            liveClass = PriceEngineServiceImpl::class
        )
    }

    @Bean
    fun itemizerService(): ItemizerService {
        return create(
            prefix = "itemizer",
            stubClass = ItemizerServiceStub::class,
            liveClass = ItemizerServiceImpl::class
        )
    }

    @Bean
    fun apiGatewayService(): ApiGatewayService {
        return create(
            prefix = "apiGateway",
            stubClass = ApiGatewayServiceStub::class,
            liveClass = ApiGatewayServiceImpl::class
        )
    }

    private fun <S: Any> create(prefix: String, stubClass: KClass<out S>, liveClass: KClass<out S>): S {
        val factory = context.autowireCapableBeanFactory
        val mode = context.environment.getProperty("$prefix.mode", "live")
        return when (mode) {
            "live" -> factory.createBean(liveClass.java)
            "stub" -> factory.createBean(stubClass.java)
            "auto-discover" -> {
                log.info("Auto discovering upstream service [{}]", prefix)
                val baseUrl = context.environment.getProperty("$prefix.baseUrl")
                val discovered = isHealthy(baseUrl)
                if (discovered) {
                    log.info("Discovered service [{}] - will use live one", prefix)
                } else {
                    log.info("Did not discover service [{}] - will use stub", prefix)
                }
                if (discovered) factory.createBean(liveClass.java) else factory.createBean(stubClass.java)
            }
            else -> {
                log.error("Unrecognized mode: {}", mode)
                factory.createBean(liveClass.java)
            }
        }
    }

    private fun isHealthy(baseUrl: String): Boolean {
        return try {
            val response: ResponseEntity<Any> = rest.getForEntity("$baseUrl/actuator/health")
            response.statusCode.is2xxSuccessful
        } catch (e: Exception) {
            false
        }
    }
}
