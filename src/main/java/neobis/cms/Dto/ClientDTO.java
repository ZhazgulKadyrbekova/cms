package neobis.cms.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ClientDTO {
    private String phoneNo;
    private String name;
    private String email;
    private String status;
    private String occupation;
    private String target;
    private boolean experience;
    private boolean laptop;
    private String description;
    private String city;
}
