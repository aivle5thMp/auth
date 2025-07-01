package mp.infra;

import javax.transaction.Transactional;
import mp.config.kafka.KafkaProcessor;
import mp.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    private static final Logger logger = LoggerFactory.getLogger(PolicyHandler.class);

    @Autowired
    UserRepository userRepository;

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='Subscribed'"
    )
    public void wheneverSubscribed_UpdateSubscriptionStatus(
        @Payload Subscribed subscribed
    ) {
        logger.info("Received Subscribed event for user: {}", subscribed.getUserId());
        try {
            User.statusChanged(subscribed);
            logger.info("Successfully processed Subscribed event for user: {}", subscribed.getUserId());
        } catch (Exception e) {
            logger.error("Error processing Subscribed event for user: {}", subscribed.getUserId(), e);
        }
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='AuditCompleted'"
    )
    public void wheneverAuditCompleted_PromoteToAuthor(
        @Payload AuditCompleted auditCompleted
    ) {
        logger.info("Received AuditCompleted event for user: {} with status: {}", 
                   auditCompleted.getUserId(), auditCompleted.getStatus());
        try {
            User.statusChanged(auditCompleted);
            logger.info("Successfully processed AuditCompleted event for user: {}", auditCompleted.getUserId());
        } catch (Exception e) {
            logger.error("Error processing AuditCompleted event for user: {}", auditCompleted.getUserId(), e);
        }
    }
}
//>>> Clean Arch / Inbound Adaptor
