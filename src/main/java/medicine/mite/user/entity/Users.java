package medicine.mite.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import medicine.mite.user.dto.UsersDto;

@Getter
@Setter
@Entity
@Table(name="users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String userid;

    private String userpw;
    private String username;
    private String usernumber;

    public static Users createUsers(UsersDto usersDto) {
        Users users = new Users();
        users.setUserid(usersDto.getUserid());
        users.setUserpw(usersDto.getUserpw());
        users.setUsername(usersDto.getUsername());
        users.setUsernumber(usersDto.getUsernumber());
        return users;
    }
}
