package neobis.cms.Entity.Osh;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import neobis.cms.Entity.Base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "Courses")
public class OshCourses extends Base {

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private BigDecimal price;

    @OneToOne
    private OshTeachers teacher;
}
