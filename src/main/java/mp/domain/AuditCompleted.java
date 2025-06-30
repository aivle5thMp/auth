package mp.domain;

import java.util.Date;
import java.util.UUID;
import lombok.*;
import mp.infra.AbstractEvent;

@Data
@ToString
public class AuditCompleted extends AbstractEvent {
    private UUID id;
    private UUID userId;
    private String status;  // "APPROVED" or "REJECTED"
    private Date completedAt;
}
