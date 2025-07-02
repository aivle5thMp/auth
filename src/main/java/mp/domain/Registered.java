package mp.domain;

import java.util.UUID;
import lombok.*;
import mp.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class Registered extends AbstractEvent {

    private UUID id;

    public Registered(User aggregate) {
        super();
        this.id = aggregate.getId();
    }

    public Registered() {
        super();
    }
}
//>>> DDD / Domain Event
