package medicine.mite.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import medicine.mite.chat.dto.MedicineDto;
import medicine.mite.chat.service.ChatService;
import medicine.mite.user.dto.UsersDto;
import medicine.mite.user.entity.Users;
import medicine.mite.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BorderController {

    @Autowired
    private UserService userService;

    @Autowired
    private ChatService chatService;

    @GetMapping("/compare")
    public String comparePage(Model model, HttpSession session) {
        List<MedicineDto> medicines = (List<MedicineDto>) session.getAttribute("medicinesToCompare");
        if (medicines != null) {
            model.addAttribute("medicines", medicines);
        }
        return "compare";
    }
    @PostMapping("/save-medicine-info")
    @ResponseBody
    public String saveMedicineInfo(@RequestBody MedicineDto medicineDto, HttpSession session) {
        List<MedicineDto> medicines = (List<MedicineDto>) session.getAttribute("medicinesToCompare");

        if (medicines == null) {
            medicines = new ArrayList<>();
        }
        medicines.add(medicineDto);
        session.setAttribute("medicinesToCompare", medicines);

        return "success"; // 클라이언트에게 성공 메시지 반환
    }
    @GetMapping("/mypage")
    public String myPage(Model model, HttpSession session, Users users) {
        List<MedicineDto> recentMedicines = (List<MedicineDto>) session.getAttribute("recentMedicines");
        if (recentMedicines == null) {
            recentMedicines = new ArrayList<>(); // 빈 리스트로 초기화
        }
        model.addAttribute("recentMedicines", recentMedicines);
        return "mypage";
    }
    // 비밀번호 확인
    @GetMapping("/userupdate")
    public String userUpdatePage(Model model, HttpSession session,Users users) {
        model.addAttribute("users", users);
        return "userupdate"; // userupdate.html로 이동
    }
}
