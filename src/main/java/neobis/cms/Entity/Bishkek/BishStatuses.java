package neobis.cms.Entity.Bishkek;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import neobis.cms.Entity.Base;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "Status")
public class BishStatuses extends Base {
//    "ПЕРВЫЙ_КОНТАКТ", "ПЕРЕЗВОН", "ЗВОНОК_СОВЕРШЕН", "ЗАПИСЬ_НА_ПРОБ_УРОК",
//    "ЗАПИСЬ_НА_КУРС", "ОТЛОЖЕНО", "СТУДЕНТ", "ЗАВЕРШИЛ_ОБУЧЕНИЕ");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private long ID;

    @Column(name = "name")
    private String name;
}
