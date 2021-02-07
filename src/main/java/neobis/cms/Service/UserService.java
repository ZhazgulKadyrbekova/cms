package neobis.cms.Service;

import neobis.cms.Dto.UserSaveDTO;
import neobis.cms.Dto.UserPositionDTO;
import neobis.cms.Entity.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(long id);
    User findByEmail(String email);
    void createAdmin(User user);
    String sendActivationToUser(UserPositionDTO userPositionDTO);
    String sendActivationToAdmin(UserPositionDTO userPositionDTO);
    String activate(String activationCode);
    User saveUser(UserSaveDTO userDTO);
}
