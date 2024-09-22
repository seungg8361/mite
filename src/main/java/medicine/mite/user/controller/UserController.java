package medicine.mite.user.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medicine.mite.user.dto.UsersDto;
import medicine.mite.user.entity.Users;import medicine.mite.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("usersDto", new UsersDto());
        return "signup";
    }
    @PostMapping("/signup")
    public String signupSubmit(@Validated UsersDto usersDto, Model model) {
        try {
            Users user = Users.createUsers(usersDto);
            userService.saveUsers(user);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "signup";
        }
        return "redirect:/login";
    }
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("usersDto", new UsersDto());
        return "login";
    }
    @PostMapping("/login")
    public String loginSubmit(@ModelAttribute UsersDto usersDto, HttpSession session, Model model) {
        String message = userService.validateLogin(usersDto);
        if (message.startsWith("환영합니다")) {
            // 로그인 성공 시 세션에 사용자 정보 저장
            Optional<Users> usersinfo = userService.findByUserid(usersDto.getUserid());
            if (usersinfo.isPresent()) {
                Users user = usersinfo.get(); // 실제 Users 객체를 가져옴
                session.setAttribute("userkey", user);
                // 세션에 정보가 잘 담겼는지 확인하는 로그
                log.info("User logged in: {} (ID: {})", user.getUsername(),user.getUserid());
                return "redirect:/index"; // index 페이지로 리다이렉트
            } else {
                model.addAttribute("message", "사용자를 찾을 수 없습니다.");
                return "login"; // 사용자 정보를 찾을 수 없는 경우 로그인 페이지로 돌아감
            }
        } else {
            model.addAttribute("message", message);
            return "login";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    @PostMapping("/checkPwdForm")
    public String checkPassword(@RequestParam String userpw, HttpSession session, Model model) {
        Users currentUser = (Users) session.getAttribute("userkey"); // 현재 사용자 정보 가져오기
        if (currentUser != null && userpw.equals(currentUser.getUserpw())) {
            return "redirect:/userupdate"; // 비밀번호가 일치하면 회원정보 수정 페이지로 이동
        }
        model.addAttribute("error", "비밀번호가 맞지 않습니다.");
        return "mypage"; // 비밀번호가 틀리면 다시 비밀번호 확인 페이지로
    }
    @PostMapping("/update")
    public String updateUser(@ModelAttribute UsersDto usersDto, HttpSession session, Model model) {
        Users currentUser = (Users) session.getAttribute("userkey");
        if (currentUser != null) {
            // 비밀번호 확인
            if (!usersDto.getUserpw().equals(usersDto.getConfirmPassword())) {
                model.addAttribute("message", "비밀번호가 일치하지 않습니다.");
                return "userupdate"; // 에러 메시지를 보여주기 위해 다시 폼으로 돌아감
            }
            // 사용자 정보 업데이트
            userService.updateUser(usersDto, currentUser);
            // 비밀번호 업데이트
            currentUser.setUserpw(usersDto.getUserpw());
            session.setAttribute("userkey", currentUser);
        }
        return "redirect:/mypage"; // 업데이트 후 리다이렉트할 페이지
    }
}
