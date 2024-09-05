package medicin.mite.controller;

import medicin.mite.entity.Users;
import medicin.mite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("users", new Users());
        return "signup";
    }

    @PostMapping("/signup")
    public String signupSubmit(@ModelAttribute Users users, Model model) {
        Optional<Users> existingUser = userService.findByUserid(users.getUserid());
        if (existingUser.isPresent()) {
            model.addAttribute("message", "이미 사용중인 ID입니다!");
            return "signup";
        }
        userService.save(users);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("users", new Users());
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit(@ModelAttribute Users users, Model model) {
        Optional<Users> existingUserOpt = userService.findByUserid(users.getUserid());
        if (existingUserOpt.isPresent()) {
            Users existingUser = existingUserOpt.get();
            if (existingUser.getUserpw().equals(users.getUserpw())) {
                model.addAttribute("message", "Welcome, " + users.getUserid() + "!");
                return "welcome";
            } else {
                model.addAttribute("message", "비밀번호가 틀렸습니다!");
                return "login";
            }
        } else {
            model.addAttribute("message", "아이디가 틀렸습니다!");
            return "login";
        }
    }
}
