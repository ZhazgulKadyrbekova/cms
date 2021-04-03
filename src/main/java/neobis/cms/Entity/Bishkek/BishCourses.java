package neobis.cms.Entity.Bishkek;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import neobis.cms.Entity.Base;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "Courses")
public class BishCourses extends Base {

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private BigDecimal price;

    @JsonIgnore
    @ManyToMany(mappedBy = "courses")
    private List<BishClient> clients;

    @JsonIgnore
    @OneToOne(mappedBy = "course")
    private BishTeachers teacher;
}
