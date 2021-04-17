package neobis.cms.Entity.Osh;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import neobis.cms.Entity.Base;

import javax.persistence.*;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "Teachers")
public class OshTeachers extends Base {

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone_no")
    private String phoneNo;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private OshPosition position;

    @Column(name = "patent")
    private String patent;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "description")
    private String description;
}
