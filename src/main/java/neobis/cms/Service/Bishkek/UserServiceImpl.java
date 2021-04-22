package neobis.cms.Service.Bishkek;

import neobis.cms.Dto.UserAuthDTO;
import neobis.cms.Dto.UserDTO;
import neobis.cms.Dto.UserPasswordsDTO;
import neobis.cms.Dto.UserRejectDTO;
import neobis.cms.Entity.Bishkek.BishPosition;
import neobis.cms.Entity.Bishkek.Role;
import neobis.cms.Entity.Bishkek.User;
import neobis.cms.Exception.IllegalArgumentException;
import neobis.cms.Exception.ResourceNotFoundException;
import neobis.cms.Repo.Bishkek.BishPositionRepo;
import neobis.cms.Repo.Bishkek.RoleRepo;
import neobis.cms.Repo.Bishkek.UserRepo;
import neobis.cms.Service.MailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

//import javax.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final MailService mailService;
    private final PasswordEncoder encoder;
    private final BishPositionRepo bishPositionRepo;

    public UserServiceImpl(UserRepo userRepo, RoleRepo roleRepo, MailService mailService, PasswordEncoder encoder, BishPositionRepo bishPositionRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.mailService = mailService;
        this.encoder = encoder;
        this.bishPositionRepo = bishPositionRepo;
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAllByConfirmed(true);
    }

    @Override
    public User findById(long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с идентификатором " + id + " не найден."));
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
    public List<User> getAllByPositionAndCity(BishPosition position, String city) {
        return userRepo.findAllByPositionAndCityContainingIgnoringCaseAndActiveAndConfirmed(position, city, true, true);
    }

    @Override
    public List<User> getAllByName(String name) {
        return userRepo.findAllByNameContainingIgnoringCaseAndActiveAndConfirmed(name, true, true);
    }

    @Override
    public List<User> getAllBySurname(String surname) {
        return userRepo.findAllBySurnameContainingIgnoringCaseAndActiveAndConfirmed(surname, true, true);
    }

    @Override
    public List<User> getAllByPhoneNo(String phoneNo) {
        return userRepo.findAllByPhoneNoContainingAndActiveAndConfirmed(phoneNo, true, true);
    }

    @Override
    public List<User> getAllByEmail(String email) {
        return userRepo.findAllByEmailContainingIgnoringCaseAndActiveAndConfirmed(email, true, true);
    }

    @Override
    public List<User> getListOfUserToConfirm() {
        return userRepo.findAllByConfirmed(false);
    }

    @Override
    public List<User> getUsersByCity(String city) {
        return userRepo.findAllByCityContainingIgnoringCaseAndActiveOrderByIDAsc(city, true);
    }

    @Override
    public String createUser(UserDTO userDTO) {
        User user = userRepo.findByEmailIgnoringCase(userDTO.getEmail());
        String city = userDTO.getCity();
        String positionName = userDTO.getPosition();
        if (user != null)
            throw new IllegalArgumentException("Пользователь с электронной почтой " + userDTO.getEmail() + " уже имеется.");
        if (!city.equalsIgnoreCase("bishkek") && !city.equalsIgnoreCase("osh")) {
            throw new IllegalArgumentException("Invalid city");
        }
        BishPosition position;
        if (positionName.equalsIgnoreCase("marketing")) {
            position = bishPositionRepo.findByNameContainingIgnoringCase("Маркетолог");
        } else if (positionName.equalsIgnoreCase("management")) {
            position = bishPositionRepo.findByNameContainingIgnoringCase("Менеджер");
        } else {
            throw new IllegalArgumentException("Invalid position");
        }

        String email = userDTO.getEmail().toLowerCase();
        if (email.startsWith("@") || email.endsWith("@") || !email.contains("@"))
            throw new IllegalArgumentException("Invalid email address");
        if (userDTO.getPassword().length() < 8)
            throw new IllegalArgumentException("Password is too short");

        user = new User();
        user.setEmail(email);
        user.setPosition(position);
        user.setCity(city);
        user.setName(userDTO.getName());
        user.setSurname(userDTO.getSurname());
        user.setPatronymic(userDTO.getPatronymic());
        user.setPhoneNo(userDTO.getPhoneNo());
        user.setPassword(encoder.encode(userDTO.getPassword()));
        user.setActivationCode(null);
        user.setActive(false);
        user.setConfirmed(false);
        String roleName = "ROLE_" + city.toUpperCase() + "_" + positionName.toUpperCase();
        Role role = roleRepo.findByNameContainingIgnoringCase(roleName);
        if (role == null) {
            role = roleRepo.save(new Role(roleName));
        }
        user.setRole(role);
        userRepo.save(user);
        return "The profile information has been saved. After confirmation by the administrator, you will receive a link to activate your account to your email.";
    }

    @Override
    public String confirm(Long id) {
        User user = findById(id);
        List<User> usersWaitingForConfirm = this.getListOfUserToConfirm();
        if (!usersWaitingForConfirm.contains(user))
            throw new ResourceNotFoundException("Пользователь с идентификатором " + id + " не ожидает подтверждения в данное время.");

        user.setConfirmed(true);
        user.setActivationCode(UUID.randomUUID().toString());

        String message = "To activate your account visit link: https://cms4.herokuapp.com/authorization/activate/" + user.getActivationCode();
        if (mailService.send(user.getEmail(), "Activation of Account", message)) {
            userRepo.save(user);
            return "Activation link successfully sent to email " + user.getEmail();
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

        return "Account activated";
    }

    @Override
    public String changePassword(String email, UserPasswordsDTO userPasswordDTO) {
        User user = userRepo.findByEmailIgnoringCaseAndActive(email, true);
        if (user == null)
            throw new ResourceNotFoundException("Пользователь с электронной почтой " + email + " не найден.");
        if (!encoder.matches(userPasswordDTO.getOldPassword(), user.getPassword()))
            return "Старый пароль не совпадает с нынешним";
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

        String message = "To restore your account visit link: https://cms4.herokuapp.com/authorization/setPassword/" + user.getActivationCode();
        if (mailService.send(user.getEmail(), "Account recovery", message)) {
            userRepo.save(user);
            return "Recovery code successfully sent to email " + user.getEmail();
        }
        return "We could not send recovery code.";
    }

    @Override
    public String setPassword(UserAuthDTO userAuthDTO) {
        User user = userRepo.findByEmailIgnoringCaseAndActive(userAuthDTO.getEmail(), true);
        if (user == null)
            throw new ResourceNotFoundException("Пользователь с электронной почтой " + userAuthDTO.getEmail() + " не найден.");
        if (user.getPassword() != null)
            throw new IllegalArgumentException("Введите данные, относящиеся к вам!");
        user.setPassword(encoder.encode(userAuthDTO.getPassword()));
        userRepo.save(user);
        return "You have successfully changed your password";
    }

    @Override
    public String reject(UserRejectDTO userRejectDTO) {
        List<User> usersWaitingForConfirm = this.getListOfUserToConfirm();
        if (!usersWaitingForConfirm.contains(userRepo.findByEmailIgnoringCase(userRejectDTO.getEmail())))
            throw new ResourceNotFoundException("Пользователь с электронной почтой " + userRejectDTO.getEmail() +
                    " не ожидает подтверждения в данное время.");
        userRepo.deleteByEmail(userRejectDTO.getEmail());
        String message = "Your registration request via email " + userRejectDTO.getEmail() +
                " was rejected by the admin.\n Comment from admin: " + userRejectDTO.getDescription();
        if (mailService.send(userRejectDTO.getEmail(), "Rejected registration request", message)) {
            return "Rejected registration request has been successfully sent to email " + userRejectDTO.getEmail();
        }
        return "We could not send rejected registration request to user's email.";
    }

    @Override
    public Set<Object> simpleSearch(String nameOrPhone) {
        Set<Object> users = new HashSet<>();
        for (String item : nameOrPhone.split(" ")) {
            users.addAll(userRepo.findAllByNameContainingIgnoringCaseAndActiveAndConfirmed(item, true, true));
            users.addAll(userRepo.findAllBySurnameContainingIgnoringCaseAndActiveAndConfirmed(item, true, true));
        }
        users.addAll(userRepo.findAllByPhoneNoContainingAndActiveAndConfirmed(nameOrPhone, true, true));
        users.addAll(userRepo.findAllByEmailContainingIgnoringCaseAndActiveAndConfirmed(nameOrPhone, true, true));
        return users;
    }
}

