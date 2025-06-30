package mp.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

//<<< EDA / CQRS
@Entity
@Table(name = "UserView_table")
@Data
public class UserView {

    @Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String name;
    private String password;
    private String role;
    private Boolean isSubscribed;
    private String email;
}
