package medicin.mite.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userid;
    private String userpw;
    private String username;
    private String usernumber;
    private String useraddress;

    public Users(String userid, String userpw, String username, String usernumber, String useraddress) {
        this.userid = userid;
        this.userpw = userpw;
        this.username = username;
        this.usernumber = usernumber;
        this.useraddress = useraddress;
    }
    public Users() {}
}
