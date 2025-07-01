package mp.domain;

import java.util.UUID;
import lombok.*;
import mp.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class Registered extends AbstractEvent {

    private UUID id;
    private String name;
    private String email;
    private String password;
    private String role;
    private Boolean isSubscribed;

    public Registered(User aggregate) {
        super(aggregate);
    }

    public Registered() {
        super();
    }
}
//>>> DDD / Domain Event
