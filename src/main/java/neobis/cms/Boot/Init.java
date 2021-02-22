package neobis.cms.Boot;

import neobis.cms.Entity.Bishkek.Role;
import neobis.cms.Entity.Bishkek.User;
import neobis.cms.Repo.Bishkek.RoleRepo;
import neobis.cms.Service.Bishkek.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Init implements CommandLineRunner {

    @Autowired
    private UserService service;

    @Autowired
    private RoleRepo roleRepo;

    @Override
    public void run(String... args) {

//        Role role = roleRepo.findByNameContainingIgnoringCase("ROLE_ADMIN");
//        if (role == null)
//            role = roleRepo.save(new Role("ROLE_ADMIN"));
//        service.createAdmin(new User(0, "admin@gmail.com", "+996100100100", "Admin Admin",
//                "Admin", null, true, null, true, "12345678", role));

    }
}
