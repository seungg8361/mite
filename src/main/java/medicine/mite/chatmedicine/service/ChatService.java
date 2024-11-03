package medicine.mite.chatmedicine.service;

import lombok.extern.slf4j.Slf4j;
import medicine.mite.chatmedicine.repository.ChatRepository;
import medicine.mite.chatmedicine.entity.Chat;
import medicine.mite.chatmedicine.entity.Medicines;
import medicine.mite.chatmedicine.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    public Map<String, Object> processMessage(String message) {
        Map<String, Object> response = new HashMap<>();
        Optional<Chat> chat = chatRepository.findByKeyword(message);

        if (chat.isPresent()) {
            Long cid = chat.get().getId();
            String info = chat.get().getInfo();
            List<Medicines> medicines = medicineRepository.findByCid(cid);

            List<Map<String, String>> medicineList = new ArrayList<>();
            for (Medicines medicine : medicines) {
                Map<String, String> medicineInfo = new HashMap<>();
                medicineInfo.put("name", medicine.getMname());
                medicineInfo.put("price", String.valueOf(medicine.getPrice()));
                medicineInfo.put("ingredients", medicine.getIngredients());
                medicineInfo.put("efficacy", medicine.getEfficacy());
                String imageUrl = medicine.getMimage();  // 이미지 URL 생성
                medicineInfo.put("image", imageUrl);

                medicineList.add(medicineInfo);
            }
            response.put("medicines", medicineList);
            response.put("info", info);
        } else {
            response.put("response", "해당 키워드에 대한 정보를 찾을 수 없습니다.");
        }
        return response;
    }
}