package neobis.cms.Boot;

import neobis.cms.Entity.Role;
import neobis.cms.Entity.User;
import neobis.cms.Repo.RoleRepo;
import neobis.cms.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Init implements CommandLineRunner {
/*
    @Autowired
    private UserService service;
    @Autowired
    private RoleRepo roleRepo;
*/
    @Override
    public void run(String... args) {
    /*
        Role role = roleRepo.findByNameContainingIgnoringCase("ADMIN");
        if (role == null)
            role = roleRepo.save(new Role("ADMIN"));
        service.createAdmin(new User(0,  "zhazgul004@gmail.com", "Zhazgul",
                "Kadyrbekova", "dev", null, true, "12345678", role));
                */
    }
}
