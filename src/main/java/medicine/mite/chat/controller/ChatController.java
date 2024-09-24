package medicine.mite.chat.controller;

import jakarta.servlet.http.HttpSession;
import medicine.mite.chat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/send-message")
    public ResponseEntity<Map<String, Object>> sendMessage(@RequestBody Map<String, String> payload, HttpSession session) {
        String message = payload.get("message");
        Map<String, Object> response = chatService.processMessage(message);
        // 예를 들어, 처리된 약 정보 리스트를 세션에 저장
        Object medicines = response.get("medicines");
        if (medicines != null) {
            session.setAttribute("medicinesToCompare", medicines);
        }
        return ResponseEntity.ok(response);
    }

}
