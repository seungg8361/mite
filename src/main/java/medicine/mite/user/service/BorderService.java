package medicine.mite.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medicine.mite.chat.dto.MedicineDto;
import medicine.mite.chat.entity.Medicines;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BorderService {

    public String saveMedicineInfo(MedicineDto medicineDto, String userId, HttpSession session) {
        List<Medicines> recentMedicines = (List<Medicines>) session.getAttribute("recentMedicines_" + userId);
        if (recentMedicines == null) {
            recentMedicines = new ArrayList<>();
        }
        // 중복 확인
        if (recentMedicines.stream().noneMatch(m -> m.getMimage().equals(medicineDto.getImage()))) {
            Medicines medicine = new Medicines();
            medicine.setMimage(medicineDto.getImage());
            medicine.setMname(medicineDto.getName());
            medicine.setPrice(medicineDto.getPrice());
            medicine.setIngredients(medicineDto.getIngredients());
            medicine.setEfficacy(medicineDto.getEfficacy());
            recentMedicines.add(medicine);
            session.setAttribute("recentMedicines_" + userId, recentMedicines);
        }
    return null;
    }
    public List<Medicines> getRecentMedicines(String userId, HttpSession session) {
        return (List<Medicines>) session.getAttribute("recentMedicines_" + userId);
    }
}
