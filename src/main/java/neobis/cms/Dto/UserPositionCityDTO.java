package neobis.cms.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserPositionCityDTO {
    private String email;
    private String position;
    private String city;
}
