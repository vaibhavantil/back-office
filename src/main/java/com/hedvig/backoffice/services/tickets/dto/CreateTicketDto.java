package com.hedvig.backoffice.services.tickets.dto;

import com.hedvig.backoffice.graphql.types.TicketInput;
import lombok.Value;
import java.time.LocalDate;
import java.time.LocalTime;

@Value
public class CreateTicketDto {
    private String createdBy;
    private String assignedTo;
    @javax.validation.constraints.Min(0)
    @javax.validation.constraints.Max(1)
    @javax.validation.Valid
    private float priority;
    private TicketType type;
    private LocalDate remindNotificationDate;
    private LocalTime remindNotificationTime;
    private String remindMessage;
    private String description;
    private TicketStatus status;

    public static CreateTicketDto fromTicketInput(TicketInput t, String createdBy) {
      return new CreateTicketDto (
        createdBy,
        t.getAssignedTo(),
        t.getPriority(),
        t.getType(),
        t.getRemindNotificationDate(),
        t.getRemindNotificationTime(),
        t.getRemindMessage(),
        t.getDescription(),
        t.getStatus()
      );
    }
}
