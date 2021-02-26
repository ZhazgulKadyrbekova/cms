package neobis.cms.Controller.Bishkek;

import lombok.extern.log4j.Log4j2;
import neobis.cms.Dto.*;
import neobis.cms.Entity.Bishkek.User;
import neobis.cms.Service.Bishkek.UserService;
import neobis.cms.Util.JwtUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/register")
public class AuthController {

    private final Logger log = LogManager.getLogger();

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public ResponseMessage createUser(@RequestBody UserDTO userDTO) {
        log.info("{} sends request to register", userDTO.toString());
        return new ResponseMessage(userService.createUser(userDTO));
    }

    @GetMapping("/toConfirm")
    public List<User> getListOfUserToConfirm() {
        return userService.getListOfUserToConfirm();
    }

    @PostMapping("/confirm/{id}")
    public ResponseMessage confirmAndSendActivation(@PathVariable Long id) {
        log.info("Admin confirms user id {}", id);
        return new ResponseMessage(userService.confirm(id));
    }

    @PostMapping("/reject")
    public ResponseMessage rejectAndSendEmail(@RequestBody UserRejectDTO userRejectDTO) {
        log.info("Admin rejects user email {}", userRejectDTO.getEmail());
        return new ResponseMessage(userService.reject(userRejectDTO));
    }

    @GetMapping("/activate/{code}")
    public ResponseMessage activate(@PathVariable("code") String code) {
        return new ResponseMessage(userService.activate(code));
    }

    @GetMapping("/restore/{code}")
    public ResponseMessage restore(@PathVariable("code") String code) {
        String message = userService.activate(code);
        if (message.equals("Account has been activated"))
            return new ResponseMessage(message + "\n To set password visit link: /register/setPassword");
        return new ResponseMessage("Something went wrong");
    }

    @PostMapping("/setPassword")
    public ResponseMessage setPassword(@RequestBody UserAuthDTO userAuthDTO) {
        return new ResponseMessage(userService.setPassword(userAuthDTO));
    }

    @PostMapping("/auth")
    public TokenDTO getToken(@RequestBody UserAuthDTO userAuthDTO) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userAuthDTO.getEmail(), userAuthDTO.getPassword()));
        } catch (Exception e) {
            throw new Exception("Auth failed");
        }
        return new TokenDTO(jwtUtil.generateToken(userAuthDTO.getEmail()));
    }

    @PostMapping("/changePassword")
    public ResponseMessage changePassword(@RequestBody UserPasswordsDTO userPasswordDTO) {
        log.info("User {} changed password", userPasswordDTO.getEmail());
        return new ResponseMessage(userService.changePassword(userPasswordDTO));
    }

    @PostMapping("/forgotPassword/{email}")
    public ResponseMessage forgotPassword(@PathVariable String email) {
        log.info("User {} forgot/restored password", email);
        return new ResponseMessage(userService.forgotPassword(email));
    }
}
