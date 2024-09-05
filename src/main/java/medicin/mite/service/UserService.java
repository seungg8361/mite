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

    public Optional<Users> findByUserid(String userid) {
        return userRepository.findByUserid(userid);
    }

    public void save(Users user) {
        userRepository.save(user);
    }
}
