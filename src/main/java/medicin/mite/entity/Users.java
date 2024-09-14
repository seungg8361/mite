package medicin.mite.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userid;
    private String userpw;
    private String username;
    private String usernumber;

    public Users(String userid, String userpw, String username, String usernumber) {
        this.userid = userid;
        this.userpw = userpw;
        this.username = username;
        this.usernumber = usernumber;
    }
    public Users() {}
}
