package medicine.mite.naver.service;

import medicine.mite.naver.entity.Drug;
import medicine.mite.naver.entity.Pharmacy;
import medicine.mite.naver.repository.DrugRepository;
import medicine.mite.naver.repository.PharmacyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PharmacyService {

    @Autowired
    private PharmacyRepository pharmacyRepository;

    @Autowired
    private DrugRepository drugRepository;

    public List<Pharmacy> searchPharmaciesByMedicine(String query) {
        // 검색한 약 이름으로 drug 테이블에서 약 정보 조회
        List<Drug> drugs = drugRepository.findByDrugName(query);
        // 약이 있는 약국의 ID 리스트를 추출
        List<Long> pharmacyIds = drugs.stream()
                .map(drug -> drug.getPharmacy().getId()) // 약의 pharmacy 객체에서 ID 가져오기
                .distinct() // 중복 제거
                .collect(Collectors.toList());
        if (pharmacyIds.isEmpty()) {
            return List.of(); // 빈 리스트 반환
        }
        // pharmacy 테이블에서 약국 정보 조회
        List<Pharmacy> pharmacies = pharmacyRepository.findByIdIn(pharmacyIds);
        // 약국의 각 약국에 제공하는 약 목록과 개수를 추가
        pharmacies.forEach(pharmacy -> {
            List<Drug> pharmacyDrugs = drugs.stream()
                    .filter(drug -> drug.getPharmacy().getId().equals(pharmacy.getId())) // pharmacy 객체에서 ID를 비교
                    .collect(Collectors.toList());
            // 약국 객체에 약 목록 추가
            pharmacy.setDrugs(pharmacyDrugs);
            // 각 약의 이미지 URL을 설정
            pharmacyDrugs.forEach(drug -> {
                String imageUrl = drug.getDimage(); // 이미지 URL 생성
                drug.setDimage(imageUrl); // 이미지 URL을 Drug 객체에 설정 (setImageUrl 메서드가 필요)
            });
        });
        return pharmacies;
    }
}
