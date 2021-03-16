package neobis.cms.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
    private String email;
    private String position;
    private String city;
    private String name;
    private String surname;
    private String phoneNo;
    private String password;
}
