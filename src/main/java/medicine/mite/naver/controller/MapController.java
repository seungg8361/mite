package medicine.mite.naver.controller;

import lombok.extern.slf4j.Slf4j;
import medicine.mite.pharmacy.entity.Pharmacy;
import medicine.mite.pharmacy.service.PharmacyService;
import medicine.mite.naver.NaverMapProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
public class MapController {

    @Autowired
    private NaverMapProperties naverMapProperties;

    @Autowired
    private PharmacyService pharmacyService;

    @GetMapping("/search")
    public String search(@RequestParam(value = "query", required = false) String query, Model model) {
        // 약 이름으로 약국 정보를 가져오는 로직
        List<Pharmacy> pharmacies = pharmacyService.searchPharmaciesByMedicine(query);

        if (!pharmacies.isEmpty()) {
            // 검색된 약국 정보를 모델에 추가
            model.addAttribute("pharmacies", pharmacies);
        } else {
            model.addAttribute("message", "검색된 약국이 없습니다.");
        }
        // 모델에 지도 클라이언트 ID 추가
        model.addAttribute("clientId", naverMapProperties.getClientId());

        return "search";  // 템플릿 파일 이름 반환
    }
}
