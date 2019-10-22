package com.hedvig.backoffice.graphql.resolvers

import com.coxautodev.graphql.tools.GraphQLResolver
import com.hedvig.backoffice.graphql.dataloaders.AccountLoader
import com.hedvig.backoffice.graphql.types.*
import com.hedvig.backoffice.graphql.types.account.Account
import com.hedvig.backoffice.graphql.types.account.NumberFailedCharges
import com.hedvig.backoffice.services.MessagesFrontendPostprocessor
import com.hedvig.backoffice.services.account.AccountService
import com.hedvig.backoffice.services.meerkat.Meerkat
import com.hedvig.backoffice.services.meerkat.dto.SanctionStatus
import com.hedvig.backoffice.services.members.MemberService
import com.hedvig.backoffice.services.messages.BotService
import com.hedvig.backoffice.services.payments.PaymentService
import com.hedvig.backoffice.services.product_pricing.ProductPricingService
import org.springframework.stereotype.Component
import java.lang.NullPointerException
import java.time.YearMonth
import java.util.*
import java.util.concurrent.CompletableFuture

@Component
class MemberResolver(
  private val paymentService: PaymentService,
  private val productPricingService: ProductPricingService,
  private val meerkat: Meerkat,
  private val accountLoader: AccountLoader,
  private val botService: BotService,
  private val messagesFrontendPostprocessor: MessagesFrontendPostprocessor,
  private val memberService: MemberService,
  private val accountService: AccountService
) : GraphQLResolver<Member> {

  fun getTransactions(member: Member): List<Transaction> {
    return paymentService.getTransactionsByMemberId(member.memberId)
      .map(Transaction::fromDTO)
  }

  fun getMonthlySubscription(member: Member, period: YearMonth): MonthlySubscription {
    return MonthlySubscription(
      productPricingService.getMonthlyPaymentsByMember(period, member.memberId))
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
        fileUploadUrl = messagesFrontendPostprocessor.processFileUrl(fileUploadDTO.fileUploadKey),
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
    return try {
      val personDTO = memberService.getPerson(memberId)
      Person(
        personFlags = listOf(personDTO.flags.debtFlag),
        debt = personDTO.debt,
        whitelisted = personDTO.whitelisted,
        status = personDTO.status
      )
    } catch (exception: Exception) {
      throw NullPointerException("No person found (exception: $exception)")
    }
  }

  fun getNumberFailedCharges(member: Member): NumberFailedCharges {
    return NumberFailedCharges.from(accountService.getNumberFailedCharges(member.memberId))
  }
}
