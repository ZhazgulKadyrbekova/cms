package neobis.cms.Entity.Osh;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "Client")
public class OshClient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private long clientID;

    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;

    @PreUpdate
    public void persistUpdate() {
        this.dateUpdated = LocalDateTime.now();
    }

    @Column(name = "phone_no")
    private String phoneNo;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "email")
    private String email;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private OshStatuses status;

    @ManyToOne
    @JoinColumn(name = "occupation_id")
    private OshOccupation occupation;

    @ManyToOne
    @JoinColumn(name = "target")
    private OshTarget target;

    @Column(name = "experience")
    private boolean experience;

    @Column(name = "laptop")
    private boolean laptop;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "Client_Course",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private List<OshCourses> courses;

    @ManyToOne
    @JoinColumn(name = "utm_id")
    private OshUTM utm;

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

    @ManyToOne
    @JoinColumn(name = "leaving_reason_id")
    private OshLeavingReason leavingReason;

    @OneToMany
    @JoinColumn(name = "payment_id")
    private List<OshPayment> payments;
}
