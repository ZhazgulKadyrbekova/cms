package neobis.cms.Controller;

import neobis.cms.Dto.UserSaveDTO;
import neobis.cms.Dto.UserPositionDTO;
import neobis.cms.Entity.User;
import neobis.cms.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/register")
public class AuthController {
//    @Autowired
  //  private JwtUtil jwtUtil;
    //@Autowired
//    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public String createUser(@RequestBody UserPositionDTO userPositionDTO) {
        return userService.sendActivationToUser(userPositionDTO);
    }

    @PostMapping("/admin")
    public String createAdmin(@RequestBody UserPositionDTO userPositionDTO) {
        return userService.sendActivationToAdmin(userPositionDTO);
    }

    @GetMapping("/activate/{code}")
    public String activate(@PathVariable("code") String code) {
        return userService.activate(code);
    }

    @PostMapping("/save")
    public User save(@RequestBody UserSaveDTO userDTO) {
        return userService.saveUser(userDTO);
    }

}
