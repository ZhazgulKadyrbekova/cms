package neobis.cms.Service.Impl;

import neobis.cms.Dto.UserSaveDTO;
import neobis.cms.Dto.UserPositionDTO;
import neobis.cms.Entity.Role;
import neobis.cms.Entity.User;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.RoleRepo;
import neobis.cms.Repo.UserRepo;
import neobis.cms.Service.MailService;
import neobis.cms.Service.UserService;
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
    public String sendActivationToUser(UserPositionDTO userPositionDTO) {
        User user = new User();
        user.setEmail(userPositionDTO.getEmail());
        user.setPosition(userPositionDTO.getPosition());
        user.setActive(false);
        user.setActivationCode(UUID.randomUUID().toString());
        Role role = roleRepo.findByNameContainingIgnoringCase("USER");
        if (role == null) {
            role = roleRepo.save(new Role("USER"));
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
        user.setEmail(userPositionDTO.getEmail());
        user.setPosition(userPositionDTO.getPosition());
        user.setActive(false);
        user.setActivationCode(UUID.randomUUID().toString());
        Role role = roleRepo.findByNameContainingIgnoringCase("ADMIN");
        if (role == null) {
            role = roleRepo.save(new Role("ADMIN"));
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
        user.setSurname(userDTO.getSurname());
        user.setPassword(encoder.encode(userDTO.getPassword()));

        userRepo.save(user);
        return user;
    }
}

