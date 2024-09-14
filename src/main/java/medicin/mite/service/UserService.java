package medicin.mite.service;

import medicin.mite.entity.Users;
import medicin.mite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String validateSignup(Users users) {
        Optional<Users> existingUser = userRepository.findByUserid(users.getUserid());

        if (existingUser.isPresent()) {
            return "이미 사용중인 아이디 입니다!";
        }
        if (users.getUserid() == null || users.getUserpw() == null) {
            return "모두 입력해주십시오.";
        }
        userRepository.save(users);
        return "success";
    }

    public String validateLogin(Users users) {
        Optional<Users> existingUserOpt = userRepository.findByUserid(users.getUserid());

        if (existingUserOpt.isPresent()) {
            Users existingUser = existingUserOpt.get();
            if (existingUser.getUserpw().equals(users.getUserpw())) {
                return "환영합니다, " + users.getUserid() + "님!";
            } else {
                return "비밀번호가 틀렸습니다!";
            }
        } else {
            return "아이디가 틀렸습니다!";
        }
    }
}
