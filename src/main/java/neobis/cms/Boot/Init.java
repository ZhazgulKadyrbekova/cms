package neobis.cms.Boot;

import neobis.cms.Entity.Bishkek.BishLeavingReason;
import neobis.cms.Entity.Bishkek.BishStatuses;
import neobis.cms.Entity.Bishkek.Role;
import neobis.cms.Entity.Bishkek.User;
import neobis.cms.Entity.Osh.OshLeavingReason;
import neobis.cms.Entity.Osh.OshStatuses;
import neobis.cms.Repo.Bishkek.BishLeavingReasonRepo;
import neobis.cms.Repo.Bishkek.BishStatusesRepo;
import neobis.cms.Repo.Bishkek.RoleRepo;
import neobis.cms.Repo.Osh.OshLeavingReasonRepo;
import neobis.cms.Repo.Osh.OshStatusesRepo;
import neobis.cms.Service.Bishkek.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Init implements CommandLineRunner {

    @Autowired private UserService service;
    @Autowired private RoleRepo roleRepo;
    @Autowired private BishStatusesRepo bishStatusesRepo;
    @Autowired private OshStatusesRepo oshStatusesRepo;
    @Autowired private BishLeavingReasonRepo bishLeavingReasonRepo;
    @Autowired private OshLeavingReasonRepo oshLeavingReasonRepo;

    @Override
    public void run(String... args) {

//        Role role = roleRepo.findByNameContainingIgnoringCase("ROLE_ADMIN");
//        if (role == null)
//            role = roleRepo.save(new Role("ROLE_ADMIN"));
//        service.createAdmin(new User(0, "admin@gmail.com", "+996100100100", "Admin",  "Admin",
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
//
//        bishLeavingReasonRepo.save(new BishLeavingReason(0, "Финансовые"));
//        bishLeavingReasonRepo.save(new BishLeavingReason(0, "Учеба"));
//        bishLeavingReasonRepo.save(new BishLeavingReason(0, "Семейные"));
//        bishLeavingReasonRepo.save(new BishLeavingReason(0, "Личные"));
//        bishLeavingReasonRepo.save(new BishLeavingReason(0, "Не доволен курсами"));
//
//        oshLeavingReasonRepo.save(new OshLeavingReason(0, "Финансовые"));
//        oshLeavingReasonRepo.save(new OshLeavingReason(0, "Учеба"));
//        oshLeavingReasonRepo.save(new OshLeavingReason(0, "Семейные"));
//        oshLeavingReasonRepo.save(new OshLeavingReason(0, "Личные"));
//        oshLeavingReasonRepo.save(new OshLeavingReason(0, "Не доволен курсами"));
    }
}
