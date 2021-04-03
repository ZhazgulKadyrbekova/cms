package neobis.cms.Boot;

import neobis.cms.Entity.Bishkek.*;
import neobis.cms.Entity.Osh.OshLeavingReason;
import neobis.cms.Entity.Osh.OshMethod;
import neobis.cms.Entity.Osh.OshPosition;
import neobis.cms.Entity.Osh.OshStatuses;
import neobis.cms.Repo.Bishkek.*;
import neobis.cms.Repo.Osh.OshLeavingReasonRepo;
import neobis.cms.Repo.Osh.OshMethodRepo;
import neobis.cms.Repo.Osh.OshPositionRepo;
import neobis.cms.Repo.Osh.OshStatusesRepo;
import neobis.cms.Service.Bishkek.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Init implements CommandLineRunner {

    private final UserService service;
    private final RoleRepo roleRepo;
    private final BishStatusesRepo bishStatusesRepo;
    private final OshStatusesRepo oshStatusesRepo;
    private final BishLeavingReasonRepo bishLeavingReasonRepo;
    private final OshLeavingReasonRepo oshLeavingReasonRepo;
    private final BishPositionRepo bishPositionRepo;
    private final OshPositionRepo oshPositionRepo;
    private final BishMethodRepo bishMethodRepo;
    private final OshMethodRepo oshMethodRepo;

    public Init(UserService service, RoleRepo roleRepo, BishStatusesRepo bishStatusesRepo, OshStatusesRepo
                oshStatusesRepo, BishLeavingReasonRepo bishLeavingReasonRepo, OshLeavingReasonRepo oshLeavingReasonRepo,
                BishPositionRepo bishPositionRepo, OshPositionRepo oshPositionRepo, BishMethodRepo bishMethodRepo,
                OshMethodRepo oshMethodRepo) {
        this.service = service;
        this.roleRepo = roleRepo;
        this.bishStatusesRepo = bishStatusesRepo;
        this.oshStatusesRepo = oshStatusesRepo;
        this.bishLeavingReasonRepo = bishLeavingReasonRepo;
        this.oshLeavingReasonRepo = oshLeavingReasonRepo;
        this.bishPositionRepo = bishPositionRepo;
        this.oshPositionRepo = oshPositionRepo;
        this.bishMethodRepo = bishMethodRepo;
        this.oshMethodRepo = oshMethodRepo;
    }

    @Override
    public void run(String... args) {

//         Role role = roleRepo.findByNameContainingIgnoringCase("ROLE_ADMIN");
  //       if (role == null)
    //         role = roleRepo.save(new Role("ROLE_ADMIN"));
      //   service.createAdmin(new User("admin@gmail.com", "+996100100100", "Admin",  "Admin", "Admin",
        //         null, null, true, null, true, "12345678", role));
//         service.createAdmin(new User("admin2@gmail.com", "+996100100100", "Admin",  "Admin", "Admin",
  //               null, null, true, null, true, "12345678", role));

//         bishStatusesRepo.save(new BishStatuses("Первый контакт", true));
  //       bishStatusesRepo.save(new BishStatuses("Перезвон", true));
    //     bishStatusesRepo.save(new BishStatuses("Звонок совершен", true));
      //   bishStatusesRepo.save(new BishStatuses("Запись на пробный урок", true));
        // bishStatusesRepo.save(new BishStatuses("Запись на курс", true));
//         bishStatusesRepo.save(new BishStatuses("Отложено", false));
  //       bishStatusesRepo.save(new BishStatuses("Студент", false));
    //     bishStatusesRepo.save(new BishStatuses("Завершил обучение", false));
      //   bishStatusesRepo.save(new BishStatuses("Не завершил обучение", false));

//         oshStatusesRepo.save(new OshStatuses("Первый контакт", true));
  //       oshStatusesRepo.save(new OshStatuses("Перезвон", true));
    //     oshStatusesRepo.save(new OshStatuses("Звонок совершен", true));
      //   oshStatusesRepo.save(new OshStatuses("Запись на пробный урок", true));
        // oshStatusesRepo.save(new OshStatuses("Запись на курс", true));
         //oshStatusesRepo.save(new OshStatuses("Отложено", false));
//         oshStatusesRepo.save(new OshStatuses("Студент", false));
  //       oshStatusesRepo.save(new OshStatuses("Завершил обучение", false));
    //     oshStatusesRepo.save(new OshStatuses("Не завершил обучение", false));

//         bishLeavingReasonRepo.save(new BishLeavingReason("Финансовые проблемы"));
  //       bishLeavingReasonRepo.save(new BishLeavingReason("Учеба"));
    //     bishLeavingReasonRepo.save(new BishLeavingReason("Семейные проблемы"));
      //   bishLeavingReasonRepo.save(new BishLeavingReason("Личные проблемы"));
        // bishLeavingReasonRepo.save(new BishLeavingReason("Не доволен курсами"));

//         oshLeavingReasonRepo.save(new OshLeavingReason("Финансовые проблемы"));
  //       oshLeavingReasonRepo.save(new OshLeavingReason("Учеба"));
    //     oshLeavingReasonRepo.save(new OshLeavingReason("Семейные проблемы"));
      //   oshLeavingReasonRepo.save(new OshLeavingReason("Личные проблемы"));
        // oshLeavingReasonRepo.save(new OshLeavingReason("Не доволен курсами"));

//         bishPositionRepo.save(new BishPosition("Marketing"));
  //       bishPositionRepo.save(new BishPosition("Management"));
    //     bishPositionRepo.save(new BishPosition("Mentor"));
      //   bishPositionRepo.save(new BishPosition("Teacher"));

//         oshPositionRepo.save(new OshPosition("Marketing"));
  //       oshPositionRepo.save(new OshPosition("Management"));
    //     oshPositionRepo.save(new OshPosition("Mentor"));
      //   oshPositionRepo.save(new OshPosition("Teacher"));

//         bishMethodRepo.save(new BishMethod("Optima Bank"));
  //       bishMethodRepo.save(new BishMethod("Demir Bank"));
    //     bishMethodRepo.save(new BishMethod("Elsom"));
      //   bishMethodRepo.save(new BishMethod("Наличные"));

//         oshMethodRepo.save(new OshMethod("Optima Bank"));
  //       oshMethodRepo.save(new OshMethod("Demir Bank"));
    //     oshMethodRepo.save(new OshMethod("Elsom"));
      //   oshMethodRepo.save(new OshMethod("Наличные"));
    }
}
