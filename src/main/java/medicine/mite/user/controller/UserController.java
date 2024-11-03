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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String signupPage(Model model) {
        // usersDto 객체 초기화
        model.addAttribute("usersDto", new UsersDto());
        return "signup"; // 회원가입 페이지로 이동
    }
    @PostMapping("/signup")
    public String signupSubmit(@Validated UsersDto usersDto, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            Users user = Users.createUsers(usersDto);
            userService.saveUsers(user);
            session.setAttribute("userkey", user);
        } catch (Exception e) {
            model.addAttribute("error", "이미 존재하는 계정입니다.");
            return "signup";
        }
        // 회원가입 성공 후 로그인 페이지로 리다이렉트
        redirectAttributes.addFlashAttribute("success", "회원가입에 성공하였습니다.");
        return "redirect:/login";
    }
    @GetMapping("/login")
    public String loginPage(Model model) {
        // usersDto 객체를 모델에 추가
        model.addAttribute("usersDto", new UsersDto());
        return "login"; // 로그인 페이지로 이동
    }
    @PostMapping("/login")
    public String loginSubmit(@ModelAttribute UsersDto usersDto, HttpSession session, Model model) {
        String message = userService.validateLogin(usersDto);
        if (message.startsWith("환영합니다")) {
            Optional<Users> usersinfo = userService.findByUserid(usersDto.getUserid());
            if (usersinfo.isPresent()) {
                Users user = usersinfo.get(); // 실제 Users 객체를 가져옴
                session.setAttribute("userkey", user);
                session.setAttribute("userid", user.getUserid());
                session.setAttribute("username", user.getUsername());
                // 새로운 로그인 시 이전 세션 데이터 초기화
                session.removeAttribute("recentMedicines");
                return "redirect:/index"; // index 페이지로 리다이렉트
            }else{return "login";}
        } else {
            // 로그인 실패 시 에러 메시지 전달
            model.addAttribute("error", message);
            return "login"; // 로그인 실패 시 로그인 페이지로 이동
        }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "redirect:/login"; // 로그인 페이지로 리다이렉트
    }

    @GetMapping("/userupdate")
    public String userUpdatePage(Model model, Users users) {
        model.addAttribute("users", users);
        return "userupdate";
    }
    @PostMapping("/update")
    public String updateUser(@ModelAttribute UsersDto usersDto, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Users currentUser = (Users) session.getAttribute("userkey");
        if (currentUser != null) {
            // 비밀번호 확인
            if (!usersDto.getUserpw().equals(usersDto.getConfirmPassword())) {
                model.addAttribute("message", "비밀번호가 일치하지 않습니다.");
                return "userupdate"; // 에러 메시지를 보여주기 위해 다시 폼으로 돌아감
            }
            // 사용자 정보 업데이트
            userService.updateUser(usersDto, currentUser);
            // 비밀번호 업데이트 (비밀번호가 변경된 경우에만)
            if (!usersDto.getUserpw().isEmpty()) {
                currentUser.setUserpw(usersDto.getUserpw());
            }
            session.setAttribute("userkey", currentUser);
            redirectAttributes.addFlashAttribute("success", "회원 정보가 수정되었습니다.");
            // 업데이트 후 마이페이지로 리다이렉트
            session.invalidate();
            return "redirect:/login";
        }
        // 현재 사용자가 없을 경우 처리
        model.addAttribute("error", "사용자가 로그인되어 있지 않습니다.");
        return "redirect:/login"; // 로그인 페이지로 리다이렉트
    }
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteUser(@ModelAttribute UsersDto usersDto, HttpSession session, RedirectAttributes redirectAttributes) {
        Users currentUser = (Users) session.getAttribute("userkey");

        if (usersDto.getUserpw().equals(currentUser.getUserpw())) {
            userService.deleteUserById(currentUser.getId());
            redirectAttributes.addFlashAttribute("success", "회원 탈퇴가 완료되었습니다.");
            session.invalidate();
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "redirect:/mypage";
        }
    }

}