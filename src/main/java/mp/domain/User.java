package mp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.persistence.*;
import lombok.Data;
import mp.AuthenticationApplication;
import mp.domain.Registered;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "User_table")
@Data
//<<< DDD / Aggregate Root
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String name;

    private String email;

    @JsonIgnore
    private String password;

    private String role;

    private Boolean isSubscribed;

    @PostPersist
    public void onPostPersist() {
        Registered registered = new Registered(this);
        registered.publishAfterCommit();
    }

    public static UserRepository repository() {
        UserRepository userRepository = AuthenticationApplication.applicationContext.getBean(
            UserRepository.class
        );
        return userRepository;
    }

    //<<< Clean Arch / Port Method
    public static void statusChanged(Subscribed subscribed) {
        repository().findById(subscribed.getUserId()).ifPresent(user -> {
            // 결제 상태가 성공인 경우에만 처리
            if ("COMPLETED".equals(subscribed.getStatus())) {
                user.setIsSubscribed(true);
                repository().save(user);
            }
        });
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void statusChanged(AuditCompleted auditCompleted) {
        repository().findById(auditCompleted.getUserId()).ifPresent(user -> {
            // 심사가 승인되고 구독 중인 경우에만 AUTHOR로 변경
            if ("APPROVED".equals(auditCompleted.getStatus())) {
                user.setRole("AUTHOR");
                repository().save(user);
            }
        });
    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
