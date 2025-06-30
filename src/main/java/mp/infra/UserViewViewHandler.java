package mp.infra;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import mp.config.kafka.KafkaProcessor;
import mp.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class UserViewViewHandler {

    //<<< DDD / CQRS
    @Autowired
    private UserViewRepository userViewRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenRegistered_then_CREATE_1(@Payload Registered registered) {
        try {
            if (!registered.validate()) return;

            // view 객체 생성
            UserView userView = new UserView();
            // view 객체에 이벤트의 Value 를 set 함
            userView.setId(registered.getId());
            userView.setName(registered.getName());
            userView.setRole(registered.getRole());
            userView.setEmail(registered.getEmail());
            userView.setPassword(registered.getPassword());
            userView.setIsSubscribed(registered.getIsSubscribed());
            // view 레파지 토리에 save
            userViewRepository.save(userView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //>>> DDD / CQRS
}
