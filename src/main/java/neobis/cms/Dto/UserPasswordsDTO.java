package neobis.cms.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserPasswordsDTO {
    private String oldPassword;
    private String newPassword;
}