package com.hedvig.backoffice.services.tickets

import com.hedvig.backoffice.config.feign.FeignConfig
import com.hedvig.backoffice.services.tickets.dto.*
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.util.UUID

@FeignClient(name = "ticket-service", url = "\${tickets.baseUrl:ticket-service}", configuration = [FeignConfig::class])
interface TicketServiceClient {

    @GetMapping("/_/tickets/all")
    fun getAllTickets(): List<TicketDto>

    @GetMapping("/_/tickets/all/unresolved")
    fun getUnresolvedTickets(): List<TicketDto>

    @GetMapping("/_/tickets/all/resolved")
    fun getResolvedTickets(): List<TicketDto>

    @GetMapping("/_/tickets/{id}")
    fun getTicket(@PathVariable("id") id: UUID): TicketDto

    @GetMapping("/_/tickets/{id}/history")
    fun getTicketHistory(@PathVariable("id") id: UUID): TicketHistoryDto

    @PostMapping("/_/tickets/create")
    fun createTicket(@RequestBody ticket: CreateTicketDto): UUID

    @PostMapping("/_/tickets/description/{id}")
    fun changeDescription(
        @PathVariable id: UUID,
        @RequestBody description: ChangeDescriptionDto
    )

    @PostMapping("/_/tickets/assign/{id}")
    fun changeAssignedTo(
        @PathVariable id: UUID,
        @RequestBody assignedTo: ChangeAssignToDto
    )

    @PostMapping("/_/tickets/status/{id}")
    fun changeStatus(
        @PathVariable id: UUID,
        @RequestBody status: ChangeStatusDto
    )

    @PostMapping("/_/tickets/reminder/{id}")
    fun changeReminder(
        @PathVariable id: UUID,
        @RequestBody reminder: ChangeReminderDto
    )

    @PostMapping("/_/tickets/priority/{id}")
    fun changePriority(
        @PathVariable id: UUID,
        @RequestBody priority: ChangePriorityDto
    )
}
