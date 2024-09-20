package medicine.mite.user.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import medicine.mite.user.dto.UsersDto;
import medicine.mite.user.repository.UserRepository;
import medicine.mite.user.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<Users> findByUserid(String userid) {
        return userRepository.findByUserid(userid);
    }
    public Users saveUsers(Users users) {
        validateSignup(users);
        return userRepository.save(users);
    }
    public void validateSignup(Users users) {
        Optional<Users> findUsers = userRepository.findByUserid(users.getUserid());
        if (findUsers.isPresent()) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }
    public String validateLogin(UsersDto usersDto) {
        Optional<Users> findUser = userRepository.findByUserid(usersDto.getUserid());
        if (findUser.isEmpty()) {
            return "아이디가 존재하지 않습니다.";
        }
        Users user = findUser.get();
        if (!user.getUserpw().equals(usersDto.getUserpw())) {
            return "비밀번호가 일치하지 않습니다.";
        }
        return "환영합니다, " + user.getUsername() + "님!";
    }
    // 사용자 정보 업데이트 메서드 추가
    public Users updateUser(UsersDto usersDto, Users currentUser) {
        currentUser.setUserpw(usersDto.getUserpw());
        currentUser.setUsername(usersDto.getUsername());
        currentUser.setUsernumber(usersDto.getUsernumber());
        return userRepository.save(currentUser); // 데이터베이스에 저장
    }
}