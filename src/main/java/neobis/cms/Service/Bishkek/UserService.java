package neobis.cms.Service.Bishkek;

import neobis.cms.Dto.UserAuthDTO;
import neobis.cms.Dto.UserDTO;
import neobis.cms.Dto.UserPasswordsDTO;
import neobis.cms.Dto.UserRejectDTO;
import neobis.cms.Entity.Bishkek.BishPosition;
import neobis.cms.Entity.Bishkek.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    List<User> findAll();
    User findById(long id);
    User findByEmail(String email);
    void createAdmin(User user);
    String createUser(UserDTO userDTO);
    List<User> getListOfUserToConfirm();
    List<User> getUsersByCity(String city);
    List<User> getAllByPositionAndCity(BishPosition position, String city);
    List<User> getAllByName(String name);
    List<User> getAllBySurname(String surname);
    List<User> getAllByPhoneNo(String phoneNo);
    List<User> getAllByEmail(String email);
    String confirm(Long id);
    String activate(String activationCode);
    String changePassword(String email, UserPasswordsDTO userPasswordDTO);
    String forgotPassword(String email);
    String setPassword(UserAuthDTO userAuthDTO);
    String reject(UserRejectDTO userRejectDTO);
    Set<Object> simpleSearch(String nameOrPhone);
}
