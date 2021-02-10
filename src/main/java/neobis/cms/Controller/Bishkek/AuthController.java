package neobis.cms.Controller.Bishkek;

import neobis.cms.Dto.UserAuthDTO;
import neobis.cms.Dto.UserDTO;
import neobis.cms.Dto.UserPasswordsDTO;
import neobis.cms.Dto.UserRejectDTO;
import neobis.cms.Entity.Bishkek.User;
import neobis.cms.Service.Bishkek.UserService;
import neobis.cms.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/register")
public class AuthController {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public String createUser(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @GetMapping("/toConfirm")
    public List<User> getListOfUserToConfirm() {
        return userService.getListOfUserToConfirm();
    }

    @PostMapping("/confirm/{id}")
    public String confirmAndSendActivation(@PathVariable Long id) {
        return userService.confirm(id);
    }

    @PostMapping("/reject")
    public String rejectAndSendEmail(@RequestBody UserRejectDTO userRejectDTO) {
        return userService.reject(userRejectDTO);
    }

    @GetMapping("/activate/{code}")
    public String activate(@PathVariable("code") String code) {
        return userService.activate(code);
    }

    @GetMapping("/restore/{code}")
    public String restore(@PathVariable("code") String code) {
        String message = userService.activate(code);
        if (message.equals("Account has been activated"))
            return message + "\n To set password visit link: /register/setPassword";
        return "Something went wrong";
    }

    @PostMapping("/setPassword")
    public String setPassword(@RequestBody UserAuthDTO userAuthDTO) {
        return userService.setPassword(userAuthDTO);
    }

    @PostMapping("/auth")
    public String getToken(@RequestBody UserAuthDTO userAuthDTO) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userAuthDTO.getEmail(), userAuthDTO.getPassword()));
        } catch (Exception e) {
            throw new Exception("Auth failed");
        }
        return jwtUtil.generateToken(userAuthDTO.getEmail());
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestBody UserPasswordsDTO userPasswordDTO) {
        return userService.changePassword(userPasswordDTO);
    }

    @PostMapping("/forgotPassword/{email}")
    public String forgotPassword(@PathVariable String email) {
        return userService.forgotPassword(email);
    }
}
