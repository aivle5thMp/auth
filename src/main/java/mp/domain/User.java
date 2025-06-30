package mp.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;
import mp.AuthenticationApplication;
import mp.domain.Registered;

@Entity
@Table(name = "User_table")
@Data
//<<< DDD / Aggregate Root
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String email;

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
        //implement business logic here:

        /** Example 1:  new item 
        User user = new User();
        repository().save(user);

        */

        /** Example 2:  finding and process
        

        repository().findById(subscribed.get???()).ifPresent(user->{
            
            user // do something
            repository().save(user);


         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void statusChanged(AuditCompleted auditCompleted) {
        //implement business logic here:

        /** Example 1:  new item 
        User user = new User();
        repository().save(user);

        */

        /** Example 2:  finding and process
        

        repository().findById(auditCompleted.get???()).ifPresent(user->{
            
            user // do something
            repository().save(user);


         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
