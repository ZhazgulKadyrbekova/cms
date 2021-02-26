package neobis.cms.Service.Bishkek;

import neobis.cms.Dto.UserAuthDTO;
import neobis.cms.Dto.UserDTO;
import neobis.cms.Dto.UserPasswordsDTO;
import neobis.cms.Dto.UserRejectDTO;
import neobis.cms.Entity.Bishkek.Role;
import neobis.cms.Entity.Bishkek.User;
import neobis.cms.Exception.IllegalArgumentException;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.Bishkek.RoleRepo;
import neobis.cms.Repo.Bishkek.UserRepo;
import neobis.cms.Service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private MailService mailService;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public List<User> findAll() {
        return userRepo.findAllByConfirmed(true);
    }

    @Override
    public User findById(long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User id " + id + " not found"));
    }

    @Override
    public User findByEmail(String email) {
        return userRepo.findByEmailIgnoringCaseAndActive(email, true);
    }

    @Override
    public void createAdmin(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.save(user);
    }

    @Override
    public List<User> getListOfUserToConfirm() {
        return userRepo.findAllByConfirmed(false);
    }

    @Override
    public String createUser(UserDTO userDTO) {
        User user = new User();
        String email = userDTO.getEmail();
        if (email.startsWith("@") || email.endsWith("@") || !email.contains("@"))
            throw new IllegalArgumentException("Invalid email address");
        user.setEmail(email);
        user.setPosition(userDTO.getPosition());
        user.setCity(userDTO.getCity());
        user.setName(userDTO.getName());
        user.setPhoneNo(userDTO.getPhoneNo());
        user.setPassword(encoder.encode(userDTO.getPassword()));
        user.setActivationCode(null);
        user.setActive(false);
        user.setConfirmed(false);
        String roleName = "ROLE_" + userDTO.getCity().toUpperCase();
        roleName += (userDTO.getPosition().equals("marketing")) ? "_MARKET" : "_SALE";
        Role role = roleRepo.findByNameContainingIgnoringCaseAndDeleted(roleName, false);
        if (role == null) {
            role = roleRepo.save(new Role(roleName));
        }
        user.setRole(role);
        userRepo.save(user);
        return "Profile info has been saved. After Admin confirmation you will get activation code to your email";
    }

    @Override
    public String confirm(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User id " + id + " is not found"));
        user.setConfirmed(true);

        user.setConfirmed(true);
        user.setActivationCode(UUID.randomUUID().toString());

        String message = "To activate your account visit link: /register/activate/" + user.getActivationCode();
        if (mailService.send(user.getEmail(), "Activation Code", message)) {
            userRepo.save(user);
            return "Activation code has been successfully sent to user's email!";
        }
        return "We could not send activation code.";
    }

    @Override
    public String activate(String activationCode) {
        User user = userRepo.findByActivationCode(activationCode);
        if (user == null)
            return "Invalid activation code";
        user.setActivationCode(null);
        user.setActive(true);

        userRepo.save(user);

        return "Account has been activated";
    }

    @Override
    public String changePassword(UserPasswordsDTO userPasswordDTO) {
        User user = userRepo.findByEmailIgnoringCaseAndActive(userPasswordDTO.getEmail(), true);
        if (user == null)
            throw new ResourceNotFoundException("User with email " + userPasswordDTO.getEmail() + " not found");
        if (!encoder.matches(user.getPassword(), userPasswordDTO.getOldPassword()))
            return "Old password did not match with your current password";
        user.setPassword(encoder.encode(userPasswordDTO.getNewPassword()));
        userRepo.save(user);
        return "You have successfully changed your password";
    }

    @Override
    public String forgotPassword(String email) {
        User user = userRepo.findByEmailIgnoringCaseAndActive(email, true);
        user.setActive(false);
        user.setPassword(null);
        user.setActivationCode(UUID.randomUUID().toString());

        String message = "To restore your account visit link: /register/restore/" + user.getActivationCode();
        if (mailService.send(user.getEmail(), "Restoration Code", message)) {
            userRepo.save(user);
            return "Restoration code has been successfully sent to your email!";
        }
        return "We could not send activation code.";
    }

    @Override
    public String setPassword(UserAuthDTO userAuthDTO) {
        User user = userRepo.findByEmailIgnoringCaseAndActive(userAuthDTO.getEmail(), true);
        if (user == null)
            throw new ResourceNotFoundException("User with email " + userAuthDTO.getEmail() + " not found");
        user.setPassword(encoder.encode(userAuthDTO.getPassword()));
        userRepo.save(user);
        return "You have successfully changed your password";
    }

    @Transactional
    @Override
    public String reject(UserRejectDTO userRejectDTO) {
        userRepo.deleteByEmail(userRejectDTO.getEmail());
        String message = "Your registration request for email " + userRejectDTO.getEmail() + " was rejected by admin.\n Comment from admin: " + userRejectDTO.getDescription();
        if (mailService.send(userRejectDTO.getEmail(), "Rejected registration request", message)) {
            return "Rejected registration request has been successfully sent to user's email!";
        }
        return "We could not send rejected registration request to user's email.";
    }
}

