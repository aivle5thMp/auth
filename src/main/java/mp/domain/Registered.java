package mp.domain;

import java.util.UUID;
import lombok.*;
import mp.infra.AbstractEvent;
import com.fasterxml.jackson.annotation.JsonIgnore;

//<<< DDD / Domain Event
@Data
@ToString
public class Registered extends AbstractEvent {

    private UUID id;
    private String name;
    private String email;
    @JsonIgnore  // 비밀번호는 Kafka 메시지에서 제외
    private String password;
    private String role;
    private Boolean isSubscribed;

    public Registered(User aggregate) {
        super(aggregate);
        // 보안상 비밀번호는 명시적으로 null로 설정
        this.password = null;
    }

    public Registered() {
        super();
    }
}
//>>> DDD / Domain Event
