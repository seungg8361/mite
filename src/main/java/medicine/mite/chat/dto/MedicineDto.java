package medicine.mite.chat.dto;

import lombok.Data;
import medicine.mite.user.dto.UsersDto;

@Data
public class MedicineDto {

    private Long id;
    private String name;
    private String image;

    private UsersDto usersDto;
}
