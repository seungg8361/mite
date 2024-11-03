package medicine.mite.pharmacy.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Pharmacy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pname; // 약국 이름
    private String paddress; // 약국 주소
    private String pnumber; // 약국 전화번호
    private String phours; // 약국 운영 시간
    private double latitude;
    private double longitude;
    private String pimage;
    @OneToMany(mappedBy = "pharmacy")
    @JsonManagedReference
    private List<Drug> drugs; // 약국이 제공하는 약 목록
}