package mp.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;
import lombok.Data;

//<<< EDA / CQRS
@Entity
@Table(name = "UserView_table")
@Data
public class UserView {

    @Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String name;
    private String password;
    private String role;
    private Boolean isSubscribed;
    private String email;
}
