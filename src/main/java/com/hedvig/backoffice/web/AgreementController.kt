package com.hedvig.backoffice.web

import com.hedvig.backoffice.services.personnel.PersonnelService
import com.hedvig.backoffice.services.product_pricing.ProductPricingService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.security.Principal
import java.util.UUID


@RestController
@RequestMapping("/_/agreements")
class AgreementController(
    val productPricingService: ProductPricingService,
    val personnelService: PersonnelService
) {

    @PostMapping("/{agreementId}/certificates")
    fun insuranceCertificate(
        @PathVariable("agreementId") agreementId: UUID,
        @RequestParam file: MultipartFile,
        @AuthenticationPrincipal principal: Principal
    ) {
        productPricingService.uploadCertificate(
            agreementId,
            file,
            personnelService.getIdToken(principal.name)
        )
    }
}
