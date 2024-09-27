package medicine.mite.user.controller;

import lombok.extern.slf4j.Slf4j;
import medicine.mite.chat.dto.MedicineDto;
import medicine.mite.chat.entity.Medicines;
import medicine.mite.user.entity.Users;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class BorderController {

    @GetMapping("/compare")
    public String comparePage(HttpSession session, Model model) {
        List<Medicines> recentMedicines = (List<Medicines>) session.getAttribute("recentMedicines");
        model.addAttribute("recentMedicines", recentMedicines);
        return "compare"; // compare.html로 이동
    }

    @PostMapping("/save-medicine-info")
    public String saveMedicineInfo(@RequestBody MedicineDto medicineDto, HttpSession session, RedirectAttributes redirectAttributes) {

        List<Medicines> recentMedicines = (List<Medicines>) session.getAttribute("recentMedicines");
        if (recentMedicines == null) {
            recentMedicines = new ArrayList<>();
        }
        // 중복 확인 (이미 같은 이미지의 약이 있는지 체크)
        if (recentMedicines.stream().noneMatch(m -> m.getMimage().equals(medicineDto.getImage()))) {
            // 새 약 정보를 생성하고 설정
            Medicines medicine = new Medicines();
            medicine.setMimage(medicineDto.getImage());
            medicine.setMname(medicineDto.getName());
            medicine.setPrice(medicineDto.getPrice());
            medicine.setIngredients(medicineDto.getIngredients());
            medicine.setEfficacy(medicineDto.getEfficacy());
            // 새로 추가한 약을 리스트에 저장
            recentMedicines.add(medicine);
            // 세션에 업데이트된 약 리스트 저장
            session.setAttribute("recentMedicines", recentMedicines);
            // 성공 메시지를 리다이렉트에 추가
            redirectAttributes.addFlashAttribute("success", "약 정보가 성공적으로 추가되었습니다.");
        } else {
            // 이미 추가된 약이라는 오류 메시지를 리다이렉트에 추가
            redirectAttributes.addFlashAttribute("error", "이미 추가된 약입니다.");
        }
        // 비교 페이지로 리다이렉트
        return "redirect:/compare";
    }

    @GetMapping("/mypage")
    public String myPage(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        List<Medicines> recentMedicines = (List<Medicines>) session.getAttribute("recentMedicines");

        String successMessage = (String) session.getAttribute("success");
        if (successMessage != null) {
            redirectAttributes.addFlashAttribute("success", successMessage);
            session.removeAttribute("success");
        }
        model.addAttribute("recentMedicines", recentMedicines);
        return "mypage";
    }

    @GetMapping("/userupdate")
    public String userUpdatePage(Model model, Users users) {
        model.addAttribute("users", users);
        return "userupdate";
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
