package neobis.cms.Controller.Bishkek;

import neobis.cms.Dto.UserAuthDTO;
import neobis.cms.Dto.UserPositionCityDTO;
import neobis.cms.Dto.UserSaveDTO;
import neobis.cms.Dto.UserPositionDTO;
import neobis.cms.Entity.Bishkek.User;
import neobis.cms.Service.Bishkek.UserService;
import neobis.cms.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

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
    public String createUser(@RequestBody UserPositionCityDTO userPositionDTO) {
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
}
