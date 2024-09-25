package medicine.mite.naver.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Drug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dname; // 약 이름
    private int quantity; // 약 개수

    private String dimage;
    @ManyToOne
    @JoinColumn(name = "pharmacy_id") // 약국 ID를 외래 키로 사용
    @JsonBackReference
    private Pharmacy pharmacy; // 약국과의 관계
}