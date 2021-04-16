package neobis.cms.Controller.Bishkek;

import neobis.cms.Dto.ResponseMessage;
import neobis.cms.Dto.UserPasswordsDTO;
import neobis.cms.Entity.Bishkek.User;
import neobis.cms.Service.Bishkek.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    private final Logger log = LogManager.getLogger();
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @GetMapping("/profile")
    public User profile(Principal principal) {
        return userService.findByEmail(principal.getName());
    }

    @GetMapping("/city/{city}")
    public List<User> getAllByCity(@PathVariable String city) {
        return userService.getUsersByCity(city);
    }

    @PostMapping("/changePassword")
    public ResponseMessage changePassword(Principal principal, @RequestBody UserPasswordsDTO userPasswordDTO) {
        log.info("User {} changed password", principal.getName());
        return new ResponseMessage(userService.changePassword(principal.getName(), userPasswordDTO));
    }

}
