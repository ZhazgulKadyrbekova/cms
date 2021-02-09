package neobis.cms.Controller.Bishkek;

import neobis.cms.Entity.Bishkek.User;
import neobis.cms.Service.Bishkek.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAll() {
            return userService.findAll();
    }

}
