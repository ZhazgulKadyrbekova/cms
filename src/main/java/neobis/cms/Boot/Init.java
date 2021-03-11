package neobis.cms.Boot;

import neobis.cms.Entity.Bishkek.BishStatuses;
import neobis.cms.Entity.Bishkek.Role;
import neobis.cms.Entity.Bishkek.User;
import neobis.cms.Entity.Osh.OshStatuses;
import neobis.cms.Repo.Bishkek.BishStatusesRepo;
import neobis.cms.Repo.Bishkek.RoleRepo;
import neobis.cms.Repo.Osh.OshStatusesRepo;
import neobis.cms.Service.Bishkek.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Init implements CommandLineRunner {

    @Autowired
    private UserService service;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private BishStatusesRepo bishStatusesRepo;

    @Autowired
    private OshStatusesRepo oshStatusesRepo;

    @Override
    public void run(String... args) {

//        Role role = roleRepo.findByNameContainingIgnoringCase("ROLE_ADMIN");
//        if (role == null)
//            role = roleRepo.save(new Role("ROLE_ADMIN"));
//        service.createAdmin(new User(0, "admin@gmail.com", "+996100100100", "Admin Admin",
//                "Admin", null, true, null, true, "12345678", role));
//
//        bishStatusesRepo.save(new BishStatuses(0, "Первый контакт", true));
//        bishStatusesRepo.save(new BishStatuses(0, "Перезвон", true));
//        bishStatusesRepo.save(new BishStatuses(0, "Звонок совершен", true));
//        bishStatusesRepo.save(new BishStatuses(0, "Запись на пробный урок", true));
//        bishStatusesRepo.save(new BishStatuses(0, "Запись на курс", true));
//        bishStatusesRepo.save(new BishStatuses(0, "Отложено", false));
//        bishStatusesRepo.save(new BishStatuses(0, "Студент", false));
//        bishStatusesRepo.save(new BishStatuses(0, "Завершил обучение", false));
//
//        oshStatusesRepo.save(new OshStatuses(0, "Первый контакт", true));
//        oshStatusesRepo.save(new OshStatuses(0, "Перезвон", true));
//        oshStatusesRepo.save(new OshStatuses(0, "Звонок совершен", true));
//        oshStatusesRepo.save(new OshStatuses(0, "Запись на пробный урок", true));
//        oshStatusesRepo.save(new OshStatuses(0, "Запись на курс", true));
//        oshStatusesRepo.save(new OshStatuses(0, "Отложено", false));
//        oshStatusesRepo.save(new OshStatuses(0, "Студент", false));
//        oshStatusesRepo.save(new OshStatuses(0, "Завершил обучение", false));

    }
}
