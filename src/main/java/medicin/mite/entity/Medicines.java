package medicin.mite.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Medicines {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String mname;
    @Lob
    private String mimage;
    private Long cid;

    // 기본 생성자
    public Medicines() {}

    // 생성자
    public Medicines(String mname, String mimage, Long cid) {
        this.mname = mname;
        this.mimage = mimage;
        this.cid = cid;
    }
}
