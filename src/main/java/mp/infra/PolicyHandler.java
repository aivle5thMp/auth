package mp.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import mp.config.kafka.KafkaProcessor;
import mp.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    UserRepository userRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='Subscribed'"
    )
    public void wheneverSubscribed_StatusChanged(
        @Payload Subscribed subscribed
    ) {
        Subscribed event = subscribed;
        System.out.println(
            "\n\n##### listener StatusChanged : " + subscribed + "\n\n"
        );

        // Sample Logic //
        User.statusChanged(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='AuditCompleted'"
    )
    public void wheneverAuditCompleted_StatusChanged(
        @Payload AuditCompleted auditCompleted
    ) {
        AuditCompleted event = auditCompleted;
        System.out.println(
            "\n\n##### listener StatusChanged : " + auditCompleted + "\n\n"
        );

        // Sample Logic //
        User.statusChanged(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
