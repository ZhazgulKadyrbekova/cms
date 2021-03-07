package neobis.cms.Controller.Bishkek;

import neobis.cms.Dto.ResponseMessage;
import neobis.cms.Dto.UserPasswordsDTO;
import neobis.cms.Entity.Bishkek.User;
import neobis.cms.Service.Bishkek.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    private final Logger log = LogManager.getLogger();

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAll() {
            return userService.findAll();
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
    public ResponseMessage changePassword(@RequestBody UserPasswordsDTO userPasswordDTO) {
        log.info("User {} changed password", userPasswordDTO.getEmail());
        return new ResponseMessage(userService.changePassword(userPasswordDTO));
    }
}
