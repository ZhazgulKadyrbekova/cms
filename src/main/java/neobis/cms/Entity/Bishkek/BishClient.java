package neobis.cms.Entity.Bishkek;

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
public class BishClient {
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

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "status_id")
    private BishStatuses status;

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
    private BishCourses course;

    @Column(name = "utm")
    private String utm;

    @Column(name = "description")
    private String description;

    @Column(name = "city")
    private String city;

    @Column(name = "form_name")
    private String formName;

    @Column(name = "timer")
    private LocalDateTime timer;

    @Column(name = "prepayment")
    private BigDecimal prepayment;

    @Column(name = "leaving_reason")
    private String leavingReason;
}
