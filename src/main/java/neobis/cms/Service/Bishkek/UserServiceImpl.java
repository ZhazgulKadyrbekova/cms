package neobis.cms.Service.Bishkek;

import neobis.cms.Dto.UserPositionCityDTO;
import neobis.cms.Dto.UserPositionDTO;
import neobis.cms.Dto.UserSaveDTO;
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
        return userRepo.findAll();
    }

    @Override
    public User findById(long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User id " + id + " not found"));
    }

    @Override
    public User findByEmail(String email) {
        return userRepo.findByEmailIgnoringCase(email);
    }

    @Override
    public void createAdmin(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.save(user);
    }

    @Override
    public String sendActivationToUser(UserPositionCityDTO userPositionDTO) {
        User user = new User();
        String email = userPositionDTO.getEmail();
        if (email.startsWith("@") || email.endsWith("@") || !email.contains("@"))
            throw new IllegalArgumentException("Invalid email address");
        user.setEmail(email);
        user.setPosition(userPositionDTO.getPosition());
        user.setCity(userPositionDTO.getCity());
        String roleName = "ROLE_" + userPositionDTO.getCity().toUpperCase();
        roleName += (userPositionDTO.getPosition().equals("marketing")) ? "_MARKET" : "_SALE";
        user.setActive(false);
        user.setActivationCode(UUID.randomUUID().toString());
        Role role = roleRepo.findByNameContainingIgnoringCase(roleName);
        if (role == null) {
            role = roleRepo.save(new Role(roleName));
        }
        user.setRole(role);

        String message = "To activate your account visit link: /register/activate/" + user.getActivationCode();
        if (mailService.send(user.getEmail(), "Activation Code", message)) {
            userRepo.save(user);
            return "Activation code has been successfully sent!";
        }
        return "We could not send activation code.";
    }

    @Override
    public String sendActivationToAdmin(UserPositionDTO userPositionDTO) {
        User user = new User();
        String email = userPositionDTO.getEmail();
        if (email.startsWith("@") || email.endsWith("@") || !email.contains("@"))
            throw new IllegalArgumentException("Invalid email address");
        user.setEmail(email);
        user.setPosition(userPositionDTO.getPosition());
        user.setActive(false);
        user.setActivationCode(UUID.randomUUID().toString());
        Role role = roleRepo.findByNameContainingIgnoringCase("ROLE_ADMIN");
        if (role == null) {
            role = roleRepo.save(new Role("ROLE_ADMIN"));
        }
        user.setRole(role);

        String message = "To activate your account visit link: /register/activate/" + user.getActivationCode();
        if (mailService.send(user.getEmail(), "Activation Code", message)) {
            userRepo.save(user);
            return "Activation code has been successfully sent!";
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
    public User saveUser(UserSaveDTO userDTO) {
        User user = userRepo.findByEmailAndIsActive(userDTO.getEmail(), true);
        user.setName(userDTO.getName());
        String phoneNo = userDTO.getPhoneNo();
        if (!phoneNo.startsWith("+996"))
            throw new IllegalArgumentException("Phone number must start with +996");
        user.setPhoneNo(phoneNo);
        user.setPassword(encoder.encode(userDTO.getPassword()));

        userRepo.save(user);
        return user;
    }
}

