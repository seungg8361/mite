package medicine.mite.user.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medicine.mite.chat.entity.Medicines;
import medicine.mite.user.dto.UsersDto;
import medicine.mite.user.entity.Users;import medicine.mite.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
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
            model.addAttribute("error", "이미 존재하는 계정입니다.");
            return "signup";
        }
        return "redirect:/login";
    }
    @GetMapping("/login")
    public String loginPage(Model model, HttpSession session) {
        // 세션에서 성공 메시지 가져오기
        String successMessage = (String) session.getAttribute("success");
        if (successMessage != null) {
            model.addAttribute("success", successMessage);
            // 성공 메시지를 세션에서 제거
            session.removeAttribute("success");
        }
        // usersDto 객체를 모델에 추가
        model.addAttribute("usersDto", new UsersDto());
        return "login"; // 로그인 페이지로 이동
    }
    @PostMapping("/login")
    public String loginSubmit(@ModelAttribute UsersDto usersDto, HttpSession session, Model model) {
        // 현재 세션에 저장된 success 메시지를 확인
        String successMessage = (String) session.getAttribute("success");
        if (successMessage != null) {
            model.addAttribute("success", successMessage);
            session.removeAttribute("success"); // 메시지 표시 후 제거
        }
        String message = userService.validateLogin(usersDto);
        if (message.startsWith("환영합니다")) {
            Optional<Users> usersinfo = userService.findByUserid(usersDto.getUserid());
            if (usersinfo.isPresent()) {
                Users user = usersinfo.get(); // 실제 Users 객체를 가져옴
                session.setAttribute("userkey", user);
                session.setAttribute("username", user.getUsername());
                return "redirect:/index"; // index 페이지로 리다이렉트
            } else {
                // 사용자를 찾을 수 없는 경우에도 로그인 페이지로 돌아감
                model.addAttribute("error", "사용자를 찾을 수 없습니다.");
                return "login"; // 사용자 정보를 찾을 수 없을 때 로그인 페이지로 이동
            }
        } else {
            // 로그인 실패 시 에러 메시지 전달
            model.addAttribute("error", message);
            return "login"; // 로그인 실패 시 로그인 페이지로 이동
        }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    @PostMapping("/mypage")
    public String checkPassword(@RequestParam("userpw") String userpw, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Users currentUser = (Users) session.getAttribute("userkey");
        // 비밀번호 확인
        if (currentUser != null && userpw.equals(currentUser.getUserpw())) {
            return "redirect:/userupdate"; // 비밀번호가 일치하면 회원정보 수정 페이지로 이동
        }
        // 비밀번호가 틀리면 에러 메시지 추가
        redirectAttributes.addFlashAttribute("error", "비밀번호가 맞지 않습니다.");
        List<Medicines> recentMedicines = (List<Medicines>) session.getAttribute("recentMedicines");
        model.addAttribute("recentMedicines", recentMedicines);
        return "redirect:/mypage"; // 비밀번호가 틀리면 다시 마이페이지로
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
            session.setAttribute("success", "회원정보가 성공적으로 수정되었습니다.");
        }
        return "redirect:/mypage"; // 업데이트 후 리다이렉트할 페이지
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