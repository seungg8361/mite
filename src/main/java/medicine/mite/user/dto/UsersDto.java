package medicine.mite.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UsersDto {

    @NotBlank(message = "아이디는 필수 입력 값 입니다.")
    private String userid;
    @NotEmpty(message = "비밀번호는 필수 입력 값 입니다.")
    private String userpw;
    @NotEmpty(message = "이름은 필수 입력 값 입니다.")
    private String username;
    @NotEmpty(message = "전화번호는 필수 입력 값 입니다.")
    @Length(min = 10, max = 11, message = "전화번호는 10자 이상, 11자 이하로 입력해주세요.")
    private String usernumber;

    private String confirmPassword;

}