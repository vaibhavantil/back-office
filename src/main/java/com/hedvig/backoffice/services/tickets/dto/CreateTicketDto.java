package com.hedvig.backoffice.services.tickets.dto;

import com.hedvig.backoffice.graphql.types.TicketInput;
import lombok.Value;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.time.LocalTime;

@Value
public class CreateTicketDto {
    private String createdBy;
    private String assignedTo;
    private String memberId;
    private String referenceId;
    @Min(0)
    @Max(1)
    private Float priority;
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
        null,
        null,
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
