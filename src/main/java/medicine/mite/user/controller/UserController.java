package medicine.mite.user.controller;

import jakarta.servlet.http.HttpSession;
import medicine.mite.user.entity.Users;
import medicine.mite.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
        String message = userService.validateSignup(users);

        if (!message.equals("success")) {
            model.addAttribute("message", message);
            return "signup";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("users", new Users());
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit(@ModelAttribute Users users, HttpSession session, Model model) {
        String message = userService.validateLogin(users);

        if (message.startsWith("환영합니다")) {
            session.setAttribute("userid", users.getUserid());
            model.addAttribute("message", message);
            return "index";
        } else {
            model.addAttribute("message", message);
            return "login";
        }
    }
    @GetMapping("/compare")
    public String comparePage(Model model, HttpSession session) {
        // 세션에서 약 정보 가져오기
        Object medicines = session.getAttribute("medicinesToCompare");
        // 약 정보가 있을 경우 모델에 추가
        if (medicines != null) {
            model.addAttribute("medicines", medicines);
        }
        return "compare"; // compare.html로 이동
    }
}

