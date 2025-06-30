package mp.domain;

import java.util.*;
import lombok.*;
import mp.domain.*;
import mp.infra.AbstractEvent;

@Data
@ToString
public class AuditCompleted extends AbstractEvent {

    private String id;
    private String userId;
    private String info;
    private String status;
    private String portfolioUrl;
}
