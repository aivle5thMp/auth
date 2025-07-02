package mp.config.kafka;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface KafkaProcessor {
    String AUTHOR_REVIEW_IN = "author-review-in";
    String PAYMENT_SUBSCRIPTION_IN = "payment-subscription-in";
    String USER_SIGNUP_OUT = "user-signup-out";

    @Input(AUTHOR_REVIEW_IN)
    SubscribableChannel authorReviewIn();

    @Input(PAYMENT_SUBSCRIPTION_IN)
    SubscribableChannel paymentSubscriptionIn();

    @Output(USER_SIGNUP_OUT)
    MessageChannel userSignupOut();
}
