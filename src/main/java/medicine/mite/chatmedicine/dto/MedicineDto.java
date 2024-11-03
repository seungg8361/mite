package medicine.mite.chatmedicine.dto;

import lombok.Data;

@Data
public class MedicineDto {

    private Long id;
    private String name;
    private String image;
    private int price;
    private String 	ingredients;
    private String efficacy;
}
