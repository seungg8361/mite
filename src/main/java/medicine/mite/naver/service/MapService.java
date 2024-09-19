package medicine.mite.naver.service;

import lombok.extern.slf4j.Slf4j;
import medicine.mite.naver.NaverMapProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class MapService {

    @Autowired
    private NaverMapProperties naverMapProperties;

    public String searchNearby(double latitude, double longitude) {
        String clientId = naverMapProperties.getClientId();
        String clientSecret = naverMapProperties.getClientSecret();
        try {
            // 좌표를 'lon,lat' 형식으로 사용
            String coordinates = longitude + "," + latitude;
            // Nearby Search API URL
            String apiUrl = "https://naveropenapi.apigw.ntruss.com/map-place/v1/search?coordinate=" + coordinates;
            // 로깅 추가
            log.debug("Request URL: {}", apiUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-NCP-APIGW-API-KEY-ID", clientId);
            headers.set("X-NCP-APIGW-API-KEY", clientSecret);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();

            // API 요청 보내기
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);

            // 로깅 추가
            log.debug("Response Status Code: {}", response.getStatusCode());
            log.debug("Response Body: {}", response.getBody());

            // 응답 처리
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                log.error("Failed to get a valid response from Naver API, Status code: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error during search for coordinates: {}, Error: {}", latitude + "," + longitude, e.getMessage());
        }
        // 응답 결과를 찾지 못한 경우 null 반환
        return null;
    }
}
