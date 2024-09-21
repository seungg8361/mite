package medicine.mite.user.controller;

import lombok.extern.slf4j.Slf4j;
import medicine.mite.chat.dto.MedicineDto;
import medicine.mite.chat.entity.Medicines;
import medicine.mite.user.entity.Users;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class BorderController {

    @GetMapping("/compare")
    public String comparePage(Model model, HttpSession session) {
        List<MedicineDto> medicines = (List<MedicineDto>) session.getAttribute("recentMedicines");
        if (medicines != null) {
            model.addAttribute("medicines", medicines);
        }
        return "compare";
    }
    @PostMapping("/save-medicine-info")
    public String saveMedicineInfo(@RequestBody MedicineDto medicineDto, HttpSession session) {
        // 약 정보를 세션에 추가
        List<Medicines> recentMedicines = (List<Medicines>) session.getAttribute("recentMedicines");
        if (recentMedicines == null) {
            recentMedicines = new ArrayList<>();
        }
        // 이미 추가된 약인지 확인
        if (recentMedicines.stream().noneMatch(m -> m.getMimage().equals(medicineDto.getImage()))) {
            Medicines medicine = new Medicines();
            medicine.setId(medicineDto.getId());
            medicine.setMimage(medicineDto.getImage());
            medicine.setMname(medicineDto.getName());
            recentMedicines.add(medicine);
            session.setAttribute("recentMedicines", recentMedicines);
        }

        return "redirect:/compare";
    }
    @GetMapping("/mypage")
    public String myPage(HttpSession session, Model model) {
        List<Medicines> recentMedicines = (List<Medicines>) session.getAttribute("recentMedicines");
        model.addAttribute("recentMedicines", recentMedicines);
        return "mypage";
    }
    // 비밀번호 확인
    @GetMapping("/userupdate")
    public String userUpdatePage(Model model, Users users) {
        model.addAttribute("users", users);
        return "userupdate"; // userupdate.html로 이동
    }
    @GetMapping("/index")
    public String indexPage(HttpSession session) {
        // 세션에 저장된 값 확인
        Users sessionUser = (Users) session.getAttribute("userkey");
        if (sessionUser != null) {
            log.info("Session User: {} (ID: {})", sessionUser.getUsername(), sessionUser.getUserid());
        } else {
            log.warn("No user found in session.");
        }
        return "index";
    }
}
