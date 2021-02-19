package neobis.cms.Entity.Bishkek;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    @Column(precision = 0, name = "deleted", nullable = false)
    private boolean isDeleted;

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

    //Course_id

    @Column(name = "utm")
    private String utm;

    @Column(name = "description")
    private String description;

    @Column(name = "city")
    private String city;

    @Column(name = "form_name")
    private String formName;

}
