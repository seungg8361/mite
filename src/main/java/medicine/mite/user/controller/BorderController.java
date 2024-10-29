package medicine.mite.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medicine.mite.chat.dto.MedicineDto;
import medicine.mite.chat.entity.Medicines;
import medicine.mite.user.entity.Users;
import medicine.mite.user.service.BorderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BorderController {

    private final BorderService borderService;

    @PostMapping("/save-medicine-info")
    public String saveMedicineInfo(@RequestBody MedicineDto medicineDto, HttpSession session, RedirectAttributes redirectAttributes) {
        Users sessionUser = (Users) session.getAttribute("userkey");
        if (sessionUser == null) {
            redirectAttributes.addFlashAttribute("error", "사용자가 로그인되어 있지 않습니다.");
            return "redirect:/login";
        }

        String userId = sessionUser.getUserid();
        String message = borderService.saveMedicineInfo(medicineDto, userId, session);
        redirectAttributes.addFlashAttribute(message.contains("성공") ? "success" : "error", message);
        return "redirect:/compare";
    }

    @GetMapping("/compare")
    public String comparePage(HttpSession session, Model model) {
        Users sessionUser = (Users) session.getAttribute("userkey");
        if (sessionUser == null) {
            return "redirect:/login"; // 로그인 안 된 경우 리다이렉트
        }

        String userId = sessionUser.getUserid();
        List<Medicines> recentMedicines = borderService.getRecentMedicines(userId, session);
        model.addAttribute("recentMedicines", recentMedicines);
        return "compare"; // compare.html로 이동
    }

    @GetMapping("/mypage")
    public String myPage(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Users sessionUser = (Users) session.getAttribute("userkey");
        if (sessionUser == null) {
            return "redirect:/login"; // 로그인 안 된 경우 리다이렉트
        }

        String userId = sessionUser.getUserid();
        List<Medicines> recentMedicines = borderService.getRecentMedicines(userId, session);

        String successMessage = (String) session.getAttribute("success");
        if (successMessage != null) {
            redirectAttributes.addFlashAttribute("success", successMessage);
            session.removeAttribute("success");
        }
        model.addAttribute("recentMedicines", recentMedicines);
        return "mypage";
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
    @GetMapping("/index")
    public String indexPage(HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        Users sessionUser = (Users) session.getAttribute("userkey");
        if (sessionUser != null) {
            String username = (String) session.getAttribute("username");
            model.addAttribute("username", username); // 모델에 사용자 이름 추가
        } else {
            redirectAttributes.addFlashAttribute("error", "사용자가 로그인되어 있지 않습니다.");
            return "redirect:/login";
        }
        return "index"; // index.html로 이동
    }
}
