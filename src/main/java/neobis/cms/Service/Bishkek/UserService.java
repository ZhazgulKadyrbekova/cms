package neobis.cms.Service.Bishkek;

import neobis.cms.Dto.UserPositionCityDTO;
import neobis.cms.Dto.UserPositionDTO;
import neobis.cms.Dto.UserSaveDTO;
import neobis.cms.Entity.Bishkek.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(long id);
    User findByEmail(String email);
    void createAdmin(User user);
    String sendActivationToUser(UserPositionCityDTO userPositionDTO);
    String sendActivationToAdmin(UserPositionDTO userPositionDTO);
    String activate(String activationCode);
    User saveUser(UserSaveDTO userDTO);
}
