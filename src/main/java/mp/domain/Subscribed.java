package mp.domain;

import java.util.Date;
import java.util.UUID;
import lombok.*;
import mp.domain.*;
import mp.infra.AbstractEvent;

@Data
@ToString
public class Subscribed extends AbstractEvent {

    private UUID id;
    private UUID userId;
    private String item;
    private Long amount;
    private String status;
    private Date createdAt;
}
