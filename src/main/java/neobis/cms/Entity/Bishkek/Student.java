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
@Table(name = "students")
public class Student extends Base {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "student_id")
    private long ID;

    @Column(name = "name")
    private String name;
}
