package com.hedvig.backoffice.graphql.resolvers

import com.coxautodev.graphql.tools.GraphQLResolver
import com.hedvig.backoffice.graphql.GraphQLConfiguration
import com.hedvig.backoffice.graphql.dataloaders.AccountLoader
import com.hedvig.backoffice.graphql.types.DirectDebitStatus
import com.hedvig.backoffice.graphql.types.FileUpload
import com.hedvig.backoffice.graphql.types.Member
import com.hedvig.backoffice.graphql.types.MonthlySubscription
import com.hedvig.backoffice.graphql.types.Person
import com.hedvig.backoffice.graphql.types.ProductType
import com.hedvig.backoffice.graphql.types.Quote
import com.hedvig.backoffice.graphql.types.Transaction
import com.hedvig.backoffice.graphql.types.account.Account
import com.hedvig.backoffice.graphql.types.account.NumberFailedCharges
import com.hedvig.backoffice.graphql.types.claims.TestClaim
import com.hedvig.backoffice.services.UploadedFilePostprocessor
import com.hedvig.backoffice.services.account.AccountService
import com.hedvig.backoffice.services.claims.ClaimsService
import com.hedvig.backoffice.services.meerkat.Meerkat
import com.hedvig.backoffice.services.meerkat.dto.SanctionStatus
import com.hedvig.backoffice.services.members.MemberService
import com.hedvig.backoffice.services.messages.BotService
import com.hedvig.backoffice.services.payments.PaymentService
import com.hedvig.backoffice.services.personnel.PersonnelService
import com.hedvig.backoffice.services.product_pricing.ProductPricingService
import com.hedvig.backoffice.services.product_pricing.dto.contract.Contract
import com.hedvig.backoffice.services.product_pricing.dto.contract.ContractMarketInfo
import com.hedvig.backoffice.services.underwriter.UnderwriterService
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component
import java.time.YearMonth
import java.util.ArrayList
import java.util.concurrent.CompletableFuture

@Component
class MemberResolver(
  private val paymentService: PaymentService,
  private val productPricingService: ProductPricingService,
  private val meerkat: Meerkat,
  private val accountLoader: AccountLoader,
  private val botService: BotService,
  private val uploadedFilePostprocessor: UploadedFilePostprocessor,
  private val memberService: MemberService,
  private val accountService: AccountService,
  private val underwriterService: UnderwriterService,
  private val claimsService: ClaimsService,
  private val personnelService: PersonnelService
) : GraphQLResolver<Member> {

  fun getTransactions(member: Member): List<Transaction> {
    return paymentService.getTransactionsByMemberId(member.memberId)
      .map((Transaction)::fromDTO)
  }

  fun getMonthlySubscription(member: Member, period: YearMonth): MonthlySubscription {
    return MonthlySubscription(
      productPricingService.getMonthlyPaymentsByMember(period, member.memberId)
    )
  }

  fun getDirectDebitStatus(member: Member): DirectDebitStatus {
    val statusDTO = paymentService.getDirectDebitStatusByMemberId(member.memberId)
      ?: return DirectDebitStatus(member.memberId, false)
    return DirectDebitStatus(statusDTO.memberId, statusDTO.directDebitActivated)
  }

  fun getSanctionStatus(member: Member): SanctionStatus {
    return meerkat.getMemberSanctionStatus(String.format("%s %s", member.firstName, member.lastName))
  }

  fun getAccount(member: Member): CompletableFuture<Account> {
    return accountLoader.load(member.memberId)
  }

  fun fileUploads(member: Member): List<FileUpload> {
    val fileUploadDTOS = botService.files(member.memberId, null)

    if (fileUploadDTOS.isEmpty()) {
      return ArrayList()
    }

    val fileUploads = ArrayList<FileUpload>()

    for (fileUploadDTO in fileUploadDTOS) {
      val fileUpload = FileUpload(
        fileUploadUrl = uploadedFilePostprocessor.processFileUrl(fileUploadDTO.fileUploadKey),
        timestamp = fileUploadDTO.timestamp,
        mimeType = fileUploadDTO.mimeType,
        memberId = fileUploadDTO.memberId
      )
      fileUploads.add(fileUpload)
    }
    return fileUploads
  }

  fun getPerson(member: Member): Person {
    val memberId = member.memberId
    val personDTO = memberService.getPerson(memberId)
    return Person(
      debtFlag = personDTO.flags.debtFlag,
      debt = personDTO.debt,
      whitelisted = personDTO.whitelisted,
      status = personDTO.status
    )
  }

  fun getNumberFailedCharges(member: Member): NumberFailedCharges {
    return NumberFailedCharges.from(accountService.getNumberFailedCharges(member.memberId))
  }

  fun getTotalNumberOfClaims(member: Member, env: DataFetchingEnvironment): Int {
    return claimsService.listByUserId(member.memberId, GraphQLConfiguration.getIdToken(env, personnelService)).filterNot {
      it.type == "TestClaim"
    }.size
  }

  fun getQuotes(member: Member): List<Quote> {
    val quotes = underwriterService.getQuotes(member.memberId)
      .map((Quote)::from)

    return reqularAndSignableQuotes(member.memberId, quotes)
  }


  private fun reqularAndSignableQuotes(
    memberId: String,
    quotes: List<Quote>
  ): List<Quote> {
    val contractTypeNames = productPricingService.getContractsByMemberId(memberId)
      .map { contract -> contract.contractTypeName }.distinct()
    if (contractTypeNames.size == 1) {
      val contractTypeName = contractTypeNames.first()
      if (contractTypeName.equals("Swedish Apartment")) {
        return quotes.map { quote ->
          when (quote.productType) {
            ProductType.HOUSE -> quote.copy(isReadyToSign = true)
            else -> quote
          }
        }
      }
      if (contractTypeName.equals("Swedish House")) {
        return quotes.map { quote ->
          when (quote.productType) {
            ProductType.APARTMENT -> quote.copy(isReadyToSign = true)
            else -> quote
          }
        }
      }
      if (contractTypeName.equals("Norwegian Home Content")) {
        return quotes.map { quote ->
          when (quote.productType) {
            ProductType.TRAVEL -> quote.copy(isReadyToSign = true)
            else -> quote
          }
        }
      }
      if (contractTypeName.equals("Norwegian Travel")) {
        return quotes.map { quote ->
          when (quote.productType) {
            ProductType.HOME_CONTENT -> quote.copy(isReadyToSign = true)
            else -> quote
          }
        }
      }
    }
    return quotes
  }

  fun getContracts(member: Member): List<Contract> {
    val contracts = productPricingService.getContractsByMemberId(member.memberId)
    contracts.forEach { contract -> contract.holderMember = member }
    return contracts
  }

  fun getContractMarketInfo(member: Member): ContractMarketInfo? =
    productPricingService.getContractMarketInfoByMemberId(member.memberId)
}
