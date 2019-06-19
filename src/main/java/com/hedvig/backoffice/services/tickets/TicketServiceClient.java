package com.hedvig.backoffice.services.tickets;

import com.hedvig.backoffice.config.feign.FeignConfig;
import com.hedvig.backoffice.services.tickets.dto.TicketDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
  name= "ticket-service",
  url = "${tickets.baseUrl}",
  configuration = FeignConfig.class)

public interface TicketServiceClient {
  @GetMapping("/_/tickets/{id}")
  TicketDto getTicket (@PathVariable("id") String id);

  @GetMapping ("/_/tickets/all")
  List<TicketDto> getTickets ();

  @PostMapping("/_/tickets/new/{id}")
  void createTicket (@PathVariable String id, @RequestBody TicketDto ticket);

}
