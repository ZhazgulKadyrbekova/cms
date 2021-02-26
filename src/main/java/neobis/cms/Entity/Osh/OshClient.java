package neobis.cms.Entity.Osh;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "Client")
public class OshClient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private long client_id;

    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;

    @Column(name = "is_deleted", precision = 0, nullable = false)
    private boolean deleted;

    @PreUpdate
    public void persistUpdate() {
        this.dateUpdated = LocalDateTime.now();
    }

    @Column(name = "phone_no")
    private String phoneNo;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "status")
    private String status;

    @Column(name = "occupation")
    private String occupation;

    @Column(name = "target")
    private String target;

    @Column(name = "experience")
    private boolean experience;

    @Column(name = "laptop")
    private boolean laptop;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    private OshCourses course;

    @Column(name = "utm")
    private String utm;

    @Column(name = "description")
    private String description;

    @Column(name = "city")
    private String city;

    @Column(name = "form_name")
    private String formName;

    //timer

    @Column(name = "prepayment")
    private BigDecimal prepayment;
}
