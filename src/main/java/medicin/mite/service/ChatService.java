package medicin.mite.service;

import medicin.mite.entity.Chat;
import medicin.mite.entity.Medicines;
import medicin.mite.repository.ChatRepository;
import medicin.mite.repository.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
                String imageUrl = medicine.getMimage();  // 이미지 URL 생성
                medicineInfo.put("image", imageUrl);

                System.out.println("Medicine Name: " + medicine.getMname());
                System.out.println("Image URL: " + imageUrl);
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